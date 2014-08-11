package net.technowizardry.xmpp.messages

import net.technowizardry.{XMLWriter, XMLReader, XMLObjectType}
import net.technowizardry.xmpp.XmppNamespaces

class StreamInitMessage(server: String, features : List[XmppFeature]) extends XmppProtocolMessage with WritableXmppMessage {
	def WriteMessage(writer: XMLWriter) {
		writer.WriteStartElement("stream", "stream", XmppNamespaces.Streams)
		writer.WriteNamespace("stream", XmppNamespaces.Streams)
		writer.WriteDefaultNamespace(XmppNamespaces.Jabber)
		writer.WriteAttribute("version", "1.0", XmppNamespaces.Jabber)
		writer.WriteAttribute("to", server, XmppNamespaces.Jabber)
		writer.WriteText("")
	}
	def SupportsStartTls() = SupportsFeature("starttls", XmppNamespaces.Tls)
	def SupportsFeature(name : String, namespace : String) = features.exists(p => p.GetNamespace() == namespace && p.GetName() == name)
	def GetServer() = server
	def GetFeatures() = features
}

class XmppFeature(ns : String, name : String, required : Boolean) {
	def GetNamespace() = ns
	def GetName() = name
	def IsRequired() = required
}

class XmppMechanismFeature(ns : String, name : String, required : Boolean, mechs : Array[String]) extends XmppFeature(ns, name, required) {
	def GetMechanisms() = mechs
	def SupportsMechanism(mechanism : String) = mechs.exists(mech => mech == mechanism)
}

object StreamInitMessageParser {
	def Unpack(reader : XMLReader) : StreamInitMessage = {
		var from = reader.GetAttributeValue(XmppNamespaces.Jabber, "from")
		reader.Next() // Advance to the features tag
		var features = UnpackFeatures(reader)
		var msg = new StreamInitMessage(from, features)

		return msg
	}
	private def UnpackFeatures(reader : XMLReader) : List[XmppFeature] = {
		var features = List[XmppFeature]()
		reader.Next()
		while (reader.LocalName() != "features") {
			features ::= UnpackFeature(reader)
		}
		return features
	}
	private def UnpackFeature(reader : XMLReader) : XmppFeature = {
		var local = reader.LocalName()
		var ns = reader.NamespaceURI()
		var req = false
		reader.Next()
		if (reader.NodeType() == XMLObjectType.StartElement) {
			reader.LocalName() match {
				case "required" => req = true
				case "mechanism" =>
			}
			reader.Next() // Advance to </feature>
		}
		reader.Next() // Advance to next <feature> or </features>
		// We might be in an unsafe state here
		return new XmppFeature(ns, local, req)
	}
}