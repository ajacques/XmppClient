package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmpp.XmppContact;
import net.technowizardry.xmppclient.ConnectionManagerService;
import net.technowizardry.xmppclient.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
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
	public static boolean isActive;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.actionbar_contact);
		fragmentManager = getFragmentManager();
		isActive = true;
		loadRoster();
	}

	private void loadRoster() {
		FragmentTransaction fragmentTransaction;
		fragmentTransaction = fragmentManager.beginTransaction();

		for (Jid j : ConnectionManagerService.pendingSubscriptions) {
			SubscriptionFragment fragment = new SubscriptionFragment(j);
			fragmentTransaction.add(R.id.contactMainLLayout, fragment, j.GetBareJid().toString());
		}

		Iterable<XmppContact> roster;
		roster = ConnectionManagerService.getUpdatedroster();
		for (XmppContact contact : roster) {
			ContactFragment fragment = new ContactFragment(contact.Username());
			fragmentTransaction.add(R.id.contactMainLLayout, fragment, contact.Username().GetBareJid().toString());
		}

		fragmentTransaction.commit();
	}

	private void addContact() {
		EditText newContactText = (EditText)findViewById(R.id.actionbarContactText);
		String username = newContactText.getText().toString().trim();
		int index = username.indexOf("@");
		String domainName = (index == -1) ? "" : username.substring(index+1, username.length());
		if (domainName != "") {
			String localName = username.substring(0,index);
			Jid newContact = new Jid(localName,domainName);
			ConnectionManagerService.addNewContact(newContact);
			Toast.makeText(getBaseContext(),  "Request sent to " + username, Toast.LENGTH_LONG).show();
			newContactText.setText("");
		}
		else {
			Toast.makeText(getBaseContext(), "Need a domain name to add a contact", Toast.LENGTH_LONG).show();
		}
	}

	public static void newSubscriptionRequest(Jid jid) {
		if (isActive) {
			FragmentTransaction fragmentTransaction;
			fragmentTransaction = fragmentManager.beginTransaction();
			SubscriptionFragment fragment = new SubscriptionFragment(jid);
			fragmentTransaction.add(R.id.contactMainLLayout, fragment, jid.GetBareJid().toString());
			fragmentTransaction.commit();
		}

	}

	public static void removeSubRequest(Jid jid) {
		FragmentTransaction fragmentTransaction;
		fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragReq = fragmentManager.findFragmentByTag(jid.GetBareJid().toString());
		fragmentTransaction.remove(fragReq);
		for (Jid j : ConnectionManagerService.pendingSubscriptions) {
			Fragment frag = fragmentManager.findFragmentByTag(j.GetBareJid().toString());
			SubscriptionFragment fragment = new SubscriptionFragment(j);
			if(frag == null) {
				fragmentTransaction.add(R.id.contactMainLLayout, fragment, j.GetBareJid().toString());
			}
			else {
				fragmentTransaction.remove(fragment);
				fragmentTransaction.add(R.id.contactMainLLayout, fragment, j.GetBareJid().toString());
			}
		}

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
			addContact();
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

	protected void onResume() {
		super.onResume();
		isActive = true;
	}
	protected void onDestroy() {
		super.onDestroy();
		isActive = false;
	}
	protected void onPause() {
		super.onPause();
		isActive = false;
	}
}