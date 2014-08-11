package net.technowizardry.xmpp.messages

import net.technowizardry.XMLReader

class StreamErrorMessage extends XmppProtocolMessage {

}

object StreamErrorMessageParser {
	def Unpack(reader : XMLReader) : XmppProtocolMessage = {
		while (true) {
			println(reader.toString())
			reader.Next()
		}
		return null
	}
}