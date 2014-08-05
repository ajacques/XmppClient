package net.technowizardry

import java.io.{InputStream,OutputStream}

class JavaXMLStreamFactory extends XMLStreamFactory {
	def CreateReader(stream : InputStream) = new JavaXMLReader(stream)
	def CreateWriter(stream : OutputStream) = new JavaXMLWriter(stream)
}