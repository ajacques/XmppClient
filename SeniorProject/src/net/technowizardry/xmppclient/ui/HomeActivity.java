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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private Intent connectionManagerIntent;
	private String connectionReturnData;
	private String localName;
	private String domainName;
	private String password;
	private TextView invalid;
	private EditText loginText;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectionReturnData = null;
		localName = getIntent().getStringExtra("localName");
		domainName = getIntent().getStringExtra("domainName");
		password = getIntent().getStringExtra("password");

		startConnectivityManager();
		setContentView(R.layout.login_loading);
	}

	private void main() {
		/*View container = findViewById(R.id.conversationLinearLayout);
		container.setOnHoverListener(new View.OnHoverListener() {
			@Override
			public boolean onHover(View v, MotionEvent event) {
				Log.d("LOG", "Hover: ");
				return false;
			}*/
		/*	@Override
			public boolean More onHover(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_HOVER_ENTER:
					mMessageTextView.setText(Hover.this.getResources().getString(
					R.string.hover_message_entered_at,
					event.getX(), event.getY()));
					break;
				case MotionEvent.ACTION_HOVER_MOVE:
					mMessageTextView.setText(Hover.this.getResources().getString(
					R.string.hover_message_moved_at,
					event.getX(), event.getY()));
					break;
				case MotionEvent.ACTION_HOVER_EXIT:
					mMessageTextView.setText(Hover.this.getResources().getString(
					R.string.hover_message_exited_at,
					event.getX(), event.getY()));
					break;
				}
				return false;
			}*/
		//});
	}

	private void loadHomeScreen() {
		setContentView(R.layout.home_screen);
		loadConversations();
		main();
	}

	private void loadConversations() {
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		String message = "Need to get all mesages! Need to get all mesages! Need to get all mesages! Need to get all mesages!";
		ConversationFragment fragment = new ConversationFragment(localName, "getOtherUsernameFromDatbase", message, "10:49 AM");
		fragmentTransaction.add(R.id.homeMainLLayout , fragment, "one");
		fragmentTransaction.commit();
	}

	private void connectionFailed() {
		setContentView(R.layout.login_screen);
		invalid = (TextView)findViewById(R.id.invalidUsernameTextView);
		invalid.setText("Invalid Username or Password");
		loginText = (EditText)findViewById(R.id.loginUsername);
	}

	private void startConnectivityManager() {
		connectionManagerIntent = new Intent(getApplicationContext(), ConnectionManagerService.class);
		connectionManagerIntent.putExtra("domainName", domainName);
		connectionManagerIntent.putExtra("localName", localName);
		connectionManagerIntent.putExtra("password", password);
		startService(connectionManagerIntent);
	}

	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			connectionReturnData = intent.getStringExtra("connection");
			if(connectionReturnData == "Connected") {
				loadHomeScreen();
			}
			else if(connectionReturnData == "Failed") {
				connectionFailed();
			}
			Log.d("LOG", connectionReturnData);
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
