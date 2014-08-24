package net.technowizardry

import sun.misc.{BASE64Encoder, BASE64Decoder}

object Base64 {
	def Encode(input : String) = new BASE64Encoder().encode(input.getBytes())
	def Encode(input : Array[Byte]) = new BASE64Encoder().encode(input)
	def Decode(input : String) = new String(new BASE64Decoder().decodeBuffer(input))
}