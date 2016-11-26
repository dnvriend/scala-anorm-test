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

package com.github.dnvriend.component.controller

import javax.inject.Inject
import java.util.logging.Logger

import anorm._
import com.github.dnvriend.component.controller.marshaller.MarshallerOps._
import com.github.dnvriend.repository.Person._
import com.github.dnvriend.repository.{Person, PersonRepository}
import play.api.db.Database
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

class DatabaseController @Inject() (db: Database, repo: PersonRepository, logger: Logger)(implicit ec: ExecutionContext) extends Controller {
  logger.info(s"==> Database: ${db.name}, ${db.url}")

  def getConnection = Action {
    db.withConnection { conn =>
      logger.info(s"Connection: $conn")
      Ok(s"$db, $conn")
    }
  }

  def getOne = Action {
    db.withConnection { implicit conn =>
      val result = SQL("SELECT 1").as(SqlParser.int(1).single)
      Ok(s"$result")
    }
  }

  def getNow = Action {
    db.withConnection { implicit conn =>
      val result = SQL("SELECT now()").as(SqlParser.date(1).single)
      Ok(s"$result")
    }
  }

  def getPeople: Action[AnyContent] =
    Action.async(repo.getPeople)

  def addPerson(): Action[AnyContent] = Action.async { request =>
    request.body.asJson.flatMap(_.asOpt[Person]).map { person =>
      repo.addPerson(person).map(x => Ok(Json.toJson(x)))
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def getPersonById(id: Long): Action[AnyContent] =
    Action.async(repo.getPersonOpt(id))

}
