import io.github.scottweaver.zio.aspect.DbMigrationAspect
import io.github.scottweaver.zio.testcontainers.postgres.ZPostgreSQLContainer
import person.service._
import pleasure.service._
import zio.test._

/** A test suite for PleasureService which allows us to test that the methods defined
 * in it work as expected.
 *
 * Because the methods interact directly with the database and we want to limit
 * unnecessary queries to our local database, which could result in data
 * inconsistencies, we are using ZIO Test Containers to create a temporary
 * database.
 */
object PleasureServiceSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment, Throwable] = {
    suite("PleasureService")(
      suite("added pleasures exist in db")(
        test("returns true confirming existence of added pleasure") {
          for {
            user <- PersonService.create(
              "Emily",
              "Elizabeth",
              "1 Birdwell Island, New York, NY",
              "212-215-1928",
              "emily@bigreddog.com",
              "qwerty"
            )
            pleasure <-
              PleasureService.create("Keyboard", "https://geekboards.ru/product/varmilo-minilo", "varmilo keyboard", user.id)
            getPleasure <- PleasureService.get(pleasure.id)
          } yield assertTrue(getPleasure.get == pleasure)
        },
        test("returns true confirming existence of many added pleasures") {
          for {
            user1 <- PersonService.create(
              "Fern",
              "Arable",
              "Arable Farm, Brooklin, ME",
              "207-711-1899",
              "fern@charlottesweb.com",
              "qwerty"
            )
            user2 <-
              PersonService.create("Sherlock", "Holmes", "221B Baker St, London, England, UK", "+44-20-7224-3688", "sherlock@sherlockholmes.com", "qwerty")
            pleasure1 <-
              PleasureService.create("Car", "https://www.lamborghini.com/en-en/models/urus", "lamborghini urus", user1.id)
            pleasure2 <-
              PleasureService.create("Cellphone", "https://www.apple.com/iphone-14-pro/", "iphone 14", user2.id)
            pleasures <- PleasureService.getAll
          } yield assertTrue(pleasures.contains(pleasure1) && pleasures.contains(pleasure2))
        }
      ),
      suite("deleted pleasures do not exist in db")(
        test("returns true confirming non-existence of deleted pleasure") {
          for {
            user <-
              PersonService.create(
                "Sherlock",
                "Holmes",
                "221B Baker St, London, England, UK",
                "+44-20-7224-3688",
                "sherlock@sherlockholmes.com",
                "qwerty"
              )
            pleasure <-
              PleasureService.create("Cellphone", "https://www.apple.com/iphone-14-pro/", "iphone 14", user.id)
            _ <- PleasureService.delete(pleasure.id)
            getPleasure <- PleasureService.get(pleasure.id)
          } yield assertTrue(getPleasure.isEmpty)
        },
      ),
      suite("updated pleasures contain accurate information")(
        test("returns true confirming updated pleasure information") {
          for {
            user <- PersonService.create(
              "Emily",
              "Elizabeth",
              "1 Birdwell Island, New York, NY",
              "212-215-1928",
              "emily@bigreddog.com",
              "qwerty"
            )
            pleasure <-
              PleasureService.create("Keyboard", "https://geekboards.ru/product/varmilo-minilo", "varmilo keyboard", user.id)
            _ <- PleasureService.update(pleasure.id, Some("Mouse"), None, None, None)
            getPleasure <- PleasureService.get(pleasure.id)
          } yield assertTrue(getPleasure.get.name == "Mouse")
        }
      )
    ) @@ DbMigrationAspect.migrateOnce()() @@ TestAspect.withLiveRandom
  }.provideShared(
    PleasureServiceLive.live,
    PersonServiceLive.live,
    ZPostgreSQLContainer.Settings.default,
    ZPostgreSQLContainer.live,
    TestContainerLayers.dataSourceLayer,
    Live.default
  )

}
