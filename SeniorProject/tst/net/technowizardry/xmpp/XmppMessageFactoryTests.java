package net.technowizardry.xmpp;

import static org.junit.Assert.*;
import junit.framework.TestCase;
import net.technowizardry.XMLReader;
import net.technowizardry.xmpp.messages.XmppProtocolMessage;

import org.junit.Before;
import org.junit.Test;

import scala.Function1;

public class XmppMessageFactoryTests extends TestCase {
	private XmppMessageFactory factory;

	@Before
	public void setUp() {
		factory = new XmppMessageFactory();
	}

	@Test
	public void testFromXMLReader() {
		Function1<XMLReader, XmppProtocolMessage> parser = factory.FindParserForGlobalName(XmppNamespaces.Streams(), "stream");
		assertNotNull(parser);
	}
}
