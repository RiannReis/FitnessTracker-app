package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.fitnesstracker.model.Calc

class PercentActivity : AppCompatActivity() {

    private lateinit var editAge: EditText
    private lateinit var editImc: EditText
    private lateinit var editRadioGroup: RadioGroup
    private lateinit var editMan: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_percent)

        editAge = findViewById(R.id.edit_perc_age)
        editRadioGroup = findViewById(R.id.radio_group_perc)
        editImc = findViewById(R.id.edit_perc_imc)
        editMan = findViewById(R.id.radio_btn_man_perc)

        val btnSend: Button = findViewById(R.id.perc_send)

        btnSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val imc = editImc.text.toString().toDouble()
            val age = editAge.text.toString().toInt()

            val result = if (editMan.isChecked){
                calculatePercFatMale(imc, age)
            } else {
                calculatePercFatFemale(imc, age)
            }

            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.perc_answer, result))
                setPositiveButton(android.R.string.ok) { dialog, which ->

                }
                setNegativeButton(R.string.save) { dialog, wich ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()

                        val updateId = intent.extras?.getInt("updateId")

                        if (updateId != null) {
                            dao.update(Calc(id = updateId, type = "%gor", res = result))
                        } else {
                            dao.insert(Calc(type = "%gor", res = result))
                        }

                        runOnUiThread {
                            openListActivity()
                            Toast.makeText(this@PercentActivity, R.string.update, Toast.LENGTH_SHORT).show()
                        }
                    }.start()
                }
            }.create().show()

            val service = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
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
        i.putExtra("type", "%gor")
        startActivity(i)
    }

    private fun calculatePercFatMale(imc: Double, age: Int): Double{
        return (1.20 * imc) + (0.23 * age) - (10.8 * 1) - 5.4
    }

    private fun calculatePercFatFemale(imc: Double, age: Int): Double{
        return (1.20 * imc) + (0.23 * age) - (10.8 * 0) - 5.4
    }

    private fun validate(): Boolean {
        return (editAge.text.toString().isNotEmpty() &&
                editImc.text.toString().isNotEmpty() &&
                editRadioGroup.checkedRadioButtonId != -1 &&
                !editAge.text.toString().startsWith("0") &&
                !editImc.text.toString().startsWith("0"))

    }

}