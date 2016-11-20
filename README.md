# scala-anorm-test
A small study project on anorm

# What is Anorm?
Anorm is a simple data access layer that uses plain SQL to interact with the database and provides an API to parse and 
transform the resulting datasets.

# Documentation
- [Github - Anorm](https://github.com/playframework/anorm)
- [Github - User Guide](https://github.com/playframework/anorm/blob/master/docs/manual/working/scalaGuide/main/sql/ScalaAnorm.md)
- [Anorm - Scaladoc](https://oss.sonatype.org/service/local/repositories/releases/archive/com/typesafe/play/anorm_2.11/2.5.2/anorm_2.11-2.5.2-javadoc.jar/!/index.html)
- [Playframework - Configuring and using JDBC](https://www.playframework.com/documentation/2.5.x/ScalaDatabase)
- [Playframework - Anorm Migration Guide](https://playframework.com/documentation/2.5.x/Anorm)
- [Playframework - Scala Anorm](https://www.playframework.com/documentation/2.5.x/ScalaAnorm)
- [Playframework - Play-Slick FAQ](https://www.playframework.com/documentation/2.5.x/PlaySlickFAQ)
- [Playframework - Database Evolutions](https://www.playframework.com/documentation/2.5.x/Evolutions)
- [Playframework - Play Versions](https://www.playframework.com/download#older-versions)

# Setting up Play with Anorm
Anorm has been pulled out of the core of Play into a separately managed project that can have its own lifecycle. To add a dependency on it, use:

```scala
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.2"
```

# Setting up JDBC
You should definitely read the - [Playframework - Configuring and using JDBC](https://www.playframework.com/documentation/2.5.x/ScalaDatabase).

```scala
# Default database configuration using H2 database engine in an in-memory mode
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
```

# Configuring the Connection Pool
Out of the box, Play uses the HikariCP connection pool

# Slick and Anorm
You cannot combine them, please remove all references to Slick eg.

```scala
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2"
```

should not be there.

