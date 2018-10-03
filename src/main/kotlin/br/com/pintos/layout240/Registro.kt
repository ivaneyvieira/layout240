package br.com.pintos.layout240

import br.com.pintos.layout240.ETipo.ALFA
import br.com.pintos.layout240.ETipo.DATA
import br.com.pintos.layout240.ETipo.HORA
import br.com.pintos.layout240.ETipo.NUM
import br.com.pintos.layout240.ETipo.VALOR

open class Registro(val linha: String) {
  val banco = numero(1, 3)
  val lote = numero(4, 7)
  val registro = numero(8, 8)

  protected fun numero(inicio: Int, fim: Int): Campo {
    return Campo(NUM, linha.extraiCampo(inicio, fim))
  }

  protected fun string(inicio: Int, fim: Int): Campo {
    return Campo(ALFA, linha.extraiCampo(inicio, fim))
  }

  protected fun valor(inicio: Int, fim: Int): Campo {
    return Campo(VALOR, linha.extraiCampo(inicio, fim))
  }

  protected fun date(inicio: Int): Campo {
    return Campo(DATA, linha.extraiCampo(inicio, inicio + 7))
  }

  protected fun hora(inicio: Int): Campo {
    return Campo(HORA, linha.extraiCampo(inicio, inicio + 5))
  }

  protected fun tipoInscricao(inicio: Int): Campo {
    val tipo = when (linha.extraiCampo(inicio, inicio)) {
      "0"  -> "Isento/Não Informado"
      "1"  -> "CPF"
      "2"  -> "CGC/CNPJ"
      "3"  -> "PIS/PASEP"
      "9"  -> "Outros"
      else -> ""
    }
    return Campo(ALFA, tipo)
  }

  protected fun numeroDigito(inicio: Int, fim: Int): Campo {
    val numero = linha.extraiCampo(inicio, fim - 1)
    val digito = linha.extraiCampo(fim, fim)
    return Campo(ALFA, "$numero/$digito")
  }

  protected fun tipoArquivo(inicio: Int): Campo {
    val tipo = when (linha.extraiCampo(inicio, inicio)) {
      "0"  -> "Remessa"
      "1"  -> "Retorno"
      else -> ""
    }
    return Campo(ALFA, tipo)
  }

  protected fun naturezaLancamento(inicio: Int): Campo {
    val tipo = when (linha.extraiCampo(inicio, inicio + 2)) {
      "DPV" -> "TIPO DISPONÍVEL"
      "SCR" -> "TIPO VINCULADO"
      "SSR" -> "TIPO BLOQUEADO"
      "CDS" -> "COMPOSIÇÃO DE DIVERSOS SALDOS"
      else  -> ""
    }
    return Campo(ALFA, tipo)
  }

  protected fun categoriaLancamento(inicio: Int): Campo {
    val tipo = when (linha.extraiCampo(inicio, inicio + 3)) {
      "101" -> "Cheques"
      "102" -> "Encargos"
      "103" -> "Estornos"
      "104" -> "Lançamento Avisado"
      "105" -> "Tarifas"
      "106" -> "Aplicação"
      "107" -> "Empréstimo/Financiamento"
      "108" -> "Câmbio"
      "109" -> "CPMF"
      "110" -> "IOF"
      "111" -> "Imposto de Renda"
      "112" -> "Pagamento Fornecedores"
      "113" -> "Pagamentos Salário"
      "114" -> "Saque Eletrônico"
      "115" -> "Ações"
      "117" -> "Transferência entre Contas"
      "118" -> "Devolução da Compensação"
      "119" -> "Devolução de Cheque Depositado"
      "120" -> "Transferência Interbancária (DOC, TED)"
      "121" -> "Antecipação a Fornecedores"
      "122" -> "OC/AEROPS"
      "123" -> "Saque em Espécie"
      "201" -> "Depósitos"
      "202" -> "Líquido de Cobrança"
      "203" -> "Devolução de Cheques"
      "204" -> "Estornos"
      "205" -> "Lançamento Avisado"
      "206" -> "Resgate de Aplicação"
      "207" -> "Empréstimo/Financiamento"
      "208" -> "Câmbio"
      "209" -> "Transferência Interbancária (DOC, TED)"
      "210" -> "Ações"
      "211" -> "Dividendos"
      "212" -> "Seguro"
      "213" -> "Transferência entre Contas"
      "214" -> "Depósitos Especiais"
      "215" -> "Devolução da Compensação"
      "216" -> "OCT"
      "217" -> "Pagamentos Fornecedores"
      "218" -> "Pagamentos Diversos"
      "219" -> "Pagamentos Salários"
      "220" -> "Depósito em Espécie"
      else  -> ""
    }
    return Campo(ALFA, tipo)
  }

  protected fun posicaoSaldo(inicio: Int): Campo {
    val tipo = when (linha.extraiCampo(inicio, inicio)) {
      "P"  -> "Parcial"
      "F"  -> "Final"
      "I"  -> "Intra-Dia"
      else -> ""
    }
    return Campo(ALFA, tipo)
  }
}

class RegistroTrailerLote(linha: String) : Registro(linha) {
  val bloqueadoAcima24 = valor(89, 106)
  val limite = valor(107, 124)
  val bloqueadoAte24 = valor(89, 106)
  val dataSaldoFilna = date(143)
  val valorSaldoFinal = valor(151,168)
  val situacaoSaldoFinal = string(169, 168)
  val posicaoSaldoFinal = posicaoSaldo(170)
  val quantidadeRegistros = numero(171, 176)
  val totalDebitos = valor(177, 194)
  val totalCreditos = valor(195, 212)
}

class RegistroHeaderLote(linha: String) : Registro(linha) {
  val dataSaldoInicial = date(143)
  val valorSaldoInicial = valor(151, 168)
  val situacaoSaldoInicial = string(169, 169)
  val posicaoSaldoInicial = posicaoSaldo(170)
  val moeda = string(171, 173)
  val sequancia = numero(174, 178)
}

class RegistroDetalheLote(linha: String) : Registro(linha) {
  val tipoInscricao = tipoInscricao(18)
  val inscricao = numero(19, 32)
  val convenio = string(33, 52)
  val agencia = numeroDigito(53, 58)
  val conta = numeroDigito(59, 71)
  val nomeEmpresa = string(73, 102)
  val natureza = naturezaLancamento(109)
  val dataLancamento = date(143)
  val valorLancamento = valor(151, 168)
  val tipoLancamento = string(169, 169)
  val categoriaLancamento = categoriaLancamento(170)
  val codigoHitorico = string(173, 176)
  val historico = string(177, 201)
  val numeroDocumento = string(202, 240)
}

class RegistroTrailerArquivo(linha: String) : Registro(linha) {
  val quantLotes = numero(18, 23)
  val quantRegistros = numero(24, 29)
  val quantContas = numero(30, 35)
}

class RegistroHeaderArquivo(linha: String) : Registro(linha) {
  val nomeBanco = string(103, 132)
  val tipoArquivo = tipoArquivo(143)
  val dataGeracao = date(144)
  val horaGeracao = hora(152)
  val sequencial = numero(158, 163)
  val layout = numero(164, 166)
  val densidade = numero(167, 171)
}

data class Campo(
  val tipo: ETipo,
  val valor: String = ""
)

enum class ETipo {
  NUM, VALOR, ALFA, DATA, HORA
}
