package net.technowizardry

import java.io.InputStream

class TracingInputStream(inner : InputStream) extends InputStream {
	def read() : Int = {
		val t = inner.read()
		print(new String(Array[Byte] ( t.toByte )))
		return t
	}
}