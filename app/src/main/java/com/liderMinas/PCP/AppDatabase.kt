package com.liderMinas.PCP


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
/*

class DBHelper(context: Context){
    SQLiteOpenHelper(context, OfflineSessionDB, null, DATABASE_VERSION)
    {
        companion object {
            private val DATABASE_VERSION = 1
            private val DATABASE_NAME = "OfflineSessionDB"

        }


        // below is the method for creating a database by a sqlite query
        override fun onCreate(db: SQLiteDatabase?) {
            // below is a sqlite query, where column names
            // along with their data types is given
            val query = ("CREATE TABLE " + Login + " ("
                    + idLogin + " INTEGER PRIMARY KEY, " +
                    usernameLogin + " TEXT, " +
                    passwordLogin + " TEXT" + ")")

            db?.execSQL(query)
            // we are calling sqlite
            // method for executing our query


        }
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            db!!.execSQL("DROP TABLE IF EXISTS " + )
            onCreate(db)
        }



        //Confirmação de dados de login
class databaseLogin(val login: String, val password: String){
    val query = ("Select username AND password FROM login WHERE username = $login AND password = $password")

    db.execSQL(query)


}


 */
