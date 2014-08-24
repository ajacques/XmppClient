package net.technowizardry.xmpp.messages;

import static org.junit.Assert.*;
import net.technowizardry.XMLObjectType;
import net.technowizardry.XMLReader;
import net.technowizardry.xmpp.XmlReaderHelpers;
import net.technowizardry.xmpp.XmppPackets;

import org.junit.Test;

public class SaslSuccessMessageTests {
	@Test
	public void testParse() {
		XMLReader reader = XmlReaderHelpers.readerFromString(XmppPackets.SASL_SUCCESS);
		XmppProtocolMessage message = SaslParser.UnpackSuccess(reader);
		assertNotNull(message);
		assertTrue(message instanceof SaslSuccessMessage);
		assertEquals(XMLObjectType.EndElement(), reader.NodeType());
	}
}
