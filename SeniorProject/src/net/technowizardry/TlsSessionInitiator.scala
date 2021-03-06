package net.technowizardry

import java.net.Socket
import java.io.{InputStream, OutputStream}
import java.security.SecureRandom
import javax.net.ssl.{SSLSocket, SSLContext, TrustManager, SSLSocketFactory, HandshakeCompletedListener, HandshakeCompletedEvent}

class TlsSessionInitiator(domainName : String, port : Integer, socket : Socket) {
	val sslFactory = GetSSLSocketFactory()
	def Negotiate(input: InputStream, output: OutputStream, completeCallback : (InputStream, OutputStream) => Unit) {
		var sslSocket = sslFactory.createSocket(socket, domainName, port, false) match {
			case s : SSLSocket => s
		}
		sslSocket.addHandshakeCompletedListener(new HandshakeCompletedListener {
			def handshakeCompleted(event : HandshakeCompletedEvent) = {
				println(String.format("TLS Negotiated - Cipher: %s", event.getCipherSuite()))
				completeCallback(sslSocket.getInputStream(), sslSocket.getOutputStream())
			}
		})
		sslSocket.startHandshake()
	}
	private def GetSSLSocketFactory() : SSLSocketFactory = {
		val context = SSLContext.getInstance("SSL")
		// Only Java would use this design pattern
		context.init(null, Array[TrustManager] { new CustomTrustManager() }, null)
		return context.getSocketFactory()
	}
}