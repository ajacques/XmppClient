package net.technowizardry.xmpp.messages;

import java.io.IOException;

import junit.framework.TestCase;
import net.technowizardry.XMLStreamFactory;
import net.technowizardry.XMLStreamFactoryFactory;
import net.technowizardry.XMLWriter;
import net.technowizardry.xmpp.MemoryStream;

import org.junit.Before;
import org.junit.Test;

public class CompressionMessageTests extends TestCase {
	private MemoryStream memoryStream;
	private XMLWriter writer;
	private final XMLStreamFactory streamFactory = XMLStreamFactoryFactory.newInstance();

	@Before
	public void setUp() throws Exception {
		memoryStream = new MemoryStream();
		writer = streamFactory.CreateWriter(memoryStream.getStream());
	}

	@Test
	public void testSerialize() throws IOException {
		CompressionInitMessage message = new CompressionInitMessage("zlib");
		message.WriteMessage(writer);
		writer.Flush();
		assertEquals("<compress xmlns=\"http://jabber.org/protocol/compress\"><method>zlib</method></compress>", memoryStream.readAll());
	}
}
