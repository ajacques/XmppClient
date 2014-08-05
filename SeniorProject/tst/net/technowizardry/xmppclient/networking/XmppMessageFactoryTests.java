package net.technowizardry.xmppclient.networking;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import net.technowizardry.XMLReader;
import net.technowizardry.XMLStreamFactory;
import net.technowizardry.XMLStreamFactoryFactory;

import org.junit.Before;
import org.junit.Test;

import scala.Function1;

public class XmppMessageFactoryTests {
	private XmppMessageFactory factory;
	private final XMLStreamFactory streamFactory = XMLStreamFactoryFactory.newInstance();

	@Before
	public void setUp() {
		factory = new XmppMessageFactory();
	}

	@Test
	public void testFromXMLReader() {
		Function1<XMLReader, XmppProtocolMessage> parser = factory.FindParserForGlobalName(XmppNamespaces.StreamsNS(), "stream");
		assertNotNull(parser);
	}

	@Test
	public void testFullParseStreamInit() {
		XMLReader reader = readerFromString(XmppPackets.STREAM_INIT);
		reader.Next();
		XmppProtocolMessage message = factory.FromXMLReader(reader);
		assertNotNull(message);
		assertTrue(message instanceof StreamInitMessage);
	}

	private XMLReader readerFromString(String xml) {
		return streamFactory.CreateReader(new ByteArrayInputStream(xml.getBytes()));
	}
}
