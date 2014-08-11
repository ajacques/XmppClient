package net.technowizardry.xmpp.messages

import net.technowizardry.{XMLWriter, XMLReader}
import net.technowizardry.xmpp._

abstract class SaslAuthMessage extends XmppProtocolMessage with WritableXmppMessage {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteStartElement("auth", XmppNamespaces.Sasl)
		writer.WriteAttribute("mechanism", GetMechanismName(), XmppNamespaces.Sasl)
		WriteAuthBody(writer)
		writer.WriteEndElement()
	}
	protected def WriteAuthBody(writer : XMLWriter)
	protected def GetMechanismName() : String
}

class SaslChallengeMessage extends XmppProtocolMessage {}

class SaslFailureMessage(message : String) extends XmppProtocolMessage {}

class SaslSuccessMessage extends XmppProtocolMessage {}

object SaslParser {
	def UnpackChallenge(reader : XMLReader) : XmppProtocolMessage = {
		return new SaslChallengeMessage()
	}
	def UnpackSuccess(reader : XMLReader) : XmppProtocolMessage = {
		return new SaslSuccessMessage()
	}
	def UnpackFailure(reader : XMLReader) : XmppProtocolMessage = {
		reader.Next()
		val message = reader.ElementText()
		reader.Next()
		return new SaslFailureMessage(message)
	}
}