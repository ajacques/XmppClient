package net.technowizardry.xmpp

import net.technowizardry.XMLReader
import net.technowizardry.xmpp.messages._
import net.technowizardry.XMLObjectType

class XmppMessageFactory {
	var parsers : Map[String, XMLReader => XmppProtocolMessage] = Map()
	parsers += (GetKey(XmppNamespaces.Streams, "stream") -> StreamInitMessageParser.Unpack)
	parsers += (GetKey(XmppNamespaces.Streams, "error") -> StreamErrorMessageParser.Unpack)
	parsers += (GetKey(XmppNamespaces.Tls, "proceed") -> TlsMessageParser.UnpackProceed)
	parsers += (GetKey(XmppNamespaces.Sasl, "challenge") -> SaslParser.UnpackChallenge)
	parsers += (GetKey(XmppNamespaces.Sasl, "failure") -> SaslParser.UnpackFailure)
	parsers += (GetKey(XmppNamespaces.Sasl, "success") -> SaslParser.UnpackSuccess)
	def FromXMLReader(reader : XMLReader) : XmppProtocolMessage = {
		var ns = reader.NamespaceURI
		var name = reader.LocalName
		var parser = FindParserForGlobalName(ns, name)
		val msg = parser(reader)
		return msg
	}
	def FindParserForGlobalName(namespace : String, name : String) : XMLReader => XmppProtocolMessage = {
		return parsers(GetKey(namespace, name))
	}
	private def GetKey(namespace : String, name : String) = String.format("%s\0%s", namespace, name)
}