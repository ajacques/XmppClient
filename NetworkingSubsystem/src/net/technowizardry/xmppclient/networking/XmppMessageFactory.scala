package net.technowizardry.xmppclient.networking

import net.technowizardry.XMLReader

class XmppMessageFactory {
	var parsers : Map[String, XMLReader => XmppProtocolMessage] = Map()
	parsers += (GetKey(XmppNamespaces.StreamsNS, "stream") -> StreamInitMessageParser.Unpack)
	def FromXMLReader(reader : XMLReader) : XmppProtocolMessage = {
		var parser = FindParserForGlobalName(reader.NamespaceURI, reader.LocalName)
		return parser(reader)
	}
	def FindParserForGlobalName(namespace : String, name : String) : XMLReader => XmppProtocolMessage = {
		return parsers(GetKey(namespace, name))
	}
	private def GetKey(reader : XMLReader) : String = GetKey(reader.NamespaceURI, reader.LocalName)
	private def GetKey(namespace : String, name : String) = String.format("%s\0%s", namespace, name)
}