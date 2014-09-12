package net.technowizardry.xmppclient.networking;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.xbill.DNS.ARecord;

import net.technowizardry.HostnameEndPoint;

public class ServiceEndpointResolver {
	private final DnsResolver dnsResolver;

	public ServiceEndpointResolver(DnsResolver resolver) {
		dnsResolver = resolver;
	}

	public List<HostnameEndPoint> fetchEndpoints(String domain) throws IOException {
		try {
			return performSRVLookup(domain);
		} catch (UnknownHostException e) {
			return performALookup(domain);
		}
	}

	public List<HostnameEndPoint> performSRVLookup(String domain) throws IOException {
		SortedSet<ServiceRecord> records = dnsResolver.performSRVLookup(String.format("_xmpp-client._tcp.%s", domain));
		return sortRecords(records);
	}

	public List<HostnameEndPoint> performALookup(String domain) throws IOException {
		Set<ARecord> records = dnsResolver.performALookup(domain);
		List<HostnameEndPoint> result = new ArrayList<HostnameEndPoint>();

		for (ARecord record : records) {
			result.add(new HostnameEndPoint(record.getAddress().toString(), 5222));
		}

		return result;
	}

	protected List<HostnameEndPoint> sortRecords(SortedSet<ServiceRecord> records) {
		List<HostnameEndPoint> result = new ArrayList<HostnameEndPoint>();

		for (ServiceRecord record : records) {
			result.add(new HostnameEndPoint(record.getAddress(), record.getPort()));
		}

		return result;
	}
}
