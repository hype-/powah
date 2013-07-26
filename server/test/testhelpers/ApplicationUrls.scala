package testhelpers

trait ApplicationUrls {
  protected val testServerPort = 3333
  protected val baseUrl = s"http://localhost:$testServerPort"
  
  protected val homePage = baseUrl + "/"
}
