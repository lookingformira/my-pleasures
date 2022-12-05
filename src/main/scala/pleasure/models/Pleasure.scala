package pleasure.models

import person.models.PersonId
import zio.UIO
import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Pleasure(id: PleasureId,
                          name: String,
                          link: String,
                          description: String,
                          personId: PersonId)

object Pleasure {

  /** Uses the `random` method defined on our PleasureId wrapper to generate a
   * random ID and assign that to the Pleasure we are creating.
   */
  def make(name: String,
           link: String,
           description: String,
           userId: PersonId): UIO[Pleasure] =
    PleasureId.random.map(Pleasure(_, name, link, description, userId))

  /** Derives a JSON codec for the User type allowing it to be (de)serialized.
   */
  implicit val codec: JsonCodec[Pleasure] =
    DeriveJsonCodec.gen[Pleasure]

}
