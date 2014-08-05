package net.technowizardry.xmppclient.networking

import java.io.InputStream
import java.io.OutputStream
import net.technowizardry.{XMLReader,XMLStreamFactory}

class XmppConnection(domain: String, username: String, streamFactory : XMLStreamFactory) {
	var stream : XmppStream = _
	val msgfactory = new XmppMessageFactory
	def Negotiate(input: InputStream, output: OutputStream) {
		stream = new XmppStream(input, output, streamFactory, HandleMessage)
		stream.StartReaderThread
		stream.SendMessage(new StreamInitMessage(domain))
		stream.Flush()
	}
	def Disconnect() {
		stream.Shutdown
	}
	private def HandleMessage(reader : XMLReader) {
		val msg = msgfactory.FromXMLReader(reader)
	}
}