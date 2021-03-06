package net.technowizardry;

import java.io.InputStream;
import java.io.OutputStream;

import net.technowizardry.XMLReader;
import net.technowizardry.XMLStreamFactory;
import net.technowizardry.XMLWriter;

public class AndroidXMLStreamFactory implements XMLStreamFactory {
	@Override
	public XMLReader CreateReader(InputStream stream) {
		return new PullParserXMLReader(stream);
	}

	@Override
	public XMLWriter CreateWriter(OutputStream stream) {
		return new AndroidXMLWriter(stream);
	}
}
