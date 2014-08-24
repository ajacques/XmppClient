package net.technowizardry.xmpp

import net.technowizardry.XMLReader
import net.technowizardry.xmpp.messages._
import net.technowizardry.XMLObjectType

object SingleElementParser {
	def Unpack(message : XmppProtocolMessage)(reader : XMLReader) : XmppProtocolMessage = {
		reader.Next()
		message
	}
}

class XmppMessageFactory {
	var parsers : Map[String, XMLReader => XmppProtocolMessage] = Map()
	parsers += (GetKey(XmppNamespaces.Streams, "stream") -> StreamInitMessageParser.Unpack)
	parsers += (GetKey(XmppNamespaces.Streams, "error") -> StreamErrorMessageParser.Unpack)
	parsers += (GetKey(XmppNamespaces.Tls, "proceed") -> SingleElementParser.Unpack(new StartTlsProceedMessage()) _)
	parsers += (GetKey(XmppNamespaces.Sasl, "challenge") -> SaslParser.UnpackChallenge)
	parsers += (GetKey(XmppNamespaces.Sasl, "failure") -> SaslParser.UnpackFailure)
	parsers += (GetKey(XmppNamespaces.Sasl, "success") -> SaslParser.UnpackSuccess)
	parsers += (GetKey(XmppNamespaces.Jabber, "iq") -> IqParser.Unpack)
	parsers += (GetKey(XmppNamespaces.Compression, "compressed") -> SingleElementParser.Unpack(new CompressedMessage()) _)
	def FromXMLReader(reader : XMLReader) : XmppProtocolMessage = {
		var ns = reader.NamespaceURI
		var name = reader.LocalName
		var parser = FindParserForGlobalName(ns, name)
		val msg = parser(reader)
		if (!reader.IsExpectedEndElement(ns, name)) {
			println(String.format("WARNING: Possible unbalanced XML Reader (Cursor: %s) (Responsible Parser: %s)", reader, parser.toString()));
		}
		return msg
	}
	def FindParserForGlobalName(namespace : String, name : String) : XMLReader => XmppProtocolMessage = {
		return parsers(GetKey(namespace, name))
	}
	private def GetKey(namespace : String, name : String) = String.format("%s\0%s", namespace, name)
}