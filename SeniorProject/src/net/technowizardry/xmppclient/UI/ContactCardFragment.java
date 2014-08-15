package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactCardFragment extends Fragment {
	private String id;
	private String otherId;
	private Intent clicked;

	public ContactCardFragment() {} // Android BS
	public ContactCardFragment (String username, String otherUsername) {
		id = username;
		otherId = otherUsername;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v =  inflater.inflate(R.layout.contact_fragment, container, false);

		((TextView) v.findViewById(R.id.chatConv)).setText("Adam is da BESTESET! <3");
		((TextView) v.findViewById(R.id.chatUsername)).setText("Username");
		((TextView) v.findViewById(R.id.chatDate)).setText("date");

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

