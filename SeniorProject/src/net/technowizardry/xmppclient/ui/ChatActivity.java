package net.technowizardry.xmppclient.ui;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChatActivity extends Activity {
	private ImageButton sendButton;
	private Jid contact;
	private static FragmentManager fragmentManager;
	private static FragmentTransaction fragmentTransaction;

	public ChatActivity(){}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		String localName = getIntent().getStringExtra("local");
		String domainName = getIntent().getStringExtra("domain");
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
		if (!messageText.getText().toString().isEmpty()) {
			MessageHistory.AddToHistory(this, contact, messageText.getText().toString());
			ConnectionManagerService.sendMessage(contact, messageText.getText().toString());
			loadMessage(contact, messageText.getText().toString(), "Now", true);
			messageText.setText("");
		}
	}

	private void loadConversation() {
		List<Message> conversation = MessageHistory.GetHistory(this, contact);
		for (Message message : JavaConversions.asJavaCollection(conversation)) {
			if(message.Source().equals(contact)) {
				loadMessage(message.Source(), message.Message(), message.Date().toString(), false);
			}
			else {
				loadMessage(message.Source(), message.Message(), message.Date().toString(), true);
			}
		}
	}

	public static void loadMessage(Jid sender, String message, String date, boolean isLocal) {
		fragmentTransaction = fragmentManager.beginTransaction();
		MessageFragment frag = new MessageFragment(message, date, isLocal);
		fragmentTransaction.add(R.id.chatMainLLayout, frag, "one");
		fragmentTransaction.commit();
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
