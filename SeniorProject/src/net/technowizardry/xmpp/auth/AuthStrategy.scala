package net.technowizardry.xmpp.auth

import net.technowizardry.xmpp.messages.{SaslChallengeMessage,WritableXmppMessage}

trait AuthStrategy {
	def BeginAuthentication() : WritableXmppMessage
	def HandleChallenge(challenge : SaslChallengeMessage) : WritableXmppMessage
}