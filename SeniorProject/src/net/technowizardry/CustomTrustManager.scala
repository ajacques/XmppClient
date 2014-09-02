package net.technowizardry

import java.net.Socket
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLEngine, X509TrustManager}

class CustomTrustManager extends X509TrustManager {
	def checkClientTrusted(chain : Array[X509Certificate], authType : String) = throw new RuntimeException("Not implemented")
	def checkServerTrusted(chain : Array[X509Certificate], authType : String) {
		println("In TrustManager verify certificate chain")
		chain.foreach(cert => println(" " + cert.getSubjectDN().getName()))
		// TODO: Assert that this is a valid certificate chain
	}
	def getAcceptedIssuers() : Array[X509Certificate] = {
		return null
	}
}