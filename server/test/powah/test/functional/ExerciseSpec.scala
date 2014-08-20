package powah.test.functional

import play.api.db.slick.Config.driver.simple._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import powah.exercise.{Exercises, Exercise}
import powah.test.helpers.{TestUser, AppSpecBase}
import powah.common.UniqueConstraintException
import org.specs2.execute.Success

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
        """{"exercises": [{"id": %d, "name": "xoo lus"}]}""".format(exercise.id)
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

    "throws UniqueConstraintException on duplicate exercise" in testApp {
      createTestUser

      anExercise.withName("duplicate").build

      try {
        val result = route(FakeRequest(POST, "/exercises")
          .withHeaders(CONTENT_TYPE -> "application/json")
          .withSession(TestUser.inSession)
          .withBody(Json.parse("""{"name": "duplicate"}"""))
        ).get

        contentAsString(result) must equalTo("this should throw")

        failure("Should have thrown")
      } catch {
        case e: UniqueConstraintException => Success()
      }
    }

    "be able to search exercises by name" in testApp {
      createTestUser

      val exercise1 = anExercise.withName("foo bar").build

      // Should be omitted in the response
      val exercise2 = anExercise.withName("xoo bar").build

      val result = route(
        FakeRequest(GET, "/exercises?name=foo")
          .withHeaders(CONTENT_TYPE -> "application/json")
          .withSession(TestUser.inSession)
      ).get

      assertJsonResponse(result, OK)

      val expectedJson = Json.parse(
        """{"exercises": [{"id": %d, "name": "foo bar"}]}""".format(exercise1.id)
      ).toString

      contentAsString(result) must equalTo(expectedJson)
    }
  }
}
