package net.technowizardry.xmpp.messages

import net.technowizardry.XMLWriter

class IqQueryBody(namespace : String) extends IqRequestBody {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteStartElement("query", namespace)
		writer.WriteEndElement()
	}
}