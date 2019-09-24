package controllers

import javax.inject.{Inject, Singleton}
import services.BankService
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class BankController @Inject() (cc: ControllerComponents, bankService: BankService) (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def importDataFromCsv() = {
    bankService.importDataFromCsv()
  }

  def readBank(bankId: String): Action[AnyContent] = Action.async { implicit request =>
    bankService.readBank(bankId.toLong).map {
      bank => Ok(Json.toJson(bank))
    }.recover(recoverError)
  }

  def all(): Action[AnyContent] = Action.async { implicit request =>
    bankService.readAll().map {
      bank => Ok(Json.toJson(bank))
    }.recover(recoverError)
  }

  def deleteAll(): Action[AnyContent] = Action.async { implicit request =>
    bankService.deleteAll().map {
      result => Ok(Json.toJson(result))
    }.recover(recoverError)
  }

  val recoverError: PartialFunction[Throwable, Result] = {
    case e: Exception => InternalServerError("Error while requesting data from the server: " + e)
  }
}