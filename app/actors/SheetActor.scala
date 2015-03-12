package actors

import akka.actor._
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import play.api.Logger
import akka.dispatch.ExecutionContexts._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.{ Future, Await }
import scala.concurrent.duration._
import org.apache.poi.ss.usermodel.FormulaEvaluator
import actors.utils.{ Sheet, Row }
import play.api.libs.json._


case class SheetMessage(workbook: XSSFWorkbook)
trait SheetResponse
case class ErrorSheetResponse(message: String) extends SheetResponse
case class ValidSheetResponse(json: JsValue) extends SheetResponse

case class Transect(name: String, categories: List[MajorCategory])
case class MajorCategory(name: String, code: String, percentage: BigDecimal, subcategories: List[SubCategory])
case class SubCategory(name: String, code: String, percentage: BigDecimal)

trait SubCatResult
case class ValidSubCat(subcat: SubCategory) extends SubCatResult
case class InvalidSubCat() extends SubCatResult

trait CatResult
case class ValidCatResult(category: MajorCategory) extends CatResult
case class InvalidCatResult() extends CatResult

class SheetActor extends Actor {

  val mc = List("CORAL (C)", "OTHER INVERTEBRATES (OI)", "MACROALGAE (MA)", "BRANCHING CORALLINE ALGAE (BCA)", "CRUSTOSE CORALLINE ALGAE (CCA)", "SAND (SD)", "FLESHY CORALLINE ALGAE (FCA)", "CHRYSOPHYTE (CHRYS)", "TURF ALGAE (T)",
    "TAPE, WAND, SHADOW (TWS)")

  implicit val timeout = Timeout(5 seconds)

  implicit def orderingRowByIndex[A <: XSSFRow]: Ordering[A] =
    Ordering.by(e => (e.getRowNum()))

  implicit def orderingCellByIndex[A <: XSSFCell]: Ordering[A] = Ordering.by(e => (e.getColumnIndex()))
  implicit val subCategoryToJson = new Writes[SubCategory] {
    def writes(data: SubCategory) = Json.obj(
      "name" -> data.name,
      "code" -> data.code,
      "value" -> data.percentage
    )
  }

  implicit val categoryToJson = new Writes[MajorCategory] {
    def writes(data: MajorCategory) = Json.obj(
      "name" -> data.name,
      "code" -> data.code,
      "value" -> data.percentage,
      "subcategories" -> data.subcategories
    )
  }

  implicit val transectToJson = new Writes[Transect] {
    def writes(data: Transect) = Json.obj (
      "name" -> data.name,
      "major_categories" -> data.categories
    )
  }

  def receive = {

    case message: SheetMessage => processMessage(sender, message)

  }

  def processMessage(requestor: ActorRef, msg: SheetMessage) {
    Logger.debug("Finding the sheet")

    val response = Option(msg.workbook.getSheet("Data Summary"))

    response match {
      case Some(xssfSheet) => {

        // wrapped sheet object
        val sheet = new Sheet(xssfSheet)
        // for evaluating formulas
        val evaluator: FormulaEvaluator = msg.workbook.getCreationHelper().createFormulaEvaluator()

        // transects as list of string
        val transects = findTransects(xssfSheet)

        // is a map with a major category row as the key, and it's subcategories as a value
        val dataList = extractData(sheet)

        val results = for ((t, i) <- transects.zipWithIndex) yield {

          // iterate over map, returning a sequence of catresults
          val data = for (d <- dataList) yield {

            def extractCat(subcats: List[SubCategory], eval: FormulaEvaluator, index: Int, cells: Seq[XSSFCell]): CatResult = {
              // category name and code as a tuple (name, code)
              val nameCode = getNameCode(cells(0).getStringCellValue())
              val maybeCell = cells(index + 1)

              if (maybeCell != null && maybeCell.getCellType() == 2) {
                // evaluate the formula
                val cell = eval.evaluate(maybeCell)
                val category = new MajorCategory(nameCode._1, nameCode._2, cell.getNumberValue(), subcats)
                new ValidCatResult(category)
              } else {
                new InvalidCatResult()
              }
            }

            def getNameCode(input: String): (String, String) = {
              val names = input.split('(')
              val name = names(0).trim()
              val code = names(1).split(')')(0)
              (name, code)
            }

            // yield subcatresult list
            val respss = for (s <- d._2) yield {

              val cells = new Row(s).cells()
              val nameCode = getNameCode(cells(0).getStringCellValue())
              val maybeCell = cells(i + 1)
              if (maybeCell != null && maybeCell.getCellType() == 2) {
                val cell = evaluator.evaluate(maybeCell)
                val subcate = new SubCategory(nameCode._1, nameCode._2, cell.getNumberValue())
                new ValidSubCat(subcate)
              } else {
                new InvalidSubCat()
              }
            }

            val subcats = respss.filter((r: SubCatResult) => r.isInstanceOf[ValidSubCat]).map((s: SubCatResult) => s.asInstanceOf[ValidSubCat].subcat).toList

            extractCat(subcats, evaluator, i, new Row(d._1).cells())

          }
//          Logger.debug("data list is: " + data.toString())
          val categories = data.filter((c: CatResult) => c.isInstanceOf[ValidCatResult]).map((cr: CatResult) => cr.asInstanceOf[ValidCatResult].category)
  //        Logger.debug("Category list length is: " + categories.length)
          new Transect(t, categories)
        }
        Logger.debug("returning positive results")
        requestor ! new ValidSheetResponse(Json.toJson(results))

      }
      case None =>
        val errorMessage= "why are you putting me through this?"
        Logger.error(errorMessage)
        requestor ! new ErrorSheetResponse(errorMessage)
    }

  }

  def extractData(sheet: Sheet): List[(XSSFRow, Seq[XSSFRow])] = {
    // curried function for getting rows between two rows
    val grb = (sheet.getRowsBetween _).curried

    // the first row from the major categories
    val majorCatRow = (k: String) => (sheet.retrieveFirstTwo(k))(0)
    // the rows between two
    val subCategories = (x: Int, y: Int) => (grb(x))(y)
    // the row number for the second major category row
    val rowNum = (k: String) => (sheet.retrieveFirstTwo(k))(1).getRowNum()

    val m = for (cc <- mc.iterator.sliding(2).toList) yield majorCatRow(cc(0)) -> subCategories(rowNum(cc(0)), rowNum(cc(1)))
    m
  }

  def findTransects(xsheet: XSSFSheet): Seq[String] = {

    def hasCellWithTransectNameMatch(row: Row): Boolean = {

      val maybeMatch = row.cells.find((c: XSSFCell) => {

        if (c != null) {
          c.getCellType() match {
            case 1 => {

              if (c.getStringCellValue() != null) {
                return c.getStringCellValue() == "TRANSECT NAME"
              } else {
                return false
              }
            }
            case _ => false
          }
        } else {
          return false
        }
      })

      !maybeMatch.isEmpty
    }
    val sheet = new Sheet(xsheet)
    Logger.debug("Transformed sheet")
    val maybeRow = sheet.rows.find((r: XSSFRow) => hasCellWithTransectNameMatch(new Row(r)))

    maybeRow match {
      case None => Seq("")
      case Some(row) => {
        extractTransectValues(new Row(row))
      }
    }
  }

  def extractTransectValues(row: Row): Seq[String] = {

    val filteredCells = row.cells.filter((c: XSSFCell) => {
      if (c == null) {
        false
      } else {
        c.getCellType() == 1 && c.getStringCellValue() != "TRANSECT NAME"
      }

    })
    val filteredMapCells = filteredCells.map((c: XSSFCell) => c.getStringCellValue())

    (for (x <- filteredMapCells) yield x)(collection.breakOut)

  }

}
