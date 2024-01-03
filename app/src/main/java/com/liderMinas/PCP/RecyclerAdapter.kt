package com.liderMinas.PCP


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.Color.WHITE
import android.graphics.Color.parseColor
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonDisposableHandle.parent
import java.lang.Float.parseFloat

//import com.google.android.material.internal.ContextUtils.getActivity
//import com.kingdom.controledeestoque.database.getNotificacao

public class RecyclerAdapter(cursorE: Cursor?, context: Context): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var cursor = cursorE
    var ctxt = context
    var db = SQLiteHelper(ctxt)
    @OptIn(InternalCoroutinesApi::class)


    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val id = itemView.findViewById<TextView>(R.id.ID)
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

            Log.d("id","${id}")
            Log.d("cod","${cod}")
            Log.d("qtdReq","${qtdReq}")
            Log.d("qtdAtend","${qtdAtend}")

            var dateFormatted = "${dty.toString().substring(8,10)}:${dty.toString().substring(11,13)} ${dty.toString().substring(6,8)}/${dty.toString().substring(4,6)}/${dty.toString().substring(0,4)}"

            var descprod= cod?.let { db.getDescProdutosEst(it) }
            var prodNome = "${
                descprod?.getString(1)?.substring(18)}"
            Log.d("descprod","${descprod}")
            Log.d("prodNome","${prodNome}")

            holder.id.text = "$id"
            holder.title.text = "${prodNome}"
            holder.qtdReq.text = "Qtd solicitada: ${qtdReq.toString()}"
            holder.qtdReq.hint = "${qtdReq.toString()}"
            if (qtdAtend !=null) {
                holder.qtdAtend.text = "Qtd enviada: ${qtdAtend?.toString() ?: ""}"
            } else {
                holder.qtdAtend.text = "Qtd enviada: Aguardando resposta"
            }
            holder.userReq.text = "Solicitado por ${userReq.toString()}"
            if (userAtend !=null) {
                holder.userAtend.text = "Respondido por: ${userAtend?.toString() ?: ""}"
            } else {
                holder.userAtend.text = "Respondido por: Aguardando resposta"
            }
            //holder.userAtend.text = "Respondido por: ${userAtend?.toString() ?: ""}"//if (userAtend) {userAtend.toString()} else {""}
            holder.dty.text = dateFormatted


            /*if (lido == "N") {
                holder.read.visibility = View.VISIBLE
                holder.card.setCardBackgroundColor(parseColor("#E1F3FF"))
            } else {
                holder.read.visibility = View.INVISIBLE
                holder.card.setCardBackgroundColor(WHITE)
            }*/


            holder.card.setOnClickListener {


                //SQLiteHelper(ctxt).setNotificationRead(id)
                //holder.read.visibility = View.INVISIBLE
                //holder.card.setCardBackgroundColor(WHITE)

                var dialogBuilder = MaterialAlertDialogBuilder(ctxt)
                    .setTitle("Solicitação $id")
                    .setView(R.layout.alertdialog_requisicao_step2)
                    //.setMessage(msg)
                    //.setMessage("Digite a quantidade recebida e clique em Salvar")
                    .setPositiveButton("Salvar") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()

                var prodName =
                    dialogBuilder.findViewById<TextInputEditText>(R.id.editTextProdutoName)
                        ?.setText("${holder.title.text}")
                Log.d("holder.title.text","${holder.title.text}")
                dialogBuilder.findViewById<TextInputEditText>(R.id.editTextProdutoName)
                    ?.setHint("Produto")
                dialogBuilder.findViewById<TextInputEditText>(R.id.editTextProdutoName)?.isEnabled =
                    false

                Log.d("holder.qtdAtend.hint","${holder.qtdAtend.hint}")

                var qtd = dialogBuilder.findViewById<TextInputEditText>(R.id.qtd)
                qtd?.setText("${holder.qtdAtend.hint}")

                var txt = "null"
                    if (qtd?.text == null || holder.qtdAtend.hint == "null" || holder.qtdAtend.hint == null) {
                        var soma1 =
                            dialogBuilder.findViewById<MaterialButton>(R.id.soma1)?.setOnClickListener {
                                var qtdS = parseFloat(qtd?.text.toString())
                                qtdS += 1
                                qtd?.setText("$qtdS")
                            }
                        var subt1 =
                            dialogBuilder.findViewById<MaterialButton>(R.id.subt1)?.setOnClickListener {
                                var qtdS = parseFloat(qtd?.text.toString())
                                qtdS -= 1
                                qtd?.setText("$qtdS")
                            }
                        dialogBuilder.findViewById<TextView>(R.id.nomePasso)?.setText("Aguarde pelo envio da resposta antes de continuar.")
                        //dialogBuilder.setMessage("Aguarde pelo envio da resposta antes de continuar.")
                        dialogBuilder.findViewById<MaterialButton>(R.id.subt1)?.visibility = GONE
                        dialogBuilder.findViewById<MaterialButton>(R.id.soma1)?.visibility = GONE
                        dialogBuilder.findViewById<TextInputEditText>(R.id.qtd)?.visibility = GONE
                    } else {
                        var soma1 =
                            dialogBuilder.findViewById<MaterialButton>(R.id.soma1)?.setOnClickListener {
                                var qtdS = parseFloat(qtd.text.toString())
                                qtdS += 1
                                qtd.setText("$qtdS")
                            }
                        var subt1 =
                            dialogBuilder.findViewById<MaterialButton>(R.id.subt1)?.setOnClickListener {
                                var qtdS = parseFloat(qtd.text.toString())
                                qtdS -= 1
                                qtd.setText("$qtdS")
                            }
                        dialogBuilder.findViewById<TextView>(R.id.nomePasso)?.setText("Digite a quantidade recebida e clique em Salvar")
                        dialogBuilder.findViewById<MaterialButton>(R.id.subt1)?.visibility = VISIBLE
                        dialogBuilder.findViewById<MaterialButton>(R.id.soma1)?.visibility = VISIBLE
                        dialogBuilder.findViewById<TextInputEditText>(R.id.qtd)?.visibility = VISIBLE
                    }

            }




                //if (user != null) {

                    //Notificacoes(user, ctxt).updateBadge()
                //}

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