package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class NewConversationActivity extends Activity {
	private String localName;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);

		loadRoster();
	}

	private void loadRoster() {
		localName = "GET ROSTER NAMES";

		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		ContactFragment fragment = new ContactFragment(localName);
		fragmentTransaction.add(R.id.contactMainLLayout , fragment, "one");
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