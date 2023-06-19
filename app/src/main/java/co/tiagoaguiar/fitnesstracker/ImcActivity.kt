package co.tiagoaguiar.fitnesstracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        }
    }

    private fun validate(): Boolean {

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

        //OP 03 -> retorna direto ou true ou false
        return (editWeigth.text.toString().isNotEmpty() &&
            editHeigth.text.toString().isNotEmpty() &&
            !editWeigth.text.toString().startsWith("0") &&
            !editHeigth.text.toString().startsWith("0"))
    }
}