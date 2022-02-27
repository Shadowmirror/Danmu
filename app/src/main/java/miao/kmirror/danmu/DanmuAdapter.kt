package miao.kmirror.danmu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class DanmuAdapter(var danmuList: MutableList<String>) :
    RecyclerView.Adapter<DanmuAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val danmuMsg: TextView = view.findViewById(R.id.danmuMsg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.danmu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val danmu = danmuList[position]
        holder.danmuMsg.text = danmu
    }

    override fun getItemCount() = danmuList.size


    private val lock = ReentrantLock()

    fun addData(danmu: String) {
        lock.withLock {
            this.danmuList.add(danmu)
            notifyDataSetChanged()
        }
    }

}