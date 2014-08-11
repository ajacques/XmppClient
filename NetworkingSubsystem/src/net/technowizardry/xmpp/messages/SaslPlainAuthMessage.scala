package net.technowizardry.xmpp.messages

import net.technowizardry.XMLWriter
import sun.misc.BASE64Encoder
import java.nio.ByteBuffer

class SaslPlainAuthMessage(username : String, password : String) extends SaslAuthMessage {
	def WriteAuthBody(writer : XMLWriter) {
		val busername = username.getBytes()
		val bpassword = password.getBytes()
		val buffer = Array[Byte](0) ++ busername ++ Array[Byte](0) ++ bpassword
		val base64d = new BASE64Encoder().encode(buffer)
		writer.WriteText(base64d)
	}
	def GetMechanismName() = "PLAIN"
}