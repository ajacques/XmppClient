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
}