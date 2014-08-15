package net.technowizardry.xmpp

import java.io.InputStream
import java.io.OutputStream
import net.technowizardry._
import net.technowizardry.xmpp.messages._

class XmppConnection(domain: String, username: String, password : String, streamFactory : XMLStreamFactory) {
	private val msgfactory = new XmppMessageFactory
	private val authenticator = new XmppAuthenticator(this, username, password)
	private val initiator = new TlsSessionInitiator(domain, 5222)
	private var stream : XmppStream = _
	private var state = XmppConnectionState.NotConnected
	private var msghandlers = Map[Class[_], XmppProtocolMessage => Unit]()
	private var connectCallback : () => Unit = _
	def Negotiate(input: InputStream, output: OutputStream, callback : () => Unit) {
		connectCallback = callback
		InitiateUnderlyingStream(input, output)
		state = XmppConnectionState.ConnectionEstablished
	}
	private def InitiateUnderlyingStream(input : InputStream, output : OutputStream) {
		stream = new XmppStream(input, output, streamFactory, HandleMessage)
		stream.StartReaderThread
		stream.SendMessage(new StreamInitMessage(domain, List[XmppFeature]()))
		stream.Flush
	}
	def SendMessageImmediately(message : WritableXmppMessage) = {
		stream.SendMessage(message)
		stream.Flush()
	}
	def Disconnect() {
		stream.SendMessage(new StreamCloseMessage())
		stream.Flush()
		stream.Shutdown
	}
	def RegisterMessageCallback(msgtype : Class[_], handler : XmppProtocolMessage => Unit) {
		msghandlers += (msgtype -> handler)
	}
	def GetState() = state
	private def HandleMessage(reader : XMLReader) {
		val msg = msgfactory.FromXMLReader(reader)
		InvokeMessageHandler(msg)
	}
	private def InvokeMessageHandler(message : XmppProtocolMessage) {
		println("Received message of type: " + message.getClass().getName())
		msghandlers.get(message.getClass()) match {
			case Some(f) => f(message)
			case _ => {
				message match {
					case si : StreamInitMessage => HandleStreamInitMessage(si)
					case s : StartTlsProceedMessage => NegotiateTLS()
					case s : SaslSuccessMessage => FinalizeAuthentication()
				}
			}
		}
	}
	private def HandleStreamInitMessage(message : StreamInitMessage) {
		message.GetFeatures().foreach(f => println(String.format("Feature: %s:%s", f.GetNamespace(), f.GetName())))
		if (message.SupportsStartTls) {
			stream.SendMessage(new StartTlsMessage())
			stream.Flush
		} else {
			authenticator.AttemptAuthentication(message.GetMechanisms())
		}
	}
	private def NegotiateTLS() {
		stream.Shutdown // Tear the exist XMPP session down

		initiator.Negotiate(stream.GetInnerInputStream(), stream.GetInnerOutputStream(), CompleteTLSNegotiation)
	}
	private def FinalizeAuthentication() {
		state = XmppConnectionState.Authenticated
	}
	private def CompleteTLSNegotiation(input : InputStream, output : OutputStream) {
		println("Completed TLS negotiation")
		InitiateUnderlyingStream(input, output)
	}
}