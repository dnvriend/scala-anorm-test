/*
 * Copyright 2016 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dnvriend.repository

import java.sql.Connection
import java.util.logging.Logger
import javax.inject.Inject

import anorm._
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz.{Disjunction, NonEmptyList}

class DefaultPersonRepository @Inject() (db: Database, logger: Logger)(implicit ec: ExecutionContext) extends PersonRepository {
  logger.info(s"==> Database: ${db.name}, ${db.url}")

  private def withConnection[A](f: Connection => A): Future[A] =
    Future(db.withConnection(f))

  override def getPeople: Future[List[Person]] = withConnection[List[Person]] { implicit conn =>
    SQL"SELECT * FROM person".as(Person.parser.*)
  }

  override def getPeopleNel: Future[NonEmptyList[Person]] = withConnection { implicit conn =>
    val xs: List[Person] = SQL"SELECT * FROM person".as(Person.parser.+)
    NonEmptyList(xs.head, xs.tail: _*)
  }

  override def getPerson(id: Long): Future[Person] = withConnection { implicit conn =>
    SQL"SELECT * FROM person WHERE id=#$id".as(Person.parser.single)
  }

  override def getPersonOpt(id: Long): Future[Option[Person]] = withConnection { implicit conn =>
    SQL"SELECT * FROM person WHERE id=#$id".as(Person.parser.singleOpt)
  }

  override def getPersonD(id: Long): Future[Disjunction[String, Person]] =
    getPersonOpt(id).map(_.toRightDisjunction[String](s"No person found for id: $id"))

  override def addPerson(person: Person): Future[Person] = withConnection { implicit conn =>
    import anorm.SqlParser.long
    val theId = SQL"INSERT INTO person (name, age) values (${person.name}, ${person.age})".executeInsert(long(1).single)
    person.copy(id = Option(theId))
  }
}
