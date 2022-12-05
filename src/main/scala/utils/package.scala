import errors.AppError
import pleasure.models.PleasureId
import person.models.PersonId
import zio.IO
import zhttp.http.Request
import zio.json._
import zio._

package object utils {

  def parseBody[A: JsonDecoder](request: Request): IO[AppError, A] =
    for {
      body <- request.bodyAsString.orElseFail(AppError.MissingBodyError)
      parsed <- ZIO.from(body.fromJson[A]).mapError(AppError.JsonDecodingError)
    } yield parsed

  /** Parses a PleasureId from the provided string.
   */
  def parsePleasureId(id: String): IO[AppError.InvalidIdError, PleasureId] =
    PleasureId.fromString(id).orElseFail(AppError.InvalidIdError("Invalid pleasure id"))

  /** Parses a UserId from the provided string.
   */
  def parseUserId(id: String): IO[AppError.InvalidIdError, PersonId] =
    PersonId.fromString(id).orElseFail(AppError.InvalidIdError("Invalid user id"))

}
