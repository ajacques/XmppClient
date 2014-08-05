package net.technowizardry.xmppclient.networking

import java.io.{InputStream,OutputStream}
import net.technowizardry.{XMLReader,XMLWriter,XMLStreamFactory}

class XmppStream(inputStream: InputStream, outputStream: OutputStream, streamFactory : XMLStreamFactory, messageReader : XMLReader => Unit) {
	var writer : XMLWriter = streamFactory.CreateWriter(outputStream)
	var reader : XMLReader = _
	var readThread : Thread = _

	writer.WriteStartDocument("utf-8", "1.0")
	def SendMessage(message : XmppProtocolMessage) {
		message.WriteMessage(writer)
	}
	def Flush() = writer.Flush
	def StartReaderThread() {
		readThread = new Thread(new Runnable {
			def run() {
				reader = streamFactory.CreateReader(inputStream)
				while (reader.HasNext()) {
					reader.Next()
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