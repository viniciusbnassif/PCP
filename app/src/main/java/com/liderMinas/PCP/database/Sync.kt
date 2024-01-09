package com.liderMinas.PCP.database

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.net.*


class Sync : AppCompatActivity(), LifecycleEventObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var ctxt = this


        if (Looper.myLooper() == null) {
            Looper.prepare()
        }


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
                clientSocket.connect(address, 10000)
                //clientSocket.soTimeout = 5000

                // do something with the successfully opened socket
                clientSocket.close()
            } catch (e: ConnectException) {
                // host and port combination not valid
                Log.d("Test Connection Error", "Ao tentar conectar o seguinte erro aconteceu: ${e.toString()} /n Possivelmente: host and port combination not valid")
                message = "Falha"
                e.printStackTrace()
                return@withContext message
            } catch (e: Exception) { //sem conexão
                message = "Sem Conexão"
                e.printStackTrace()
                Log.d("Test Connection Error", "Ao tentar conectar o seguinte erro aconteceu: ${e.toString()}")
                return@withContext message
            }
            message = "Sucesso"
            return@withContext message
        }as? String?
    }


    suspend fun sync(cod: Int, ctxt: android.content.Context): String? {

        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
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
                    if (Looper.myLooper() == null) {
                        Looper.prepare()
                    }
                    if (cod == 0 || cod == 1) {
                        uploadAE(ctxt)
                        uploadAP(ctxt)
                        uploadRequisicoes(ctxt)
                        if (cod == 1) {
                            message = "Sucesso" //Sincronizado com sucesso
                            return@withContext message
                        }
                    }
                    if (cod == 0) {
                        //downloadRequisicoes(ctxt)

                        downloadProdutoExt(ctxt)
                        downloadMotivoExt(ctxt)
                        downloadProdEstoqueExt(ctxt)
                        downloadRequisicoes(ctxt)

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

        fun syncNoSuspension(cod: Int, ctxt: android.content.Context): String? {


            /*0 zero Sincroniza tudo
            1 sincroniza apenas os inserts ao servidor
            9 sem conexão - não tenta sincronizar nada
            || = xor (ou lógico)
            && = e
             */

            var message: String

            //var result = MainScope().launch{ testConnection()}
            var result = "Sucesso"

            if (result == "Sucesso") {
                if (cod == 0 || cod == 1) {
                    uploadAE(ctxt)
                    uploadAP(ctxt)
                    uploadRequisicoes(ctxt)
                if (cod == 1) {
                    message = "Sucesso" //Sincronizado com sucesso
                    return message
                }
            }
            if (cod == 0) {
                downloadProdutoExt(ctxt)
                downloadMotivoExt(ctxt)
                downloadProdEstoqueExt(ctxt)
                //downloadRequisicoes(ctxt)


                message = "Sucesso" //Sincronizado com sucesso
                return message
            }
            else {
                //TODO
            }
        } else {
            message = "Falha"
            return message
        } as? String
        message = "Falha"
        return message


    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }



}
