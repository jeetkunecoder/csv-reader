package database

import javax.inject.{Inject, Singleton}
import slick.jdbc.PostgresProfile.api._
import models.Bank

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BankRepository @Inject()(implicit executionContext: ExecutionContext) {

  class BankTable(tag: Tag) extends Table[Bank](tag, "banks_demo") {
    def name = column[String]("name")
    def bankIdentifier = column[Long]("bank_identifier", O.PrimaryKey)
    def * = (name, bankIdentifier) <> (Bank.tupled, Bank.unapply)
  }
  val banks = TableQuery[BankTable]

  val db = Database.forConfig("postgres")

  def readAll(): Future[Seq[Bank]] = db.run(banks.result)

  def insert(bank: Bank): Future[Int] = db.run(banks += bank)

  def read(bankIdentifier: Long): Future[Seq[Bank]] = db.run(banks.filter(_.bankIdentifier === bankIdentifier).result)

  def delete(bankIdentifier: Long): Future[Int] = db.run(banks.filter(_.bankIdentifier === bankIdentifier).delete)

  def deleteAll(): Future[Int] = db.run(banks.delete)
}