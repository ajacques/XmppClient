package net.technowizardry.xmpp.messages;

import static org.junit.Assert.*;
import junit.framework.TestCase;
import net.technowizardry.XMLReader;
import net.technowizardry.xmpp.XmlReaderHelpers;

import org.junit.Test;

public class RosterParserMessageTests extends TestCase {

	@Test
	public void testParser() {
		XMLReader reader = XmlReaderHelpers.readerFromString("<iq xmlns='jabber:client' to='user@example.com/baloney' type='result' id='roster_1'>"
				+ "<query xmlns='jabber:iq:roster'>"
					+ "<item jid='romeo@example.net' name='Romeo' subscription='both'>"
					+ "<group>Friends</group>"
					+ "</item>"
					+ "<item jid='mercutio@example.org' name='Mercutio' subscription='both' />"
				+ "</query>");
		reader.Next(); // Advanced to <query because that's the state the reader will be in when the parser gets to it
		IqResponseBody body = IqParser.UnpackRoster(reader);
		assertNotNull(body);
	}

}
