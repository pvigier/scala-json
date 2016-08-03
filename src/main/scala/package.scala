import json.JsonParser

package object json {
	type JsonObject = Map[String, Any]

	def isObj(m: Map[_, _]): Boolean =
		(m.isEmpty) ||
		(m forall {
			case (key: String, value: Any) => true
			case _ => false
		})

	def isObjSeq(seq: Seq[_]): Boolean = 
		(!seq.isEmpty) && 
		(seq forall {
			case x: Map[_, _] => isObj(x)
			case _ => false
		})

    implicit class MapImprovements(val m: JsonObject) {
    	private def seqToJson(x: Seq[_]) = "[" + ((x map{ encode(_) }) mkString ", ") + "]"

		private def seqToPrettyJson(x: Seq[_], offset: String) = {
			val newOffset = offset + '\t'
			"[\n" + newOffset + '\t' + 
			((x map{ encode(_, true, newOffset) }) mkString ",\n" + newOffset + '\t') + 
			'\n' + newOffset + "]"
		}

    	private def encode(value: Any, pretty: Boolean = false, offset: String = ""): String = value match {
    		// Must convert explicitly Array to Seq
			case x: JsonObject @unchecked if isObj(x) => 
				if (pretty) x.toIndentedJson(offset + '\t') else x.toJson
			case x: Seq[_] if (pretty && isObjSeq(x)) => seqToPrettyJson(x, offset)
			case x: Array[_] if (pretty && isObjSeq(x)) => seqToPrettyJson(x, offset)
			case x: Seq[_] => seqToJson(x)
			case x: Array[_] => seqToJson(x)
			case x: Number => x.toString
			case x: Boolean => x.toString
			case null => "null"
			case x => "\"" + x.toString + "\""
    	}

		def toJson: String = {
	        val members = m map { case (key, value) => s""""$key": ${encode(value)}""" }
	        "{" + (members mkString ", ") + "}"
	    }

	    def toIndentedJson(offset: String = ""): String = {
	    	val members = m map { case (key, value) => offset + "\t" + s""""$key": ${encode(value, true, offset)}""" }
	        "{\n" + (members mkString ",\n") + "\n" + offset + "}"
	    }

		def toPrettyJson: String = toIndentedJson()
	}

	private val parser = new JsonParser

	implicit class StringImprovements(val s: String) {
		def toObject: JsonObject = {
			try {
				val tokens = new parser.lexical.Scanner(s)
				parser.phrase(parser.obj)(tokens).get
			} catch {
				case e: Exception => throw new JsonException(e.getMessage)
			}
		}
	}
}