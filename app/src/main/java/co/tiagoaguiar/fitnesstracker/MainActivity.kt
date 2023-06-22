package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), OnItemClickListener {

    //    private lateinit var btnImc: LinearLayout
    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.ic_baseline_calculate_24,
                textStringId = R.string.label_imc,
                color = Color.GRAY
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.ic_baseline_run_circle_24,
                textStringId = R.string.label_tmb,
                color = Color.LTGRAY
            )
        )


        //1 layout XML
        //2 onde a RecyclerView vai aparecer (tela principal)
        // 3 logica: conectar o XML da celula dentro do RecyclerView + sua qtd de elementos dinamicos
        val adapter = MainAdapter(mainItems, this)
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)


        //parte logica precisa de classe para administrar a RecyclerView e suas células (layots de itens)
        //Adapter: é a classe que é responsável por nos permitir criar layouts especificos de forma dinamica
        //O SDK não sabe como é o formato de nosso layout, por isso precisa do adaptador, pra informar
        //como ele irá conectar o layout à RecyclerView.
        //ViewHolder: é a classe que é a célula em si (layout) que serve como guia para a classe Adapter
        //é a classe que podemos usar para buscar as referências de cada célula da nossa RecyclerView
        //LayoutManager: informa a posiçao que o elemento será colocado, em colunas, grades, ...

//        btnImc = findViewById(R.id.btn_imc)
//
//        btnImc.setOnClickListener {
//            //codigo padrão pra navegar pra proxima tela e é chamado de empilhamento de activities
//            val i = Intent(this, ImcActivity::class.java)
//            startActivity(i)
//        }
    }

    private inner class MainAdapter(
        private val mainItems: List<MainItem>,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        //qual é o layout XML da célula específica (item)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        //disparado toda vez que houver uma rolagem na tela e for necessário trocar o conteúdo da célula
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent)

        }

        //informar quantas células essa listagem terá
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
                container.setBackgroundColor(item.color)

                container.setOnClickListener {
                    onItemClickListener.onClick(item.id)
                }
            }

    }

    }

    override fun onClick(id: Int) {
        when(id){
            1 -> {
                val i = Intent(this, ImcActivity::class.java)
                startActivity(i)
            }
            2 -> {

            }
        }
    }
}