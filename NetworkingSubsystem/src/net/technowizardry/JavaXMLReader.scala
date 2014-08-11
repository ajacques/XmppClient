package net.technowizardry

import javax.xml.stream.{XMLInputFactory,XMLStreamConstants}
import java.io.InputStream

class JavaXMLReader(stream : InputStream) extends XMLReader {
	var inner =  XMLInputFactory.newFactory().createXMLStreamReader(stream, "utf-8")
	def HasNext() : Boolean = inner.hasNext
	def Next() = inner.next()
	def NodeType() : XMLObjectType.XMLObjectType = {
		return inner.getEventType() match {
			case XMLStreamConstants.START_DOCUMENT => XMLObjectType.Document
			case XMLStreamConstants.START_ELEMENT => XMLObjectType.StartElement
			case XMLStreamConstants.END_ELEMENT => XMLObjectType.EndElement
			case XMLStreamConstants.CHARACTERS => XMLObjectType.Text
		}
	}
	def LocalName() = inner.getLocalName()
	def NamespaceURI() = inner.getNamespaceURI()
	def GetAttributeValue(ns : String, key : String) = inner.getAttributeValue(ns, key)
	def ElementText() = inner.getElementText()

	override def toString() : String = {
		return inner.getEventType() match {
			case XMLStreamConstants.START_DOCUMENT => String.format("XML Document: %s - %s", inner.getEncoding(), inner.getVersion())
			case XMLStreamConstants.START_ELEMENT => String.format("XML Element: <%s xmlns='%s'", inner.getLocalName(), inner.getNamespaceURI())
			case XMLStreamConstants.END_ELEMENT => String.format("XML End Element: </%s>", inner.getLocalName())
			case XMLStreamConstants.CHARACTERS => String.format("XML Characters: %s", inner.getText())
		}
	}
}