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

class CfmActivity : AppCompatActivity() {

    private lateinit var editAge: EditText
    private lateinit var editRadioGroup: RadioGroup
    private lateinit var editMan: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cfm)

        editAge = findViewById(R.id.edit_fcm_age)
        editRadioGroup = findViewById(R.id.radio_group)
        editMan = findViewById(R.id.radio_btn_man)

        val btnSend: Button = findViewById(R.id.fcm_send)

        val btnTips: Button = findViewById(R.id.btn_fcm_tips)

        btnTips.setOnClickListener {
            val i = Intent(this, CfmDescActivity::class.java)
            startActivity(i)
        }


        btnSend.setOnClickListener {
            if (!validate()) {
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = editAge.text.toString().toInt()

            val result = if (editMan.isChecked) 220.0 - age else 226.0 - age

            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.cfm_answer, result))
                setPositiveButton(android.R.string.ok) { _, _ ->

                }
                setNegativeButton(R.string.save) { _, _ ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()

                        val updateId = intent.extras?.getInt("updateId")

                        if (updateId != null) {
                            dao.update(Calc(id = updateId, type = "fcm", res = result))
                        } else {
                            dao.insert(Calc(type = "fcm", res = result))
                        }

                        runOnUiThread {
                            openListActivity()
                            Toast.makeText(this@CfmActivity, R.string.update, Toast.LENGTH_SHORT)
                                .show()
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
        i.putExtra("type", "fcm")
        startActivity(i)
    }

    private fun validate(): Boolean {
        return (editAge.text.toString().isNotEmpty() &&
                editRadioGroup.checkedRadioButtonId != -1 &&
                !editAge.text.toString().startsWith("0"))

    }
}