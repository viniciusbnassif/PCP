package com.liderMinas.PCP.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.Data

class MyHelper(context: Context): SQLiteOpenHelper(context, "PCP",null, 1) {

    val db = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE usuario(idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        "username TEXT NOT NULL,"+
                        "password TEXT NOT NULL," +
                        "nome TEXT NOT NULL);")
        db?.execSQL("CREATE TABLE motivo(" +
                "idMotivo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "descMotivo TEXT NOT NULL" +
                ");")
        db?.execSQL("CREATE TABLE produto(" +
                "idProduto INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "descProduto TEXT NOT NULL," +
                "qeProduto INTEGER NOT NULL," +
                "validProduto INTEGER NOT NULL," +
                "tipoVProduto TEXT NOT NULL" +
                ");")
        db?.execSQL("CREATE TABLE apontEmbalado(" +
                "idApontEmbalado INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "pilhaApontada INTEGER NOT NULL," +
                "dataHoraApontamento TEXT NOT NULL," +
                "lote INTEGER NOT NULL," +
                "caixaAvulsa INTEGER NOT NULL," +
                "unidadeAvulsa INTEGER NOT NULL," +
                "validade TEXT NOT NULL," +
                "idProduto INTEGER NOT NULL," +
                "qeProduto INTEGER NOT NULL," +
                "validProduto TEXT NOT NULL," +
                "idUsuario INTEGER NOT NULL," +
                "FOREIGN KEY(idProduto) REFERENCES produto(idProduto)," +
                "FOREIGN KEY(qeProduto) REFERENCES produto(qeProduto)," +
                "FOREIGN KEY(validProduto) REFERENCES produto(validProduto)," +
                "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario)" +
                ");")
        db?.execSQL("CREATE TABLE apontPerda(" +
                "idApontPerda INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "qtdPerda FLOAT," +
                "dataHoraPerda TEXT," +
                "idUsuario INTEGER NOT NULL," +
                "idProduto INTEGER NOT NULL," +
                "idMotivo INTEGER NOT NULL," +
                "idApontEmbalado INTEGER NOT NULL," +
                "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario)," +
                "FOREIGN KEY(idProduto) REFERENCES produto(idProduto)," +
                "FOREIGN KEY(idMotivo) REFERENCES motivo(idMotivo)," +
                "FOREIGN KEY(idApontEmbalado) REFERENCES apontEmbalado(idApontEmbalado)" +
                ");")
        db?.execSQL("CREATE TABLE sync(" +
                "idSync INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "dataHoraSync TEXT," +
                "statusSync INTEGER" +
                ");")



        /*===========================================*/

        db?.execSQL("INSERT INTO usuario (username, password, nome) VALUES ('kane','123', 'Kane Garcia'), ('gilberto','12345', 'Gilberto Gonçalves'), ('zack', 'zsjl', 'Zachary Snyder');")
        db?.execSQL("INSERT INTO produto (descProduto, qeProduto, validProduto, tipoVProduto) VALUES ('Pão Pão','5', '15', 'D'), ('Pãozinho bonitinho','13', '3', 'M'), ('Cara de Broa', '1', '13', 'S');")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    fun readIdAndProduto(): MutableList<A>
}