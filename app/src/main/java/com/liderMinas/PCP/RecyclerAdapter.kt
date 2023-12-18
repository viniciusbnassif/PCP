package com.liderMinas.PCP


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.Color.WHITE
import android.graphics.Color.parseColor
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.android.material.internal.ContextUtils.getActivity
//import com.kingdom.controledeestoque.database.getNotificacao

public class RecyclerAdapter(cursorE: Cursor?, context: Context): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var cursor = cursorE
    var ctxt = context
    var db = SQLiteHelper(ctxt)

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.titleID)
        val qtdReq = itemView.findViewById<TextView>(R.id.qtdReq)
        val qtdAtend = itemView.findViewById<TextView>(R.id.qtdAtend)
        val userReq = itemView.findViewById<TextView>(R.id.userReq)
        val userAtend = itemView.findViewById<TextView>(R.id.userAtend)
        val dty = itemView.findViewById<TextView>(R.id.hora)
        val card = itemView.findViewById<CardView>(R.id.card)
        val read = itemView.findViewById<View>(R.id.read)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_requisicao, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (cursor != null) {
            cursor!!.moveToPosition(position)
            //var dados = cursor?.getString(3)?.split("|")?.toTypedArray()
            //var dados = ArrayList<String>()
            //while (cursor!!.moveToNext()) {
                //dados.add(getStringFromCursor(cursor))
            //}
            //var msg = cursor?.getString(1)
            //var lido = cursor?.getString(5)
            //var user = cursor?.getString(4)
            var id = cursor?.getInt(0)
            var cod = cursor?.getString(1)
            var qtdReq = cursor?.getFloat(2)
            var qtdAtend = cursor?.getFloatOrNull(3)
            var userReq = cursor?.getString(5)
            var userAtend = cursor?.getStringOrNull(6)
            var dty = cursor?.getString(8)

            var dateFormatted = "${dty.toString().substring(8,10)}:${dty.toString().substring(11,13)} ${dty.toString().substring(6,8)}/${dty.toString().substring(4,6)}/${dty.toString().substring(0,4)}"

            var descprod= id?.let { db.getDescProdutosEst(it) }
            var prodNome = "${
                descprod?.getString(1)?.substring(18)}"

            holder.title.text = "${prodNome}"
            holder.qtdReq.text = "Qtd solicitada: ${qtdReq.toString()}"
            holder.qtdAtend.text = "Qtd enviada: ${qtdAtend?.toString() ?: ""}"
            holder.userReq.text = "Solicitado por ${userReq.toString()}"
            holder.userAtend.text = "Respondido por: ${userAtend?.toString() ?: ""}"//if (userAtend) {userAtend.toString()} else {""}
            holder.dty.text = dateFormatted


            /*if (lido == "N") {
                holder.read.visibility = View.VISIBLE
                holder.card.setCardBackgroundColor(parseColor("#E1F3FF"))
            } else {
                holder.read.visibility = View.INVISIBLE
                holder.card.setCardBackgroundColor(WHITE)
            }*/

            holder.card.setOnClickListener{
                //SQLiteHelper(ctxt).setNotificationRead(id)
                //holder.read.visibility = View.INVISIBLE
                holder.card.setCardBackgroundColor(WHITE)
                MaterialAlertDialogBuilder(ctxt)
                    .setTitle("Mensagem detalhada")
                    //.setMessage(msg)
                    .setPositiveButton("Fechar") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()

                //if (user != null) {

                    //Notificacoes(user, ctxt).updateBadge()
                //}
            }
            //cursor!!.moveToNext()
        }

    }

    override fun getItemCount(): Int {
        if (cursor != null){
            var rtn = cursor!!.count
            return rtn
        }
        return 0


    }

}