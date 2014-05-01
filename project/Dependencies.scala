import sbt._

object Dependencies {

  object V {
    val scalaTest = "2.0"
    val slf4j = "1.7.6"
    val logback = "1.1.1"

    val akka = "2.3.1"
    val spray = "1.3.1"
    val typesafeLogging = "1.0.1"

    val json4s = "3.2.9"
  }

  object Libraries {
    val scalaTest= "org.scalatest"           %% "scalatest"                % V.scalaTest % "test"
    val typesafeLogging = "com.typesafe"     %% "scalalogging-slf4j"       % V.typesafeLogging
    val logback = "ch.qos.logback"           % "logback-classic"          % V.logback % "test"

    val sprayClient = "io.spray"             % "spray-client"     % V.spray
    val sprayRouting = "io.spray"            % "spray-routing"     % V.spray
    val sprayTestKit = "io.spray"            % "spray-testkit" % V.spray % "test"

    val json4sNative = "org.json4s"          %% "json4s-native" % V.json4s
    val json4sJackon = "org.json4s"          %% "json4s-jackson" % V.json4s
    val json4sExtensions = "org.json4s"      %% "json4s-ext" % V.json4s

    val akka = "com.typesafe.akka"           %% "akka-actor"    % V.akka
  }

  import Libraries._ 

  val hipChatClient = Seq(scalaTest, typesafeLogging, logback, sprayClient, sprayRouting, json4sJackon, akka, sprayTestKit)
}
