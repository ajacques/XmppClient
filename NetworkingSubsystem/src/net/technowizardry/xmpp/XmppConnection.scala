package net.technowizardry.xmpp

import java.io.InputStream
import java.io.OutputStream
import net.technowizardry._
import net.technowizardry.xmpp.messages._

class XmppConnection(domain: String, username: String, password : String, streamFactory : XMLStreamFactory) {
	private var stream : XmppStream = _
	private var state = XmppConnectionState.NotConnected
	val msgfactory = new XmppMessageFactory
	def Negotiate(input: InputStream, output: OutputStream) {
		stream = new XmppStream(input, output, streamFactory, HandleMessage)
		stream.StartReaderThread
		stream.SendMessage(new StreamInitMessage(domain, List[XmppFeature]()))
		stream.Flush
		state = XmppConnectionState.ConnectionEstablished
	}
	def Disconnect() {
		stream.Shutdown
	}
	def GetState() = state
	private def HandleMessage(reader : XMLReader) {
		val msg = msgfactory.FromXMLReader(reader)
		InvokeMessageHandler(msg)
	}
	private def InvokeMessageHandler(message : XmppProtocolMessage) {
		println("Received message of type: " + message.getClass().getName())
		message match {
			case si : StreamInitMessage => HandleStreamInitMessage(si)
			case s : StartTlsProceedMessage => NegotiateTLS()
			case s : SaslSuccessMessage => FinalizeAuthentication()
		}
	}
	private def HandleStreamInitMessage(message : StreamInitMessage) {
		if (message.SupportsStartTls) {
			stream.SendMessage(new StartTlsMessage())
			stream.Flush
		} else {
			stream.SendMessage(new SaslPlainAuthMessage(username, password))
			stream.Flush()
		}
	}
	def NegotiateTLS() {
		stream.Shutdown // Tear the exist XMPP session down

		// Initiator should be passed to the XmppConnection constructor for dependency injection
		val initiator = new TlsSessionInitiator(domain, 5222)
		initiator.Negotiate(stream.GetInnerInputStream(), stream.GetInnerOutputStream(), CompleteTLSNegotiation)
	}
	def FinalizeAuthentication() {
		state = XmppConnectionState.Authenticated
	}
	private def CompleteTLSNegotiation(input : InputStream, output : OutputStream) {
		println("Completed TLS negotiation")
		Negotiate(input, output)
	}
}