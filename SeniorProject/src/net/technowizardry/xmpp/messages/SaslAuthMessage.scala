package net.technowizardry.xmpp.messages

import net.technowizardry.{Base64, XMLWriter, XMLReader}
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

class SaslChallengeMessage(message : String) extends XmppProtocolMessage {
	val b64decode = Base64.Decode(message)
	val dict = b64decode
		.split(',')
		.map(_.split('='))
		.map { case Array(k, v) => (k, v)}
		.toMap
	def Message = b64decode
	def GetProperty(prop : String) = dict.get(prop).orNull
}

class SaslResponseMessage(message : String) extends XmppProtocolMessage with WritableXmppMessage {
	def WriteMessage(writer : XMLWriter) {
		writer.WriteStartElement("response", XmppNamespaces.Sasl)
		writer.WriteText(Base64.Encode(message))
		writer.WriteEndElement()
	}
	def Message = message
}

class SaslFailureMessage(message : String) extends XmppProtocolMessage {}

class SaslSuccessMessage extends XmppProtocolMessage {}

object SaslParser {
	def UnpackChallenge(reader : XMLReader) : XmppProtocolMessage = {
		val string = reader.ElementText
		//reader.Next()
		return new SaslChallengeMessage(string)
	}
	def UnpackSuccess(reader : XMLReader) : XmppProtocolMessage = {
		reader.Next();
		return new SaslSuccessMessage()
	}
	def UnpackFailure(reader : XMLReader) : XmppProtocolMessage = {
		reader.Next()
		val message = reader.ElementText()
		reader.Next()
		return new SaslFailureMessage(message)
	}
}