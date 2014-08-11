package net.technowizardry

trait XMLReader {
	def HasNext() : Boolean
	def Next()
	def NodeType() : XMLObjectType.XMLObjectType
	def LocalName() : String
	def NamespaceURI() : String
	def GetAttributeValue(ns : String, key : String) : String
	def ElementText() : String
}