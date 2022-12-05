package person.models

import zio.json.JsonCodec
import zio.{Random, Task, UIO, ZIO}

import java.util.UUID

case class PersonId(id: UUID) extends AnyVal

object PersonId {

  /** Generates a Random UUID and wraps it in the UserId type. */
  def random: UIO[PersonId] = Random.nextUUID.map(PersonId(_))

  /** Allows a UUID to be parsed from a string which is then wrapped in the
   * UserId type.
   */
  def fromString(id: String): Task[PersonId] =
    ZIO.attempt {
      PersonId(UUID.fromString(id))
    }

  /** Derives a codec allowing a UUID to be (de)serialized as an UserId. */
  implicit val codec: JsonCodec[PersonId] =
    JsonCodec[UUID].transform(PersonId(_), _.id)
}
