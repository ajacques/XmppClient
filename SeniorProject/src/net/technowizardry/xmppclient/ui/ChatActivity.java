package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ChatActivity extends Activity {
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);

		loadMessages();
	}

	private void loadMessages() {
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		MessageFragment fragment = new MessageFragment("Cody", "Hey whats uppp", true);
		fragmentTransaction.add(R.id.chatMainLL , fragment, "one");
		fragmentTransaction.commit();
		fragment = new MessageFragment("Adam", "nothing, you are the most important person ever to me", false);
		fragmentTransaction.add(R.id.chatMainLL , fragment, "one");
		fragmentTransaction.commit();
	}

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

}
