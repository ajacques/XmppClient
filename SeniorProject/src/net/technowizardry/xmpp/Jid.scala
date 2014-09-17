package net.technowizardry.xmpp

class Jid(username : String, domain : String, resource : String) {
	def this(username : String, domain : String) = this(username, domain, null)
	def Username = username
	def Domain = domain
	def Resource = resource
	def GetBareJid() = new Jid(username, domain, null)
	override def toString() : String = {
		Resource match {
			case null => String.format("%s@%s", Username, Domain)
			case _ => String.format("%s@%s/%s", Username, Domain, Resource)
		}
	}
}

object Jid {
	val jidRegex = """([^@]+)@([^/]+)(?:/(.+))?""".r
	def FromString(input : String) = {
		val m = jidRegex findFirstMatchIn input get
		val user = m.group(1)
		val domain = m.group(2)
		val resource = m.groupCount match {
			case 3 => m.group(3)
			case _ => null
		}
		new Jid(user, domain, resource)
	}
}