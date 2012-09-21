import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Upload"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
    "net.java.dev.jets3t" % "jets3t" % "0.9.0"
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    resolvers += "jets3t" at "http://www.jets3t.org/maven2"
    
      // Add your own project settings here      
    )
}
