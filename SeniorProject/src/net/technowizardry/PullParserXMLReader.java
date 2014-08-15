package net.technowizardry;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import scala.Enumeration.Value;

public class PullParserXMLReader extends XMLReader {
	private final XmlPullParser parser;

	public PullParserXMLReader(InputStream stream) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);;
			parser = factory.newPullParser();
			parser.setInput(stream, "UTF-8");
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean HasNext() {
		return true;
	}

	@Override
	public void Next() {
		try {
			parser.next();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Value NodeType() {
		try {
			switch (parser.getEventType())
			{
			case XmlPullParser.START_DOCUMENT:
				return XMLObjectType.Document();
			case XmlPullParser.START_TAG:
				return XMLObjectType.StartElement();
			case XmlPullParser.TEXT:
				return XMLObjectType.Text();
			case XmlPullParser.END_TAG:
				return XMLObjectType.EndElement();
			case XmlPullParser.END_DOCUMENT:
				return XMLObjectType.EndDocument();
			default:
				throw new RuntimeException("UGHHHH" + parser.getEventType());
			}
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String LocalName() {
		try {
			return parser.getName();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String NamespaceURI() {
		try {
			return parser.getNamespace();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String GetAttributeValue(String ns, String key) {
		try {
			return parser.getAttributeValue(ns, key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String ElementText() {
		try {
			return parser.getText();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
