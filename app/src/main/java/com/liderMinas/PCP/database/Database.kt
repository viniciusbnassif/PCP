package com.liderMinas.PCP

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.liderMinas.PCP.database.ProdutoModelo


/*import android.database.sqlite.SQLiteQuery*/


class SQLiteHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
    {
        companion object {

            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "pcp.db"
            private const val TBL_USUARIO = "usuario"
            private const val ID_USUARIO = "idUsuario"
            private const val USERNAME = "username"
            private const val PASSWORD = "password"
            private const val NOME = "nome"

            /*========================================================================*/

            private const val TBL_PRODUTO = "produto"
            private const val ID_PRODUTO = "idProduto"
            private const val DESC_PROD = "descProduto"
            private const val QE_PROD = "qeProduto"
            private const val VALID_PROD = "validProduto"
            private const val TIPOV_PROD = "tipoVProduto"

            /*========================================================================*/

            private const val TBL_MOTIVO = "motivo"
            private const val ID_MOTIVO = "idMotivo"
            private const val DESC_MOTIVO = "descMotivo"

            /*========================================================================*/

            private const val TBL_APONTEMBALADO = "apontEmbalado"
            private const val ID_AE = "idApontEmbalado"
            private const val PILHA_AE = "pilhaApontada"
            private const val DATA_AE = "dataHoraApontamento"
            private const val LOTE_AE = "lote"
            private const val CAIXA_AE = "caixaAvulsa"
            private const val UNID_AE = "unidadeAvulsa"
            private const val VALID_AE = "validade"

            /*========================================================================*/

            private const val TBL_APONTPERDA = "apontPerda"
            private const val ID_AP = "idApontPerda"
            private const val QTD_AP = "qtdPerda"
            private const val DATA_AP = "dataHoraPerda"

            /*========================================================================*/

            private const val TBL_SYNC = "sync"
            private const val ID_SYNC = "idSync"
            private const val DATA_SYNC = "dataHoraSync"
            private const val STATUS_SYNC = "statusSync"

            /*========================================================================*/

            val createDBUSUARIO = (
                    "CREATE TABLE " + TBL_USUARIO + " (" +
                    ID_USUARIO + " INTEGER NOT NULL PRIMARY KEY, " +
                    USERNAME + " VARCHAR(64) NOT NULL, " +
                    PASSWORD + " VARCHAR(64) NOT NULL, " +
                    NOME + " VARCHAR(64) NOT NULL " +
                    "); ")
            val createDBMOTIVO=(
                    "CREATE TABLE "+ TBL_MOTIVO + " (" +
                    ID_MOTIVO + " INTEGER NOT NULL PRIMARY KEY," +
                    DESC_MOTIVO + " VARCHAR(128) NOT NULL" +
                    ");")
            val createDBPRODUTO = (
                    "CREATE TABLE "+ TBL_PRODUTO + " (" +
                    ID_PRODUTO + " INTEGER NOT NULL PRIMARY KEY," +
                    DESC_PROD + " VARCHAR(64)NOT NULL," +
                    QE_PROD + " INTEGER NOT NULL," +
                    VALID_PROD + " INTEGER NOT NULL," +
                    TIPOV_PROD + " VARCHAR(1) NOT NULL" +
                    ");" )
            val createDBAE = (
                    "CREATE TABLE "+ TBL_APONTEMBALADO+ " (" +
                    ID_AE + " INTEGER NOT NULL PRIMARY KEY," +
                    PILHA_AE + " INTEGER NOT NULL," +
                    DATA_AE + " VARCHAR(14) NOT NULL," +
                    LOTE_AE + " INTEGER NOT NULL," +
                    CAIXA_AE + " INTEGER NOT NULL," +
                    UNID_AE + " INTEGER NOT NULL," +
                    VALID_AE + " VARCHAR(8) NOT NULL," +
                    ID_PRODUTO + " INTEGER NOT NULL," +
                    QE_PROD + " INTEGER NOT NULL," +
                    VALID_PROD + " VARCHAR(2) NOT NULL," +
                    ID_USUARIO + " INTEGER NOT NULL," +
                    "FOREIGN KEY("+ ID_PRODUTO +") REFERENCES "+TBL_PRODUTO+" ("+ ID_PRODUTO +")," +
                    "FOREIGN KEY("+ QE_PROD +") REFERENCES "+TBL_PRODUTO+" ("+ QE_PROD +")," +
                    "FOREIGN KEY("+ VALID_PROD +") REFERENCES "+TBL_PRODUTO+" ("+ VALID_PROD +")," +
                    "FOREIGN KEY("+ ID_USUARIO +") REFERENCES "+ TBL_USUARIO +" ("+ ID_USUARIO +")" +
                    ");" )
            val createDBAP = (
                    "CREATE TABLE "+ TBL_APONTPERDA +" (" +
                    ID_AP +" INTEGER NOT NULL PRIMARY KEY," +
                    QTD_AP +" FLOAT," +
                    DATA_AP +" VARCHAR(14)," +
                    ID_USUARIO +" INTEGER NOT NULL," +
                    ID_PRODUTO +" INTEGER NOT NULL," +
                    ID_MOTIVO +" INTEGER NOT NULL," +
                    ID_AE +" INTEGER NOT NULL," +
                    "FOREIGN KEY("+ ID_USUARIO +") REFERENCES "+TBL_USUARIO+" ("+ ID_USUARIO +")," +
                    "FOREIGN KEY("+ID_PRODUTO+") REFERENCES "+TBL_PRODUTO+" ("+ID_PRODUTO+")," +
                    "FOREIGN KEY("+ID_MOTIVO+") REFERENCES "+ TBL_MOTIVO+" ("+ID_MOTIVO+")," +
                    "FOREIGN KEY("+ID_AE+") REFERENCES "+ TBL_APONTEMBALADO +" ("+ID_AE+")" +
                    ");" )
            val createDBSYNC = (
                    "CREATE TABLE "+TBL_SYNC+" (" +
                    ID_SYNC +" INTEGER NOT NULL PRIMARY KEY," +
                    DATA_SYNC +" VARCHAR(14)," +
                    STATUS_SYNC +" INTEGER" +
                    ");"+
                    "INSERT INTO "+ TBL_USUARIO + " (username, password, nome) VALUES (guilherme.augusto, 123, Guilherme Augusto);"+
                    "INSERT INTO " + TBL_PRODUTO + " (descPoduto, qeProduto, validProduto, tipoVProduto) VALUES (Pão de teste, 5, 45, M);")

        }

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(createDBUSUARIO)
            db?.execSQL(createDBMOTIVO)
            db?.execSQL(createDBPRODUTO)
            db?.execSQL(createDBAE)
            db?.execSQL(createDBAP)
            db?.execSQL(createDBSYNC)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

            var dropDBPCP = ("DROP TABLE IF EXISTS $TBL_PRODUTO;" +
                             "DROP TABLE IF EXISTS $TBL_APONTPERDA;" +
                             "DROP TABLE IF EXISTS $TBL_APONTEMBALADO;" +
                             "DROP TABLE IF EXISTS $TBL_MOTIVO;" +
                             "DROP TABLE IF EXISTS $TBL_USUARIO;")
            db.execSQL(dropDBPCP)
            onCreate(db)
        }

        fun insertProduto(prd: ProdutoModelo): Long {
            val db= this.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(ID_PRODUTO, prd.idProduto)
            contentValues.put(DESC_PROD, prd.descProduto)
            contentValues.put(QE_PROD, prd.qeProduto)
            contentValues.put(VALID_PROD, prd.validProduto)
            contentValues.put(TIPOV_PROD, prd.tipoVProduto)

            val success = db.insert(TBL_PRODUTO, null,contentValues)
            db.close()
            return success
        }
        @SuppressLint("Range")

        /* Select de todos os produtos*/
        fun getAllProdutos(): ArrayList<ProdutoModelo> {
            val prdList: MutableList<ProdutoModelo> = ArrayList()
            val selectQuery = "SELECT * FROM $TBL_PRODUTO;"
            val db = this.readableDatabase


            val cursor: Cursor?

            try {
                cursor = db.rawQuery(selectQuery, null)
            } catch (e: Exception) {
                e.printStackTrace()
                db.execSQL(selectQuery)
                return ArrayList()
            }

            var idProduto: Int
            var descProduto: String
            var qeProduto: Int
            var validProduto: Int
            var tipoVProduto: String

            if (cursor.moveToFirst()) {
                do {
                    idProduto =     cursor.getInt(cursor.getColumnIndex("idProduct"))
                    descProduto =   cursor.getString(cursor.getColumnIndex("descProduto"))
                    qeProduto =     cursor.getInt(cursor.getColumnIndex("qeProduto"))
                    validProduto =  cursor.getInt(cursor.getColumnIndex("validProduto"))
                    tipoVProduto =  cursor.getString(cursor.getColumnIndex("tipoVProduto"))

                    val prd = ProdutoModelo(idProduto = idProduto, descProduto = descProduto, qeProduto = qeProduto, validProduto = validProduto, tipoVProduto = tipoVProduto)
                    prdList.add(prd)
                }while (cursor.moveToNext())
            }
            return prdList
        }
    }


