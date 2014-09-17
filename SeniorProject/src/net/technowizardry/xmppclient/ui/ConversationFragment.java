package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConversationFragment extends Fragment {
	private String id;
	private String otherId;
	private String message;
	private String date;
	private Intent clicked;

	public ConversationFragment (String localUsername, String otherUsername, String message, String date) {
		id = localUsername;
		otherId = otherUsername;
		this.message = message;
		this.date = date;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v =  inflater.inflate(R.layout.conversation_fragment, container, false);

		((TextView) v.findViewById(R.id.conversationMessage)).setText(message);
		((TextView) v.findViewById(R.id.chatUsername)).setText(id);
		((TextView) v.findViewById(R.id.chatRecentDate)).setText(date);

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clicked = new Intent(getActivity(), ChatActivity.class);
				clicked.putExtra("clientUsername", id);
				clicked.putExtra("otherUsername", otherId);
				startActivity(clicked);
			}
		});

		return v;
	}

}

