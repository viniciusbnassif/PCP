package com.liderMinas.PCP

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

//import com.liderMinas.PCP.database.connectMSSQL


/*import android.database.sqlite.SQLiteQuery*/


class SQLiteHelper(context: Context?):

    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    //var dbConnectExt = queryProdutoExt()
    //var connector = connectMSSQL()
    val db = this.writableDatabase
    companion object {

        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "pcp.db"


        
        private const val TBL_USUARIO = "Usuario"
        private const val ID_USUARIO = "idUsuario"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"

        /*========================================================================*/

        const val TBL_PRODUTO = "Produto"
        private const val ID_PRODUTO = "idProduto"
        private const val DESC_PROD = "descProduto"
        private const val QE_PROD = "qeProduto"
        private const val VALID_PROD = "validProduto"
        private const val TIPOV_PROD = "tipoVProduto"

        /*========================================================================*/

        private const val TBL_MOTIVO = "Motivo"
        private const val ID_MOTIVO = "idMotivo"
        private const val DESC_MOTIVO = "descMotivo"

        /*========================================================================*/

        private const val TBL_APONTEMBALADO = "ApontEmbalado"
        private const val ID_AE = "idApontEmbalado"
        private const val QTD_AE = "qtdApontada"
        private const val TIPO_AE = "tipoUnitizador"
        private const val DATA_AE = "dataHoraApontamento"
        private const val LOTE_AE = "lote"
        private const val CAIXA_AE = "caixaAvulsa"
        private const val UNID_AE = "unidadeAvulsa"
        private const val VALID_AE = "validade"
        private const val TOTAL = "total"
        private const val STATUS_SYNC_AE = "statusSync"



        /*========================================================================*/

        private const val TBL_APONTPERDA = "ApontPerda"
        private const val ID_AP = "idApontPerda"
        private const val QTD_AP = "qtdPerda"
        private const val UN_AP = "unidPerda"
        private const val DATA_AP = "dataHoraPerda"
        private const val STATUS_SYNC_AP = "statusSync"

        /*========================================================================*/

        private const val TBL_SYNC = "Sync"
        private const val ID_SYNC = "idSync"
        private const val DATA_SYNC = "dataHoraSync"
        private const val STATUS_SYNC = "statusSync"

        /*========================================================================*/

        private const val TBL_PROD_EST = "ProdutoEstoque"
        private const val ID_PROD_EST = "idProduto"
        private const val COD_PROD_EST = "codProduto"
        private const val DESC_PROD_EST = "descProduto"
        private const val TIPO_PROD_EST = "tipoProduto"
        private const val UNID_PROD_EST = "unidMedida"
        private const val RASTRO_PROD_EST = "rastro"

        /*========================================================================*/

        private const val TBL_REQUISICAO = "Requisicao"
        private const val ID_REQUISICAO = "idRequisicao"
        private const val COD_PROD_REQ = "codProduto"
        private const val QTD_REQ = "qtdRequisicao"
        private const val QTD_ATEND = "qtdAtendida"
        private const val QTD_CONF = "qtdConfirmacao"
        private const val USER_REQ = "userRequisicao"
        private const val USER_ATEND = "userAtendimento"
        private const val USER_CONF = "userConfirmacao"
        private const val DATA_REQ = "dataHoraRequisicao"
        private const val DATA_ATEND = "dataHoraAtendimento"
        private const val DATA_CONF = "dataHoraConfirmacao"
        /*private const val STATUS_SYNC = "statusSync"*/

        /*========================================================================*/

        val createDBUSUARIO = (
                "CREATE TABLE " + TBL_USUARIO + " (" +
                        ID_USUARIO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        USERNAME + " VARCHAR(64) NOT NULL, " +
                        PASSWORD + " VARCHAR(64) NOT NULL " +
                        "); ")
        val createDBMOTIVO = (
                "CREATE TABLE "+ TBL_MOTIVO + " (" +
                        ID_MOTIVO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        DESC_MOTIVO + " VARCHAR(128) NOT NULL" +
                        ");")
        val createDBPRODUTO = (
                "CREATE TABLE "+ TBL_PRODUTO + " (" +
                        ID_PRODUTO + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        DESC_PROD + " VARCHAR(64)NOT NULL," +
                        QE_PROD + " INTEGER NOT NULL," +
                        VALID_PROD + " INTEGER NOT NULL," +
                        TIPOV_PROD + " VARCHAR(1) NOT NULL" +
                        ");" )
        val createDBAE = (
                "CREATE TABLE "+ TBL_APONTEMBALADO+ " (" +
                        ID_AE + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        QTD_AE + " INTEGER NOT NULL," +
                        TIPO_AE + " VARCHAR(10) NOT NULL," +
                        DATA_AE + " VARCHAR(13) NOT NULL," +
                        LOTE_AE + " INTEGER NOT NULL," +
                        CAIXA_AE + " INTEGER NOT NULL," +
                        UNID_AE + " INTEGER NOT NULL," +
                        VALID_AE + " VARCHAR(8) NOT NULL," +
                        TOTAL + " INTEGER NOT NULL," +
                        STATUS_SYNC_AE + " INTEGER NOT NULL," +
                        ID_PRODUTO + " INTEGER NOT NULL," +
                        QE_PROD + " INTEGER NOT NULL," +
                        VALID_PROD + " INTEGER NOT NULL," +
                        TIPOV_PROD + " VARCHAR(1) NOT NULL," +
                        USERNAME + " VARCHAR(64) NOT NULL," +
                        "FOREIGN KEY("+ ID_PRODUTO +") REFERENCES "+TBL_PRODUTO+" ("+ ID_PRODUTO +")," +
                        "FOREIGN KEY("+ QE_PROD +") REFERENCES "+TBL_PRODUTO+" ("+ QE_PROD +")," +
                        "FOREIGN KEY("+ VALID_PROD +") REFERENCES "+TBL_PRODUTO+" ("+ VALID_PROD +")," +
                        "FOREIGN KEY("+ TIPOV_PROD +") REFERENCES "+TBL_PRODUTO+" ("+ TIPOV_PROD +")," +
                        "FOREIGN KEY("+ USERNAME +") REFERENCES "+ TBL_USUARIO +" ("+ USERNAME +")" +
                        ");" )
        val createDBAP = (
                "CREATE TABLE "+ TBL_APONTPERDA +" (" +
                        ID_AP +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        QTD_AP +" FLOAT, " +
                        UN_AP + " VARCHAR(2), " +
                        DATA_AP +" VARCHAR(13), " +
                        USERNAME +" VARCHAR(64) NOT NULL, " +
                        ID_PRODUTO +" INTEGER NOT NULL, " +
                        ID_MOTIVO +" INTEGER NOT NULL, " +
                        STATUS_SYNC_AP + " INTEGER NOT NULL, " +
                        "FOREIGN KEY("+ USERNAME +") REFERENCES "+TBL_USUARIO+" ("+ USERNAME +")," +
                        "FOREIGN KEY("+ID_PRODUTO+") REFERENCES "+TBL_PRODUTO+" ("+ID_PRODUTO+")," +
                        "FOREIGN KEY("+ID_MOTIVO+") REFERENCES "+ TBL_MOTIVO+" ("+ID_MOTIVO+")" +
                        ");" )
        val createDBSYNC = (
                "CREATE TABLE "+TBL_SYNC+" (" +
                        ID_SYNC +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        DATA_SYNC +" INTEGER," +
                        STATUS_SYNC +" INTEGER" +
                        ");")

        val createDBPE = (
                "CREATE TABLE "+ TBL_PROD_EST +" (" +
                        ID_PROD_EST +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        COD_PROD_EST +" VARCHAR(15) NOT NULL, " +
                        DESC_PROD_EST + " VARCHAR(64) NOT NULL, " +
                        TIPO_PROD_EST +" VARCHAR(2) NOT NULL, " +
                        UNID_PROD_EST +" VARCHAR(2) NOT NULL, " +
                        RASTRO_PROD_EST +" VARCHAR(1) NOT NULL " +
                        ");" )

        val createDBREQS = (
                "CREATE TABLE "+ TBL_REQUISICAO +" (" +
                        ID_REQUISICAO +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        COD_PROD_REQ +" VARCHAR(15) NOT NULL, " +
                        QTD_REQ +" FLOAT NOT NULL, " +
                        QTD_ATEND + " FLOAT, " +
                        QTD_CONF +" FLOAT, " +
                        USER_REQ +" VARCHAR(64), " +
                        USER_ATEND +" VARCHAR(64), " +
                        USER_CONF +" VARCHAR(64), " +
                        DATA_REQ +" VARCHAR(13) NOT NULL, " +
                        DATA_ATEND +" VARCHAR(13), " +
                        DATA_CONF +" VARCHAR(13), " +
                        STATUS_SYNC +" INTEGER, " +
                        "FOREIGN KEY("+ COD_PROD_EST +") REFERENCES "+ TBL_PROD_EST+" ("+ COD_PROD_EST +")" +
                        ");" )
    }



    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createDBUSUARIO)
        db?.execSQL(createDBMOTIVO)
        db?.execSQL(createDBPRODUTO)
        db?.execSQL(createDBAE)
        db?.execSQL(createDBAP)
        db?.execSQL(createDBSYNC)
        db?.execSQL(createDBPE)
        db?.execSQL(createDBREQS)
        //db?.execSQL("INSERT INTO Usuario (username, password) VALUES ('kane','123'), ('gilberto','12345'), ('zack', 'zsjl');")
        //db?.execSQL("INSERT INTO produto (descProduto, qeProduto, validProduto, tipoVProduto) VALUES ('Selecione o item','', '', '');")
        //db?.execSQL("INSERT INTO produto (descProduto, qeProduto, validProduto, tipoVProduto) VALUES ('Pão 5 15 D','5', '15', 'D'), ('Pão 13 3 M','13', '3', 'M'), ('Pão 1 13 S', '1', '13', 'S');")
        /*db?.execSQL("INSERT INTO Requisicao (codProduto, qtdRequisicao, qtdAtendida, qtdConfirmacao," +
                " userRequisicao, userAtendimento, userConfirmacao, dataHoraRequisicao, dataHoraAtendimento, dataHoraConfirmacao, statusSync) " +
                "VALUES ('101113',1.5, 1.4, 1.3, 'vinicius.nassif', 'guilherme.augusto', 'vinicius.nassif', '2023121022:10', '2023121022:15', '2023121022:19', 0);")
                */



    }




    fun externalExecSQL(query: String){
        val db = this.writableDatabase
        db?.execSQL(query)
    }

    fun externalExecSQLSelect(username: String, pw: String): Boolean {
        var checked = false
        var result = db.query(
            "Usuario",
            arrayOf("username",
                "password",
                /*QE_PROD,
                VALID_PROD,
                TIPOV_PROD*/
            ),
            "username = '$username' AND password = '$pw'"/* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null //DESC_PROD + " ASC" /* ORDER BY clause products will be shown alphabetically a->z*/
        )
        if (result != null && result.count > 0) {
            checked = true
            Log.d("Debug", "Result $result")
            var gg = result
            return checked
        }else{
            return checked
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        var dropDBPCP = ("DROP TABLE IF EXISTS $TBL_USUARIO; " +
                "DROP TABLE IF EXISTS $TBL_MOTIVO; " +
                "DROP TABLE IF EXISTS $TBL_PRODUTO; " +
                "DROP TABLE IF EXISTS $TBL_APONTEMBALADO; " +
                "DROP TABLE IF EXISTS $TBL_APONTPERDA; " +
                "DROP TABLE IF EXISTS $TBL_SYNC; ")
        db.execSQL(dropDBPCP)
        onCreate(db)
    }

    fun getProdutos(): Cursor {
        return db.query(
            TBL_PRODUTO,
            arrayOf("$ID_PRODUTO AS ${BaseColumns._ID}",
                DESC_PROD,
                /*QE_PROD,
                VALID_PROD,
                TIPOV_PROD*/
            ),
            null /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null //DESC_PROD + " ASC" /* ORDER BY clause products will be shown alphabetically a->z*/
        )
    }

    fun getDetailProdutos(idPrd: Int): Cursor {
        return db.query(
            TBL_PRODUTO,
            arrayOf("$ID_PRODUTO AS ${BaseColumns._ID}",
                QE_PROD,
                VALID_PROD,
                TIPOV_PROD,
                DESC_PROD
            ),
            "idProduto = $idPrd" /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null /* ORDER BY clause products will be shown alphabetically a->z*/
        )
    }

    fun getDescProdutos(idPrd: Int): Cursor? {
        var cursor = db.query(
            TBL_PRODUTO,
            arrayOf("$ID_PRODUTO AS ${BaseColumns._ID}",
                DESC_PROD,
            ),
            "idProduto = $idPrd" /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null /* ORDER BY clause products will be shown alphabetically a->z*/
        )

        if (cursor == null || !cursor.moveToFirst()) {
            return null
        }
        return cursor

    }

    fun getDescMotivo(idMtv: Int): Cursor? {
        var cursor = db.query(
            TBL_MOTIVO,
            arrayOf("$ID_MOTIVO AS ${BaseColumns._ID}",
                DESC_MOTIVO
            ),
            "idMotivo = $idMtv" /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null /* ORDER BY clause products will be shown alphabetically a->z*/
        )

        if (cursor == null || !cursor.moveToFirst()) {
            return null
        }
        return cursor

    }


    fun getAE(): Cursor? {
        val selectQuery =
            "SELECT $ID_AE, " +
                    "$QTD_AE, " +
                    "$TIPO_AE, " +
                    "$DATA_AE," +
                    "$LOTE_AE, " +
                    "$CAIXA_AE, " +
                    "$UNID_AE, " +
                    "$VALID_AE, " +
                    "$TOTAL, " +
                    "$ID_PRODUTO, " +
                    "$QE_PROD, " +
                    "$VALID_PROD, " +
                    "$TIPOV_PROD, " +
                    "$USERNAME, " +
                    "$STATUS_SYNC_AE " +
                    "FROM $TBL_APONTEMBALADO WHERE $STATUS_SYNC_AE = 0;"
        val result = db.rawQuery(selectQuery, null)
        return result
    }

    fun getAP(): Cursor? {
        val selectQuery =
            "SELECT $ID_AP, " +
                    "$QTD_AP, " +
                    "$UN_AP, " +
                    "$DATA_AP, " +
                    "$USERNAME, " +
                    "$ID_PRODUTO, " +
                    "$ID_MOTIVO, " +
                    "$STATUS_SYNC_AP " +
                    "FROM $TBL_APONTPERDA WHERE $STATUS_SYNC_AP = 0;"
        val result = db.rawQuery(selectQuery, null)
        return result
    }

    fun getReq(): Cursor? {
        val selectQuery =
            "SELECT $ID_REQUISICAO, " +
                    "$COD_PROD_EST, " +
                    "$QTD_REQ, " +
                    "$QTD_ATEND, " +
                    "$QTD_CONF, " +
                    "$USER_REQ, " +
                    "$USER_ATEND, " +
                    "$USER_CONF, " +
                    "$DATA_REQ, " +
                    "$DATA_ATEND, " +
                    "$DATA_CONF," +
                    "$STATUS_SYNC " +
                    "FROM $TBL_REQUISICAO WHERE $STATUS_SYNC = 0;"
        val result = db.rawQuery(selectQuery, null)
        return result
    }
    fun getUpdReq(): Cursor? {
        val selectQuery =
            "SELECT $ID_REQUISICAO, " +
                    "$COD_PROD_EST, " +
                    "$QTD_REQ, " +
                    "$QTD_ATEND, " +
                    "$QTD_CONF, " +
                    "$USER_REQ, " +
                    "$USER_ATEND, " +
                    "$USER_CONF, " +
                    "$DATA_REQ, " +
                    "$DATA_ATEND, " +
                    "$DATA_CONF, " +
                    "$STATUS_SYNC " +
                    "FROM $TBL_REQUISICAO WHERE $STATUS_SYNC = 2;"
        val result = db.rawQuery(selectQuery, null)
        return result
    }



    fun insertDone(table: String, id: Int?) {
        var query: String
        var idName: String
        if (table == TBL_APONTEMBALADO){
            idName = ID_AE
        }else if (table == TBL_APONTPERDA){
            idName = ID_AP
        }else /*if (table == TBL_REQUISICAO)*/{
            idName = ID_REQUISICAO
        }
        if (id != null){
            query = "UPDATE $table " +
                    "SET statusSync = 1 " +
                    "WHERE $idName = $id"
            db.execSQL(query)
        }
    }

    fun getMotivo(): Cursor {
        return db.query(
            TBL_MOTIVO,
            arrayOf("$ID_MOTIVO AS ${BaseColumns._ID}",
                DESC_MOTIVO
            ),
            null /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null //DESC_PROD + " ASC" /* ORDER BY clause products will be shown alphabetically a->z*/
        )
    }

    fun getProdutosEstoque(): Cursor {
        return db.query(
            TBL_PROD_EST,
            arrayOf("$ID_PROD_EST AS ${BaseColumns._ID}",
                DESC_PROD_EST,
                /*QE_PROD,
                VALID_PROD,
                TIPOV_PROD*/
            ),
            null /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null //DESC_PROD + " ASC" /* ORDER BY clause products will be shown alphabetically a->z*/
        )
    }
    fun arrayIdReqs(): ArrayList<Int>? {
        var cursor = db.query(
            TBL_REQUISICAO,
            arrayOf(
                "$ID_REQUISICAO AS ${BaseColumns._ID}",
            ),
            null /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            ID_REQUISICAO //DESC_PROD + " ASC" /* ORDER BY clause products will be shown alphabetically a->z*/
        )
        if (cursor == null || !cursor.moveToFirst()) {
            return null
        }
        var cursorArray = ArrayList<Int>()
        cursor.moveToFirst()
        cursorArray.add(cursor.getInt(0))
        while (cursor.moveToNext()) {
            cursorArray.add(
                cursor.getInt(0)
            )
        }
        return cursorArray
    }

    fun countReqs(username: String): Int {
        var result = db.query(
            TBL_REQUISICAO,
            arrayOf("$ID_REQUISICAO AS ${BaseColumns._ID}"
            ),
            null ,//"$LIDO = 'N' AND $USERNAME = '$username'" /* WHERE clause less the WHERE keyword, null = no WHERE clause */
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null //DESC_PROD + " ASC" /* ORDER BY clause products will be shown alphabetically a->z*/
        )
        if (result != null && result.count > 0) {
            return result.count
        }else{
            return 0
        }

    }
    fun getInternalRequisicao(): Cursor? {
        var cursor = db.query(
            TBL_REQUISICAO,
            arrayOf(
                "$ID_REQUISICAO AS ${BaseColumns._ID}",
                COD_PROD_REQ,
                QTD_REQ,
                QTD_ATEND,
                QTD_CONF,
                USER_REQ,
                USER_ATEND,
                USER_CONF,
                DATA_REQ,
                DATA_ATEND,
                DATA_CONF,
                STATUS_SYNC
            ),
            USER_CONF + " IS NULL" /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            ID_REQUISICAO + " DESC"//ID_REQUISICAO + " DESC" //DESC_PROD + " ASC" /* ORDER BY clause products will be shown alphabetically a->z*/
        )
        if (cursor == null || !cursor.moveToFirst()) {
            return null
        }
        return cursor
    }

    fun getCodRealProd(id: Int?): Cursor? {
        var cursor = db.query(
            TBL_PROD_EST,
            arrayOf("$ID_PRODUTO AS ${BaseColumns._ID}",
                COD_PROD_EST,
            ),
            "idProduto = $id" /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null /* ORDER BY clause products will be shown alphabetically a->z*/
        )

        if (cursor == null || !cursor.moveToFirst()) {
            return null
        }
        return cursor

    }
    fun getDescProdutosEst(idPrd: String): Cursor? {
        var cursor = db.query(
            TBL_PROD_EST,
            arrayOf("$ID_PROD_EST AS ${BaseColumns._ID}",
                DESC_PROD_EST,
                COD_PROD_EST
            ),
            "codProduto = '$idPrd'" /* WHERE clause less the WHERE keyword, null = no WHERE clause */,
            null /* arguments to replace ? place holder in the WHERE clause, null if none */,
            null /* GROUP BY clause, null if no GROUP BY clause */,
            null /* HAVING CLAUSE, null if no HAVING clause */,
            null /* ORDER BY clause products will be shown alphabetically a->z*/
        )

        if (cursor == null || !cursor.moveToFirst()) {
            return null
        }
        return cursor

    }
}


