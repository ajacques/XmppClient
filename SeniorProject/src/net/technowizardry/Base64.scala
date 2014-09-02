package net.technowizardry

import sun.misc.{BASE64Encoder, BASE64Decoder}

object Base64 extends Base64er {
	val thinger = new AndroidBase64Thing()
	def Encode(input : String) = thinger.Encode(input)
	def Encode(input : Array[Byte]) = thinger.Encode(input)
	def Decode(input : String) = thinger.Decode(input)
}

trait Base64er {
	def Encode(input : String) : String
	def Encode(input : Array[Byte]) : String
	def Decode(input : String) : String
}

class JavaBase64Thing extends Base64er {
	def Encode(input : String) = Encode(input.getBytes())
	def Encode(input : Array[Byte]) = new BASE64Encoder().encode(input)
	def Decode(input : String) = new String(new BASE64Decoder().decodeBuffer(input))
}

class AndroidBase64Thing extends Base64er {
	def Encode(input : String) = Encode(input.getBytes())
	def Encode(input : Array[Byte]) = android.util.Base64.encodeToString(input, 0)
	def Decode(input : String) = new String(android.util.Base64.decode(input, 0))
}