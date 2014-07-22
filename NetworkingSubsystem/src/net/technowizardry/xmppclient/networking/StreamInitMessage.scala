package net.technowizardry.xmppclient.networking

import javax.xml.stream.XMLStreamWriter

class StreamInitMessage(server: String) extends XmppProtocolMessage {
	def WriteMessage(writer: XMLStreamWriter) {
		writer.writeStartDocument("utf-8", "1.0")
		writer.writeStartElement("stream", "stream", StreamsNS)
		writer.writeNamespace("stream", StreamsNS)
		writer.writeAttribute("version", "1.0")
		writer.writeAttribute("to", server)
		writer.writeCharacters("")
	}
}