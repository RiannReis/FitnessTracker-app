package co.tiagoaguiar.fitnesstracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.ic_baseline_calculate_24,
                textStringId = R.string.label_imc
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.ic_baseline_run_circle_24,
                textStringId = R.string.label_tmb
            )
        )
        mainItems.add(
            MainItem(
                id = 3,
                drawableId = R.drawable.ic_baseline_favorite_24,
                textStringId = R.string.label_fcm
            )
        )
        mainItems.add(
            MainItem(
                id = 4,
                drawableId = R.drawable.ic_baseline_accessibility_24,
                textStringId = R.string.label_fat_perc
            )
        )

        val adapter = MainAdapter(mainItems) { id ->

            when (id) {
                1 -> {
                    openActivity(this@MainActivity, ImcActivity::class.java)
                }
                2 -> {
                    openActivity(this@MainActivity, TmbActivity::class.java)
                }
                3 -> {
                    openActivity(this@MainActivity, CfmActivity::class.java)
                }
                4 -> {
                    openActivity(this@MainActivity, PercentActivity::class.java)
                }
            }
        }
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)

    }

    private inner class MainAdapter(
        private val mainItems: List<MainItem>,
        private val onItemClickListener: (Int) -> Unit
    ) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)

        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        private inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val name: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container_imc)

                img.setImageResource(item.drawableId)
                name.setText(item.textStringId)

                container.setOnClickListener {
                    onItemClickListener.invoke(item.id)
                }
            }

        }

    }

    private fun <T> openActivity(packageContext: Context, cls: Class<T>) {
        val i = Intent(packageContext, cls)
        startActivity(i)
    }

}