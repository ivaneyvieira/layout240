package br.com.pintos.layout240

import java.io.File

fun main(args: Array<String>) {
  val file = File("/home/ivaneyvieira/Dropbox/projetoBancoSafra/ITAU/retorno/RET_ITAU_2787_02101803.RET")
  val arquivo = Arquivo(file)
  arquivo.processaArquivo()
}