import json.JsonParser

package object json {
    implicit class MapImprovements(val m: Map[String, Any]) {
    	private def encode(value: Any, pretty: Boolean = false, offset: String = ""): String = value match {
			case x: Map[String @unchecked, Any @unchecked] => 
				if (pretty) x.toIndentedJson(offset + '\t') else x.toJson
			case x: Seq[_] => "[" + (x mkString ", ") + "]"
			case x: Array[_] => "[" + (x mkString ", ") + "]"
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
		def toObject: Map[String, Any] = {
			try {
				val tokens = new parser.lexical.Scanner(s)
				parser.phrase(parser.obj)(tokens).get
			} catch {
				case e: Exception => throw new JsonException(e.getMessage)
			}
		}
	}
}