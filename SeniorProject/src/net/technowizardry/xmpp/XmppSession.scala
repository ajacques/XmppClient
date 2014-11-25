package net.technowizardry.xmpp

import net.technowizardry.xmpp.messages._

class XmppSession(connection : XmppConnection) {
	val correlator = new IqCorrelator()
	var MessageReceivedCallback = (jid : Jid, message: String) => { println(message) }
	var PresenceUpdatedCallback = (from: Jid, updateType: String,  status: String, priority: Integer) => {}
	connection.RegisterMessageCallback(classOf[IqResponseMessage], HandleIqMessage)
	connection.RegisterMessageCallback(classOf[ChatMessage], HandleChatMessage)
	connection.RegisterMessageCallback(classOf[PresenceUpdateMessage], HandlePresenceMessage)
	def BindToResource(resourceName : String) {
		SendIqRequest(new ResourceBindMessage(resourceName), "set", NoopCallback)
	}
	def SendIqRequest(iq : IqRequestBody, mtype : String, callback : IqResponseMessage => Unit) {
		val msg = correlator.RegisterRequest(iq, mtype, callback)
		connection.SendMessageImmediately(msg)
	}
	def ApproveSubscriptionRequest(jid : Jid) {
		connection.SendMessageImmediately(new PresenceMessage(jid, "subscribed"))
	}
	def SendRequest(jid : Jid) {
		connection.SendMessageImmediately(new PresenceMessage(jid, "subscribe"))
	}
	def UpdateOwnStatus(status : String, message : String, priority : Int) {
		connection.SendMessageImmediately(new PresenceUpdateMessage(status, message, priority))
	}
	def FetchRoster(callback : (List[XmppContact]) => Unit) {
		SendIqRequest(new IqQueryBody(XmppNamespaces.Roster), "get", HandleRosterResponse(callback))
	}
	def SendMessageTo(jid : Jid, message : String) {
		connection.SendMessageImmediately(new ChatMessage(jid, message))
	}
	def Roster = new XmppRoster(this)
	private def HandlePresenceMessage(message : XmppProtocolMessage) {
		val msg = message match {
			case x : PresenceUpdateMessage => x
		}
		PresenceUpdatedCallback(msg.From, msg.UpdateType, msg.Status, msg.Priority)
	}
	private def HandleChatMessage(message : XmppProtocolMessage) {
		val msg = message match {
			case x : ChatMessage => x
		}
		if (msg.MessageBody == null) {
			return;
		}
		MessageReceivedCallback(msg.To, msg.MessageBody)
	}
	private def HandleIqMessage(message : XmppProtocolMessage) {
		try {
			message match {
				case x : IqResponseMessage => {
					correlator.FetchCallback(x)(x)
				}
			}
		} catch {
			case x : NoSuchElementException => {}
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