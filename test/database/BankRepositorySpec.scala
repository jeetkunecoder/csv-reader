package database

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.Matchers._
import play.api.Application
import models.Bank
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BankRepositorySpec extends PlaySpec with ScalaFutures with GuiceOneAppPerSuite {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(20, Seconds), interval = Span(100, Millis))

  "Bank Repository" should {
    val cache = Application.instanceCache[BankRepository]
    val repository = cache(app)

    "ensure that tested value doesn't exist on application start" in {
      val futureDelete = repository.delete(101)
      whenReady(Future(futureDelete)) { _ =>
        repository.read(101).futureValue shouldBe empty
      }
    }

    "insert bank in DB" in {
      val bank = new Bank("Bac International", 101)
      val expected = Seq(
        Bank("Bac International", 101)
      )
      val futureInsert = repository.insert(bank)
      whenReady(Future(futureInsert)) { _ =>
        repository.read(101).futureValue shouldBe expected
      }
    }

    "clean-up inserted row on exit" in {
      val futureDelete = repository.delete(101)
      whenReady(Future(futureDelete)) { _ =>
        repository.read(101).futureValue shouldBe empty
      }
    }
  }
}

