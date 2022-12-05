import pleasure.server.PleasureRoutes
import pleasure.service.PleasureServiceLive
import person.server.PersonRoutes
import person.service.PersonServiceLive
import utils.QuillContext
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    ZIO
      .serviceWithZIO[MyPleasureServer](_.start)
      .provide(
        MyPleasureServer.live,
        PersonRoutes.live,
        PleasureRoutes.live,
        PersonServiceLive.live,
        PleasureServiceLive.live,
        Migrations.layer,
        QuillContext.dataSourceLayer,
      )
}