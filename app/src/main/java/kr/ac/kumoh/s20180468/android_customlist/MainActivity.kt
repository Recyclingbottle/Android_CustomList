package kr.ac.kumoh.s20180468.android_customlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180468.android_customlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: SongViewModel
    private val songAdapter = SongAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[SongViewModel::class.java]

        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = songAdapter
        }

        model.list.observe(this) {
            // 좀더 구체적인 이벤트를 사용하라고 warning 나와서 변경함
            //songAdapter.notifyDataSetChanged()
            //Log.i("size", "${model.list.value?.size ?: 0}")

            // Changed가 아니라 Inserted
            songAdapter.notifyItemRangeInserted(
                0,
                model.list.value?.size ?: 0
            )
        }

        model.requestSong()
    }

    inner class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , OnClickListener{
            val txTitle: TextView = itemView.findViewById(R.id.text1)
            val txSinger: TextView = itemView.findViewById(R.id.text2)
            val niImage: NetworkImageView = itemView.findViewById(R.id.image)

            init {
                //이미지 가 없는 경우 디폴트로 안드로이드에 있는 이미지
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(
                //{
                    //Toast.makeText(applicationContext, txTitle.text ,Toast.LENGTH_LONG).show()
                    //ViewHolder, 한 학생마다 코드 복사 or 깃허브 주소 알려주기
                    //ViewHodler 하나 만들자 View Holder 에 OnclickListener 넣기
                    this
                //}
                )
            }

            override fun onClick(v: View?) {
                //Toast.makeText(application, model.list.value?.get(adapterPosition)?.title, Toast.LENGTH_SHORT).show()
                val intent = Intent(application, SongActivity::class.java)
                val song = model.list.value?.get(adapterPosition)
                intent.putExtra(SongActivity.KEY_TITLE, song?.title)
                intent.putExtra(SongActivity.KEY_SINGER, song?.singer)
                intent.putExtra(SongActivity.KEY_IMAGE, model.getImageUri(adapterPosition))
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_song,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txTitle.text = model.list.value?.get(position)?.title
            holder.txSinger.text = model.list.value?.get(position)?.singer
            holder.niImage.setImageUrl(model.getImageUri(position), model.imageLoader)
        }

        override fun getItemCount() = model.list.value?.size ?: 0
    }
}