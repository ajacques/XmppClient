package net.technowizardry.xmppclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		LinearLayout fragContainer = (LinearLayout) findViewById(R.id.homeMainLLayout);
		for(int i = 0; i < 15; i++) {
	        LinearLayout ll = new LinearLayout(this);
	        ll.setOrientation(LinearLayout.HORIZONTAL);
	        ll.setId(1234+i); 
	        getFragmentManager().beginTransaction().add(ll.getId(), new ContactCardFragment()).commit();
	        fragContainer.addView(ll);
		}
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