package co.tiagoaguiar.fitnesstracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.fitnesstracker.model.Calc

class TmbActivity : AppCompatActivity() {

    private lateinit var lifeStyle: AutoCompleteTextView

    private lateinit var editWeigth: EditText
    private lateinit var editHeigth: EditText
    private lateinit var editAge: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)

        editWeigth = findViewById(R.id.edit_tmb_weigth)
        editHeigth = findViewById(R.id.edit_tmb_heigth)
        editAge = findViewById(R.id.edit_tmb_age)

        lifeStyle = findViewById(R.id.auto_life_style)
        val items = resources.getStringArray(R.array.tmb_life_style)
        lifeStyle.setText(items.first())
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lifeStyle.setAdapter(adapter)

        val btnSend: Button = findViewById(R.id.btn_tmb_send)
        btnSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weigth = editWeigth.text.toString().toInt()
            val heigth = editHeigth.text.toString().toInt()
            val age = editAge.text.toString().toInt()

            val result = calculateTmb(weigth, heigth, age)
            val tmbResponseId = tmbResponse(result)

            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.tmb_response, tmbResponseId))
                setPositiveButton(android.R.string.ok) { _, _ ->

                }
                setNegativeButton(R.string.save) { _, _ ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()

                        val updateId = intent.extras?.getInt("updateId")

                        if (updateId != null) {
                            dao.update(Calc(id = updateId, type = "tmb", res = tmbResponseId))
                        } else {
                            dao.insert(Calc(type = "tmb", res = tmbResponseId))
                        }

                        runOnUiThread {
                            openListActivity()
                            Toast.makeText(this@TmbActivity, R.string.update, Toast.LENGTH_SHORT).show()
                        }
                    }.start()
                }
                create()
                show()
            }

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
        i.putExtra("type", "tmb")
        startActivity(i)
    }

    private fun tmbResponse(tmb: Double): Double {
        val items = resources.getStringArray(R.array.tmb_life_style)
        return when {
            lifeStyle.text.toString() == items[0] -> tmb * 1.2
            lifeStyle.text.toString() == items[1] -> tmb * 1.375
            lifeStyle.text.toString() == items[2] -> tmb * 1.55
            lifeStyle.text.toString() == items[3] -> tmb * 1.725
            lifeStyle.text.toString() == items[4] -> tmb * 1.9
            else -> 0.0
        }
    }

    private fun calculateTmb(weigth: Int, heigth: Int, age: Int): Double {
        return 66 + (13.8 * weigth) + (5 * heigth) - (6.8 * age)
    }

    private fun validate(): Boolean {
        return (editWeigth.text.toString().isNotEmpty() &&
                editHeigth.text.toString().isNotEmpty() &&
                editAge.text.toString().isNotEmpty() &&
                !editWeigth.text.toString().startsWith("0") &&
                !editAge.text.toString().startsWith("0") &&
                !editHeigth.text.toString().startsWith("0"))

    }
}