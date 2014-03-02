package powah.test.functional

import play.api.db.slick.Config.driver.simple._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import powah.exercise.{Exercise, RepSets, RepSet}
import powah.test.helpers.{TestUser, AppSpecBase}
import powah.user.User
import org.joda.time.DateTime

class RepSetSpec extends AppSpecBase {
  "RepSet" should {
    "get rep sets" in testApp {
      val user = createTestUser

      val date = new DateTime("2011-03-05T12:03:45")

      val exercise1 = anExercise.build
      val repSet1 = aRepSet
        .withWeight(100)
        .withReps(9)
        .withTime(date)
        .withExerciseId(exercise1.id)
        .withUserId(user.id)
        .build

      // Test that a rep set with different exercise is not included in the response
      val repSet2 = aRepSet.withWeight(50).withReps(12).withUserId(user.id)build

      // Test that a rep set with different date is not included in the response
      val repSet3 = aRepSet
        .withExerciseId(exercise1.id)
        .withTime(new DateTime("2013-01-02"))
        .withUserId(user.id)
        .build

      // Test that a rep set with different user is not included in the response
      val repSet4 = aRepSet.withExerciseId(exercise1.id).withTime(date).build

      val result = route(
        FakeRequest(GET, "/days/%s/exercises".format(date.toString("yyyy-MM-dd")))
          .withSession(TestUser.inSession)
          .withHeaders(CONTENT_TYPE -> "application/json")
      ).get

      assertJsonResponse(result, OK)

      val expectedJson = Json.obj(
        "exercises" -> Json.arr(
          Json.obj(
            "id" -> exercise1.id,
            "name" -> exercise1.name,
            "rep_sets" -> Json.arr(
              Json.obj(
                "id" -> repSet1.id,
                "weight" -> 100.0,
                "reps" -> 9
              )
            )
          )
        )
      ).toString()

      contentAsString(result) must equalTo(expectedJson)
    }

    "add a RepSet with Exercise" in testApp {
      val user = createTestUser

      val exercise = anExercise.build
      val date = new DateTime("2014-08-03")

      val result = route(
        FakeRequest(
          POST,
          "/days/%s/exercises/%d/rep_sets".format(
            date.toString("yyyy-MM-dd"),
            exercise.id
          )
        )
          .withHeaders(CONTENT_TYPE -> "application/json")
          .withSession(TestUser.inSession)
          .withBody(Json.parse("""{"reps": 8, "weight": 50}"""))
      ).get

      assertJsonResponse(result, CREATED)

      val repSet = assertRepSetWasCreated(user, exercise)

      // TODO: Absolute URL?
      header(LOCATION, result) must beSome(
        "/days/%s/exercises/%d/rep_sets/%d".format(
          date.toString("yyyy-MM-dd"),
          exercise.id,
          repSet.id
        )
      )
    }

    def assertRepSetWasCreated(user: User, exercise: Exercise): RepSet = {
      val q = for { r <- RepSets } yield r
      val repSet = q.first

      repSet.weight must equalTo(50)
      repSet.reps must equalTo(8)
      repSet.time must equalTo(new DateTime("2014-08-03"))
      repSet.exerciseId must equalTo(exercise.id)
      repSet.userId must equalTo(user.id)

      repSet
    }
  }
}
