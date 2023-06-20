package co.tiagoaguiar.fitnesstracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ImcActivity : AppCompatActivity() {

    private lateinit var editWeigth: EditText
    private lateinit var editHeigth: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        editWeigth = findViewById(R.id.edit_imc_weigth)
        editHeigth = findViewById(R.id.edit_imc_heigth)

        val btnSend: Button = findViewById(R.id.btn_imc_send)

        btnSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weigth = editWeigth.text.toString().toInt()
            val heigth = editHeigth.text.toString().toInt()

            val result = calculateImc(weigth, heigth)

            val imcResponseId = imcResponse(result)

            val dialog = AlertDialog.Builder(this)

                //OP 01: a função e dialog exposto
//            dialog.setTitle(getString(R.string.imc_response, result))
//            dialog.setMessage(imcResponseId)

//            dialog.setPositiveButton(android.R.string.ok, object: DialogInterface.OnClickListener{
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//
//                }
//            dialog.create()
//            dialog .show()


                //OP 02: usando lambda e dialog oculto
                .setTitle(getString(R.string.imc_response, result))
                .setMessage(imcResponseId)
            dialog.setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->

            }
                .create()
                .show()


        }
    }

    @StringRes
    private fun imcResponse(imc: Double): Int {
        //OP 03 -> usar return direto no when
        return when {
            imc < 15.0 -> R.string.imc_severely_low_weight
            imc < 16.0 -> R.string.imc_very_low_weight
            imc < 18.0 -> R.string.imc_low_weight
            imc < 25.0 -> R.string.normal
            imc < 30.0 -> R.string.imc_high_weight
            imc < 35.0 -> R.string.imc_so_high_weight
            imc < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }

        //OP 01 -> if / else
//        if (imc < 15.0){
//            return R.string.imc_severely_low_weight
//        }else if (imc < 16.0){
//            return R.string.imc_very_low_weight
//        }else if (imc < 18.5){
//            return  R.string.imc_low_weight
//        }else if (imc < 25.0){
//            return R.string.normal
//        }else if (imc < 30.0){
//            return R.string.imc_high_weight
//        }else if (imc < 35.0){
//            return R.string.imc_so_high_weight
//        }else if (imc < 40.0) {
//            return R.string.imc_severely_high_weight
//        }else{
//            return R.string.imc_extreme_weight
//        }

        //OP 02 -> when
//        when{
//            imc < 15.0 -> return R.string.imc_severely_low_weight
//            imc < 16.0 -> return R.string.imc_very_low_weight
//            imc < 18.0 -> return R.string.imc_low_weight
//            imc < 25.0 -> return R.string.normal
//            imc < 30.0 -> return R.string.imc_high_weight
//            imc < 35.0 -> return R.string.imc_so_high_weight
//            imc < 40.0 -> return R.string.imc_severely_high_weight
//            else -> return R.string.imc_extreme_weight
//        }


    }

    private fun calculateImc(weigth: Int, heigth: Int): Double {
        return weigth / ((heigth / 100.0) * (heigth / 100.0))
    }

    private fun validate(): Boolean {
        //OP 03 -> retorna direto ou true ou false
        return (editWeigth.text.toString().isNotEmpty() &&
                editHeigth.text.toString().isNotEmpty() &&
                !editWeigth.text.toString().startsWith("0") &&
                !editHeigth.text.toString().startsWith("0"))

        //OP 01 -> usar if / else
//        if (editWeigth.text.toString().isNotEmpty() &&
//            editHeigth.text.toString().isNotEmpty() &&
//            editWeigth.text.toString().startsWith("0") &&
//            editHeigth.text.toString().startsWith("0")) {
//            return true
//        } else {
//            return false
//        }

        //OP 02 -> usar return pra simular if / else
//        if (editWeigth.text.toString().isNotEmpty() &&
//            editHeigth.text.toString().isNotEmpty() &&
//            editWeigth.text.toString().startsWith("0") &&
//            editHeigth.text.toString().startsWith("0")) {
//            return true
//        }
//        return false

    }
}