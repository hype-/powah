import com.tzavellas.sse.guice.ScalaModule
import play.api.db.slick.DB

class ProdModule extends ScalaModule {
  def configure() {
    bind[DB].toInstance(DB)
  }
}

class DevModule extends ScalaModule {
  def configure() {
    bind[DB].toInstance(DB)
  }
}
