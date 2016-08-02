import json.JSONParser

package object json {
    implicit class MapFormatting(val m: Map[String, Any]) {
    	private def encode(value: Any, pretty: Boolean = false, offset: String = ""): String = value match {
			case x: Map[String @unchecked, Any @unchecked] => 
				if (pretty) x.toIndentedJSON(offset + '\t') else x.toJSON
			case x: Seq[_] => "[" + (x mkString ", ") + "]"
			case x: Array[_] => "[" + (x mkString ", ") + "]"
			case x: Number => x.toString
			case x: Boolean => x.toString
			case null => "null"
			case x => "\"" + x.toString + "\""
    	}

		def toJSON: String = {
	        val members = m map { case (key, value) => s""""$key": ${encode(value)}""" }
	        "{" + (members mkString ", ") + "}"
	    }

	    def toIndentedJSON(offset: String = ""): String = {
	    	val members = m map { case (key, value) => offset + "\t" + s""""$key": ${encode(value, true, offset)}""" }
	        "{\n" + (members mkString ",\n") + "\n" + offset + "}"
	    }

		def toPrettyJSON: String = toIndentedJSON()
	}

	object JSON extends JSONParser {
		def apply(s: String) = {
			val tokens = new lexical.Scanner(s)
			phrase(obj)(tokens).get
		}
	}
}