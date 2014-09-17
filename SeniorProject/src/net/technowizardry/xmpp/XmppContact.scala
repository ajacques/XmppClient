package net.technowizardry.xmpp

class XmppContact(jid : Jid, name : String) {
	def Username = jid
	def Name = name
	override def toString() = String.format("%s [%s]", Name, Username)
}