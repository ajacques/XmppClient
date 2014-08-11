package net.technowizardry

import java.net.Socket
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLEngine, X509ExtendedTrustManager}

class CustomTrustManager extends X509ExtendedTrustManager {
	def checkClientTrusted(chain : Array[X509Certificate], authType : String) = throw new RuntimeException("Not implemented")
	def checkClientTrusted(chain : Array[X509Certificate], authType : String, socket : Socket) = throw new RuntimeException("Not implemented")
	def checkClientTrusted(chain : Array[X509Certificate], authType : String, engine : SSLEngine) = throw new RuntimeException("Not implemented")
	def checkServerTrusted(chain : Array[X509Certificate], authType : String) {
		println("In TrustManager verify certificate chain")
		chain.foreach(cert => println(" " + cert.getSubjectDN().getName()))
		// TODO: Assert that this is a valid certificate chain
	}
	def checkServerTrusted(chain : Array[X509Certificate], authType : String, socket : Socket) = checkServerTrusted(chain, authType)
	def checkServerTrusted(chain : Array[X509Certificate], authType : String, engine : SSLEngine) = checkServerTrusted(chain, authType)
	def getAcceptedIssuers() : Array[X509Certificate] = {
		return null
	}
}