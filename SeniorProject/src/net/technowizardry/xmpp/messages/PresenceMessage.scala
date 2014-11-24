package net.technowizardry.xmpp.messages

import net.technowizardry.{XMLWriter, XMLReader}
import net.technowizardry.xmpp.{Jid, XmppNamespaces}

class PresenceUpdateMessage(from: Jid, status: String, updateType: String, message: String, priority: Int) extends XmppProtocolMessage with WritableXmppMessage {
	def this(status: String, message: String, priority: Int) = this(null, status, null, message, priority)
	def WriteMessage(writer: XMLWriter) {
		writer WriteStartElement("presence", XmppNamespaces.Jabber)
		if (status != null) {
			writer.WriteStartElement("show", null)
			writer.WriteText(status)
			writer.WriteEndElement()
		}
		if (message != null) {
			writer.WriteStartElement("status", null)
			writer.WriteText(message)
			writer.WriteEndElement()
		}
		writer.WriteStartElement("priority", null)
		writer.WriteText(priority toString)
		writer.WriteEndElement()
		writer.WriteEndElement()
	}
	def From = from
	def Status = status
	def Message = message
	def Priority = priority
	def UpdateType = updateType
}

class PresenceMessage(to : Jid, ptype : String) extends XmppProtocolMessage with WritableXmppMessage {
	def WriteMessage(writer: XMLWriter) {
		writer.WriteStartElement("presence", XmppNamespaces.Jabber)
		writer.WriteAttribute("to", to.toString(), null)
		writer.WriteAttribute("type", ptype, null)
		writer.WriteEndElement()
	}
}

object PresenceMessage {
	def Parse(reader: XMLReader) : XmppProtocolMessage = {
		val from = Jid.FromString(reader.GetAttributeValue(null, "from"))
		val updateType = reader.GetAttributeValue(null, "type")
		var priority = 0
		var show = ""
		reader.Next()
		while (!reader.IsExpectedEndElement(XmppNamespaces.Jabber, "presence")) {
			println(reader.toString() + " " + reader.ElementText())
			reader.LocalName() match {
				case "show" => show = reader.ElementText()
				case "priority" => {
					reader.Next()
					priority = Integer.parseInt(reader.ElementText())
					reader.Next()
				}
				case _ =>
			}
			reader.Next()
		}
		new PresenceUpdateMessage(from, show, updateType, null, priority)
	}
}