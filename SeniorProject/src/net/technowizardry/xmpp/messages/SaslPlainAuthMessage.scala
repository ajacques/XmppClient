package net.technowizardry.xmpp.messages

import net.technowizardry.XMLWriter
import net.technowizardry.Base64
import java.nio.ByteBuffer

class SaslPlainAuthMessage(username : String, password : String) extends SaslAuthMessage {
	def WriteAuthBody(writer : XMLWriter) {
		val busername = username.getBytes()
		val bpassword = password.getBytes()
		val buffer = Array[Byte](0) ++ busername ++ Array[Byte](0) ++ bpassword
		val base64d = Base64.Encode(buffer)
		writer.WriteText(base64d)
	}
	def GetMechanismName() = "PLAIN"
}