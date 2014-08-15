package net.technowizardry.xmpp.messages;

import static org.junit.Assert.*;

import java.io.IOException;

import net.technowizardry.XMLStreamFactory;
import net.technowizardry.XMLStreamFactoryFactory;
import net.technowizardry.XMLWriter;
import net.technowizardry.xmpp.MemoryStream;

import org.junit.Before;
import org.junit.Test;

public class SaslPlainAuthMessageTests {
	private MemoryStream memoryStream;
	private XMLWriter writer;
	private final XMLStreamFactory streamFactory = XMLStreamFactoryFactory.newInstance();

	@Before
	public void setUp() throws Exception {
		memoryStream = new MemoryStream();
		writer = streamFactory.CreateWriter(memoryStream.getStream());
	}

	@Test
	public void test() throws IOException {
		SaslPlainAuthMessage message = new SaslPlainAuthMessage("test", "test");
		message.WriteMessage(writer);
		assertEquals("<auth xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\" mechanism=\"PLAIN\">AHRlc3QAdGVzdA==</auth>", memoryStream.readAll());
	}
}
