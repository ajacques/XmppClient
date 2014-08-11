package net.technowizardry.xmpp;

import static org.junit.Assert.*;

import net.technowizardry.XMLReader;
import net.technowizardry.xmpp.messages.StreamInitMessage;
import net.technowizardry.xmpp.messages.StreamInitMessageParser;
import net.technowizardry.xmpp.messages.XmppFeature;

import org.junit.Before;
import org.junit.Test;

import scala.collection.immutable.List;

public class StreamInitMessageTests {
	private static final String TEST_SERVER = "technowizardry.net";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSerialize() {
		StreamInitMessage message = new StreamInitMessage(TEST_SERVER, null);
		assertEquals(TEST_SERVER, message.GetServer());
	}

	@Test
	public void testDeserialize() {
		String packet = XmppPackets.buildStreamInit(XmppPackets.STREAM_INIT_STARTTLS_REQUIRED);
		XMLReader reader = XmlReaderHelpers.readerFromString(packet);
		reader.Next();
		StreamInitMessage message = (StreamInitMessage)StreamInitMessageParser.Unpack(reader);
		assertNotNull(message);
		List<XmppFeature> features = message.GetFeatures();
		assertNotNull(features);
		assertEquals(1, features.size());
		XmppFeature feature = features.head();
		assertNotNull(feature);
		assertFalse(feature.IsRequired());
		assertEquals(XmppNamespaces.Tls(), feature.GetNamespace());
		assertEquals("starttls", feature.GetName());
	}
}
