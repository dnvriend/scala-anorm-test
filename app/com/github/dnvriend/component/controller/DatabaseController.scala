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

import java.util.logging.Logger
import javax.inject.Inject

import anorm._
import play.api.db.Database
import play.api.mvc.{Action, Controller}

class DatabaseController @Inject() (db: Database, logger: Logger) extends Controller {
  def getConnection = Action {
    logger.info(s"Database: $db")
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
}
