package net.technowizardry.xmppclient.networking;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ServiceEndpointResolverTests extends TestCase {
	private ServiceEndpointResolver resolver;

	static class LocalResolver implements DnsResolver {
		@Override
		public List<ServiceRecord> performSRVLookup(String domain)
				throws IOException, UnknownHostException {
			List<ServiceRecord> records = new ArrayList<ServiceRecord>();

			records.add(new ServiceRecord("example.com", 5222, 10, 10));

			return records;
		}
	}

	@Override
	protected void setUp() {
		resolver = new ServiceEndpointResolver(new LocalResolver());
	}

	public void testFindEndpoint() throws IOException {
		List<HostnameEndPoint> targets = resolver.performSRVLookup("domain.local");
		assertNotNull(targets);
	}
}
