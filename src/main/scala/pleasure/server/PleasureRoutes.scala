package pleasure.server

import pleasure.models.api.{CreatePleasure, UpdatePleasure}
import pleasure.service.PleasureService
import utils._
import zhttp.http._
import zio._
import zio.json._

final case class PleasureRoutes(service: PleasureService) {

  val routes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

    case Method.GET -> !! / "pleasures" =>
      service.getAll.map(pleasures => Response.json(pleasures.toJson))

    case Method.GET -> !! / "pleasures" / id =>
      for {
        id <- parsePleasureId(id)
        pet <- service.get(id)
      } yield Response.json(pet.toJson)

    case Method.GET -> !! / "users" / id / "pleasures" =>
      for {
        id <- parseUserId(id)
        pleasures <- service.getForUser(id)
      } yield Response.json(pleasures.toJson)

    case req@Method.POST -> !! / "pleasures" =>
      for {
        createPleasure <- parseBody[CreatePleasure](req)
        pet <- service.create(createPleasure.name, createPleasure.link, createPleasure.description, createPleasure.userId)
      } yield Response.json(pet.toJson)

    /** Updates a single Pleasure found by their parsed ID using the information
     * parsed from the UpdatePleasure request body and returns a 200 status code
     * indicating success.
     */
    case req@Method.PATCH -> !! / "pleasures" / id =>
      for {
        pleasureId <- parsePleasureId(id)
        updatePleasure <- parseBody[UpdatePleasure](req)
        _ <- service.update(pleasureId, updatePleasure.name, updatePleasure.link, updatePleasure.description, updatePleasure.userId)
      } yield Response.ok

    case Method.DELETE -> !! / "pleasures" / id =>
      for {
        id <- parsePleasureId(id)
        _ <- service.delete(id)
      } yield Response.ok

  }

}

object PleasureRoutes {
  def live: URLayer[PleasureService, PleasureRoutes] =
    ZLayer.fromFunction(PleasureRoutes.apply _)
}
