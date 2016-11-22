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



# What's new in Anorm 2.5

- Row parser automatically generated for case classes: `Macro.namedParser[T]`, `Macro.indexedParser[T]` and `Macro.parser[T](names)`.
- New `SqlParser.folder` to fold over "non-strict" row columns.
- New numeric and temporal conversions.

# What's new in Anorm 2.4

- Improved statement preparation & string interpolation: new `#$value` syntax and better performance.
- New positional getter on `Row`.
- Unified column resolution by label, whatever it is (name or alias).
- New streaming API; Functions `fold` and `foldWhile` to work with result stream (e.g. `SQL"Select count(*) as c from Country".fold(0l) { (c, _) => c + 1 }`).
- Function `withResult` to provide custom stream parser (e.g. `SQL("Select name from Books").withResult(customTailrecParser(_, List.empty[String]))`).
- Supports array (`java.sql.Array`) from column (e.g. `SQL("SELECT str_arr FROM tbl").as(scalar[Array[String]].*)`) or as parameter (e.g. `SQL"""UPDATE Test SET langs = ${Array("fr", "en", "ja")}""".execute()`).
- Improved conversions for numeric and boolean columns.
- New conversions for binary columns (bytes, stream, blob), to parsed them as `Array[Byte]` or `InputStream`.
- New conversions for temporal types. For Java8 `Instant`, `LocalDateTime`, `ZonedDateTime`, Joda `Instant` or `DateTime`, from `Long`, `Date` or `Timestamp` column.
- Added conversions to support `List[T]`, `Set[T]`, `SortedSet[T]`, `Stream[T]` and `Vector[T]` as multi-value parameter.
- New conversion to parse text column as UUID (e.g. `SQL("SELECT uuid_as_text").as(scalar[java.util.UUID].single)`).