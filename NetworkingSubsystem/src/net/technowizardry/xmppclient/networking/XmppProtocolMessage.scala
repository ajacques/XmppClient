package net.technowizardry.xmppclient.networking

import javax.xml.stream.XMLStreamWriter

abstract class XmppProtocolMessage {
	val StreamsNS = "http://etherx.jabber.org/streams";
	def WriteMessage(writer: XMLStreamWriter)
}