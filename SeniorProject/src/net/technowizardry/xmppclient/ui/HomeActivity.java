package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.ConnectionManagerService;
import net.technowizardry.xmppclient.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends Activity {
	private Intent connectionManagerIntent;
	private String connectionReturnData;
	private String localName;
	private String domainName;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectionReturnData = null;
		localName = getIntent().getStringExtra("localName");
		domainName = getIntent().getStringExtra("domainName");

		startConnectivityManager();
		setContentView(R.layout.home_screen);

		loadConversations();
	}

	private void loadConversations() {
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		ContactCardFragment fragment = new ContactCardFragment(localName + domainName, "getOtherUsernameFromDatbase");
		fragmentTransaction.add(R.id.homeMainLLayout , fragment, "one");
		fragmentTransaction.commit();
	}

	private void startConnectivityManager() {
		connectionManagerIntent = new Intent(getApplicationContext(), ConnectionManagerService.class);
		connectionManagerIntent.putExtra("domainName", domainName);
		connectionManagerIntent.putExtra("localName", localName);
		startService(connectionManagerIntent);
	}

	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			connectionReturnData = intent.getStringExtra("connected");
			Log.d("return DATA", connectionReturnData.toString());
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onResume() {
		super.onResume();
		registerReceiver(connectionReceiver, new IntentFilter(ConnectionManagerService.BROADCAST_ACTION));
	}

	protected void onPause() {
		super.onPause();
		unregisterReceiver(connectionReceiver);
	}

}
