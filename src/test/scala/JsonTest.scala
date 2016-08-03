import org.scalatest._

import json._


class JsonSpec extends WordSpec {

	def isObjTest(m: Map[_, _], expected: Boolean) = {
		"isObj(" + m + ")" should {
			"return " + expected in {
				assert(isObj(m) == expected)
			}
		}
	}

	def isObjSeqTest(seq: Seq[_], expected: Boolean) = {
		"isObjSeq(" + seq + ")" should {
			"return " + expected in {
				assert(isObjSeq(seq) == expected)
			}
		}
	}

	def jsonTest(json: String, expectedMap: Map[String, Any]) = {
		json should {
			"return " + expectedMap in {
				assert(json.toObject == expectedMap)
			}
		}
	}

	def jsonFailureTest(json: String) = {
		json should {
			"not be parsed successfully" in {
				assert(
					try {
						json.toObject
						false
					} catch {
						case e: JsonException => true
					}
				)
			}
		}
	}

	def jsonFormattingTest(m: Map[String, Any], expectedString: String) = {
		m.toString should {
			"produce Json " + expectedString in {
				assert(m.toJson == expectedString)
			}
		}
	}

	def jsonPrettyFormattingTest(m: Map[String, Any], expectedString: String) = {
		m.toString should {
			"produce pretty Json " + expectedString in {
				assert(m.toPrettyJson == expectedString)
			}
		}
	}

	/*
	 * isObj and isObjSeq
	 */

	isObjTest(Map(), true)
	isObjTest(Map(("id", 32), ("price", 9.99)), true)
	isObjTest(Map((32, 45)), false)
	isObjTest(Map(("id", 32), (32, 45)), false)

	isObjSeqTest(List(), false)
	isObjSeqTest(List(Map(("id", 32))), true)
	isObjSeqTest(Array(Map(("id", 32))), true)
	isObjSeqTest(List("id"), false)
	isObjSeqTest(Array("id"), false)
	isObjSeqTest(List(Map(("id", 32)), "id"), false)
	isObjSeqTest(Array(Map(("id", 32)), "id"), false)

	/*
	 * Simple tests
	 */

	jsonTest("{}", Map())
	jsonTest("""{"id": 32}""", Map(("id", 32)))
	jsonTest("""{"id": -32}""", Map(("id", -32)))
	jsonTest("""{"size": 1.32}""", Map(("size", 1.32)))
	jsonTest("""{"size": -1.32}""", Map(("size", -1.32)))
	jsonTest("""{"success": true}""", Map(("success", true)))
	jsonTest("""{"success": false}""", Map(("success", false)))
	jsonTest("""{"id": null}""", Map(("id", null)))
	jsonTest("""{"items": []}""", Map(("items", List())))
	jsonTest("""{"items": [2, 5, 7]}""", Map(("items", List(2, 5, 7))))
	jsonTest("""{"title": ""}""", Map(("title", "")))
	jsonTest("""{"title": "Foundation"}""", Map(("title", "Foundation")))

	/*
	 * Objects with several fields
	 */

	jsonTest("""{"id": 32, "name": "John", "age": 45, "movies": [1, 2, 3]}""", 
	 	Map(("id", 32), ("name", "John"), ("age", 45), ("movies", List(1, 2, 3))))

	/*
	 * Nested objects
	 */

	jsonTest("""
		{
			"user": {
				"id": 32,
				"name": "John",
				"age": 45,
				"address": {
					"country": "USA",
					"state": "DC"
				}
			},
			"book": {
				"title": "Foundation",
				"price": 9.99
			}
		}
		""",
		Map("user" -> Map(
				"id" -> 32,
				"name" -> "John",
				"age" -> 45,
				"address" -> Map(
					"country" -> "USA",
					"state" -> "DC"
				)
			),
			"book" -> Map(
				"title" -> "Foundation",
				"price" -> 9.99
			)
		)
	)

	/*
	 * List of objects
	 */

	jsonTest("""{"objects": [{"id": 1}, {"id": 2}, {"id": 3}]}""",
		Map("objects" -> List(Map("id" -> 1), Map("id" -> 2), Map("id" -> 3))))

	/*
	 * Poorly indented objects
	 */

	jsonTest("{\n\t \"id\"\n\t :\t\n 32\n\t }", Map(("id", 32)))

	jsonTest("""
		{"user"  :  {
 "id" : 32  ,   "name" : "John" ,
	"age": 45,
		"address": 
	{ "country" :"USA" , "state" : "DC" } } ,
	  "book" : { "title" : "Foundation", "price" : 9.99 }}
		""",
		Map("user" -> Map(
				"id" -> 32,
				"name" -> "John",
				"age" -> 45,
				"address" -> Map(
					"country" -> "USA",
					"state" -> "DC"
				)
			),
			"book" -> Map(
				"title" -> "Foundation",
				"price" -> 9.99
			)
		)
	)

	/*
	 * Syntax errors
	 */

	jsonFailureTest("""{"id: 32, "name": "John", "age": 45, "movies": [1, 2, 3]}""")
	jsonFailureTest("""{"id": 32, "name": "John", "age": 45, "movies": [1, 2, 3]""")
	jsonFailureTest("""{"id": 32, "name": "John", "age": 45, "movies": [1, 2, 3}""")
	jsonFailureTest("""{"id": 32, "name": "John, "age": 45, "movies": [1, 2, 3]}""")
	jsonFailureTest("""{"id": 32, "name": "John", "age": 45., "movies": [1, 2, 3]}""")
	jsonFailureTest("""{"id": 32, "name": "John", "age": 45, "movies": [1,, 3]}""")
	jsonFailureTest("""{"id": 32, "name": "John" "age": 45, "movies": [1, 2, 3]}""")

	/*
	 * Formatting (toJson)
	 */

	jsonFormattingTest(Map(), "{}")
	jsonFormattingTest(Map(("id", 32)), """{"id": 32}""")
	jsonFormattingTest(Map(("id", -32)), """{"id": -32}""")
	jsonFormattingTest(Map(("size", 1.32)), """{"size": 1.32}""")
	jsonFormattingTest(Map(("size", -1.32)), """{"size": -1.32}""")
	jsonFormattingTest(Map(("success", true)), """{"success": true}""")
	jsonFormattingTest(Map(("success", false)), """{"success": false}""")
	jsonFormattingTest(Map(("id", null)), """{"id": null}""")
	jsonFormattingTest(Map(("items", List())), """{"items": []}""")
	jsonFormattingTest(Map(("items", List(2, 5, 7))), """{"items": [2, 5, 7]}""")
	jsonFormattingTest(Map(("title", "")), """{"title": ""}""")
	jsonFormattingTest(Map(("title", "Foundation")), """{"title": "Foundation"}""")
	jsonFormattingTest(Map(("id", 32), ("name", "John"), ("age", 45), ("movies", List(1, 2, 3))),
		"""{"id": 32, "name": "John", "age": 45, "movies": [1, 2, 3]}""")
	jsonFormattingTest(
		Map("user" -> Map(
				"id" -> 32,
				"name" -> "John",
				"age" -> 45,
				"address" -> Map(
					"country" -> "USA",
					"state" -> "DC"
				)
			),
			"book" -> Map(
				"title" -> "Foundation",
				"price" -> 9.99
			)
		),
		"""{"user": {"id": 32, "name": "John", "age": 45, "address": {"country": "USA", "state": "DC"}}, "book": {"title": "Foundation", "price": 9.99}}"""
	)
	jsonFormattingTest(Map("objects" -> List(Map("id" -> 1), Map("id" -> 2), Map("id" -> 3))),
		"""{"objects": [{"id": 1}, {"id": 2}, {"id": 3}]}""")
	jsonFormattingTest(Map("objects" -> Array(Map("id" -> 1), Map("id" -> 2), Map("id" -> 3))),
		"""{"objects": [{"id": 1}, {"id": 2}, {"id": 3}]}""")

	/*
	 * Formatting (toPrettyJson)
	 */
	 
	jsonPrettyFormattingTest(Map(), "{\n\n}")
	jsonPrettyFormattingTest(Map(("id", 32)), "{\n\t\"id\": 32\n}")
	jsonPrettyFormattingTest(Map(("id", -32)), "{\n\t\"id\": -32\n}")
	jsonPrettyFormattingTest(Map(("size", 1.32)), "{\n\t\"size\": 1.32\n}")
	jsonPrettyFormattingTest(Map(("size", -1.32)), "{\n\t\"size\": -1.32\n}")
	jsonPrettyFormattingTest(Map(("success", true)), "{\n\t\"success\": true\n}")
	jsonPrettyFormattingTest(Map(("success", false)), "{\n\t\"success\": false\n}")
	jsonPrettyFormattingTest(Map(("id", null)), "{\n\t\"id\": null\n}")
	jsonPrettyFormattingTest(Map(("items", List())), "{\n\t\"items\": []\n}")
	jsonPrettyFormattingTest(Map(("items", List(2, 5, 7))), "{\n\t\"items\": [2, 5, 7]\n}")
	jsonPrettyFormattingTest(Map(("title", "")), "{\n\t\"title\": \"\"\n}")
	jsonPrettyFormattingTest(Map(("title", "Foundation")), "{\n\t\"title\": \"Foundation\"\n}")
	jsonPrettyFormattingTest(Map(("id", 32), ("name", "John"), ("age", 45), ("movies", List(1, 2, 3))),
		"{\n\t\"id\": 32,\n\t\"name\": \"John\",\n\t\"age\": 45,\n\t\"movies\": [1, 2, 3]\n}")
	jsonPrettyFormattingTest(
		Map("user" -> Map(
				"id" -> 32,
				"name" -> "John",
				"age" -> 45,
				"address" -> Map(
					"country" -> "USA",
					"state" -> "DC"
				)
			),
			"book" -> Map(
				"title" -> "Foundation",
				"price" -> 9.99
			)
		),
		"{\n\t\"user\": {\n\t\t\"id\": 32,\n\t\t\"name\": \"John\",\n\t\t\"age\": 45,\n\t\t" +
		"\"address\": {\n\t\t\t\"country\": \"USA\",\n\t\t\t\"state\": \"DC\"\n\t\t}\n\t},\n\t" + 
		"\"book\": {\n\t\t\"title\": \"Foundation\",\n\t\t\"price\": 9.99\n\t}\n}"
	)
	jsonPrettyFormattingTest(Map("objects" -> List(Map("id" -> 1), Map("id" -> 2), Map("id" -> 3))),
		"{\n\t\"objects\": [\n\t\t{\n\t\t\t\"id\": 1\n\t\t}," +
		"\n\t\t{\n\t\t\t\"id\": 2\n\t\t},\n\t\t{\n\t\t\t\"id\": 3\n\t\t}\n\t]\n}"
	)
	jsonPrettyFormattingTest(Map("objects" -> Array(Map("id" -> 1), Map("id" -> 2), Map("id" -> 3))),
		"{\n\t\"objects\": [\n\t\t{\n\t\t\t\"id\": 1\n\t\t}," +
		"\n\t\t{\n\t\t\t\"id\": 2\n\t\t},\n\t\t{\n\t\t\t\"id\": 3\n\t\t}\n\t]\n}"
	)
}