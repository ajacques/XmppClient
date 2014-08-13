package net.technowizardry.xmpp

import net.technowizardry.xmpp.messages._

class XmppAuthenticator(connection : XmppConnection, username : String, password : String) {
	var authhandlers : Map[String, () => Unit] = Map()
	authhandlers += ("PLAIN" -> PerformPlainAuth)
	def AttemptAuthentication(mechanisms : List[String]) {
		val chosenauth = mechanisms.find(p => authhandlers.contains(p)).get
		println(String.format("Selected %s as authentication mechanism", chosenauth))
		authhandlers.apply(chosenauth)()
	}
	private def PerformPlainAuth() {
		connection.SendMessageImmediately(new SaslPlainAuthMessage(username, password))
	}
}