package co.tiagoaguiar.fitnesstracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.fitnesstracker.model.Calc

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

            AlertDialog.Builder(this)

                .setTitle(getString(R.string.imc_response, result))
                .setMessage(imcResponseId)
                .setPositiveButton(android.R.string.ok) { dialog, which ->

                }
                .setNegativeButton(R.string.save) { dialog, wich ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()

                        val updateId = intent.extras?.getInt("updateId")

                        if (updateId != null) {
                            dao.update(Calc(id = updateId, type = "imc", res = result))
                        } else {
                            dao.insert(Calc(type = "imc", res = result))
                        }

                        runOnUiThread {
                            openListActivity()
                            Toast.makeText(this, R.string.update, Toast.LENGTH_SHORT).show()
                        }
                    }.start()
                }
                .create()
                .show()

            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)


        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            finish()
            openListActivity()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity() {
        val i = Intent(this, ListCalcActivity::class.java)
        i.putExtra("type", "imc")
        startActivity(i)
    }

    @StringRes
    private fun imcResponse(imc: Double): Int {
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


    }

    private fun calculateImc(weigth: Int, heigth: Int): Double {
        return weigth / ((heigth / 100.0) * (heigth / 100.0))
    }

    private fun validate(): Boolean {
        return (editWeigth.text.toString().isNotEmpty() &&
                editHeigth.text.toString().isNotEmpty() &&
                !editWeigth.text.toString().startsWith("0") &&
                !editHeigth.text.toString().startsWith("0"))

    }
}