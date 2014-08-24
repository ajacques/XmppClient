package net.technowizardry.xmpp.auth

import net.technowizardry.xmpp.messages._
import net.technowizardry.xmpp.XmppConnection

class XmppAuthenticator(connection : XmppConnection, username : String, password : String) {
	var authhandlers : Map[String, () => Unit] = Map()
	var strategy : AuthStrategy = _
	authhandlers += ("PLAIN" -> PerformPlainAuth)
	authhandlers += ("SCRAM-SHA-1" -> PerformScramSha1)
	connection.RegisterMessageCallback(classOf[SaslChallengeMessage], ProcessChallenge)
	def AttemptAuthentication(mechanisms : List[String]) {
		val chosenauth = mechanisms.find(p => authhandlers.contains(p)).get
		println(String.format("Selected %s as authentication mechanism", chosenauth))
		authhandlers.apply(chosenauth)()
	}
	private def PerformPlainAuth() {
		connection.SendMessageImmediately(new SaslPlainAuthMessage(username, password))
	}
	private def PerformScramSha1() {
		strategy = new ScramAuthStrategy(username, password)
		connection.SendMessageImmediately(strategy.BeginAuthentication())
	}
	private def ProcessChallenge(message : XmppProtocolMessage) {
		val challenge = message match {
			case x : SaslChallengeMessage => x
		}
		connection.SendMessageImmediately(strategy.HandleChallenge(challenge))
	}
}