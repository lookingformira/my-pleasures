package person.service

import person.models.{Person, PersonId}
import zio._

import javax.sql.DataSource

final case class PersonServiceLive(dataSource: DataSource) extends PersonService {

  import utils.QuillContext._

  /** Creates a new User
   */
  override def create(firstName: String, lastName: String, address: String, phone: String, email: String): Task[Person] =
    for {
      user <- Person.make(firstName, lastName, address, phone, email)
      _ <- run(query[Person].insertValue(lift(user)))
        .provideEnvironment(ZEnvironment(dataSource))
    } yield user

  /** Deletes an existing User
   */
  override def delete(id: PersonId): Task[Unit] =
    run(query[Person].filter(_.id == lift(id)).delete)
      .provideEnvironment(ZEnvironment(dataSource)).unit

  /** Retrieves an User from the database
   */
  override def get(id: PersonId): Task[Option[Person]] =
    run(query[Person].filter(_.id == lift(id)))
      .provideEnvironment(ZEnvironment(dataSource))
      .map(_.headOption)

  /** Retrieves all Users from the database
   */
  override def getAll: Task[List[Person]] = run(query[Person])
    .provideEnvironment(ZEnvironment(dataSource))

  /** Updates an existing User
   */
  override def update(id: PersonId,
                      firstName: Option[String],
                      lastName: Option[String],
                      address: Option[String],
                      phone: Option[String],
                      email: Option[String]): Task[Unit] = run(
    dynamicQuery[Person]
      .filter(_.id == lift(id))
      .update(
        setOpt(_.firstName, firstName),
        setOpt(_.lastName, lastName),
        setOpt(_.address, address),
        setOpt(_.phone, phone),
        setOpt(_.email, email)
      )
  )
    .provideEnvironment(ZEnvironment(dataSource)).unit
}

object PersonServiceLive {
  def live: URLayer[DataSource, PersonServiceLive] = ZLayer.fromFunction(PersonServiceLive.apply _)
}
