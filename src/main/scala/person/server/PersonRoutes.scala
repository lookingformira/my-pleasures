package person.server

import person.models.api.{CreatePerson, UpdatePerson}
import person.service.PersonService
import utils._
import zhttp.http._
import zio._
import zio.json._

final case class PersonRoutes(service: PersonService) {


  val routes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

    case Method.GET -> !! / "users" =>
      service.getAll.map(users => Response.json(users.toJson))

    case Method.GET -> !! / "users" / id =>
      for {
        id <- parseUserId(id)
        user <- service.get(id)
      } yield Response.json(user.toJson)

    case req@Method.POST -> !! / "users" =>
      for {
        createUser <- parseBody[CreatePerson](req)
        user <-
          service.create(
            createUser.firstName,
            createUser.lastName,
            createUser.address,
            createUser.phone,
            createUser.email
          )
      } yield Response.json(user.toJson).setHeaders(Headers.apply(List(("Access-Control-Allow-Origin", "*"))))

    /** Updates a single User found by their parsed ID using the information
     * parsed from the UpdateUser request and returns a 200 status code
     * indicating success.
     */
    case req@Method.PATCH -> !! / "users" / id =>
      for {
        userId <- parseUserId(id)
        updateUser <- parseBody[UpdatePerson](req)
        _ <- service.update(
          userId,
          updateUser.firstName,
          updateUser.lastName,
          updateUser.address,
          updateUser.phone,
          updateUser.email
        )
      } yield Response.ok

    case Method.DELETE -> !! / "users" / id =>
      for {
        id <- parseUserId(id)
        _ <- service.delete(id)
      } yield Response.ok

  }

}

object PersonRoutes {
  def live: URLayer[PersonService, PersonRoutes] = ZLayer.fromFunction(PersonRoutes.apply _)
}
