package net.technowizardry.xmppclient.networking;

import java.net.UnknownHostException;
import java.util.List;

import org.xbill.DNS.TextParseException;

import junit.framework.TestCase;

public class ExternalLibraryBasedDnsResolverTests extends TestCase {
	private ExternalLibraryBasedDnsResolver resolver;
	private static final String TEST_DOMAIN = "_xmpp-client.tcp_technowizardry.net";
	private static final String TEST_SERVER = "vps1.technowizardry.net.";

	protected void setUp() throws Exception {
		super.setUp();
		resolver = new ExternalLibraryBasedDnsResolver();
	}

	public void testQuicklookup() throws TextParseException, UnknownHostException {
		List<ServiceRecord> records = resolver.performSRVLookup("_xmpp-client._tcp.technowizardry.net");
		assertNotNull(records);
		assertEquals(1, records.size());
		ServiceRecord record = records.get(0);
		assertEquals(TEST_SERVER, record.getAddress());
		assertEquals(5222, record.getPort());
		assertEquals(10, record.getWeight());
		assertEquals(10, record.getPriority());
	}
}