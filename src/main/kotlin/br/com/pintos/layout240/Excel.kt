package br.com.pintos.layout240

import br.com.pintos.layout240.ETipo.ALFA
import br.com.pintos.layout240.ETipo.DATA
import br.com.pintos.layout240.ETipo.HORA
import br.com.pintos.layout240.ETipo.NUM
import br.com.pintos.layout240.ETipo.VALOR
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Excel {
  val workbook = XSSFWorkbook()
  val createHelper = workbook.getCreationHelper()
  val fieldsHeader = RegistroHeaderArquivo("").campos + RegistroHeaderLote("").campos + RegistroDetalhe("").campos
  val sheetName
    get() = "Resultado"
  val mapStyles: Map<ETipo, XSSFCellStyle> = mapOf(
    NUM to styleNum(),
    VALOR to styleValor(),
    ALFA to styleAlfa(),
    DATA to styleData(),
    HORA to styleHora()
  )

  fun styleNum(): XSSFCellStyle {
    val cellStyle = workbook.createCellStyle()
    cellStyle.dataFormat = createHelper.createDataFormat().getFormat("#,##0")
    return cellStyle
  }

  fun styleValor(): XSSFCellStyle {
    val cellStyle = workbook.createCellStyle()
    cellStyle.dataFormat = createHelper.createDataFormat().getFormat("#,##0.00")
    return cellStyle
  }

  fun styleAlfa(): XSSFCellStyle {
    return workbook.createCellStyle()
  }

  fun styleData(): XSSFCellStyle {
    val cellStyle = workbook.createCellStyle()
    cellStyle.dataFormat = createHelper.createDataFormat().getFormat("dd/mm/yyyy")
    return cellStyle
  }

  fun styleHora(): XSSFCellStyle {
    return workbook.createCellStyle()
  }

  fun geraPlanilha(arquivos: List<RegistroHeaderArquivo>) {
    val sheet = workbook.createSheet(sheetName)
    createHeader(sheet)
    createDetalhe(
      sheet,
      arquivos
    )
    finalizaSheet(sheet)
  }

  private fun createDetalhe(sheet: XSSFSheet, arquivos: List<RegistroHeaderArquivo>) {
    var rowIdx = 1
    arquivos.forEach { arquivo ->
      arquivo.lotes.forEach { lote ->
        if (lote.registros.isEmpty()) {
          val row = sheet.createRow(rowIdx++)
          val colLot = addCampos(
            arquivo,
            row,
            0
          )
          addCampos(
            lote,
            row,
            colLot
          )
        } else {
          lote.registros.forEach { detalhe ->
            val row = sheet.createRow(rowIdx++)
            val colLot = addCampos(
              arquivo,
              row,
              0
            )
            val colDet = addCampos(
              lote,
              row,
              colLot
            )
            addCampos(
              detalhe,
              row,
              colDet
            )
          }
        }
      }
    }
  }

  private fun <T : Registro<T>> addCampos(registro: T, row: XSSFRow, colIdx: Int): Int {
    registro.campos.forEachIndexed { index, prop ->
      val cell = row.createCell(index + colIdx)
      val campo: Campo = prop.get(registro)
      cell.setCellType(campo.tipoExcel())
      cell.cellStyle = mapStyles[campo.tipo]

      when (campo.tipo) {
        NUM   -> cell.setCellValue(campo.valorExcel().toDoubleOrNull() ?: 0.00)
        VALOR -> cell.setCellValue(campo.valorExcel().toDoubleOrNull() ?: 0.00)
        ALFA  -> cell.setCellValue(campo.valorExcel())
        DATA  -> cell.setCellValue(campo.valorExcel().toDate())
        HORA  -> cell.setCellValue(campo.valorExcel())
      }
    }
    return colIdx + registro.campos.size
  }

  private fun createHeader(sheet: XSSFSheet) {
    val headerFont = workbook.createFont()
    headerFont.bold = true
    headerFont.color = IndexedColors.WHITE.index
    val headerCellStyle = workbook.createCellStyle()
    headerCellStyle.setFont(headerFont)
    headerCellStyle.fillBackgroundColor = IndexedColors.GREY_50_PERCENT.index
    headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
    // Row for Header
    val headerRow = sheet.createRow(0)
    fieldsHeader.forEachIndexed { index, props ->
      val cell = headerRow.createCell(index)
      cell.cellStyle = headerCellStyle
      cell.setCellValue(props.name)
    }
  }

  private fun finalizaSheet(sheet: XSSFSheet) {
    fieldsHeader.forEachIndexed { index, _ ->
      sheet.autoSizeColumn(index)
    }
    val fileOut = FileOutputStream("dados.xlsx")
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
  }
}

private fun String.toDate(): Date? {
  return try {
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    sdf.parse(this)
  } catch (e: ParseException) {
    null
  }
}

private fun Campo.tipoExcel(): CellType {
  return when (tipo) {
    NUM   -> CellType.NUMERIC
    VALOR -> CellType.NUMERIC
    ALFA  -> CellType.STRING
    DATA  -> CellType.NUMERIC
    HORA  -> CellType.STRING
  }
}

private fun Campo.valorExcel(): String {
  return when (tipo) {
    NUM   -> "0" + valor.replaceFirst(
      "^0*",
      "0"
    )
    VALOR -> valor.substring(
      0,
      valor.length - 2
    ) + "." + valor.substring(valor.length - 3)
    ALFA  -> valor.trim()
    DATA  -> valor.substring(
      0,
      2
    ) + "/" + valor.substring(
      2,
      4
    ) + "/" + valor.substring(
      4,
      8
    )
    HORA  -> valor.substring(
      0,
      2
    ) + ":" + valor.substring(
      2,
      4
    ) + ":" + valor.substring(
      4,
      6
    )
  }
}
