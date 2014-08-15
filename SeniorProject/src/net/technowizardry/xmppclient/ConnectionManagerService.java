package net.technowizardry.xmppclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import net.technowizardry.xmppclient.networking.ExternalLibraryBasedDnsResolver;
import net.technowizardry.xmppclient.networking.ServiceEndpointResolver;
import net.technowizardry.xmpp.XmppConnection;
import net.technowizardry.xmppclient.networking.XmppSocketFactory;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public class ConnectionManagerService extends Service {
	private Intent broadcast;
	private String domain;
	private String localName;
	private XmppConnection connection;
	private XmppSocketFactory factory;
	public static final String BROADCAST_ACTION = "net.technowizardry.xmppclient.BROADCAST";

	public ConnectionManagerService() {
		broadcast = new Intent();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		domain = intent.getStringExtra("domainName");
		localName = intent.getStringExtra("localName");

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
				connection = new XmppConnection(domain, localName, null, null);
				factory = new XmppSocketFactory(new ServiceEndpointResolver(new ExternalLibraryBasedDnsResolver()));
				Socket socket = null;
				try {
					socket = factory.newConnection(domain);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if(socket.isConnected()) {
					try {
						connection.Negotiate(socket.getInputStream(), socket.getOutputStream(), null);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		};
		t.start();

		broadcast.setAction(BROADCAST_ACTION);
		broadcast.putExtra("connected", "Connected");
		this.sendBroadcast(broadcast);
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
