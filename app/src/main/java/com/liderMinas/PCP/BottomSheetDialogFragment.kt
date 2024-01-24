import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.liderMinas.PCP.MainActivity
import com.liderMinas.PCP.R

class BottomSheetFragment : BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.bottom_sheet, container, false).apply {


        var ctxt = activity?.applicationContext

        var guia = findViewById<MaterialButton>(R.id.btnGuia)
        guia.setOnClickListener{
            TODO()
        }
        var sair = findViewById<MaterialButton>(R.id.btnSair)
        sair.setOnClickListener {
            var intent = Intent(ctxt, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


}