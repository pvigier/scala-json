lazy val root = (project in file(".")).
  settings(
    name := "scala-json",
    version := "0.1",
    scalaVersion := "2.11.8",

    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
    libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6",
	libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )
