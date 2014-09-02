package net.technowizardry.xmpp

import net.technowizardry.xmpp.messages._

class XmppSession(connection : XmppConnection) {
	val correlator = new IqCorrelator()
	connection.RegisterMessageCallback(classOf[IqResponseMessage], HandleIqMessage)
	def BindToResource(resourceName : String) {
		SendIqRequest(new ResourceBindMessage(resourceName), NoopCallback)
	}
	def SendIqRequest(iq : IqRequestBody, callback : IqResponseMessage => Unit) {
		val msg = correlator.RegisterRequest(iq, callback)
		connection.SendMessageImmediately(msg)
	}
	def FetchRoster() {
		SendIqRequest(new IqQueryBody(XmppNamespaces.Roster), NoopCallback)
	}
	private def HandleIqMessage(message : XmppProtocolMessage) {
		message match {
			case x : IqResponseMessage => {
				correlator.FetchCallback(x)(x)
			}
		}
	}
	private def NoopCallback(message : IqResponseMessage) {
		println(message)
	}
}