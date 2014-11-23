package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmpp.XmppContact;
import net.technowizardry.xmppclient.ConnectionManagerService;
import net.technowizardry.xmppclient.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NewConversationActivity extends Activity {
	private static FragmentManager fragmentManager;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.actionbar_contact);
		fragmentManager = getFragmentManager();
		loadRoster();
	}

	private void loadRoster() {
		FragmentTransaction fragmentTransaction;
		fragmentTransaction = fragmentManager.beginTransaction();

		Iterable<XmppContact> roster;
		roster = ConnectionManagerService.getRoster();
		for (XmppContact contact : roster) {
			ContactFragment fragment = new ContactFragment(contact.Username());
			fragmentTransaction.add(R.id.contactMainLLayout , fragment);
		}
		fragmentTransaction.commit();
	}

	private void addContact(String username) {
		int index = username.indexOf("@");
		String domainName = (index == -1) ? "" : username.substring(index+1, username.length());
		if (domainName != "") {
			String localName = username.substring(0,index);
			Jid newContact = new Jid(localName,domainName);
			ConnectionManagerService.addNewContact(newContact);
		}
		else {
			Toast.makeText(getBaseContext(), "Need a domain name to add a contact", Toast.LENGTH_LONG).show();
		}
	}

	public static void newSubscriptionRequest(Jid jid) {
		FragmentTransaction fragmentTransaction;
		fragmentTransaction = fragmentManager.beginTransaction();
		SubscriptionFragment fragment = new SubscriptionFragment(jid);
		fragmentTransaction.add(R.id.contactMainLLayout , fragment);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater mif = getMenuInflater();
		mif.inflate(R.menu.action_new_contact, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId())
		{
		case R.id.actionNewContact:
			EditText newContactText = (EditText)findViewById(R.id.actionbarContactText);
			String username = newContactText.getText().toString().trim();
			addContact(username);
			return true;
		case R.id.action_settings:
			Toast.makeText(getBaseContext(),  "clicked action settings button", Toast.LENGTH_LONG).show();
			return true;
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
}