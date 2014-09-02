package net.technowizardry.xmpp.messages

import net.technowizardry.XMLWriter

class StreamCloseMessage extends XmppProtocolMessage with WritableXmppMessage {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteEndElement()
	}
}