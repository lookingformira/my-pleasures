package person.models.api

import zio.json.{DeriveJsonCodec, JsonCodec}

/** Models the parameters of a patch request that the client will send to the
 * server while removing the need for the request to handle generating an
 * UserId.
 */
final case class UpdatePerson(firstName: Option[String],
                              lastName: Option[String],
                              address: Option[String],
                              phone: Option[String],
                              email: Option[String])

object UpdatePerson {
  implicit val codec: JsonCodec[UpdatePerson] =
    DeriveJsonCodec.gen[UpdatePerson]
}
