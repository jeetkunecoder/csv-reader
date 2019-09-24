package services

import akka.actor._
import akka.stream._
import akka.stream.alpakka.csv.scaladsl.CsvParsing
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import cats.effect.{IO, Resource}
import database.BankRepository
import javax.inject.{Inject, Singleton}
import models.Bank

import scala.concurrent.{ExecutionContext, Future}
import scala.io.BufferedSource

@Singleton
class BankService @Inject() (bankRepository: BankRepository) (implicit ex: ExecutionContext) {

  implicit val actorSystem = ActorSystem("csv-reader")
  implicit val materializer = ActorMaterializer()
  val filePath = "./resources/banks.csv"

  def importDataFromCsv() = {

    val acquire = IO {
      scala.io.Source.fromFile(filePath)
    }

    Resource.fromAutoCloseable(acquire)
      .use(source => IO(processFile(source)))
      .unsafeRunSync()

    // Read bank identifier #10040000
    readBank(10040000).map {
      bank => System.out.println("Bank identifier 10040000 - Name:" + bank.head.name)
    }

    play.mvc.Results.ok()
  }

  def processFile(source: BufferedSource) = {
    val fileLines = source.getLines
    for (line <- fileLines) {
      Source.single(ByteString(line))
        .via(CsvParsing.lineScanner(CsvParsing.SemiColon))
        .map(_.map(_.utf8String))
        .runWith(Sink.foreach(processRow))
    }
  }

  def processRow(row: List[String]) = {
    val name = row(0)
    val bankIdentifier = row(1).toLong
    val bank = new Bank(name, bankIdentifier)
    bankRepository.insert(bank)
  }

  def readBank(bankId: Long): Future[Seq[Bank]] = {
    bankRepository.read(bankId)
  }

  def readAll(): Future[Seq[Bank]] = {
    bankRepository.readAll()
  }

  def deleteAll() = {
    bankRepository.deleteAll()
  }
}
