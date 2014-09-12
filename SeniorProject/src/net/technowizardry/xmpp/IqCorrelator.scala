package net.technowizardry.xmpp

import net.technowizardry.Base64
import net.technowizardry.xmpp.messages._
import java.nio.ByteBuffer
import scala.util.Random

class IqCorrelator {
	var id = new Random().nextLong()
	var inflight_iqs = Map[String, IqResponseMessage => Unit]()
	def RegisterRequest(request : IqRequestBody, mtype : String, callback : IqResponseMessage => Unit) : IqRequestMessage = {
		val id = EncodeIdentifier(GenerateNextIdentifier())
		inflight_iqs += (id -> callback)
		new IqRequestMessage(id, mtype, request)
	}
	def FetchCallback(response : IqResponseMessage) : IqResponseMessage => Unit = {
		val callback = inflight_iqs(response.MessageId)
		inflight_iqs -= response.MessageId
		callback
	}
	def GenerateNextIdentifier() : Long = {
		id = (id >> 1L) ^ (-(id & 1L) & 0x1800000000000030L)
		id
	}
	def EncodeIdentifier(identifier : Long) : String = Base64.Encode(ByteBuffer.allocate(8).putLong(identifier).array()).stripSuffix("=")
}