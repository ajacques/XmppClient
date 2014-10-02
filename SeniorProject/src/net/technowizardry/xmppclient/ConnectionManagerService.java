package net.technowizardry.xmppclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import scala.Function1;
import scala.Function2;
import scala.collection.JavaConversions;
import scala.collection.immutable.List;
import scala.runtime.AbstractFunction1;
import scala.runtime.AbstractFunction2;
import scala.runtime.BoxedUnit;
import net.technowizardry.XMLStreamFactoryFactory;
import net.technowizardry.xmppclient.networking.ExternalLibraryBasedDnsResolver;
import net.technowizardry.xmppclient.networking.ServiceEndpointResolver;
import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmpp.XmppConnection;
import net.technowizardry.xmpp.XmppContact;
import net.technowizardry.xmpp.XmppSession;
import net.technowizardry.xmppclient.networking.XmppSocketFactory;
import net.technowizardry.xmppclient.ui.ChatActivity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

public class ConnectionManagerService extends Service {
	private String domain;
	private String localName;
	private String password;
	private XmppConnection connection;
	private XmppSocketFactory factory;
	private static XmppSession session;
	private static Iterable<XmppContact> theRoster;
	public static final String BROADCAST_ACTION = "net.technowizardry.xmppclient.BROADCAST";

	public ConnectionManagerService() {}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		domain = intent.getStringExtra("domainName");
		localName = intent.getStringExtra("localName");
		password = intent.getStringExtra("password");

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnected()) {
			XmppConnect();
		}

		return Service.START_NOT_STICKY;
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
		Intent broadcast = new Intent();
		broadcast.setAction(BROADCAST_ACTION);
		broadcast.putExtra("connection", "Connected");
		this.sendBroadcast(broadcast);
	}

	private void connectionCompleted() {
		session = new XmppSession(connection);
		Function1<List<XmppContact>, BoxedUnit> rosterCallback = new AbstractFunction1<List<XmppContact>, BoxedUnit>() {
			@Override
			public BoxedUnit apply(List<XmppContact> contacts) {
				theRoster = JavaConversions.asJavaIterable(contacts);
				Log.d("LOG", "CONTACTS");
				for (XmppContact contact : theRoster) {
					Log.d("LOG", contact.toString());
				}
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
	}

	public void receivedMessage(Jid jid, String message) {
		MessageHistory.AddToHistory(this, jid, message);
		ChatActivity.loadMessage(jid, message, "Now", false);
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
