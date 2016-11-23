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

package com.github.dnvriend.component.controller.marshaller

import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Result, Results}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

object MarshallerOps {

  implicit class ListResult[A: Writes](xs: List[A]) extends Results {
    def marshal: Result = if (xs.isEmpty) NotFound("No results") else Ok(Json.toJson(xs))
  }

  implicit class OptionalResult[A: Writes](opt: Option[A]) extends Results {
    def marshal: Result = if (opt.isEmpty) NotFound("No results") else Ok(Json.toJson(opt))
  }

  implicit def listToResponse[A: Writes](that: List[A]): Result =
    that.marshal

  implicit def optToResponse[A: Writes](that: Option[A]): Result =
    that.marshal

  implicit def listFutureToResponse[A: Writes](that: Future[List[A]])(implicit ec: ExecutionContext): Future[Result] =
    that.map(_.marshal)

  implicit def optFutureToResponse[A: Writes](that: Future[Option[A]])(implicit ec: ExecutionContext): Future[Result] =
    that.map(_.marshal)
}
