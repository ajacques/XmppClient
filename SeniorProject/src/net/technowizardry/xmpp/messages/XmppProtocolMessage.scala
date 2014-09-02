package net.technowizardry.xmpp.messages

import net.technowizardry.{XMLWriter,XMLReader}

abstract class XmppProtocolMessage {
}

trait WritableXmppMessage {
	def WriteMessage(writer: XMLWriter)
}