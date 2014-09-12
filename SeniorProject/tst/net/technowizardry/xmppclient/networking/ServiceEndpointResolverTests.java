package net.technowizardry.xmppclient.networking;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.technowizardry.HostnameEndPoint;
import net.technowizardry.xmppclient.networking.DnsResolver;
import net.technowizardry.xmppclient.networking.ServiceEndpointResolver;
import net.technowizardry.xmppclient.networking.ServiceRecord;

import org.junit.Test;
import org.xbill.DNS.ARecord;

import junit.framework.TestCase;

public class ServiceEndpointResolverTests extends TestCase {
	private ServiceEndpointResolver resolver;

	static class LocalResolver implements DnsResolver {
		@Override
		public SortedSet<ServiceRecord> performSRVLookup(String domain)
				throws IOException, UnknownHostException {
			SortedSet<ServiceRecord> records = new TreeSet<ServiceRecord>();

			records.add(new ServiceRecord("example.com", 5222, 10, 10));

			return records;
		}

		@Override
		public Set<ARecord> performALookup(String domain) throws IOException, UnknownHostException {
			// TODO Auto-generated method stub
			return null;
		}
	}

	@Override
	protected void setUp() {
		resolver = new ServiceEndpointResolver(new LocalResolver());
	}

	@Test
	public void testFindEndpoint() throws IOException {
		List<HostnameEndPoint> targets = resolver.performSRVLookup("domain.local");
		assertNotNull(targets);
		assertEquals(1, targets.size());
	}
}
