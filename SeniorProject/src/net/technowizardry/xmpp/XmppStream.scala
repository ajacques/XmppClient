package net.technowizardry.xmpp

import java.util.concurrent.Semaphore
import java.io.{InputStream,OutputStream}
import net.technowizardry._
import net.technowizardry.xmpp.messages._
import java.io.BufferedOutputStream
import javax.xml.stream.XMLStreamException

class XmppStream(inputStream : InputStream, outputStream : OutputStream, streamFactory : XMLStreamFactory, messageReader : XMLReader => Unit) {
	var writer : XMLWriter = streamFactory.CreateWriter(new BufferedOutputStream(outputStream))
	var readThread : Thread = _
	var runReaderThread = false
	val threadBarrier = new Semaphore(1)

	writer.WriteStartDocument("utf-8", "1.0")
	def SendMessage(message : WritableXmppMessage) {
		println("Sending message of type: " + message.getClass().getName());
		message.WriteMessage(writer)
	}
	def Flush() = writer.Flush
	def StartReaderThread {
		readThread = new Thread(new Runnable {
			def run() {
				try {
					println("Pending message read loop entry TID: " + Thread.currentThread().getId())
					threadBarrier.acquire()
					runReaderThread = true
					val reader = streamFactory.CreateReader(inputStream)
					println("Entering XMPP message read loop TID: " + Thread.currentThread().getId())
					while (runReaderThread && reader.HasNext()) {
						reader.Next()
						println("Got something: " + reader.NamespaceURI() + reader.LocalName())
						messageReader(reader)
					}
				} finally {
					println("Exiting message read loop " + runReaderThread + " TID: " + Thread.currentThread().getId())
					threadBarrier.release()
				}
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