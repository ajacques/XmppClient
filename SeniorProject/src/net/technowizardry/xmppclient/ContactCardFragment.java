package net.technowizardry.xmppclient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactCardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.contact_fragment, container, false);

        ((TextView) v.findViewById(R.id.chatConv)).setText("Adam is da BESTESET! <3");
        ((TextView) v.findViewById(R.id.chatUsername)).setText("Username");
        ((TextView) v.findViewById(R.id.chatDate)).setText("date");
        return v;
    }
    
}

