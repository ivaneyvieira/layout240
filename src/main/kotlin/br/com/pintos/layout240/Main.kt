package br.com.pintos.layout240

import java.io.File

fun main(args: Array<String>) {
  //val dirRetornos = args[0] //home/ivaneyvieira/Dropbox/projetoBancoSafra
  val fileDirRetorno = File("/home/ivaneyvieira/Dropbox/projetoBancoSafra")
  val arquivosLogico = processaArquivos(fileDirRetorno)

  Excel().geraPlanilha(arquivosLogico)
}

fun processaArquivos(fileDirRetorno: File): List<RegistroHeaderArquivo> {
  return if (fileDirRetorno.isDirectory) {
    val lista = ArrayList<RegistroHeaderArquivo>()
    fileDirRetorno.listFiles().forEach { file ->
      lista.addAll(processaArquivos(file))
    }
    lista
  } else {
    if (fileDirRetorno.name.toUpperCase().endsWith(".RET") ||
        fileDirRetorno.name.toUpperCase().endsWith(".TXT")) {
      val arquivo = Arquivo(fileDirRetorno)
      arquivo.processaArquivo()
      arquivo.arquivosLogico()
    } else emptyList()
  }
}
