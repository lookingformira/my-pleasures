package pleasure.server

import pleasure.models.api.{CreatePleasure, UpdatePleasure}
import pleasure.service.PleasureService
import utils._
import zhttp.http._
import zio._
import zio.json._

final case class PleasureRoutes(service: PleasureService) {

  val createDeleteRoutes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

    case req@Method.POST -> !! / "pleasures" =>
      for {
        createPleasure <- parseBody[CreatePleasure](req)
        pleasure <- req.headerValue("pleasure-user-id") match {
          case Some(value) if value == createPleasure.userId.id.toString =>
            service.create(createPleasure.name, createPleasure.link, createPleasure.description, createPleasure.userId).asSome
          case _ => ZIO.attempt(None)
        }
      } yield pleasure match {
        case Some(value) => Response.json(value.toJson)
        case None => Response.text("access denied").setStatus(Status.NotAcceptable)
      }

    /** Updates a single Pleasure found by their parsed ID using the information
     * parsed from the UpdatePleasure request body and returns a 200 status code
     * indicating success.
     */
    case req@Method.PATCH -> !! / "pleasures" / id =>
      for {
        pleasureId <- parsePleasureId(id)
        updatePleasure <- parseBody[UpdatePleasure](req)
        option <- req.headerValue("pleasure-user-id") match {
          case Some(header) => updatePleasure.userId match {
            case Some(value) if header == value.id.toString =>
              service.update(pleasureId, updatePleasure.name, updatePleasure.link, updatePleasure.description, updatePleasure.userId).as(Some(()))
            case _ => ZIO.attempt(None)
          }
          case None => ZIO.attempt(None)
        }
      } yield option match {
        case Some(_) => Response.ok
        case None => Response.text("access denied or pleasure is not exist").setStatus(Status.BadRequest)
      }


    case req@Method.DELETE -> !! / "pleasures" / id =>
      for {
        id <- parsePleasureId(id)
        optPleasure <- service.get(id)
        option <- req.headerValue("pleasure-user-id") match {
          case Some(header) => optPleasure match {
            case Some(value) if header == value.id.toString => service.delete(id).as(Some(()))
            case _ => ZIO.attempt(None)
          }
          case None => ZIO.attempt(None)
        }
      } yield option match {
        case Some(_) => Response.ok
        case None => Response.text("access denied or pleasure is not exist").setStatus(Status.BadRequest)
      }
  }

  val getRoutes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

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

  }

}

object PleasureRoutes {
  def live: URLayer[PleasureService, PleasureRoutes] =
    ZLayer.fromFunction(PleasureRoutes.apply _)
}
