package net.technowizardry.xmpp

import java.io.{InputStream,OutputStream}
import net.technowizardry._
import net.technowizardry.xmpp.messages._
import java.io.BufferedOutputStream
import javax.xml.stream.XMLStreamException

class XmppStream(inputStream : InputStream, outputStream : OutputStream, streamFactory : XMLStreamFactory, messageReader : XMLReader => Unit) {
	var writer : XMLWriter = streamFactory.CreateWriter(new BufferedOutputStream(outputStream))
	var readThread : Thread = _
	var runReaderThread = false

	writer.WriteStartDocument("utf-8", "1.0")
	def SendMessage(message : WritableXmppMessage) {
		println("Sending message of type: " + message.getClass().getName());
		message.WriteMessage(writer)
	}
	def Flush() = writer.Flush
	def StartReaderThread {
		runReaderThread = true
		readThread = new Thread(new Runnable {
			def run() {
				val reader = streamFactory.CreateReader(inputStream)
				println("Entering XMPP message read loop")
				while (runReaderThread && reader.HasNext()) {
					try {
						reader.Next()
					} catch {
						case e : XMLStreamException => println(reader.toString())
					}
					println("Got something: " + reader.NamespaceURI())
					messageReader(reader)
				}
				println("Exiting message read loop " + runReaderThread)
			}
		})
		readThread.start
	}
	def GetInnerInputStream() = inputStream
	def GetInnerOutputStream() = outputStream
	def Shutdown() = runReaderThread = false
	def ForceShutdown() {
		Shutdown()
		readThread.interrupt()
	}
}