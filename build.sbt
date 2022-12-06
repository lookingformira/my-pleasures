scalaVersion := "2.13.10"
version := "0.1.0-SNAPSHOT"
organization := "com.example"
organizationName := "example"

name := "my-pleasures"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.4",
  "io.d11" %% "zhttp" % "2.0.0-RC10",
  "dev.zio" %% "zio-json" % "0.3.0-RC11",

  "io.getquill" %% "quill-jdbc-zio" % "4.6.0",
  "org.postgresql" % "postgresql" % "42.3.5",

  "org.flywaydb" % "flyway-core" % "9.8.3",

  "dev.zio" %% "zio-test" % "2.0.4" % Test,

  "io.github.scottweaver" %% "zio-2-0-testcontainers-postgresql" % "0.9.0",
  "io.github.scottweaver" %% "zio-2-0-db-migration-aspect" % "0.9.0",
)
