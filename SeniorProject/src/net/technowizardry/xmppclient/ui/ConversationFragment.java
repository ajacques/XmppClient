package net.technowizardry.xmppclient.ui;

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
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v =  inflater.inflate(R.layout.conversation_fragment, container, false);

		((TextView) v.findViewById(R.id.conversationMessage)).setText(message);
		((TextView) v.findViewById(R.id.chatUsername)).setText(otherId);
		((TextView) v.findViewById(R.id.chatRecentDate)).setText(date);

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clicked = new Intent(getActivity(), ChatActivity.class);
				clicked.putExtra("domain", otherDomain);
				clicked.putExtra("local", otherId);
				startActivity(clicked);
			}
		});

		return v;
	}

}

