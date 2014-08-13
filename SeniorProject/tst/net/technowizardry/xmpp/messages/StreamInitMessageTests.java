package net.technowizardry.xmpp.messages;

import static org.junit.Assert.*;
import junit.framework.TestCase;
import net.technowizardry.XMLObjectType;
import net.technowizardry.XMLReader;
import net.technowizardry.xmpp.XmlReaderHelpers;
import net.technowizardry.xmpp.XmppNamespaces;
import net.technowizardry.xmpp.XmppPackets;

import org.junit.Test;

import scala.collection.immutable.List;

public class StreamInitMessageTests extends TestCase {
	@Test
	public void testNoFeatures() {
		StreamInitMessage message = unpackMessage(XmppPackets.buildStreamInit());
		assertFalse(message.SupportsStartTls());
		assertFalse(message.IsFeatureRequired("starttls", XmppNamespaces.Tls()));
		assertEquals(0, message.GetFeatures().size());
	}

	@Test
	public void testSupportsRequiredStartTls() {
		StreamInitMessage message = unpackMessage(XmppPackets.buildStreamInit(XmppPackets.STREAM_INIT_STARTTLS_REQUIRED));
		assertTrue(message.SupportsStartTls());
		assertTrue(message.IsFeatureRequired("starttls", XmppNamespaces.Tls()));
		assertEquals(1, message.GetFeatures().size());
	}

	@Test
	public void testSupportsStartTls() {
		StreamInitMessage message = unpackMessage(XmppPackets.buildStreamInit(XmppPackets.STREAM_INIT_STARTTLS));
		assertTrue(message.SupportsStartTls());
		assertFalse(message.IsFeatureRequired("starttls", XmppNamespaces.Tls()));
		assertEquals(1, message.GetFeatures().size());
	}

	@Test
	public void testPlainMechanismParse() {
		StreamInitMessage message = unpackMessage(XmppPackets.buildStreamInit(XmppPackets.buildMechanismList("PLAIN")));
		List<String> mechs = message.GetMechanisms();
		assertNotNull(mechs);
		assertTrue(mechs.contains("PLAIN"));
	}

	@Test
	public void testFullParseStreamInit() {
		StreamInitMessage message = unpackMessage(XmppPackets.STREAM_INIT_NOFEATURE);
		assertFalse(message.SupportsStartTls());
	}

	@Test
	public void testSupportsCompression() {
		StreamInitMessage message = unpackMessage(XmppPackets.buildStreamInit(XmppPackets.STREAM_INIT_COMPRESS));
		List<XmppFeature> features = message.GetFeatures();
		XmppFeature feature = message.GetFeature("compression", XmppNamespaces.CompressionFeature());
		assertNotNull(feature);
	}

	@Test
	public void testUnknownFeature() {
		StreamInitMessage message = unpackMessage(XmppPackets.buildStreamInit(XmppPackets.STREAM_INIT_WEIRD_FEATURE));
		assertNotNull(message);
	}

	private StreamInitMessage unpackMessage(String xml) {
		XMLReader reader = XmlReaderHelpers.readerFromString(xml);
		reader.Next();
		XmppProtocolMessage message = StreamInitMessageParser.Unpack(reader);
		assertNotNull(message);
		assertTrue(message instanceof StreamInitMessage);
		assertEquals(XMLObjectType.EndElement(), reader.NodeType());
		return (StreamInitMessage)message;
	}
}
