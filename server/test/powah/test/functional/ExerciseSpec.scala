package powah.test.functional

import play.api.db.slick.Config.driver.simple._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import powah.exercise.{Exercises, Exercise}
import powah.test.helpers.{TestUser, AppSpecBase}

class ExerciseSpec extends AppSpecBase {
  "ExerciseController" should {
    "be able to get exercises" in testApp {
      createTestUser

      val exercise = anExercise.withName("xoo lus").build

      val result = route(FakeRequest(GET, "/exercises")
        .withSession(TestUser.inSession)
        .withHeaders(CONTENT_TYPE -> "application/json")
      ).get

      assertJsonResponse(result, OK)

      val expectedJson = Json.parse(
        """{"data": [{"id": %d, "name": "xoo lus"}]}""".format(exercise.id)
      ).toString

      contentAsString(result) must equalTo(expectedJson)
    }

    "be able to add an exercise" in testApp {
      createTestUser

      val result = route(FakeRequest(POST, "/exercises")
        .withHeaders(CONTENT_TYPE -> "application/json")
        .withSession(TestUser.inSession)
        .withBody(Json.parse("""{"name": "some exercise"}"""))
      ).get

      assertJsonResponse(result, CREATED)

      val exercise = assertExerciseWasCreated

      val expectedJson = Json.parse(
        """{"id": %d, "name": "some exercise"}""".format(exercise.id)
      ).toString

      contentAsString(result) must equalTo(expectedJson)

      header(LOCATION, result) must beSome("/exercises/%d".format(exercise.id))
    }

    def assertExerciseWasCreated: Exercise = {
      val q = for { e <- Exercises } yield e
      val exercise = q.first

      exercise.name must equalTo("some exercise")

      exercise
    }
  }
}