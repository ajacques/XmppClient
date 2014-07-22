package net.technowizardry.xmppclient.networking

import java.io.InputStream
import java.io.OutputStream
import javax.xml.stream.XMLStreamReader

class XmppConnection(domain: String, username: String) {
	var stream : XmppStream = _
	val test = (this.HandleMessage _)
	def Negotiate(input: InputStream, output: OutputStream) {
		stream = new XmppStream(input, output, HandleMessage) 
		stream.StartReaderThread
		stream.SendMessage(new StreamInitMessage(domain))
	}
	def Disconnect() {
		stream.Shutdown
	}
	def myMethod4[A](f: A => Unit, a: A): Unit = {
		val value = f(a)
		()
	}
	private def HandleMessage(reader : XMLStreamReader) {
	}
}