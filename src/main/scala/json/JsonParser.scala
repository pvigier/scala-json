package json

import scala.util.parsing.combinator.syntactical._

class JsonException(message: String) extends Exception(message)

class JsonParser extends StandardTokenParsers {
	lexical.delimiters += ("{", "}", "[", "]", ":", ",", ".", "+", "-")
	lexical.reserved += ("null", "true", "false")

	def obj: Parser[Map[String, Any]] =
		"{" ~> repsep(member, ",") <~ "}" ^^ (Map() ++ _)

	def arr: Parser[List[Any]] =
		"[" ~> repsep(value, ",") <~ "]"

	def member: Parser[(String, Any)] =
		stringLit ~ ":" ~ value ^^ { case name ~ ":" ~ value => (name, value) }

	def uint: Parser[Int] = 
		numericLit ^^ { _.toInt }

	def int: Parser[Int] = 
		opt("+" | "-") ~ uint ^^ { 
			case Some("+") ~ x => x
			case Some("-") ~ x => -x
			case None ~ x => x
		}

	def udouble: Parser[Double] = numericLit ~ "." ~ numericLit ^^ {
			case integerPart ~ "." ~ fractionalPart => 
				(integerPart + "." + fractionalPart).toDouble
		}

	def double: Parser[Double] = 
		opt("+" | "-") ~ udouble ^^ { 
			case Some("+") ~ x => x
			case Some("-") ~ x => -x
			case None ~ x => x
		}

	def value: Parser[Any] =
		obj | arr | stringLit | double | int |
		"null" ^^^ null | "true" ^^^ true | "false" ^^^ false 
}