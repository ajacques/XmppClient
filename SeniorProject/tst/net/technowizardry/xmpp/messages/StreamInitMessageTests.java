package net.technowizardry.xmpp.messages;

import static org.junit.Assert.*;
import net.technowizardry.XMLObjectType;
import net.technowizardry.XMLReader;
import net.technowizardry.xmpp.XmlReaderHelpers;
import net.technowizardry.xmpp.XmppPackets;

import org.junit.Before;
import org.junit.Test;

public class StreamInitMessageTests {
	@Test
	public void testSupportsStartTls() {
		StreamInitMessage message = unpackMessage(XmppPackets.buildStreamInit(XmppPackets.STREAM_INIT_STARTTLS_REQUIRED));
		assertTrue(message.SupportsStartTls());
	}

	@Test
	public void testFullParseStreamInit() {
		StreamInitMessage message = unpackMessage(XmppPackets.STREAM_INIT_NOFEATURE);
		assertFalse(message.SupportsStartTls());
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
