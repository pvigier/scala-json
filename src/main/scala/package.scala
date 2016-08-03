import json.JsonParser

package object json {
    implicit class MapFormatting(val m: Map[String, Any]) {
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

	object Json extends JsonParser {
		def apply(s: String) = {
			val tokens = new lexical.Scanner(s)
			phrase(obj)(tokens).get
		}
	}
}