package net.technowizardry.xmpp.messages;

import static org.junit.Assert.*;
import net.technowizardry.XMLReader;
import net.technowizardry.xmpp.XmlReaderHelpers;
import net.technowizardry.xmpp.XmppPackets;

import org.junit.Test;

public class IqResponseMessageTests {
	private static final String BIND_RESPONSE = "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><jid>user@example.com/test</jid></bind>";
	@Test
	public void testUnpackResponse() {
		XMLReader reader = XmlReaderHelpers.readerFromString(XmppPackets.buildIqMessage("result", BIND_RESPONSE));
		XmppProtocolMessage message = IqParser.Unpack(reader);
		assertNotNull(message);
		assertTrue(message instanceof IqResponseMessage);
		IqResponseMessage msg = (IqResponseMessage)message;
		assertEquals("1", msg.MessageId());
	}
}
