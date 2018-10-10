package br.com.pintos.layout240

fun String.tipoRegistro(): Char {
  val strTipo = extraiCampo(
    8,
    8
  )
  return strTipo[0]
}

fun String.extraiCampo(inicio: Int, fim: Int): String {
  return if (inicio > length || fim > length) ""
  else substring(
    inicio - 1,
    fim
  )
}
