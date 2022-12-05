package pleasure.models.api

import person.models.PersonId
import zio.json.{DeriveJsonCodec, JsonCodec}

/** Models the parameters of a post request that the client will send to the
 * server while removing the need for the request to handle generating an
 * PleasureId.
 */
final case class CreatePleasure(name: String,
                                link: String,
                                description: String,
                                userId: PersonId)

object CreatePleasure {
  implicit val codec: JsonCodec[CreatePleasure] =
    DeriveJsonCodec.gen[CreatePleasure]
}


