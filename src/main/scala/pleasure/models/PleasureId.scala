package pleasure.models

import zio.json.JsonCodec
import zio.{Random, Task, UIO, ZIO}

import java.util.UUID

case class PleasureId(id: UUID) extends AnyVal

object PleasureId {

  /** Generates a Random UUID and wraps it in the PleasureId type. */
  def random: UIO[PleasureId] = Random.nextUUID.map(PleasureId(_))

  /** Allows a UUID to be parsed from a string which is then wrapped in the
   * PleasureId type.
   */
  def fromString(id: String): Task[PleasureId] =
    ZIO.attempt {
      PleasureId(UUID.fromString(id))
    }

  /** Derives a codec allowing a UUID to be (de)serialized as an PleasureId. */
  implicit val codec: JsonCodec[PleasureId] =
    JsonCodec[UUID].transform(PleasureId(_), _.id)
}
