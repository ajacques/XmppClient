package net.technowizardry.xmpp.messages

import net.technowizardry.{XMLReader,XMLWriter}
import net.technowizardry.xmpp.XmppNamespaces

abstract class IqMessage extends XmppProtocolMessage {
	
}

class IqRequestMessage(id : String, body : IqRequestBody) extends IqMessage with WritableXmppMessage {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteStartElement("iq", XmppNamespaces.Jabber)
		writer.WriteAttribute("type", "set", XmppNamespaces.Jabber)
		writer.WriteAttribute("id", id, XmppNamespaces.Jabber)
		body.WriteMessage(writer)
		writer.WriteEndElement()
	}
}

class IqResponseMessage(id : String, body : IqRequestBody) extends IqMessage {
	def MessageId = id
}

abstract class IqRequestBody extends WritableXmppMessage {
}

abstract class IqResponseBody {}

class RosterResponse extends IqResponseBody {
	
}

object IqParser {
	val parsers = Map[String, XMLReader => IqRequestBody](XmppNamespaces.Roster -> UnpackRoster)
	def Unpack(reader : XMLReader) : XmppProtocolMessage = {
		val id = reader.GetAttributeValue(null, "id")
		reader.Next()
		val ns = reader.NamespaceURI()
		reader.Next()
		val body = parsers.get(ns) match {
			case Some(f) => f(reader)
		}
		reader.ReadUntilEndElement(reader.NamespaceURI(), reader.LocalName())
		return new IqResponseMessage(id, body)
	}
	// These shouldn't be here
	def UnpackRoster(reader : XMLReader) : IqRequestBody = {
		while (!reader.IsExpectedEndElement(XmppNamespaces.Roster, "query")) {
			
			reader.Next()
		}
		null
	}
}