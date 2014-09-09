package net.technowizardry

abstract class XMLReader {
	def HasNext() : Boolean
	def Next()
	def NodeType() : XMLObjectType.XMLObjectType
	def LocalName() : String
	def NamespaceURI() : String
	def GetAttributeValue(ns : String, key : String) : String
	def ElementText() : String
	def ReadUntilEndElement(ns : String, name : String) = {
		while (HasNext() && !IsExpectedEndElement(ns, name)) {
			Next()
		}
	}
	def IsExpectedEndElement(ns : String, name : String): Boolean = {
		if (NodeType() == XMLObjectType.EndElement) {
			return LocalName() == name && NamespaceURI() == ns
		} else {
			return false
		}
	}
}