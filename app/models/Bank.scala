package models

import play.api.libs.json.{Json, Writes}

case class Bank(name: String, bankIdentifier: Long)

object Bank {
  implicit val writes: Writes[Bank] = Json.writes[Bank]
  def tupled:((String, Long)) => Bank = (Bank.apply _).tupled
}
