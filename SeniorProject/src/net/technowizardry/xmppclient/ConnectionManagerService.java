package net.technowizardry.xmppclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import scala.Function1;
import scala.Function2;
import scala.Function4;
import scala.collection.JavaConversions;
import scala.collection.immutable.List;
import scala.runtime.AbstractFunction1;
import scala.runtime.AbstractFunction2;
import scala.runtime.AbstractFunction4;
import scala.runtime.BoxedUnit;
import net.technowizardry.XMLStreamFactoryFactory;
import net.technowizardry.xmppclient.networking.ExternalLibraryBasedDnsResolver;
import net.technowizardry.xmppclient.networking.ServiceEndpointResolver;
import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmpp.XmppConnection;
import net.technowizardry.xmpp.XmppContact;
import net.technowizardry.xmpp.XmppSession;
import net.technowizardry.xmppclient.networking.XmppSocketFactory;
import net.technowizardry.xmppclient.ui.HomeActivity;
import net.technowizardry.xmppclient.ui.LoginActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public class ConnectionManagerService extends Service {
	private String domain;
	private String localName;
	private String password;
	private XmppConnection connection;
	private XmppSocketFactory factory;
	private static XmppSession session;
	private static Iterable<XmppContact> theRoster;
	public static final String BROADCAST_ACTION = "net.technowizardry.xmppclient.BROADCAST";
	public static boolean isLoading = false;

	public ConnectionManagerService() {
		super();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyProperties", 0);
		if(pref.getBoolean("loggedIn", false)) {
			domain = pref.getString("domainName", null);
			localName = pref.getString("localName", null);
			password = pref.getString("password", null);
		}
		else {
			domain = intent.getStringExtra("domainName");
			localName = intent.getStringExtra("localName");
			password = intent.getStringExtra("password");
		}
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnected()) {
			XmppConnect();
		}

		return Service.START_STICKY;
	}

	private void XmppConnect() {
		Thread t = new Thread(){
			public void run() {
				connection = new XmppConnection(domain, localName, password, XMLStreamFactoryFactory.newInstance());
				factory = new XmppSocketFactory(new ServiceEndpointResolver(new ExternalLibraryBasedDnsResolver()));
				Socket socket = null;
				try {
					socket = factory.newConnection(domain);
				} catch (UnknownHostException e) {
					connectionFailed();
					return;
				} catch (IOException e) {
					connectionFailed();
					return;
				}

				if(socket.isConnected()) {
					try {
						Function1<Object, BoxedUnit> loginCallback = new AbstractFunction1<Object, BoxedUnit>() {
							@Override
							public BoxedUnit apply(Object success) {
								if ((Boolean) success) {
									connectionCompleted();
								}
								else {
									connectionFailed();
								}
								return null;
							}
						};
						connection.Negotiate(socket, socket.getInputStream(), socket.getOutputStream(), loginCallback);
					} catch (IOException e) {
						connectionFailed();
						return;
					}
				}
			}
		};
		t.start();
	}

	private void rosterComplete() {
		isLoading = true;
		Intent broadcast = new Intent();
		broadcast.setAction(BROADCAST_ACTION);
		broadcast.putExtra("connection", "Connected");
		this.sendBroadcast(broadcast);
	}

	private void connectionCompleted() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyProperties", 0);
		Editor editor = pref.edit();
		editor.putBoolean("loggedIn", true);
		editor.putString("localName", localName);
		editor.putString("domainName", domain);
		editor.putString("password", password);
		editor.commit();
		session = new XmppSession(connection);
		Function1<List<XmppContact>, BoxedUnit> rosterCallback = new AbstractFunction1<List<XmppContact>, BoxedUnit>() {
			@Override
			public BoxedUnit apply(List<XmppContact> contacts) {
				theRoster = JavaConversions.asJavaIterable(contacts);
				rosterComplete();
				return null;
			}
		};
		session.BindToResource("android-phone");
		session.FetchRoster(rosterCallback);
		session.UpdateOwnStatus(null, null, 5);

		Function2<Jid, String, BoxedUnit> messageCallback = new AbstractFunction2<Jid, String, BoxedUnit>() {
			@Override
			public BoxedUnit apply(Jid jid, String message) {
				System.out.println("New Message from: " + jid.toString() + ": " + message.toString());
				receivedMessage(jid, message);
				return null;
			}
		};
		session.MessageReceivedCallback_$eq(messageCallback);
		Function4<Jid, String, String, Integer, BoxedUnit> presenceCallback = new AbstractFunction4<Jid, String, String, Integer, BoxedUnit>() {
			@Override
			public BoxedUnit apply(Jid arg0, String arg1, String arg2, Integer arg3) {
				System.out.println("New presence update from: " + arg0.toString() + " Classification: " + arg1 + " Status: " + arg2 + " Priority: " + arg2);
				return null;
			}
		};
		session.PresenceUpdatedCallback_$eq(presenceCallback);
	}

	public static boolean isStarted() {
		if (session == null)
			return false;
		else
			return true;
	}
	public void receivedMessage(Jid jid, String message) {
		newNotification(jid.Username(), message);
		MessageHistory.AddToHistory(this, jid, jid, message);
		System.out.println("RECIEVED MESSAGE");
		if(ConnectionManagerService.isStarted() && !isLoading) {
			HomeActivity.newMessage(jid, message, "Now", false);
		}
	}

	private void connectionFailed() {
		Intent broadcast = new Intent();
		broadcast.setAction(BROADCAST_ACTION);
		broadcast.putExtra("connection", "Failed");
		this.sendBroadcast(broadcast);
	}

	public static Iterable<XmppContact> getRoster() {
		return theRoster;
	}

	public static void sendMessage(Jid contact, String message) {
		final Jid c = contact;
		final String m = message;

		Thread t = new Thread(){
			public void run() {
				session.SendMessageTo(c, m);
			}
		};
		t.start();
	}

	private void newNotification(String sender, String message) {
		Notification.Builder mBuilder = new Notification.Builder(this)
			.setContentTitle("New Message From " + sender)
			.setContentText(message)
			.setSmallIcon(R.drawable.ic_launcher)
			.setAutoCancel(true);

		//currently going to start the loginactivity which will direct to the home activity.
		//need to make this go to the actual chat.
		Intent resultIntent = new Intent(this, LoginActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(LoginActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		final int NOTI_ID = 1;
		mNotificationManager.notify(NOTI_ID, mBuilder.build());
	}

	@Override
	public void onDestroy() {
		connection.Disconnect();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
