import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appVersion      = "1.0-SNAPSHOT"
  val projectScalaVersion = "2.10.0"

  val libraryDeps = Seq(
    // Add your project dependencies here,
    "net.liftweb" %% "lift-mapper" % "2.5",
    "mysql" % "mysql-connector-java" % "5.1.23"
  )

  val appDependencies = Seq(
    anorm
  )
  def runChildPlayServer = Command.command("run")( state => {
    val subState = Command.process("project babylon-server",state)
    Command.process("run",subState)
    state
  })

  lazy val commonSettings = Defaults.defaultSettings ++ ideaPluginSettings ++ Seq(
    organization := "jp.utokyo",
    scalaVersion := projectScalaVersion,
    version := appVersion,
    libraryDependencies ++= Seq()
  )
  def ideaPluginSettings = {
    import org.sbtidea.SbtIdeaPlugin
    SbtIdeaPlugin.settings/* ++ Seq(        
      SbtIdeaPlugin.commandName := "idea",
      SbtIdeaPlugin.includeScalaFacet := true
    )*/
  }


  lazy val main = Project(id = "babylon",base=file("."), settings = commonSettings ++ Seq(
    commands ++= Seq(runChildPlayServer)
  )).aggregate(library,server)

  lazy val library = Project(id = "babylon-library",base=file("babylon-library"), settings = commonSettings ++ Seq(
    libraryDependencies ++= libraryDeps
  ))

  lazy val server = play.Project("babylon-server",appVersion,path = file("./babylon-server")).settings(
    organization := "jp.utokyo",
    scalaVersion := projectScalaVersion,
    libraryDependencies ++= appDependencies
  ).dependsOn(library)
}
