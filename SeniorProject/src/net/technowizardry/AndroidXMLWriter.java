package net.technowizardry;

import java.io.IOException;
import java.io.OutputStream;

import net.technowizardry.XMLWriter;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class AndroidXMLWriter implements XMLWriter {
	private final OutputStream innerStream;
	private final XmlSerializer xmlSerializer;

	public AndroidXMLWriter(OutputStream outputStream) {
		innerStream = outputStream;
		try {
			xmlSerializer = Xml.newSerializer();
			xmlSerializer.setOutput(outputStream, "utf-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void WriteStartDocument(String encoding, String version) {
		try {
			xmlSerializer.startDocument(encoding, false);
		} catch (IOException e) {
			// No fucks given
			throw new RuntimeException(e);
		}
	}

	@Override
	public void WriteStartElement(String prefix, String elemName, String namespace) {
		try {
			xmlSerializer.startTag(namespace, elemName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void WriteAttribute(String name, String value, String namespace) {
		try {
			xmlSerializer.attribute(namespace, name, value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void Flush() {
		try {
			innerStream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void WriteNamespace(String prefix, String namespace) {
	}

	@Override
	public void WriteDefaultNamespace(String namespace) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WriteText(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WriteStartElement(String elemName, String namespace) {
		// TODO Auto-generated method stub
		
	}
}
