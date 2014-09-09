package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageFragment extends Fragment {
	private String username;
	private String message;
	private boolean isLocalUser;

	public MessageFragment() {} // Android BS
	public MessageFragment (String useraname, String message, boolean localUser) {
		this.username = username;
		this.message = message;
		isLocalUser = localUser;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v;

		if (isLocalUser) {
			v =  inflater.inflate(R.layout.localuser_message_fragment, container, false);

			((TextView) v.findViewById(R.id.chatMessage)).setText("Adam is da BESTESET! <3");
			((TextView) v.findViewById(R.id.messageDate)).setText("date");
		}
		else {
			v =  inflater.inflate(R.layout.otheruser_message_fragment, container, false);

			((TextView) v.findViewById(R.id.chatMessage)).setText("Adam is da BESTESET! <3");
			((TextView) v.findViewById(R.id.messageDate)).setText("date");
		}

		return v;
	}

}
