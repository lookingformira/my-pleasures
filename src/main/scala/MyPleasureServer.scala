import pleasure.server.PleasureRoutes
import person.server.PersonRoutes
import zhttp.http._
import zhttp.http.middleware.Cors.CorsConfig
import zhttp.service.Server
import zio._

/** MyPleasureServer is a service that houses the details for how to set up the
 * ZIO-Http server.
 *
 * It is comprised of the various routes, which in this case are also services
 * that we defined in the different route files
 */
final case class MyPleasureServer(
                                   userRoutes: PersonRoutes,
                                   pleasureRoutes: PleasureRoutes,
                                   migrations: Migrations
                             ) {

  val corsConfig = CorsConfig(
    anyOrigin = false,
    anyMethod = false,
    allowedOrigins = s => s.equals("localhost"),
    allowedMethods = Some(Set(Method.GET, Method.POST, Method.PATCH, Method.DELETE))
  )

  /** Composes the routes together, returning a single HttpApp.
   */
  val allRoutes: HttpApp[Any, Throwable] = (userRoutes.routes ++ pleasureRoutes.getRoutes ++ pleasureRoutes.createDeleteRoutes) @@ Middleware.cors(corsConfig)

  /** Resets the database to the initial state every 15 minutes to clean up the
   * deployed Heroku data. Then, it obtains a port from the environment on
   * which to start the server. In the case of being run in production, the
   * port will be provided by Heroku, otherwise the port will be 8080. The
   * server is then started on the given port with the routes provided.
   */
  def start: ZIO[Any, Throwable, Unit] =
    for {
      _    <- migrations.reset
//        .repeat(Schedule.fixed(15.minutes)).fork
      port <- System.envOrElse("PORT", "8080").map(_.toInt)
      _    <- Server.start(port, allRoutes)
    } yield ()

}

/** Here in the companion object, we define the layer that will be used to
 * create the server.
 */
object MyPleasureServer {

  val live: ZLayer[PersonRoutes with PleasureRoutes with Migrations, Nothing, MyPleasureServer] =
    ZLayer.fromFunction(MyPleasureServer.apply _)

}
