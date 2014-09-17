package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactFragment extends Fragment {
	private String id;
	
	public ContactFragment (String username) {
		id = username;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.contact_fragment, container, false);

		((TextView) v.findViewById(R.id.contactUsername)).setText(id);

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent clicked = new Intent(getActivity(), ChatActivity.class);
				clicked.putExtra("clientUsername", id);
				startActivity(clicked);
			}
		});

		return v;
	}

}
