package com.liderMinas.PCP.database

import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import com.google.api.Context
import com.liderMinas.PCP.SQLiteHelper
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