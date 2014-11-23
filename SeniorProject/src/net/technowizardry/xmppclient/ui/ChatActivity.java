package net.technowizardry.xmppclient.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import scala.collection.JavaConversions;
import scala.collection.immutable.List;
import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmppclient.ConnectionManagerService;
import net.technowizardry.xmppclient.Message;
import net.technowizardry.xmppclient.MessageHistory;
import net.technowizardry.xmppclient.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

public class ChatActivity extends Activity {
	private ImageButton sendButton;
	private Jid contact;
	private static FragmentManager fragmentManager;
	private static FragmentTransaction fragmentTransaction;
	private static boolean isActive;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		isActive = true;
		String localName = getIntent().getStringExtra("local");
		String domainName = getIntent().getStringExtra("domain");
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setTitle(localName);

		contact = new Jid(localName, domainName);
		fragmentManager = getFragmentManager();
		loadConversation();

		sendButton = (ImageButton)findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessage();
			}
		});
	}

	private void sendMessage() {
		EditText messageText = (EditText)findViewById(R.id.chatBox);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyProperties", 0);
		if (!messageText.getText().toString().isEmpty()) {
			Jid myJid = new Jid(pref.getString("localName", null),pref.getString("domainName", null));
			MessageHistory.AddToHistory(this, contact, myJid, messageText.getText().toString());
			ConnectionManagerService.sendMessage(contact, messageText.getText().toString());
			loadMessage(myJid, messageText.getText().toString(), new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").format(new Date()), true);
			messageText.setText("");
			scrollDown();
		}
	}

	private void loadConversation() {
		List<Message> conversation = MessageHistory.GetHistory(this, contact);
		for (Message message : JavaConversions.asJavaCollection(conversation)) {
			if(message.Source().toString().equals(contact.toString())) {
				loadMessage(message.Source(), message.Message(), message.Date().toString(), false);
			}
			else {
				loadMessage(message.Source(), message.Message(), message.Date().toString(), true);
			}
		}
		scrollDown();
	}

	private void scrollDown() {
		final ScrollView scrollview = ((ScrollView) findViewById(R.id.chatMainScrollView));
		scrollview.post(new Runnable() {
			@Override
			public void run() {
				scrollview.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
	public static void loadMessage(Jid sender, String message, String date, boolean isLocal) {
		if (isActive) {
			fragmentTransaction = fragmentManager.beginTransaction();
			MessageFragment frag = new MessageFragment(message, date, isLocal);
			fragmentTransaction.add(R.id.chatMainLLayout, frag, "one");
			fragmentTransaction.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//getActionBar().setDisplayHomeAsUpEnabled(true); //puts down the back button but doesn't send it back. look into this later
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()) {
			case R.id.action_settings:
				return true;
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true; //implement back button eventually
			case R.id.action_logout:
				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyProperties", 0);
				Editor editor = pref.edit();
				editor.clear();
				editor.commit();
				ConnectionManagerService.logout();
				Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(logoutIntent);
				finish();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void onResume() {
		super.onResume();
		isActive = true;
	}

	protected void onPause() {
		super.onPause();
		isActive = false;
	}

}
