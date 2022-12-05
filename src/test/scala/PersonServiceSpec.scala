import zio.Scope
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault}

import io.github.scottweaver.zio.aspect.DbMigrationAspect
import io.github.scottweaver.zio.testcontainers.postgres.ZPostgreSQLContainer
import person.service._
import pleasure.service._
import zio.test._

object PersonServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = {
    suite("PersonService")(
      suite("added persons exist in db")(
        test("returns true confirming existence of added person") {
          for {
            person <-
              PersonService.create(
                "Emily",
                "Elizabeth",
                "1 Birdwell Island, New York, NY",
                "212-215-1928",
                "emily@bigreddog.com"
              )
            getPerson <- PersonService.get(person.id)
          } yield assertTrue(getPerson.get == person)
        },
        test("returns true confirming existence of many added persons") {
          for {
            person1 <- PersonService.create(
              "Fern",
              "Arable",
              "Arable Farm, Brooklin, ME",
              "207-711-1899",
              "fern@charlottesweb.com"
            )
            person2 <-
              PersonService.create("Jon", "Arbuckle", "711 Maple St, Muncie, IN", "812-728-1945", "jon@garfield.com")
            persons <- PersonService.getAll
          } yield assertTrue(persons.contains(person1) && persons.contains(person2))
        }
      ),
      suite("deleted persons do not exist in db")(
        test("returns false confirming non-existence of deleted person") {
          for {
            person <-
              PersonService.create(
                "Sherlock",
                "Holmes",
                "221B Baker St, London, England, UK",
                "+44-20-7224-3688",
                "sherlock@sherlockholmes.com"
              )
            _ <- PersonService.delete(person.id)
            getPerson <- PersonService.get(person.id)
          } yield assertTrue(getPerson.isEmpty)
        },
        test("returns true confirming non-existence of many deleted persons") {
          for {
            person1 <-
              PersonService.create(
                "Elizabeth",
                "Hunter",
                "Ontario, Canada",
                "807-511-1918",
                "elizabeth@incrediblejourney.com"
              )
            person2 <-
              PersonService.create("Peter", "Hunter", "Ontario, Canada", "807-511-1918", "peter@incrediblejourney.com")
            person3 <-
              PersonService.create("Jim", "Hunter", "Ontario, Canada", "807-511-1918", "jim@incrediblejourney.com")
            _ <- PersonService.delete(person1.id)
            _ <- PersonService.delete(person2.id)
            getPerson1 <- PersonService.get(person1.id)
            getPerson2 <- PersonService.get(person2.id)
            getPerson3 <- PersonService.get(person3.id)
          } yield assertTrue(getPerson1.isEmpty && getPerson2.isEmpty && getPerson3.isDefined)
        }
      ),
      suite("updated persons contain accurate information")(
        test("returns true confirming updated person information") {
          for {
            person <- PersonService.create(
              "Harry",
              "Potter",
              "4 Privet Drive, Little Whinging, Surrey, UK",
              "+44-20-7224-3688",
              "harry@hogwarts.edu"
            )
            _ <- PersonService.update(person.id, None, None, Some("12 Grimmauld Place, London, England, UK"), None, None)
            getPerson <- PersonService.get(person.id)
          } yield assertTrue(
            getPerson.get.firstName == "Harry" && getPerson.get.address == "12 Grimmauld Place, London, England, UK" && getPerson.get.address != "4 Privet Drive, Little Whinging, Surrey, UK"
          )
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
