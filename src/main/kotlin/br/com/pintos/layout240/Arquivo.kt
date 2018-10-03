package br.com.pintos.layout240

import java.io.File

class Arquivo(val file: File) {
  val registros = ArrayList<Registro>()

  fun processaArquivo() {
    file.forEachLine { linha ->
      if (linha.length == 240)
        processaLinha(linha)
      else
        print("Linha inválida:\n$linha")
    }
  }

  private fun processaLinha(linha: String) {
    val tipoRegistro = linha.tipoRegistro()
    val registro: Registro? = when (tipoRegistro) {
      '0'  -> processaHeaderArquivo(linha)
      '1'  -> processaHeaderLote(linha)
      '3'  -> processaDetalheLote(linha)
      '5'  -> processaTrailerLote(linha)
      '9'  -> processaTrailerArquivo(linha)
      else -> {
        print("Tipo de registro não encontrado")
        null
      }
    }
    registro?.let { reg ->
      registros.add(reg)
    }
  }

  private fun processaTrailerArquivo(linha: String) = RegistroTrailerArquivo(linha)

  private fun processaTrailerLote(linha: String) = RegistroTrailerLote(linha)

  private fun processaDetalheLote(linha: String) = RegistroDetalheLote(linha)

  private fun processaHeaderLote(linha: String) = RegistroHeaderLote(linha)

  private fun processaHeaderArquivo(linha: String) = RegistroHeaderArquivo(linha)
}

