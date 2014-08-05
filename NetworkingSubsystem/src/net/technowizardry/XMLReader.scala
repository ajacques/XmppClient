package net.technowizardry

trait XMLReader {
	def HasNext() : Boolean
	def Next()
	def NodeType() : XMLObjectType.XMLObjectType
	def LocalName() : String
	def NamespaceURI() : String
	def GetAttributeValue(key : String) : String
}