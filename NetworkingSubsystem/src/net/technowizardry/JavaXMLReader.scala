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
		}
	}
	def LocalName() = inner.getLocalName()
	def NamespaceURI() = inner.getNamespaceURI()
	def GetAttributeValue(key : String) = inner.getAttributeValue(null, key)
}