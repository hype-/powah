import play.api.GlobalSettings
import play.api.Play
import play.api.Play.current
import play.api.Application
import com.google.inject.Guice

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
}
