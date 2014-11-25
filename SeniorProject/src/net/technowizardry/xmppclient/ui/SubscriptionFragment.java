package net.technowizardry.xmppclient.ui;

import net.technowizardry.xmpp.Jid;
import net.technowizardry.xmppclient.ConnectionManagerService;
import net.technowizardry.xmppclient.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SubscriptionFragment extends Fragment implements OnClickListener {
	private Jid jid;

	public SubscriptionFragment (Jid jid) {
		this.jid = jid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.subscription_request, container, false);

		((TextView) v.findViewById(R.id.subContactUsername)).setText(jid.Username().toString());

		ImageView accept = (ImageView) v.findViewById(R.id.subContactAccept);
		ImageView decline = (ImageView) v.findViewById(R.id.subContactDecline);
		accept.setOnClickListener(this);
		decline.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.subContactAccept:
				ConnectionManagerService.acceptSubRequest(jid);
				ConnectionManagerService.pendingSubscriptions.remove(jid);
				break;
			case R.id.subContactDecline:
				NewConversationActivity.declineSubRequest(jid);
				ConnectionManagerService.pendingSubscriptions.remove(jid);
				break;
		}
	}
}
