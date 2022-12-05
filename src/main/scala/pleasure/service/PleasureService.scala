package pleasure.service

import pleasure.models.{Pleasure, PleasureId}
import person.models.PersonId
import zio.{Task, ZIO}

trait PleasureService {

  /** Creates a new Pleasure
   */
  def create(name: String,
             link: String,
             description: String,
             userId: PersonId): Task[Pleasure]

  /** Deletes an existing Pleasure
   */
  def delete(id: PleasureId): Task[Unit]

  /** Retrieves an Pleasure from the database
   */
  def get(id: PleasureId): Task[Option[Pleasure]]

  /** Retrieves all Pleasures for a given User from the database. */
  def getForUser(userId: PersonId): Task[List[Pleasure]]

  /** Retrieves all Pleasures from the database
   */
  def getAll: Task[List[Pleasure]]

  /** Updates an existing Pleasure
   */
  def update(
              id: PleasureId,
              name: Option[String] = None,
              link: Option[String] = None,
              description: Option[String] = None,
              userId: Option[PersonId] = None,
            ): Task[Unit]

}

object PleasureService {
  def create(name: String,
             link: String,
             description: String,
             userId: PersonId): ZIO[PleasureService, Throwable, Pleasure] =
    ZIO.serviceWithZIO[PleasureService](_.create(name, link, description, userId))

  def delete(id: PleasureId): ZIO[PleasureService, Throwable, Unit] =
    ZIO.serviceWithZIO[PleasureService](_.delete(id))

  def get(id: PleasureId): ZIO[PleasureService, Throwable, Option[Pleasure]] =
    ZIO.serviceWithZIO[PleasureService](_.get(id))

  def getForUser(userId: PersonId): ZIO[PleasureService, Throwable, List[Pleasure]] =
    ZIO.serviceWithZIO[PleasureService](_.getForUser(userId))

  def getAll: ZIO[PleasureService, Throwable, List[Pleasure]] =
    ZIO.serviceWithZIO[PleasureService](_.getAll)


  def update(
              id: PleasureId,
              name: Option[String] = None,
              link: Option[String] = None,
              description: Option[String] = None,
              userId: Option[PersonId] = None,
            ): ZIO[PleasureService, Throwable, Unit] =
    ZIO.serviceWithZIO[PleasureService](_.update(id, name, link, description, userId))

}
