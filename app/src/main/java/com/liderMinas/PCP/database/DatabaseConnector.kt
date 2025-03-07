package com.liderMinas.PCP.database


import android.content.Context
import android.database.Cursor
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import android.util.Log
import androidx.core.database.getFloatOrNull
import androidx.core.database.getStringOrNull
import com.liderMinas.PCP.SQLiteHelper
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.Closeable
import java.net.ConnectException
import java.net.Socket
import java.sql.*
import java.sql.Connection

//import java.sql.SQLException;

//import com.liderMinas.PCP.database

class Connection(private val coreConnection: Connection) :
    Connection by coreConnection, Closeable {

    var ip = "192.168.1.10:1433" //local server running MSSQL Server [Porta 1433]
    var dbExt = "APP_PCP"
    var username = "APP_PCP"
    var password = "app@pcp"
    val Classes = "net.sourceforge.jtds.jdbc.Driver"
    var allProducts = mutableListOf<String>()
    var url = "jdbc:jtds:sqlserver://"+ip+"/"+dbExt

    lateinit var stmt: Statement
    //lateinit var connection: Connection

    lateinit var produtos: Array<String>

    override fun close() {
        coreConnection.close()
    }

    override fun createStatement(): Statement {
        return coreConnection.createStatement()
    }

}


fun connect(): Connection? {
    var ip = "192.168.1.10:1433" //local server running MSSQL Server [Porta 1433]
    var dbExt = "APP_PCP"
    var user = "APP_PCP"
    var password = "app@pcp"
    val Classes = "net.sourceforge.jtds.jdbc.Driver"
    var url = "jdbc:jtds:sqlserver://" + ip + "/" + dbExt

    //val c: Connection


    val policy = Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    Class.forName(Classes)
    lateinit var c : Connection
    try {
        c = DriverManager.getConnection(url, user, password)// <---------
        return Connection(c)
        Log.d("Debug", "Connected")
    } catch (e: ClassNotFoundException){
        e.printStackTrace()
        Log.d("Connection State:", "${e}, ERRO Class")
        return null
    }catch (e: SQLException) {
        e.printStackTrace()
        Log.d("Connection State:", "${e}, ERRO SQL")
        return null
    }
    return null
}

fun main(args: Array<String>) {
    //queryProdutoExt()
}

fun downloadProdutoExt(context: Context?) {
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)
    connect().use {
        var st1 = it?.createStatement()!!
        var resultSet1 = st1.executeQuery(
            """
            SELECT *
              FROM Produto
             ORDER BY idProduto
            """.trimIndent()
        )
        /*while (resultSet1.next()) {
            arrayProdutoIdExt.add(resultSet1.getInt("idProduto"))   // same as resultSet1.getLong(1)
            arrayProdutoDescExt.add(resultSet1.getString("descProduto")) // same as resultSet1.getString(2)
            arrayProdutoQeExt.add(resultSet1.getInt("qeProduto")) // same as resultSet1.getString(2)
            arrayProdutoValExt.add(resultSet1.getInt("validProduto")) // same as resultSet1.getString(2)
            arrayProdutoTipoExt.add(resultSet1.getString("tipoVProduto")) // same as resultSet1.getString(2)
            // process
        }*/
        dbIntrn.externalExecSQL("DELETE FROM Produto")
        while (resultSet1.next()){
            var query = "INSERT INTO Produto (idProduto, descProduto, qeProduto, validProduto, tipoVProduto) VALUES (${resultSet1.getInt("idProduto")}, '${resultSet1.getString("descProduto")}', ${resultSet1.getInt("qeProduto")}, ${resultSet1.getInt("validProduto")}, '${resultSet1.getString("tipoVProduto")}')"
            dbIntrn.externalExecSQL(query)
            Log.d("SQL Download Produto Ext", "${resultSet1.getString("descProduto")} inserido com sucesso")
        }
        dbIntrn.close()
        st1.close()
        connect()?.close()

        //return arrayOf(arrayProdutoIdExt, arrayProdutoDescExt,arrayProdutoQeExt, arrayProdutoValExt, arrayProdutoTipoExt)

    }
}

fun downloadMotivoExt(context: Context) {
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)
     //variavel dbIntrn recebe classe do Banco de dados localizado no dispositivo (Database.kt)
    connect().use {//Conexão ao banco de dados externo.
        var st1 = it?.createStatement()!!
        var resultSet1 = st1.executeQuery(
            """
            SELECT *
              FROM Motivo
             ORDER BY idMotivo
            """.trimIndent()
        )
        /*while (resultSet1.next()) {
            arrayProdutoIdExt.add(resultSet1.getInt("idProduto"))   // same as resultSet1.getLong(1)
            arrayProdutoDescExt.add(resultSet1.getString("descProduto")) // same as resultSet1.getString(2)
            arrayProdutoQeExt.add(resultSet1.getInt("qeProduto")) // same as resultSet1.getString(2)
            arrayProdutoValExt.add(resultSet1.getInt("validProduto")) // same as resultSet1.getString(2)
            arrayProdutoTipoExt.add(resultSet1.getString("tipoVProduto")) // same as resultSet1.getString(2)
            // process
        }*/
        dbIntrn.externalExecSQL("DELETE FROM Motivo")// <---------------------
        while (resultSet1.next()){
            var query = "INSERT INTO Motivo (idMotivo, descMotivo) VALUES (${resultSet1.getInt("idMotivo")}, '${resultSet1.getString("descMotivo")}');"
            dbIntrn.externalExecSQL(query)
        }
        dbIntrn.close()
        st1.close()
        connect()?.close()
    }
}

fun downloadProdEstoqueExt(context: Context?) {
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)
    connect().use {
        var st1 = it?.createStatement()!!
        var resultSet1 = st1.executeQuery(
            """
            SELECT *
              FROM ProdutoEstoque
             ORDER BY idProduto
            """.trimIndent()
        )
        dbIntrn.externalExecSQL("DELETE FROM ProdutoEstoque")
        while (resultSet1.next()){
            var query = "INSERT INTO ProdutoEstoque (idProduto, codProduto, descProduto, tipoProduto, unidMedida, rastro)" +
                    " VALUES (${resultSet1.getInt("idProduto")}, '${resultSet1.getString("codProduto")}', '${resultSet1.getString("descProduto")}', '${resultSet1.getString("tipoProduto")}', '${resultSet1.getString("unidMedida")}', '${resultSet1.getString("rastro")}')"
            dbIntrn.externalExecSQL(query)
            Log.d("SQL Download Prod Estoque", "${resultSet1.getString("descProduto")} inserido com sucesso")
        }

        dbIntrn.close()
        st1.close()
        connect()?.close()

        //return arrayOf(arrayProdutoIdExt, arrayProdutoDescExt,arrayProdutoQeExt, arrayProdutoValExt, arrayProdutoTipoExt)

    }
}

fun uploadAE(context: Context) {
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)

    var result = dbIntrn.getAE()
    var localResult = result


    connect().use {

        var st1 = it?.createStatement()!!
        if (localResult != null && localResult.count > 0) {
            localResult.moveToFirst()
            do {
                var id = localResult.getInt(0)

                var produto = dbIntrn.getDescProdutos(localResult.getInt(9))
                var produtoDesc = produto!!.getString(1)
                Log.d("ProdDesc", "$produtoDesc")
                try {

                    var insert = (
                        """
                        INSERT INTO ApontEmbalado 
                        (qtdApontada, tipoUnitizador, dataHoraApontamento, lote, caixaAvulsa, unidadeAvulsa, validade, total, produto, qeProduto, validProduto, tipoVProduto, username)
                        VALUES
                        (${localResult.getInt(1)}, '${localResult.getString(2)}', '${localResult.getString(3)}', ${localResult.getInt(4)},
                         ${localResult.getInt(5)}, ${localResult.getInt(6)}, '${localResult.getString(7)}', ${localResult.getInt(8)},
                         '${produtoDesc}', ${localResult.getInt(10)}, ${localResult.getInt(11)}, '${localResult.getString(12)}', 
                         '${localResult.getString(13)}');
                        """.trimIndent())
                    Log.d("uploadAE", insert)

                    var comm = st1.connection.prepareStatement(insert)
                    comm.executeUpdate()
                    //comm.connection.commit()
                } catch (e: ClassNotFoundException){
                    Log.e("Error SQL CNFE", e.toString())
                }
                catch (se: SQLException){
                    Log.e("Error SQLE", se.toString())
                }
                dbIntrn.insertDone("ApontEmbalado", id)

                //result.moveToNext()
            }while (localResult.moveToNext())


        }
        if (result != null) {
            result.close()
        }
        if (localResult != null) {
            localResult.close()
        }
        dbIntrn.close()
        st1.close()
        connect()?.close()
        }
}

fun uploadAP(context: Context) {
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)

    var result = dbIntrn.getAP()
    var localResult = result


    connect().use {

        var st1 = it?.createStatement()!!
        if (localResult != null && localResult.count > 0) {
            localResult.moveToFirst()
            do {

                var id = localResult.getInt(0)

                var produto = dbIntrn.getDescProdutos(localResult.getInt(5))
                var motivo = dbIntrn.getDescMotivo(localResult.getInt(6))
                var produtoDesc = produto!!.getString(1)
                var motivoDesc = motivo!!.getString(1)
                Log.d("ProdDesc", "$produtoDesc")
                try {

                    var insert = (
                            """
                            INSERT INTO ApontPerda 
                            (qtdPerda, unidPerda, dataHoraPerda, username, produto, motivo)
                            VALUES
                            (${localResult.getFloat(1)}, '${localResult.getString(2)}', '${localResult.getString(3)}', '${localResult.getString(4)}',
                             '${produtoDesc}', '${motivoDesc}');
                            """.trimIndent())
                    Log.d("uploadAP", insert)

                    var comm = st1.connection.prepareStatement(insert)
                    comm.executeUpdate()


                    //comm.connection.commit()
                } catch (e: ClassNotFoundException){
                    Log.e("Error SQL CNFE", e.toString())
                }
                catch (se: SQLException){
                    Log.e("Error SQLE", se.toString())
                }
                dbIntrn.insertDone("ApontPerda", id)

                //result.moveToNext()
            }while (localResult.moveToNext())


        } else {
            Log.d("uploadAP", "Erro")

        }
        dbIntrn.close()
        st1.close()
        connect()?.close()
    }
}

fun uploadRequisicoes(context: Context) {
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)

    var result = dbIntrn.getReq()
    var resultUpd = dbIntrn.getUpdReq()
    var localResult = result
    var localResultUpd = resultUpd

    connect().use {

        var st1 = it?.createStatement()!!
        if (localResult != null && localResult.count > 0) {
            localResult.moveToFirst()
            do {

                var id = localResult.getInt(0)

                //var produto = dbIntrn.getDescProdutos(localResult.getInt(5))
                //var motivo = dbIntrn.getDescMotivo(localResult.getInt(6))
                //var produtoDesc = produto!!.getString(1)
                //var motivoDesc = motivo!!.getString(1)
                Log.d("upload Req", "$id")
                try {


                    lateinit var userAtend: String

                    fun contentOrNull(any: Any?): Any? {
                        if (any == null){
                            return null
                        } else /*if (any == String && any != null)*/{
                            var anyS = "'" + "$any" + "'"
                            Log.d("UpReq AnyS test", anyS)
                            return anyS
                        }
                    }

                    var insert = (
                            """
                            INSERT INTO Requisicao
                            (codProduto, qtdRequisicao, qtdAtendida, qtdConfirmacao, userRequisicao, 
                            userAtendimento, userConfirmacao, dataHoraRequisicao, dataHoraAtendimento, dataHoraConfirmacao)
                            VALUES
                            ('${localResult.getString(1)}', ${localResult.getFloat(2)}, 
                            ${localResult.getFloatOrNull(3)},
                            ${localResult.getFloatOrNull(4)},
                            '${localResult.getString(5)}',
                            ${contentOrNull(localResult.getStringOrNull(6))},
                            ${contentOrNull(localResult.getStringOrNull(7))},
                            ${contentOrNull(localResult.getStringOrNull(8))},
                            ${contentOrNull(localResult.getStringOrNull(9))},
                            ${contentOrNull(localResult.getStringOrNull(10))});
                            """.trimIndent())
                    Log.d("Upload Requisicao", insert)

                    var query = """
                        UPDATE Requisicao 
                        SET statusSync = 1
                        WHERE idRequisicao = ${localResult.getInt(0)}
                    """.trimIndent()
                    dbIntrn.externalExecSQL(query)

                    var comm = st1.connection.prepareStatement(insert)
                    comm.executeUpdate()


                    //comm.connection.commit()
                } catch (e: ClassNotFoundException){
                    Log.e("Error SQL CNFE", e.toString())
                }
                catch (se: SQLException){
                    Log.e("Error SQLE", se.toString())
                }
                dbIntrn.insertDone("Requisicao", id)

                //result.moveToNext()
            }while (localResult.moveToNext())


        } else {
            Log.d("uploadRequisicoes", "Erro")

        }
        dbIntrn.close()
        st1.close()
        connect()?.close()
    }
}

fun uploadUpdRequisicoes(context: Context) {
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)

    var result = dbIntrn.getReq()
    var resultUpd = dbIntrn.getUpdReq()
    var localResult = result
    var localResultUpd = resultUpd

    connect().use {

        var st1 = it?.createStatement()!!
        if (localResultUpd != null && localResultUpd.count > 0) {
            localResultUpd.moveToFirst()
            do {

                var id = localResultUpd.getInt(0)

                //var produto = dbIntrn.getDescProdutos(localResult.getInt(5))
                //var motivo = dbIntrn.getDescMotivo(localResult.getInt(6))
                //var produtoDesc = produto!!.getString(1)
                //var motivoDesc = motivo!!.getString(1)
                Log.d("upload ReqUpd", "$id")
                try {


                    lateinit var userAtend: String

                    fun contentOrNull(any: Any?): Any? {
                        if (any == null){
                            return null
                        } else /*if (any == String && any != null)*/{
                            var anyS = "'" + "$any" + "'"
                            Log.d("UpReq AnyS test", anyS)
                            return anyS
                        }
                    }

                    var insert = (
                            """
                            UPDATE Requisicao 
                            SET codProduto = '${localResultUpd.getString(1)}', 
                            qtdRequisicao = ${localResultUpd.getFloat(2)},
                            qtdAtendida = ${localResultUpd.getFloatOrNull(3)}, 
                            qtdConfirmacao = ${localResultUpd.getFloatOrNull(4)}, 
                            userRequisicao = '${localResultUpd.getString(5)}', 
                            userAtendimento = ${contentOrNull(localResultUpd.getStringOrNull(6))}, 
                            userConfirmacao = ${contentOrNull(localResultUpd.getStringOrNull(7))}, 
                            dataHoraRequisicao = ${contentOrNull(localResultUpd.getStringOrNull(8))}, 
                            dataHoraAtendimento = ${contentOrNull(localResultUpd.getStringOrNull(9))}, 
                            dataHoraConfirmacao = ${contentOrNull(localResultUpd.getStringOrNull(10))}
                            WHERE codProduto = '${localResultUpd.getString(1)}' AND  
                            qtdRequisicao = ${localResultUpd.getFloat(2)} AND 
                            userRequisicao = '${localResultUpd.getString(5)}' AND
                            dataHoraRequisicao = ${contentOrNull(localResultUpd.getStringOrNull(8))};
                            
                            """.trimIndent())
                    Log.d("Upload RequisicaoUpd", insert)

                    var query = """
                        UPDATE Requisicao 
                        SET statusSync = 1
                        WHERE idRequisicao = ${localResultUpd.getInt(0)}
                    """.trimIndent()
                    dbIntrn.externalExecSQL(query)

                    var comm = st1.connection.prepareStatement(insert)
                    comm.executeUpdate()


                    //comm.connection.commit()
                } catch (e: ClassNotFoundException){
                    Log.e("Error SQL CNFE", e.toString())
                }
                catch (se: SQLException){
                    Log.e("Error SQLE", se.toString())
                }
                dbIntrn.insertDone("Requisicao", id)

                //result.moveToNext()
            }while (localResultUpd.moveToNext())


        } else {
            Log.d("uploadRequisicoes Upd ", "Erro")

        }
        dbIntrn.close()
        st1.close()
        connect()?.close()
    }
}



fun contentOrNullStr(any: Any): Any? {
    if (any == null) {
        Log.d("DwReq AnyS test", "null")
        return null
    } else {
        var anyS = "'$any'"
        Log.d("DwReq AnyS test", anyS)
        return anyS
    }
}
fun contentOrNullFloat(any: Float?): Any? {
    if (any == null) {
        Log.d("DwReq AnyF test", "null")
        return null
    } else if (any.toDouble() != 0.0) {
        var anyS = any
        Log.d("DwReq AnyF test", "$anyS")
        return anyS
    } else {
        return null
    }
}

var userAType: String? = null

fun downloadRequisicoes(context: Context?) {

    /*val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)*/

    val tbl = "Requisicao"
    val id = "idRequisicao"
    val cod = "codProduto"
    val qtdR = "qtdRequisicao"
    val qtdA = "qtdAtendida"
    val qtdC = "qtdConfirmacao"
    val userR = "userRequisicao"
    val userA = "userAtendimento"
    val userC = "userConfirmacao"
    val dataR = "dataHoraRequisicao"
    val dataA = "dataHoraAtendimento"
    val dataC = "dataHoraConfirmacao"
    //val lido = "lido"

    var dbIntrn: SQLiteHelper = SQLiteHelper(context)
    connect().use {
        var st1 = it?.createStatement()!!
        var resultSet1 = st1.executeQuery(
            """
            SELECT *
              FROM Requisicao
             ORDER BY idRequisicao
            """.trimIndent()
        )
        dbIntrn.externalExecSQL("DELETE FROM Requisicao")
        Log.d("Table $tbl", "Table $tbl deleted")
        while (resultSet1.next()){

            fun checkNullorString(origin: String?): Any? {
                if (resultSet1.getObject(origin) == null) {
                    userAType = null
                    return userAType
                } else {
                    userAType = resultSet1.getString(origin)
                    return userAType
                }
            }
            var query =
                "INSERT INTO $tbl ($id, $cod, $qtdR, $qtdA, $qtdC, $userR, $userA, $userC, $dataR, $dataA, $dataC) " +
                        "VALUES (${resultSet1.getInt("$id")}, " +
                        "'${resultSet1.getString("$cod")}', " +
                        "${contentOrNullFloat(resultSet1.getFloat("$qtdR"))}," +
                        "${contentOrNullFloat(resultSet1.getFloat("$qtdA"))}," +
                        "${contentOrNullFloat(resultSet1.getFloat("$qtdC"))}," +
                        "${checkNullorString(userR)?.let { it1 -> contentOrNullStr(it1) }}," +
                        "${checkNullorString(userA)?.let { it1 -> contentOrNullStr(it1) }}," +
                        "${checkNullorString(userC)?.let { it1 -> contentOrNullStr(it1) }}, " +
                        "${checkNullorString(dataR)?.let { it1 -> contentOrNullStr(it1) }}," +
                        "${checkNullorString(dataA)?.let { it1 -> contentOrNullStr(it1) }}," +
                        "${checkNullorString(dataC)?.let { it1 -> contentOrNullStr(it1) }}) "
            dbIntrn.externalExecSQL(query)
            Log.d(
                "SQL Download Requisicao",
                "${resultSet1.getString("$cod")} inserido com sucesso (${
                    resultSet1.getInt("$id")
                })"
            )
        }

        dbIntrn.close()
        st1.close()
        connect()?.close()

    }

}




fun confirmUnPw(username: String, password: String): Int {
    // Create an OkHttpClient object

    val policy = Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    val client = OkHttpClient()

// Create a RequestBody object with your data
    val requestBody = FormBody.Builder()
        .add("grant_type", "password")
        .build()

// Create a Request object with your URL, headers and body
    val request = Request.Builder()
        .url("http://192.168.1.11:8080/rest/api/oauth2/v1/token")
        .addHeader("password", password)
        .addHeader("username", username)
        .post(requestBody)
        .build()

// Execute the request and get a Response object

    var rtn: Int


    val host = "192.168.1.11"
    val port = 8080

    try {
        val clientSocket = Socket(host, port)

        // do something with the successfully opened socket
        clientSocket.close()
    } catch (e: ConnectException) {
        rtn = 900
        // host and port combination not valid
        e.printStackTrace()
        return rtn
    } catch (e: Exception) { //sem conexão
        rtn = 901
        e.printStackTrace()
        return rtn
    }

    val response = client.newCall(request).execute()


// Check if the response was successful and print the body
    if (response.isSuccessful) {
        Log.d("Debug UserConnection", "${response.body()?.string()}")
        //var body = response.body()?.string()
        //var token = JSONObject(body).getJSONObject("access_token")
        //rtn = if (token.length()>0) 1 else 0
        rtn = response.code()
        return rtn
    } else {
        println("Request failed: ${response.code()}")
        Log.d("Request failed", "Request failed: ${response.code()}")
        rtn = response.code()
        return rtn
    }
}

    //==========================================================================






