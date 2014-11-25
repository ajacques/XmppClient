package net.technowizardry.xmpp

import net.technowizardry.xmpp.messages.RosterModification

class XmppRoster(session : XmppSession) {
	def RemoveItem(jid : Jid) {
		session.SendIqRequest(new RosterModification(jid, "remove"), "set", null)
	}
}