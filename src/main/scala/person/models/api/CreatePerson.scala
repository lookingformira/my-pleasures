package person.models.api

import zio.json.{DeriveJsonCodec, JsonCodec}

/** Models the parameters of a post request that the client will send to the
 * server while removing the need for the request to handle generating an
 * UserId.
 */
final case class CreatePerson(firstName: String,
                              lastName: String,
                              address: String,
                              phone: String,
                              email: String,
                              password: String)

object CreatePerson {
  implicit val codec: JsonCodec[CreatePerson] =
    DeriveJsonCodec.gen[CreatePerson]
}


