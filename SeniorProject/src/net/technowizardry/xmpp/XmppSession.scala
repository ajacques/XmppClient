package net.technowizardry.xmpp

import net.technowizardry.xmpp.messages._

class XmppSession(connection : XmppConnection) {
	val correlator = new IqCorrelator()
	connection.RegisterMessageCallback(classOf[IqResponseMessage], HandleIqMessage)
	def BindToResource(resourceName : String) {
		SendIqRequest(new ResourceBindMessage(resourceName), "set", NoopCallback)
	}
	def SendIqRequest(iq : IqRequestBody, mtype : String, callback : IqResponseMessage => Unit) {
		val msg = correlator.RegisterRequest(iq, mtype, callback)
		connection.SendMessageImmediately(msg)
	}
	def FetchRoster(callback : (List[XmppContact]) => Unit) {
		SendIqRequest(new IqQueryBody(XmppNamespaces.Roster), "get", HandleRosterResponse(callback))
	}
	def SendMessageTo(jid : Jid, message : String) {
		connection.SendMessageImmediately(new ChatMessage(jid, message))
	}
	private def HandleIqMessage(message : XmppProtocolMessage) {
		message match {
			case x : IqResponseMessage => {
				correlator.FetchCallback(x)(x)
			}
		}
	}
	private def HandleRosterResponse(callback : (List[XmppContact]) => Unit)(message : IqResponseMessage) {
		val msg = message.Body match {
			case x : RosterList => x
		}
		callback(msg.GetContactList())
	}
	private def NoopCallback(message : IqResponseMessage) {
		println(message)
	}
}