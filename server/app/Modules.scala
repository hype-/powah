import com.tzavellas.sse.guice.ScalaModule
import play.api.db.slick.DB
import scala.slick.session.Database

trait CommonModule extends ScalaModule {
  def configure() {
    bind[DB].toInstance(DB)
  }
}

class ProdModule extends CommonModule

class DevModule extends CommonModule
