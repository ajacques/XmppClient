package net.technowizardry.xmpp.messages

import net.technowizardry.{XMLReader,XMLWriter}
import net.technowizardry.xmpp.{Jid,XmppNamespaces}

class ChatMessage(to : Jid, body : String) extends XmppProtocolMessage with WritableXmppMessage {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteStartElement("message", XmppNamespaces.Jabber)
		writer.WriteAttribute("to", to.toString(), null)
		writer.WriteAttribute("type", "chat", null)
		writer.WriteStartElement("body", XmppNamespaces.Jabber)
		writer.WriteText(body)
		writer.WriteEndElement()
		writer.WriteEndElement()
	}
	def To = to
	def MessageBody = body
}

object ChatMessage {
	def ParseMessage(reader : XMLReader) : XmppProtocolMessage = {
		val from = Jid.FromString(reader.GetAttributeValue(null, "from"))
		reader.Next()
		reader.Next()
		val body = reader.ElementText()
		println(body)
		reader.ReadUntilEndElement(XmppNamespaces.Jabber, "message")
		new ChatMessage(from, body) // TODO: I'm stuffing the from into the to variable... BAD BAD BAD
	}
}