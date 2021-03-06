package net.technowizardry.xmpp;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import net.technowizardry.XMLObjectType;
import net.technowizardry.XMLReader;
import net.technowizardry.XMLStreamFactory;
import net.technowizardry.XMLStreamFactoryFactory;
import net.technowizardry.XMLWriter;
import net.technowizardry.xmpp.messages.WritableXmppMessage;

import org.junit.Before;
import org.junit.Test;

import scala.Function1;
import scala.runtime.AbstractFunction1;
import scala.runtime.BoxedUnit;

public class XmppStreamTests extends TestCase {
	private static final String TEST_STRING = "TEST_STRING";
	private final XMLStreamFactory streamFactory = XMLStreamFactoryFactory.newInstance();
	private XmppStream stream;
	private PipedInputStream toServerStream;
	private BufferedWriter fromServer;
	private Semaphore messageLatch = new Semaphore(0);
	private XMLReader reader;

	private class DummyMessage implements WritableXmppMessage {
		@Override
		public void WriteMessage(XMLWriter writer) {
			writer.WriteText(TEST_STRING);
		}
	}

	@Before
	public void setUp() throws Exception {
		PipedOutputStream fromServerStream = new PipedOutputStream();
		toServerStream = new PipedInputStream();
		InputStream inputStream = new PipedInputStream(fromServerStream);
		OutputStream outputStream = new PipedOutputStream(toServerStream);
		fromServer = new BufferedWriter(new OutputStreamWriter(fromServerStream));

		Function1<XMLReader, BoxedUnit> readerListener = new AbstractFunction1<XMLReader, BoxedUnit>() {
			@Override
			public BoxedUnit apply(XMLReader arg0) {
				XmppStreamTests.this.handleMessage(arg0);
				return null;
			}
		};
		stream = new XmppStream(inputStream, outputStream, streamFactory, readerListener);
		stream.Flush();
		XMLReader reader = streamFactory.CreateReader(toServerStream);
		assertTrue(reader.HasNext());
		assertEquals(XMLObjectType.Document(), reader.NodeType());
	}

	@Test
	public void testSendMessage() throws IOException {
		stream.SendMessage(new DummyMessage());
		stream.Flush();
		assertEquals(TEST_STRING, readAll());
	}

	@Test
	public void testReceiveMessage() throws IOException, InterruptedException {
		stream.StartReaderThread();
		fromServer.write("<stream:stream xmlns:stream='foobar' stream:attrib='attrib_value'>");
		fromServer.flush();
		messageLatch.tryAcquire(5, TimeUnit.SECONDS);
		assertEquals(XMLObjectType.StartElement(), reader.NodeType());
		assertEquals("stream", reader.LocalName());
		assertEquals("foobar", reader.NamespaceURI());
		assertEquals("attrib_value", reader.GetAttributeValue("foobar", "attrib"));
		messageLatch.release();
		stream.Shutdown();
	}

	private String readAll() throws IOException {
		int avail = toServerStream.available();
		byte[] buffer = new byte[avail];
		toServerStream.read(buffer);
		return new String(buffer);
	}

	private void handleMessage(XMLReader reader) {
		this.reader = reader;
		messageLatch.release();
		try {
			Thread.sleep(500);
			messageLatch.tryAcquire(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			fail(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
