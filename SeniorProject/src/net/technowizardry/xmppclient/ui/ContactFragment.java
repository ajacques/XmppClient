package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactFragment extends Fragment {
	private Jid id;

	public ContactFragment (Jid jid) {
		id = jid;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.contact_fragment, container, false);

		((TextView) v.findViewById(R.id.contactUsername)).setText(id.Username().toString());

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent clicked = new Intent(getActivity(), ChatActivity.class);
				clicked.putExtra("local", id.Username());
				clicked.putExtra("domain", id.Domain());
				startActivity(clicked);
			}
		});

		return v;
	}
}
