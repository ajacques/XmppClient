package net.technowizardry.xmpp.auth

import net.technowizardry.{Base64, XMLWriter}
import net.technowizardry.xmpp.messages._
import scala.util.Random
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest

class ScramAuthMessage(algo : String, username : String, nonce : String) extends SaslAuthMessage {
	val body = String.format("n,,n=%s,r=%s", username, nonce)
	def MessageBody = body
	def GetMechanismName() = "SCRAM-" + algo
	def WriteAuthBody(writer : XMLWriter) {
		writer.WriteText(Base64.Encode(body))
	}
}

class ScramAuthStrategy(username : String, password : String) extends AuthStrategy {
	val hashAlgo = MessageDigest.getInstance("SHA-1")
	val clientFirst = new ScramAuthMessage("SHA-1", username, GenerateNonce())
	def BeginAuthentication() : WritableXmppMessage = {
		return clientFirst
	}
	def HandleChallenge(challenge : SaslChallengeMessage) : WritableXmppMessage = {
		val nonce = challenge.GetProperty("r") // Server sent nonce
		val salt = Base64.Decode(challenge.GetProperty("s"))
		val iterations = Integer.parseInt(challenge.GetProperty("i"))
		val serverFirstMessage = challenge.Message
		val result = GenerateResponse(nonce, salt, iterations, clientFirst.MessageBody, serverFirstMessage)
		return new SaslResponseMessage(result)
	}
	def GenerateResponse(nonce : String, salt : String, iterations : Integer, clientFirstMessage : String, serverFirstMessage : String) : String = {
		val saltedPassword = GenerateSaltedPassword(salt, iterations)
		val clientKey = GenerateClientKey(saltedPassword)
		val storedKey = hashAlgo.digest(clientKey)
		val clientFinalStart = String.format("c=biws,r=%s", nonce)
		val clientSignature = GenerateClientSignature(storedKey, clientFirstMessage, serverFirstMessage, clientFinalStart)
		val clientProof = Base64.Encode(XorArray(clientKey, clientSignature))
		String.format("%s,p=%s", clientFinalStart, clientProof)
	}
	def GenerateClientKey(saltedPassword : Array[Byte]) : Array[Byte] = {
		val key = new SecretKeySpec(saltedPassword, "HmacSHA1")
		val hmac = Mac.getInstance("HmacSHA1")
		hmac.init(key)
		hmac.doFinal("Client Key".getBytes())
	}
	def GenerateClientSignature(storedKey : Array[Byte], clientFirstMessage : String, serverFirstMessage : String, clientFinalMessage : String) : Array[Byte] = {
		val authMessage = String.format("%s,%s,%s", clientFirstMessage.substring(3), serverFirstMessage, clientFinalMessage).getBytes()
		val key = new SecretKeySpec(storedKey, "HmacSHA1")
		val hmac = Mac.getInstance("HmacSHA1")
		hmac.init(key)
		hmac.doFinal(authMessage)
	}
	def GenerateSaltedPassword(salt : String, iterations : Int) : Array[Byte] = {
		val key = new SecretKeySpec(password.getBytes(), "HmacSHA1")
		val hmac = Mac.getInstance("HmacSHA1")
		val str = salt.getBytes() ++ Array[Byte](0, 0, 0, 1)
		hmac.init(key)

		var ust = hmac.doFinal(str)
		var result = ust
		var i = 0
		for (i <- 2 to iterations) {
			ust = hmac.doFinal(ust)
			result = XorArray(result, ust)
		}

		result
	}
	def XorArray(left : Array[Byte], right : Array[Byte]) = left.zip(right).map(t => (t._1 ^ t._2).toByte)
	def GenerateNonce() : String = {
		val rand = new Random()
		val writer = new StringBuilder()
		var a = 0

		for (a <- 1 to 24) {
			val b = 48 + (rand.nextInt(61) match {
				case x if x >= 36 => x + 14
				case x if x >= 10 => x + 7
				case x => x
			})
			writer.append(b.toChar)
		}

		writer.toString()
	}
}