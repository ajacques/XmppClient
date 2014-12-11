package net.technowizardry.xmppclient.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateUtils;
import net.technowizardry.xmppclient.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("Instantiatable")
public class ConversationFragment extends Fragment {
	private String otherId;
	private String message;
	private String date;
	private String otherDomain;
	private Intent clicked;

	public ConversationFragment (String otherUsername, String otherDomain, String message, String date) {
		otherId = otherUsername;
		this.otherDomain = otherDomain;
		this.message = message;
		this.date = date;

		byte[] b = new byte[] { (byte)0xf0, (byte)0x9f, (byte)0x98, (byte)0x81 };
		byte[] b2 = new byte[] { (byte)0xf0, (byte)0x9f, (byte)0x98, (byte)0x9e };

		this.message = this.message.replaceAll(":-\\)", new String(b));
		this.message = this.message.replaceAll(":-\\(", new String(b2));
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v =  inflater.inflate(R.layout.conversation_fragment, container, false);

		try {
			Date d = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",new Locale("en_US")).parse(date);
			date = DateUtils.getRelativeTimeSpanString(d.getTime()).toString();
			if(date.equals("0 minutes ago")) date = "A few seconds ago";
			//(DateTime.Now - prevDate).TotalSeconds <= 1
		} catch (ParseException e) {
			e.printStackTrace();
		}

		((TextView) v.findViewById(R.id.conversationMessage)).setText(message);
		((TextView) v.findViewById(R.id.chatUsername)).setText(otherId);
		((TextView) v.findViewById(R.id.chatRecentDate)).setText(date);

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clicked = new Intent(getActivity(), ChatActivity.class);
				clicked.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				clicked.putExtra("domain", otherDomain);
				clicked.putExtra("local", otherId);
				startActivity(clicked);
			}
		});

		return v;
	}

}

