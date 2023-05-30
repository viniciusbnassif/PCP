package com.liderMinas.PCP.database

import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.api.Context
import com.liderMinas.PCP.SQLiteHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import java.net.*
import kotlin.concurrent.thread


class Sync : AppCompatActivity(), LifecycleEventObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    }

    suspend fun testConnection(): String? {


        var message: String
        val host = "192.168.1.11"
        val port = 8080
        var url = "192.168.1.11:8080"


        //val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        //StrictMode.setThreadPolicy(policy)

        return withContext(Dispatchers.IO) {
            try {
                if (Looper.myLooper() == null) {
                    Looper.prepare()
                }

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
                message = "Falha"
                e.printStackTrace()
                return@withContext message
            } catch (e: Exception) { //sem conexão
                message = "Sem Conexão"
                e.printStackTrace()
                return@withContext message
            }
            message = "Sucesso"
            return@withContext message
        }as? String?
    }


    suspend fun sync(cod: Int, ctxt: android.content.Context): String? {
        /*0 zero Sincroniza tudo
        1 sincroniza apenas os inserts ao servidor
        9 sem conexão - não tenta sincronizar nada
        || = xor (ou lógico)
        && = e
         */

        var message: String

        var result = testConnection()

        if (result == "Sucesso") {
            return withContext(Dispatchers.IO) {
                if (cod == 0 || cod == 1) {
                    queryExternalServerAE(ctxt)
                    queryExternalServerAP(ctxt)
                    if (cod == 1) {
                        message = "Sucesso" //Sincronizado com sucesso
                        return@withContext message
                    }
                }
                if (cod == 0) {
                    queryProdutoExt(ctxt)
                    queryMotivoExt(ctxt)

                    message = "Sucesso" //Sincronizado com sucesso
                    return@withContext message
                }
                else {
                    //TODO
                }
            }as? String
        }
        message = "Falha"
        return message

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }


}
