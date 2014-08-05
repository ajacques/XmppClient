package net.technowizardry.xmppclient.networking

import net.technowizardry.{XMLWriter,XMLReader}

class StreamInitMessage(server: String) extends XmppProtocolMessage {
	def WriteMessage(writer: XMLWriter) {
		writer.WriteStartElement("stream", "stream", XmppNamespaces.StreamsNS)
		writer.WriteNamespace("stream", XmppNamespaces.StreamsNS)
		writer.WriteDefaultNamespace(XmppNamespaces.JabberNS)
		writer.WriteAttribute("version", "1.0", XmppNamespaces.JabberNS)
		writer.WriteAttribute("to", server, XmppNamespaces.JabberNS)
		writer.WriteText("")
	}
}

object StreamInitMessageParser extends XmppProtocolMessageTrait {
	def Unpack(reader : XMLReader) : StreamInitMessage = {
		return new StreamInitMessage(null)
	}
}