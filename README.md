# Scala-json

Scala-json is a very simple and low level library to manipulate JSON.

The code was inspired by the chapter 28 of Programming in Scala by M. Odersky et al.

It is able to:

- parse a string which represents a JSON Object and produce a `Map[String, Any]` ;
- produce a string which respects JSON's style from a `Map[String, Any]`.

## License

The code is under the MIT License.

## Examples

Few examples using the REPL: 

```scala
scala> import json._
import json._

scala> JSON("""{"id": 1, "name": "John", "items": [1, 2, 3], "address": {"country": "USA", "state": "DC"}}""")
res1: Map[String,Any] = Map(id -> 1, name -> John, items -> List(1, 2, 3), address -> Map(country -> USA, state -> DC))

scala> Map("id" -> 1, "name" -> "John", "items" -> List(1, 2, 3), "address" -> Map("country" -> "USA", "state" -> "DC")).toJSON
res2: String = {"id": 1, "name": "John", "items": [1, 2, 3], "address": {"country": "USA", "state": "DC"}}

scala> Map("id" -> 1, "name" -> "John", "items" -> List(1, 2, 3), "address" -> Map("country" -> "USA", "state" -> "DC")).toPrettyJSON
res3: String =
{
	"id": 1,
	"name": "John",
	"items": [1, 2, 3],
	"address": {
		"country": "USA",
		"state": "DC"
	}
}
```