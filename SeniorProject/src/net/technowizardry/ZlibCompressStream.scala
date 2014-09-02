package net.technowizardry

import java.io.OutputStream
import org.bolet.jgz.ZlibOutputStream

class ZlibCompressStream(inner : OutputStream) extends ZlibOutputStream(inner) {
}