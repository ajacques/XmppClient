package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageFragment extends Fragment {
	private String message;
	private String date;
	private boolean isLocalUser;

	public MessageFragment() {} // Android BS
	public MessageFragment (String message, String date, boolean isLocalUser) {
		this.message = message;
		this.date = date;
		this.isLocalUser = isLocalUser;
		System.out.println(date);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v;

		if (isLocalUser) {
			v =  inflater.inflate(R.layout.localuser_message_fragment, container, false);

			((TextView) v.findViewById(R.id.chatMessage)).setText(message);
			((TextView) v.findViewById(R.id.messageDate)).setText(date);
		}
		else {
			v =  inflater.inflate(R.layout.otheruser_message_fragment, container, false);

			((TextView) v.findViewById(R.id.otherChatMessage)).setText(message);
			((TextView) v.findViewById(R.id.otherMessageDate)).setText(date);
		}

		return v;
	}

}
