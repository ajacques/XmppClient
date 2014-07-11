package net.technowizardry.xmppclient.networking;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public interface DnsResolver {
	 List<ServiceRecord> performSRVLookup(String domain) throws IOException, UnknownHostException;
}
