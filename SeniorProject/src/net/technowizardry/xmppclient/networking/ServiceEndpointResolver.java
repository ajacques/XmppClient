package net.technowizardry.xmppclient.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceEndpointResolver {
	private final DnsResolver dnsResolver;

	public ServiceEndpointResolver(DnsResolver resolver) {
		dnsResolver = resolver;
	}

	public List<HostnameEndPoint> fetchEndpoints(String domain) throws IOException {
		return performSRVLookup(domain);
	}

	public List<HostnameEndPoint> performSRVLookup(String domain) throws IOException {
		List<ServiceRecord> records = dnsResolver.performSRVLookup(String.format("_xmpp-client._tcp.%s", domain));
		return sortRecords(records);
	}

	protected List<HostnameEndPoint> sortRecords(List<ServiceRecord> records) {
		List<HostnameEndPoint> result = new ArrayList<HostnameEndPoint>();

		for (ServiceRecord record : records) {
			result.add(new HostnameEndPoint(record.getAddress(), record.getPort()));
		}

		return result;
	}
}
