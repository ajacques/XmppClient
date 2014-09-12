package net.technowizardry.xmppclient.networking;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.xbill.DNS.ARecord;

public interface DnsResolver {
	 SortedSet<ServiceRecord> performSRVLookup(String domain) throws IOException, UnknownHostException;
	 Set<ARecord> performALookup(String domain) throws IOException, UnknownHostException;
}
