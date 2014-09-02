package net.technowizardry

trait XMLWriter {
	def WriteStartDocument(encoding : String, version : String)
	def WriteStartElement(prefix : String, elemName : String, namespace : String)
	def WriteStartElement(elemName : String, namespace : String)
	def WriteAttribute(name : String, value : String, namespace : String)
	def WriteNamespace(prefix : String, namespace : String)
	def WriteDefaultNamespace(namespace : String)
	def WriteText(text : String)
	def WriteEndElement()
	def Flush()
}