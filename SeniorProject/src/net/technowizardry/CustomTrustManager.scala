package net.technowizardry

import java.net.Socket
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLEngine, X509TrustManager, TrustManagerFactory}

class CustomTrustManager extends X509TrustManager {
	val ks = KeyStore.getInstance(KeyStore.getDefaultType())
	val algo = TrustManagerFactory.getDefaultAlgorithm()
	val factory = TrustManagerFactory.getInstance(algo)
	factory.init(Stuff.NULL_KEYSTORE)
	val tr = factory.getTrustManagers()
	val trustMgr = factory.getTrustManagers().find(f => f.isInstanceOf[X509TrustManager]).get match {
		case x : X509TrustManager => x
	}
	def checkClientTrusted(chain : Array[X509Certificate], authType : String) = throw new RuntimeException("Not implemented")
	def checkServerTrusted(chain : Array[X509Certificate], authType : String) {
		println("In TrustManager verify certificate chain")
		chain.foreach(cert => println(" " + cert.getSubjectDN().getName()))
		trustMgr.checkClientTrusted(chain, authType)
	}
	def getAcceptedIssuers() : Array[X509Certificate] = {
		return null
	}
}