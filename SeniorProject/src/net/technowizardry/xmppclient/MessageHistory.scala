package net.technowizardry.xmppclient

import scala.collection.mutable._
import net.technowizardry.xmpp._
import java.util.Date
import java.io._
import java.text._
import android.content._

class Message(source : Jid, date : Date, message : String) {
	def Source = source
	def Date = date
	def Message = message
}

object MessageHistory {
	def GetHistory(context : Context, jid : Jid) : List[Message] = {
		val name = String.format("chats-%s", jid.GetBareJid().toString())
		println(name)
		try {
			val file = context.openFileInput(name)
			val reader = new BufferedReader(new InputStreamReader(file))
			val messages = MutableList[Message]()
			val parser = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			var die = true
			while (die) {
				val jidd = reader.readLine()
				if (jidd != null) {
					val date = reader.readLine()
					val message = reader.readLine()
					messages += new Message(Jid.FromString(jidd), parser.parse(date), message)
				} else {
					die = false
				}
			}
			messages.toList
		} catch {
			case e : Exception => {
				return List[Message]()
			}
		}
	}
	def AddToHistory(context : Context, jid : Jid, message : String) {
		val name = String.format("chats-%s", jid.GetBareJid().toString())
		val file = context.openFileOutput(name, Context.MODE_APPEND)
		val writer = new BufferedWriter(new OutputStreamWriter(file))
		val parser = new SimpleDateFormat("yyyy-MM-dd HH:mm")
		writer.write(jid.GetBareJid().toString())
		writer.newLine()
		writer.write(parser.format(new Date()))
		writer.newLine()
		writer.write(message)
		writer.newLine()
		writer.flush()
		writer.close()
	}
}


// I am BATMAN