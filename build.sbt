name := "scala-anorm-test"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  evolutions,
  jdbc,
  ws,
  "com.h2database" % "h2" % "1.4.193",
  "com.zaxxer" % "HikariCP" % "2.5.1",
  "org.scalaz" %% "scalaz-core" % "7.2.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.13",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.play" %% "anorm" % "2.5.2",
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.3.15" % Test,
  "org.mockito" % "mockito-core" % "2.2.21" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache2.0.php"))

fork in Test := true

parallelExecution in Test := false

import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

ScalariformKeys.preferences in Compile := formattingPreferences

ScalariformKeys.preferences in Test := formattingPreferences

def formattingPreferences = {
  import scalariform.formatter.preferences._
  FormattingPreferences()
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
    .setPreference(DoubleIndentClassDeclaration, true)
}

import de.heikoseeberger.sbtheader._
import de.heikoseeberger.sbtheader.HeaderKey._
import de.heikoseeberger.sbtheader.license.Apache2_0

headers := headers.value ++ Map(
  "scala" -> Apache2_0("2016", "Dennis Vriend"),
  "conf" -> Apache2_0("2016", "Dennis Vriend", "#")
)

// Declares endpoints. The default is Map("web" -> Endpoint("http", 0, Set.empty)).
// The endpoint key is used to form a set of environment variables for your components,
// e.g. for the endpoint key "web" ConductR creates the environment variable WEB_BIND_PORT.
BundleKeys.endpoints := Map(
  "play" -> Endpoint(bindProtocol = "http", bindPort = 0, services = Set(URI("http://:9000/play"))),
  "akka-remote" -> Endpoint("tcp")
)

normalizedName in Bundle := "play-test" // the human readable name for your bundle

BundleKeys.system := "PlayTestSystem" // represents the clustered ActorSystem

BundleKeys.startCommand += "-Dhttp.address=$PLAY_BIND_IP -Dhttp.port=$PLAY_BIND_PORT -Dplay.akka.actor-system=$BUNDLE_SYSTEM"

enablePlugins(AutomateHeaderPlugin, SbtScalariform, ConductrPlugin, PlayScala)