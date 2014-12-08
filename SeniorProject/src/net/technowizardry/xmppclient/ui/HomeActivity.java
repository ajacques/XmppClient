package net.technowizardry.xmppclient.ui;

import scala.collection.immutable.List;
import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmpp.XmppContact;
import net.technowizardry.xmppclient.ConnectionManagerService;
import net.technowizardry.xmppclient.Message;
import net.technowizardry.xmppclient.MessageHistory;
import net.technowizardry.xmppclient.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private String localName;
	private String domainName;
	private String password;
	private static FragmentManager fragmentManager;
	private static boolean isActive;
	private static Context getCon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isActive = true;
		localName = getIntent().getStringExtra("localName");
		domainName = getIntent().getStringExtra("domainName");
		password = getIntent().getStringExtra("password");
		setContentView(R.layout.login_loading);
		fragmentManager = getFragmentManager();
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyProperties", 0);
		getCon = getBaseContext();
		if(!pref.getBoolean("loggedIn", false) || !ConnectionManagerService.isStarted()) {
			startConnectivityManager();
		}
		else {
			loadHomeScreen();
		}
	}

	private void loadHomeScreen() {
		setContentView(R.layout.home_screen);
		loadConversations();
	}

	public static void newMessage(Jid jid, String message, String date, Boolean isLocal) {
		loadConversations();
		ChatActivity.loadMessage(jid, message, date, isLocal);
	}

	private static void loadConversations() {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Iterable<XmppContact> roster;
		roster = ConnectionManagerService.getRoster();
		for (XmppContact contact : roster) {
			List<Message> messages = MessageHistory.GetHistory(getCon, contact.Username().GetBareJid());
			if (!messages.isEmpty()) {
				Message m = messages.last();
				Fragment frag = fragmentManager.findFragmentByTag(contact.Username().GetBareJid().toString());
				ConversationFragment fragment = new ConversationFragment(contact.Username().GetBareJid().Username(),
						contact.Username().GetBareJid().Domain(), m.Message(), m.Date().toString());
				if (frag == null) {
					fragmentTransaction.add(R.id.homeMainLLayout, fragment, contact.Username().GetBareJid().toString());
				}
				else {
					fragmentTransaction.replace(R.id.homeMainLLayout, fragment, contact.Username().GetBareJid().toString());
				}
			}
		}
		if (isActive) {
			fragmentTransaction.commit();
		}
		ConnectionManagerService.isLoading = false;
		refresh();
	}

	private static void refresh() {
		Thread t = new Thread(){
			public void run() {
				while(isActive) {
					try {
						Thread.sleep(90000);
						if(isActive)
							loadConversations();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

	private void connectionFailed() {
		Intent nextIntent = new Intent(getApplicationContext(), LoginActivity.class);
		nextIntent.putExtra("failed", true);
		startActivity(nextIntent);
		finish();
	}

	private void startConnectivityManager() {
		Intent connectionManagerIntent = new Intent(this, ConnectionManagerService.class);
		stopService(connectionManagerIntent);
		connectionManagerIntent.putExtra("domainName", domainName);
		connectionManagerIntent.putExtra("localName", localName);
		connectionManagerIntent.putExtra("password", password);
		startService(connectionManagerIntent);
	}

	public static void newSubscriptionRequest(Jid jid) {
		NewConversationActivity.newSubscriptionRequest(jid);
	}

	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String connectionReturnData = intent.getStringExtra("connection");
			if(connectionReturnData.equals("Connected")) {
				loadHomeScreen();
			}
			else if(connectionReturnData.equals("Failed")) {
				connectionFailed();
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.action_new_conversation, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId())
		{
		case R.id.actionNewConversation:
			Intent nextIntent = new Intent(getApplicationContext(), NewConversationActivity.class);
			startActivity(nextIntent);
			return true;
		case R.id.action_settings:
			Toast.makeText(getBaseContext(),  "clicked action settings button", Toast.LENGTH_LONG).show();
			return true;
		case R.id.action_logout:
			ConnectionManagerService.logout();
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyProperties", 0);
			Editor editor = pref.edit();
			editor.clear();
			editor.commit();
			Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(logoutIntent);
			stopService(new Intent(getApplicationContext(), ConnectionManagerService.class));
			System.exit(0);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onResume() {
		super.onResume();
		isActive = true;
		registerReceiver(connectionReceiver, new IntentFilter(ConnectionManagerService.BROADCAST_ACTION));
		if (ConnectionManagerService.isStarted()) {
			loadConversations();
		}
	}
	protected void onDestroy() {
		super.onDestroy();
		isActive = false;
	}
	protected void onPause() {
		super.onPause();
		isActive = false;
		unregisterReceiver(connectionReceiver);
	}

}
