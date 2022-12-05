package person.models

import zio.UIO
import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Person(id: PersonId,
                        firstName: String,
                        lastName: String,
                        address: String,
                        phone: String,
                        email: String) {
  def fullName: String = firstName + " " + lastName
}

object Person {

  /** Uses the `random` method defined on our UserId wrapper to generate a
   * random ID and assign that to the User we are creating.
   */
  def make(firstName: String, lastName: String, address: String, phone: String, email: String): UIO[Person] =
    PersonId.random.map(Person(_, firstName, lastName, address, phone, email))

  /** Derives a JSON codec for the User type allowing it to be (de)serialized.
   */
  implicit val codec: JsonCodec[Person] =
    DeriveJsonCodec.gen[Person]

}
