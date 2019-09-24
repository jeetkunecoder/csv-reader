package controllers

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatest.Matchers._
import org.scalatest.time.{Millis, Seconds, Span}
import play.api.libs.ws.WSClient
import play.api.test.Helpers._

class BankControllerSpec extends PlaySpec with ScalaFutures with GuiceOneServerPerSuite {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(20, Seconds), interval = Span(100, Millis))

  val host = s"localhost:$port/v1"
  val banksEndpoint = s"http://$host/banks"
  val importBanksFromCsvEndpoint = s"$banksEndpoint/import"
  val deleteBanksEndpoint = s"$banksEndpoint/delete"
  val client = app.injector.instanceOf[WSClient]

  "The Bank Controller" should {

    "Import rows in DB from a csv file" in {
      val response = client
        .url(importBanksFromCsvEndpoint)
        .post("")
        .futureValue

      response.status mustBe OK
      response.body mustEqual("")
    }

    "Get a list all banks in the DB" in {
      val response = listBanks()

      response.status mustBe OK
      response.body must include("{\"name\":\"Postbank\",\"bankIdentifier\":10010010}")
      response.body must include("{\"name\":\"Commerzbank\",\"bankIdentifier\":10040000}")
      response.body must include("{\"name\":\"Eurocity\",\"bankIdentifier\":10030700}")
      response.body must include("{\"name\":\"Raiffeisenbank\",\"bankIdentifier\":22163114}")
    }

    "clean the table on exit" in {
      val deleteResponse = client
        .url(deleteBanksEndpoint)
        .delete()
        .futureValue

      deleteResponse.status mustBe OK

      val listBanksResponse = listBanks()

      listBanksResponse.status mustBe OK
      listBanksResponse.body mustBe "[]"
    }
  }

  def listBanks() = {
    client.url(banksEndpoint).execute().futureValue
  }
}
