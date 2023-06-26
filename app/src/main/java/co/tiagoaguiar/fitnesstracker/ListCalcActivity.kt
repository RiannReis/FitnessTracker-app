package co.tiagoaguiar.fitnesstracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class ListCalcActivity : AppCompatActivity(), OnListClickListener{

    private lateinit var adapter: ListCalcAdapter
    private lateinit var listCalcItems: MutableList<Calc>


    private lateinit var rvListCalc: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        listCalcItems = mutableListOf<Calc>()
        adapter = ListCalcAdapter(listCalcItems, this)

        rvListCalc = findViewById(R.id.rv_list_calc)
        rvListCalc.layoutManager = LinearLayoutManager(this)
        rvListCalc.adapter = adapter


        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")

        Thread {
            val app = application as App
            val dao = app.db.calcDao()
            val register = dao.getRegisterByType(type)

            runOnUiThread {
                listCalcItems.addAll(register)
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    override fun onClick(id: Int, type: String){
        when (type){
            "imc" -> { val i = Intent(this, ImcActivity::class.java)
                i.putExtra("updateId", id)
                startActivity(i)
            }
            "tmb" -> { val i = Intent(this, TmbActivity::class.java)
                i.putExtra("updateId", id)
                startActivity(i)
            }
        }
        finish()
    }

    override fun onLongClick(position: Int, calc: Calc) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete_message))
            .setNegativeButton(android.R.string.cancel) {
                dialog, wich ->
                Thread {
                    val app = application as App
                    val dao = app.db.calcDao()

                    val response = dao.delete(calc)

                    if (response > 0){
                        runOnUiThread {
                            listCalcItems.removeAt(position)
                            adapter.notifyItemRemoved(position)
                        }
                    }
                }.start()

            }
            .create()
            .show()
    }

    private inner class ListCalcAdapter(private val listCalc: List<Calc>,
    private val listener: OnListClickListener) :
        RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcViewHolder {
            val view = layoutInflater.inflate(R.layout.list_register_item, parent, false)
            return ListCalcViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListCalcViewHolder, position: Int) {
            val itemCurrent = listCalc[position]
            holder.bind(itemCurrent)
        }

        override fun getItemCount(): Int {
            return listCalc.size
        }

        private inner class ListCalcViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(item: Calc) {
                val result: TextView = itemView.findViewById(R.id.txt_item_list)

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))

                val dataFromTheMoment = sdf.format(item.createdDate)
                val imcValue = item.res


                result.text = getString(R.string.list_response_imc, imcValue, dataFromTheMoment)

                result.setOnLongClickListener {
                    listener.onLongClick(adapterPosition, item)
                    true
                }

                result.setOnClickListener{
                    listener.onClick(item.id, item.type)
                }
            }
        }
    }

}