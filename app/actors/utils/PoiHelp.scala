package actors.utils

import org.apache.poi.xssf.usermodel.{XSSFCell, XSSFRow, XSSFSheet}
import play.api.Logger

case class Sheet (poiSheet: XSSFSheet) {

  def retrieveFirstTwo(name: String): List[XSSFRow] = {

    def hasCellName(xrow: XSSFRow): Boolean = {
      val row = Row(xrow)
      val maybeMatch = row.cells.find((c: XSSFCell) => {
        if(c == null) {
          false
        } else {

          c.getCellType() match {
            case 1 => c.getStringCellValue() == name
            case _ => false
          }
        }
      })
      !maybeMatch.isEmpty
    }

    val matchedRows: Seq[XSSFRow] = rows.filter((r: XSSFRow) => hasCellName(r))
    // return first two as List
      (collection.immutable.SortedSet[XSSFRow]() ++ matchedRows).toList.take(2)
  }

  def getRowsBetween(start: Int, end: Int): Seq[XSSFRow] = {
    val rs = rows()
    rs.filter((r: XSSFRow) => {
      if(r != null) {
        r.getRowNum() > start && r.getRowNum() < end
      } else {
        false
      }

    })
  }

  def rows(): Seq[XSSFRow] = {
    val count: Int = poiSheet.getPhysicalNumberOfRows()
    for (i <- 0 to count) yield poiSheet.getRow(i)
  }
}

case class Row (poiRow: XSSFRow) {

  def cells(): Seq[XSSFCell] = {
    if(poiRow != null) {
      val count: Int = poiRow.getPhysicalNumberOfCells()
      for (i <- 0 to count) yield poiRow.getCell(i)
    } else {
      Seq()
    }
  }
}
