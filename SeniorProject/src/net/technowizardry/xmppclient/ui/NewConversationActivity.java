package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmpp.XmppContact;
import net.technowizardry.xmppclient.ConnectionManagerService;
import net.technowizardry.xmppclient.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class NewConversationActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);

		loadRoster();
	}

	private void loadRoster() {
		FragmentManager fragmentManager;
		FragmentTransaction fragmentTransaction;
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		Iterable<XmppContact> roster;
		roster = ConnectionManagerService.getRoster();
		for (XmppContact contact : roster) {
			ContactFragment fragment = new ContactFragment(contact.Username());
			fragmentTransaction.add(R.id.contactMainLLayout , fragment);
		}
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
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