import sbt._

object Dependencies {

  object V {
    val scalaTest = "2.2.2"
    val slf4j = "1.7.7"
    val logback = "1.1.2"

    val akka = "2.3.7"
    val spray = "1.3.2-20140909"
    val scalaLogging = "2.1.2"

    val json4s = "3.2.11"
  }

  object Libraries {
    val scalaTest= "org.scalatest"           %% "scalatest"                % V.scalaTest % "test"
    val scalaLogging="com.typesafe.scala-logging" %% "scala-logging-api" % V.scalaLogging
    val scalaLoggingSlf4j="com.typesafe.scala-logging" %% "scala-logging-slf4j" % V.scalaLogging
    val logback = "ch.qos.logback"           % "logback-classic"          % V.logback % "test"

    val sprayClient = "io.spray"             %% "spray-client"		% V.spray
    val sprayRouting = "io.spray"            %% "spray-routing"  	% V.spray
    val sprayTestKit = "io.spray"            %% "spray-testkit"  	% V.spray % "test"

    val json4sNative = "org.json4s"          %% "json4s-native"		% V.json4s
    val json4sJackon = "org.json4s"          %% "json4s-jackson"	% V.json4s
    val json4sExtensions = "org.json4s"      %% "json4s-ext"		% V.json4s

    val akka = "com.typesafe.akka"           %% "akka-actor"		% V.akka
  }

  import Libraries._ 

  val hipChatClient = Seq(scalaTest, scalaLoggingSlf4j, logback, sprayClient, sprayRouting, json4sJackon, akka, sprayTestKit)
}
