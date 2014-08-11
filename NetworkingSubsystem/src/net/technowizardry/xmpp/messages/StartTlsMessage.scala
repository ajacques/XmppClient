package net.technowizardry.xmpp.messages

import net.technowizardry.xmpp.XmppNamespaces
import net.technowizardry.{XMLReader, XMLWriter}

class StartTlsMessage extends XmppProtocolMessage with WritableXmppMessage {
	def WriteMessage(writer: XMLWriter) {
		writer.WriteStartElement("starttls", XmppNamespaces.Tls)
		writer.WriteEndElement()
	}
}

class StartTlsProceedMessage extends XmppProtocolMessage {}

object TlsMessageParser {
	def UnpackProceed(reader : XMLReader) : StartTlsProceedMessage = {
		return new StartTlsProceedMessage()
	}
}