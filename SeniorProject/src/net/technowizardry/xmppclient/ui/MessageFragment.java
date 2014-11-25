package net.technowizardry.xmppclient.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
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
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v;

		try {
			Date d = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",new Locale("en_US")).parse(date);
			date = DateUtils.getRelativeTimeSpanString(d.getTime()).toString();
			if(date.equals("0 minutes ago")) date = "A few seconds ago";
		} catch (ParseException e) {
			e.printStackTrace();
		}

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
