package net.technowizardry.xmpp.messages

import net.technowizardry.{XMLReader,XMLWriter}
import net.technowizardry.xmpp.XmppContact
import net.technowizardry.xmpp.XmppNamespaces
import net.technowizardry.XMLObjectType

abstract class IqMessage extends XmppProtocolMessage {
	
}

class IqRequestMessage(id : String, mtype : String, body : IqRequestBody) extends IqMessage with WritableXmppMessage {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteStartElement("iq", XmppNamespaces.Jabber)
		writer.WriteAttribute("type", mtype, null)
		writer.WriteAttribute("id", id, null)
		body.WriteMessage(writer)
		writer.WriteEndElement()
	}
}

class IqResponseMessage(id : String, body : IqResponseBody) extends IqMessage {
	def MessageId = id
	def Body = body
}

abstract class IqRequestBody extends WritableXmppMessage {
}

abstract class IqResponseBody {}

// HELL YEAH WE SHOULD PUT ALL THE CLASSES IN ONE FILE
class RosterList(contacts : List[XmppContact]) extends IqResponseBody {
	def GetContactList() = contacts
}

class RosterResponse extends IqResponseBody {}

object IqParser {
	val parsers = Map[String, XMLReader => IqResponseBody](XmppNamespaces.Roster -> UnpackRoster)

	def Unpack(reader : XMLReader) : XmppProtocolMessage = {
		val sns = reader.NamespaceURI()
		val sln = reader.LocalName()
		val id = reader.GetAttributeValue(null, "id")
		reader.Next()
		val ns = reader.NamespaceURI()
		reader.Next()
		println(reader.NamespaceURI())
		println("IQ message of type: " + ns)
		val body = parsers.get(ns) match {
			case Some(f) => f(reader)
			case _ => null // This will really fuck some shit up somewhere
		}
		reader.ReadUntilEndElement(sns, sln)
		return new IqResponseMessage(id, body)
	}
	// These shouldn't be here
	def UnpackRoster(reader : XMLReader) : IqResponseBody = {
		var contacts = List[XmppContact]()
		while (!reader.IsExpectedEndElement(XmppNamespaces.Roster, "query")) {
			if (reader.NodeType() == XMLObjectType.StartElement && reader.LocalName() == "item") {
				val name = reader.GetAttributeValue(null, "name")
				val jid = reader.GetAttributeValue(null, "jid")
				contacts ::= new XmppContact(jid, name)
			}
			reader.Next()
		}
		new RosterList(contacts)
	}
}