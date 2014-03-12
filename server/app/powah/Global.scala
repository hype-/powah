package powah

import com.google.inject.Guice
import play.api.GlobalSettings
import play.api.Play
import play.api.Play.current
import play.api.http.HeaderNames._
import play.api.http.MimeTypes._
import play.api.mvc.Results.Conflict
import play.api.mvc.{Result, RequestHeader}
import powah.common.UniqueConstraintException

object Global extends GlobalSettings {
  private lazy val injector = {
    Play.isProd match {
      case true => Guice.createInjector(new ProdModule)
      case false => Guice.createInjector(new DevModule)
    }
  }

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }

  override def onError(request: RequestHeader, ex: Throwable): Result = {
    ex.getCause match {
      case e: UniqueConstraintException =>
        Conflict.withHeaders(CONTENT_TYPE -> JSON)

      case _ => super.onError(request, ex)
    }
  }
}
