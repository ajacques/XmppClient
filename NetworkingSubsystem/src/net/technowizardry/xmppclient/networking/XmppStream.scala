package net.technowizardry.xmppclient.networking

import java.io.{InputStream,OutputStream}
import javax.xml.stream.{XMLStreamWriter,XMLOutputFactory,XMLStreamReader,XMLInputFactory}

class XmppStream(inputStream: InputStream, outputStream: OutputStream, messageReader : XMLStreamReader => Unit) {
	var writer : XMLStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(outputStream, "utf-8")
	var reader : XMLStreamReader = _
	var readThread : Thread = _

	def SendMessage(message : XmppProtocolMessage) {
		message.WriteMessage(writer)
		writer.flush
	}
	def StartReaderThread() {
		readThread = new Thread(new Runnable {
			def run() {
				reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream)
				while (reader.hasNext) {
					reader.next
					messageReader(reader)
				}
			}
		})
		readThread.start
	}
	def Shutdown() {
		readThread.stop()
	}
}