package net.technowizardry

import java.io.{InputStream,OutputStream}

trait XMLStreamFactory {
	def CreateReader(stream : InputStream) : XMLReader
	def CreateWriter(stream : OutputStream) : XMLWriter
}