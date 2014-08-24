package net.technowizardry

import java.io.InputStream
import org.bolet.jgz.ZlibInputStream

class ZlibDecompressStream(inner : InputStream) extends ZlibInputStream(inner) {
}