package com.liderMinas.PCP.database


import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import android.util.Log
import com.liderMinas.PCP.SQLiteHelper
import kotlinx.coroutines.*
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

class Connection(private val coreConnection: java.sql.Connection) :
    java.sql.Connection by coreConnection, Closeable {

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


    /*fun startConn() {
        val policy: StrictMode.ThreadPolicy = Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            Class.forName(Classes)
            connection = DriverManager.getConnection(url, username, password)
            Log.d("Debug: ", "Connected")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Log.d("Debug: ", "Class fail")
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.d("Debug" , "Connected no")
        }
    }*/

    override fun close() {
        coreConnection.close()
    }

    override fun createStatement(): Statement {
        return coreConnection.createStatement()
    }

    /*fun queryToServer(){
        var c = connectMSSQL();
        var con = c.startConn()
        var query = "INSERT INTO TableName(ColumnName) VALUES ('+text+') ";
        stmt = con.createStatement()
        var set: ResultSet = stmt.execute(query)

        while (set.next()){
            produtos.setText(set.getString(2))
        }
        con.close()

    }*/
}


fun connect(): java.sql.Connection? {
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
        c.autoCommit = false
        return Connection(c)
        Log.d("Debug", "Connected")
    } catch (e: ClassNotFoundException){
        e.printStackTrace();
        Log.d("Connection State:", "${e}, ERRO Class")
    }catch (e: SQLException) {
        e.printStackTrace();
        Log.d("Connection State:", "${e}, ERRO SQL")

    }
    return null
}

fun main(args: Array<String>) {
    //queryProdutoExt()
}

fun queryProdutoExt(context: Context) {
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
            Log.d("SQL Insert", "${resultSet1.getString("descProduto")} inserido com sucesso")
        }

        resultSet1.close()
        st1.close()

        //return arrayOf(arrayProdutoIdExt, arrayProdutoDescExt,arrayProdutoQeExt, arrayProdutoValExt, arrayProdutoTipoExt)

    }
}

fun queryMotivoExt(context: Context) {
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

        resultSet1.close()
        st1.close()
    }
}

fun queryExternalServerAE(context: Context){
    var dbIntrn: SQLiteHelper = SQLiteHelper(context)
    var query = "SELECT qtdApontada, tipoUnitizador, dataHoraApontamento," +
            " lote, caixaAvulsa, unidadeAvulsa, validade, total, idProduto, qeProduto, validProduto, tipoVProduto, username" +
            " FROM ApontEmbalado WHERE statusSync = 0 ORDER BY idApontEmbalado ASC"
    var result = dbIntrn.getDetailAE()
    connect().use {
        var st1 = it?.createStatement()!!
        var t = 0
        if (result.moveToFirst() != null) {
            result.moveToFirst()
                while (!result.isAfterLast()) {
                    st1.executeQuery(
                        """
                        INSERT INTO ApontEmbalado 
                        (qtdApontada, tipoUnitizador, dataHoraApontamento, lote, caixaAvulsa, unidadeAvulsa, validade, total, statusSync, idProduto, qeProduto, validProduto, tipoVProduto, username)
                        VALUES
                        (${result.getInt(1)}, '${result.getString(2)}', '${result.getString(3)}', ${result.getInt(4)},
                         ${result.getInt(5)}, ${result.getInt(6)}, ${result.getString(7)}, ${result.getInt(8)}, 
                         ${result.getInt(9)}, ${result.getInt(10)}, ${result.getInt(11)}, ${result.getInt(12)}, ${result.getString(13)}, ${result.getString(14)})
                        """.trimIndent()
                    )
                    result.moveToNext()
            }

        }
    }
}






    /*val st2 = it?.prepareStatement(
        """
            SELECT a, b
              FROM table_name
             WHERE value = 5
               AND filter_1 = ?
               AND filter_2 = ?
             ORDER BY id
            """.trimIndent()
    )
    st2.setLong(1, 1L)
    st2.setString(2, "text")
    resultSet2 = st2.executeQuery()
    while (resultSet2.next()) {
        val a = resultSet1.getLong("a")   // same as resultSet1.getLong(1)
        val b = resultSet1.getString("b") // same as resultSet1.getString(2)
        // process
    }
    resultSet2.close()
    st2?.close()*/




/*class query {
    fun queryToServer2{
        var connectionClass = connectMSSQL();
        try {
            var con = connectionClass.startConn();
            var query = "INSERT INTO TableName(ColumnName) VALUES ('+text+') ";
            var stmt: Statement = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            Log.e("ERROR", e.getMessage());
        }
    }
}*/

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






