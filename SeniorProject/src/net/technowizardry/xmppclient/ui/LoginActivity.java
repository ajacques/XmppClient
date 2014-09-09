package net.technowizardry.xmppclient.ui;

import java.io.IOException;
import java.util.List;

import net.technowizardry.xmppclient.R;
import net.technowizardry.xmppclient.networking.*;
import net.technowizardry.HostnameEndPoint;
import net.technowizardry.xmppclient.ui.CreateAccountActivity;
import net.technowizardry.xmppclient.ui.HomeActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	private String username;
	private String domainName;
	private String localName;
	private String password;
	private TextView invalid;
	private EditText loginText;
	private EditText passText;
	private Button signInButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		invalid = (TextView)findViewById(R.id.invalidUsernameTextView);
		loginText = (EditText)findViewById(R.id.loginUsername);
		passText = (EditText)findViewById(R.id.loginPassword);
		signInButton = (Button)findViewById(R.id.signInButton);
		username = null;
		domainName = null;
		localName = null;
		password = null;
		signInButton.setEnabled(true);
	}

	private class performSRVCheck extends AsyncTask<String, Void, List<HostnameEndPoint>> {
		Boolean SRVPass = true;
		@SuppressWarnings("unused")
		Context mContext;
		performSRVCheck(Context context) {
			this.mContext = context;
		}

		@Override
		protected List<HostnameEndPoint> doInBackground(String... url) {
			DnsResolver DNSResolver = new ExternalLibraryBasedDnsResolver();
			ServiceEndpointResolver resolver = new ServiceEndpointResolver(DNSResolver);
			List<HostnameEndPoint> targets = null;
			try {
				targets = resolver.performSRVLookup(domainName);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (RuntimeException ex) {
				SRVPass = false;
			}

			return targets;
		}

		protected void onPostExecute(List<HostnameEndPoint> h) {
			if(SRVPass) {
				invalid.setText("");
				activityFinish();
			}
			else {
				invalid.setText("Invalid Domain Name");
			}
			signInButton.setEnabled(true); // DO THIS AFTER LOGIN FLOW. THIS IS CURRENTLY THE END
		}

	}

	public void onClick(View v) {
		final int id = v.getId();
		switch(id) {
		case R.id.signInButton:
			signInButton.setEnabled(false);

			username = loginText.getText().toString().trim();
			int index = username.indexOf("@");
			domainName = (index == -1) ? "" : username.substring(index+1, username.length());
			password = passText.getText().toString();
			if (domainName == "" || password == "") {
				invalid.setText("Invalid Username or Password");
				signInButton.setEnabled(true);
			}
			else {
				localName = username.substring(0,index);

				performSRVCheck SRVCheck = new performSRVCheck(getApplicationContext());
				SRVCheck.execute(domainName);
			}
			break;
		case R.id.createAccount:
			startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
			break;
		}
	}

	//call this when Activity is finished
	public void activityFinish() {
		Intent nextIntent = new Intent(getApplicationContext(), HomeActivity.class);
		nextIntent.putExtra("domainName", domainName);
		nextIntent.putExtra("localName", localName);
		nextIntent.putExtra("password", password);
		startActivity(nextIntent);
		overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
		finish();
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