package net.technowizardry.xmpp

import java.net.Socket
import java.io.InputStream
import java.io.OutputStream
import net.technowizardry._
import net.technowizardry.xmpp.messages._
import net.technowizardry.xmpp.auth.XmppAuthenticator

class XmppConnection(domain: String, username: String, password : String, streamFactory : XMLStreamFactory) {
	private var msghandlers = Map[Class[_], XmppProtocolMessage => Unit]()
	private val msgfactory = new XmppMessageFactory
	private val authenticator = new XmppAuthenticator(this, username, password)
	private var initiator : TlsSessionInitiator = _
	private var stream : XmppStream = _
	private var state = XmppConnectionState.NotConnected
	private var connectCallback : () => Unit = _
	private var socket : Socket = _
	def Negotiate(socket : Socket, input: InputStream, output: OutputStream, callback : () => Unit) {
		connectCallback = callback
		InitiateUnderlyingStream(input, output)
		initiator = new TlsSessionInitiator(domain, 5222, socket)
		state = XmppConnectionState.ConnectionEstablished
	}
	private def InitiateUnderlyingStream(input : InputStream, output : OutputStream) {
		stream = new XmppStream(input, output, streamFactory, HandleMessage)
		stream.StartReaderThread
		SendMessageImmediately(new StreamInitMessage(domain, List[XmppFeature]()))
	}
	def SendMessageImmediately(message : WritableXmppMessage) = {
		stream.SendMessage(message)
		stream.Flush()
	}
	def Disconnect() {
		SendMessageImmediately(new StreamCloseMessage())
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
					case s : CompressedMessage => NegotiateCompression()
				}
			}
		}
	}
	private def HandleStreamInitMessage(message : StreamInitMessage) {
		message.GetFeatures().foreach(f => println(String.format("Feature: %s:%s", f.GetNamespace(), f.GetName())))
		if (message.SupportsStartTls) {
			SendMessageImmediately(new StartTlsMessage())
		} else if (message.SupportsFeature("mechanisms", XmppNamespaces.Sasl)) {
			authenticator.AttemptAuthentication(message.GetMechanisms())
		} else if (false && message.SupportsCompression) {
			SendMessageImmediately(new CompressionInitMessage("zlib"));
		}
	}
	private def NegotiateTLS() {
		stream.Shutdown // Tear the exist XMPP session down

		initiator.Negotiate(stream.GetInnerInputStream(), stream.GetInnerOutputStream(), CompleteTLSNegotiation)
	}
	private def NegotiateCompression() {
		stream.Shutdown

		InitiateUnderlyingStream(new ZlibDecompressStream(stream.GetInnerInputStream()), new ZlibCompressStream(stream.GetInnerOutputStream()))
	}
	private def FinalizeAuthentication() {
		state = XmppConnectionState.Authenticated
		if (connectCallback != null) {
			connectCallback()
		}
		stream.Shutdown()
		InitiateUnderlyingStream(stream.GetInnerInputStream(), stream.GetInnerOutputStream())
	}
	private def CompleteTLSNegotiation(input : InputStream, output : OutputStream) {
		println("Completed TLS negotiation")
		InitiateUnderlyingStream(input, output)
	}
}