package com.liderMinas.PCP.database

import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.api.Context
import com.liderMinas.PCP.SQLiteHelper
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import java.net.*
import kotlin.concurrent.thread


class Sync : AppCompatActivity(){

    fun testConnection(): String {
        var message: String
        val host = "192.168.1.11"
        val port = 8080
        var url = "192.168.1.11:8080"

        try {

            /*val clientHTTP = OkHttpClient()
            val clientSocket = Socket(host, port)
            clientSocket.soTimeout = 5000*/

            val clientHTTP = OkHttpClient()
            val address = InetSocketAddress(host, port);
            val clientSocket = Socket()
            clientSocket.connect(address, 5000)
            //clientSocket.soTimeout = 5000

            // do something with the successfully opened socket
            clientSocket.close()
        } catch (e: ConnectException) {
            // host and port combination not valid
            message = "Falha ao conectar (Host and port combination not valid)"
            e.printStackTrace()
            return message
        } catch (e: Exception) { //sem conexão
            message = "Sem conexão com o servidor/rede"
            e.printStackTrace()
            return message
        }
        message = "Sucesso"
        return message
    }


    fun sync(cod: Int, ctxt: android.content.Context): String {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        /*0 zero Sincroniza tudo
        1 sincroniza apenas os inserts ao servidor
        9 sem conexão - não tenta sincronizar nada
        || = xor (ou lógico)
        && = e
         */
        /*var message: String
        val host = "192.168.1.11"
        val port = 8080
        var url = "192.168.1.11:8080"

        try {

            /*val clientHTTP = OkHttpClient()
            val clientSocket = Socket(host, port)
            clientSocket.soTimeout = 5000*/

            val clientHTTP = OkHttpClient()
            val address = InetSocketAddress(host, port);
            val clientSocket = Socket()
            clientSocket.connect(address, 5000)
            //clientSocket.soTimeout = 5000

            // do something with the successfully opened socket
            clientSocket.close()
        } catch (e: ConnectException) {
            // host and port combination not valid
            message = "Falha ao conectar (Host and port combination not valid)"
            e.printStackTrace()
            return message
        } catch (e: Exception) { //sem conexão
            message = "Sem conexão com o servidor/rede"
            e.printStackTrace()
            return message
        }*/

        var message: String

        var result = testConnection()

        if (result == "Sucesso") {

            if (cod == 0 || cod == 1) {
                queryExternalServerAE(ctxt)
                queryExternalServerAP(ctxt)
                if (cod == 1) {
                    message = "Sincronização concluída"
                    return message
                }
            }
            if (cod == 0) {
                queryProdutoExt(ctxt)
                queryMotivoExt(ctxt)

                message = "Sincronização concluída"
                return message
            }
        }
        message = "Executando"
        return message
    }



}
