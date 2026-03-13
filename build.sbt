name := "zio"
version := "0.1"
scalaVersion := "3.7.4"


lazy val zioVersion = "2.1.24"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-test" % zioVersion,
  "dev.zio" %% "zio-test-sbt" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-test-junit" % zioVersion,
  "dev.zio" %% "zio-http" % "3.2.0",
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % "1.11.29",
  "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % "1.11.29",
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.11.29",
  "org.typelevel" %% "cats-effect" % "3.7.0",
  "com.softwaremill.sttp.client3" %% "zio" % "3.10.3",
  "com.softwaremill.sttp.client3" %% "upickle" % "3.10.3",
  "com.softwaremill.sttp.client3" %% "circe" % "3.10.3",
  "io.circe" %% "circe-generic" % "0.14.10"
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")