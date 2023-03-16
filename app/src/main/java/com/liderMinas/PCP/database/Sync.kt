package com.liderMinas.PCP.database

import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import com.google.api.Context
import com.liderMinas.PCP.SQLiteHelper
import java.net.ConnectException
import java.net.Socket
import kotlin.concurrent.thread


class Sync : AppCompatActivity(){
    fun sync(cod: Int, ctxt: android.content.Context) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        /*0 zero Sincroniza tudo
        1 sincroniza apenas os inserts ao servidor
        9 sem conexão - não tenta sincronizar nada
        || = xor (ou lógico)
        && = e
         */



        val host = "192.168.1.11"
        val port = 8080

        try {
            val clientSocket = Socket(host, port)

            // do something with the successfully opened socket
            clientSocket.close()
        } catch (e: ConnectException) {
            // host and port combination not valid
            e.printStackTrace()
            return
        } catch (e: Exception) { //sem conexão
            e.printStackTrace()
            return
        }


        if (cod == 0 || cod == 1){
            queryExternalServerAE(ctxt)
            queryExternalServerAP(ctxt)
        }
        if (cod == 0){
            queryProdutoExt(ctxt)
            queryMotivoExt(ctxt)
        }

    }
}