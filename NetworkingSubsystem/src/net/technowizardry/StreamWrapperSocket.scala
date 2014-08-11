package net.technowizardry

import java.net.Socket
import java.io.{InputStream,OutputStream}

class StreamWrapperSocket(input : InputStream, output : OutputStream) extends Socket {
	override def getInputStream = input
	override def getOutputStream = output
	override def isConnected() = true
}