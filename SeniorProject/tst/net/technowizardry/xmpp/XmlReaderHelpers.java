package net.technowizardry.xmpp;

import java.io.ByteArrayInputStream;

import net.technowizardry.XMLReader;
import net.technowizardry.XMLStreamFactory;
import net.technowizardry.XMLStreamFactoryFactory;

public class XmlReaderHelpers {
	private static final XMLStreamFactory streamFactory = XMLStreamFactoryFactory.newInstance();

	public static XMLReader readerFromString(String xml) {
		XMLReader reader = streamFactory.CreateReader(new ByteArrayInputStream(xml.getBytes()));
		reader.Next();
		return reader;
	}
}
