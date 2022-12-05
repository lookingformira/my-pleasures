package pleasure.models.api

import person.models.PersonId
import zio.json.{DeriveJsonCodec, JsonCodec}

/** Models the parameters of a patch request that the client will send to the
 * server while removing the need for the request to handle generating an
 * PleasureId.
 */
final case class UpdatePleasure(name: Option[String],
                                link: Option[String],
                                description: Option[String],
                                userId: Option[PersonId])

object UpdatePleasure {
  implicit val codec: JsonCodec[UpdatePleasure] =
    DeriveJsonCodec.gen[UpdatePleasure]
}
