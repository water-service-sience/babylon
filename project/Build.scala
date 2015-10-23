
import play.PlayImport.PlayKeys
import sbt.Keys._
import sbt._
import play.sbt.PlayImport._


object ApplicationBuild extends Build {

  val appVersion      = "1.0-SNAPSHOT"
  val projectScalaVersion = "2.11.7"

  val libraryDeps = Seq(
    // Add your project dependencies here,
    "net.liftweb" %% "lift-mapper" % "2.6.2",
    "mysql" % "mysql-connector-java" % "5.1.23",
    "org.json4s" %% "json4s-native" % "3.3.0"
  )

  val appDependencies = Seq(
    ws
  )
  def runChildPlayServer = Command.command("run")( state => {
    val subState = Command.process("project babylon-server",state)
    Command.process("run",subState)
    state
  })

  lazy val commonSettings = Defaults.defaultSettings ++ Seq(
    organization := "jp.utokyo",
    scalaVersion := projectScalaVersion,
    version := appVersion,
    libraryDependencies ++= Seq()
  )



  lazy val main = Project(id = "babylon",base=file("."), settings = commonSettings ++ Seq(
    commands ++= Seq(runChildPlayServer)
  )).aggregate(library,server)

  lazy val library = Project(id = "babylon-library",base=file("babylon-library"), settings = commonSettings ++ Seq(
    libraryDependencies ++= libraryDeps
  ))

  lazy val server = Project("babylon-server",file("babylon-server")).settings(
    organization := "jp.utokyo",
    scalaVersion := projectScalaVersion,
    libraryDependencies ++= appDependencies
  ).enablePlugins(play.sbt.PlayScala).dependsOn(library)
}
