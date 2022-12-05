package person.service

import person.models.{Person, PersonId}
import zio.{Task, ZIO}

trait PersonService {

  /** Creates a new User
   */
  def create(firstName: String, lastName: String, address: String, phone: String, email: String): Task[Person]

  /** Deletes an existing User
   */
  def delete(id: PersonId): Task[Unit]

  /** Retrieves an User from the database
   */
  def get(id: PersonId): Task[Option[Person]]

  /** Retrieves all Users from the database
   */
  def getAll: Task[List[Person]]

  /** Updates an existing User
   */
  def update(
              id: PersonId,
              firstName: Option[String] = None,
              lastName: Option[String] = None,
              address: Option[String] = None,
              phone: Option[String] = None,
              email: Option[String] = None
            ): Task[Unit]

}

object PersonService {
  def create(firstName: String,
             lastName: String,
             address: String,
             phone: String,
             email: String): ZIO[PersonService, Throwable, Person] =
    ZIO.serviceWithZIO[PersonService](_.create(firstName, lastName, address, phone, email))

  def delete(id: PersonId): ZIO[PersonService, Throwable, Unit] = ZIO.serviceWithZIO[PersonService](_.delete(id))

  def get(id: PersonId): ZIO[PersonService, Throwable, Option[Person]] = ZIO.serviceWithZIO[PersonService](_.get(id))

  def getAll: ZIO[PersonService, Throwable, List[Person]] = ZIO.serviceWithZIO[PersonService](_.getAll)

  def update(
              id: PersonId,
              firstName: Option[String] = None,
              lastName: Option[String] = None,
              address: Option[String] = None,
              phone: Option[String] = None,
              email: Option[String] = None
            ): ZIO[PersonService, Throwable, Unit] =
    ZIO.serviceWithZIO[PersonService](_.update(id, firstName, lastName, address, phone, email))
}
