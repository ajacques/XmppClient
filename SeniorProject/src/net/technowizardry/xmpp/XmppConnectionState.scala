package net.technowizardry.xmpp

object XmppConnectionState extends Enumeration {
	type XmppConnectionState = Value
	val NotConnected, ConnectionEstablished, Authenticated = Value
}