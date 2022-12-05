package pleasure.service

import pleasure.models.{Pleasure, PleasureId}
import person.models.PersonId
import zio._

import javax.sql.DataSource

final case class PleasureServiceLive(dataSource: DataSource) extends PleasureService {

  import utils.QuillContext._

  /** Creates a new Pleasure
   */
  override def create(name: String, link: String, description: String, userId: PersonId): Task[Pleasure] = for {
    pleasure <- Pleasure.make(name, link, description, userId)
    _ <- run(query[Pleasure].insertValue(lift(pleasure)))
      .provideEnvironment(ZEnvironment(dataSource))
  } yield pleasure

  /** Deletes an existing Pleasure
   */
  override def delete(id: PleasureId): Task[Unit] = run(query[Pleasure].filter(_.id == lift(id)).delete)
    .provideEnvironment(ZEnvironment(dataSource))
    .unit

  /** Retrieves an Pleasure from the database
   */
  override def get(id: PleasureId): Task[Option[Pleasure]] = run(query[Pleasure].filter(_.id == lift(id)))
    .provideEnvironment(ZEnvironment(dataSource))
    .map(_.headOption)

  /** Retrieves all Pleasures for a given User from the database. */
  override def getForUser(userId: PersonId): Task[List[Pleasure]] = run(query[Pleasure].filter(_.personId == lift(userId)))
    .provideEnvironment(ZEnvironment(dataSource))

  /** Retrieves all Pleasures from the database
   */
  override def getAll: Task[List[Pleasure]] = run(query[Pleasure])
    .provideEnvironment(ZEnvironment(dataSource))

  /** Updates an existing Pleasure
   */
  override def update(id: PleasureId,
                      name: Option[String],
                      link: Option[String],
                      description: Option[String],
                      userId: Option[PersonId]): Task[Unit] = run(
    dynamicQuery[Pleasure]
      .filter(_.id == lift(id))
      .update(
        setOpt(_.name, name),
        setOpt(_.link, link),
        setOpt(_.description, description),
        setOpt(_.personId, userId)
      )
  )
    .provideEnvironment(ZEnvironment(dataSource)).unit
}

object PleasureServiceLive {
  def live: URLayer[DataSource, PleasureServiceLive] = ZLayer.fromFunction(PleasureServiceLive.apply _)
}
