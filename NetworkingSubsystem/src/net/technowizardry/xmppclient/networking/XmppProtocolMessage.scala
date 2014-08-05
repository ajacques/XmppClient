package net.technowizardry.xmppclient.networking

import net.technowizardry.{XMLWriter,XMLReader}

abstract class XmppProtocolMessage {
	def WriteMessage(writer: XMLWriter)
}

trait XmppProtocolMessageTrait {
	def Unpack(reader : XMLReader) : XmppProtocolMessage
}