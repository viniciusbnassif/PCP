package com.liderMinas.PCP.database

import java.util.*

data class ProdutoModelo(
    var idProduto: Int = getAutoId(),
    var descProduto: String = "",
    var qeProduto: Int,
    val validProduto: Int,
    val tipoVProduto: String = ""

    ){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}

data class UsuarioModel(
    var idUsuario: Int = getAutoId(),
    var username: String = "",
    var password: String = "",
    var nome: String = ""
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}

data class MotivoModel(
    var idMotivo: Int = getAutoId(),
    var descMotivo: String
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}

data class ApontEmbaladoModel(
    var idApontEmbalado: Int = getAutoId(),
    var pilhaApontada: Int,
    var dataHoraApontamento: String,
    var lote: Int,
    var caixaAvulsa: Int,
    var unidadeAvulsa: Int,
    var validade: String
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}

data class ApontPerdasModel(
    var idApontPerdas: Int = getAutoId(),
    var qtdPerda: Float,
    var dataHoraPerda: String
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}


/*Tabela de sincronismo entre aparelho e servidor*/

data class SyncModel(
    var idSync: Int = getAutoId(),
    var dataHoraSync: String,
    var statusSync: Int
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}
