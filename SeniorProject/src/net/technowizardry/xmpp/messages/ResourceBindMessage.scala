package net.technowizardry.xmpp.messages

import net.technowizardry.XMLWriter
import net.technowizardry.xmpp.XmppNamespaces

class ResourceBindMessage(resource : String) extends IqRequestBody {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteStartElement("bind", XmppNamespaces.Bind)
		writer.WriteStartElement("resource", null)
		writer.WriteText(resource)
		writer.WriteEndElement()
		writer.WriteEndElement()
	}
}