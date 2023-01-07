package com.halilakpinar.cryptocurrencymobileappkotlin.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.halilakpinar.cryptocurrencymobileappkotlin.Model.CryptoModel
import com.halilakpinar.cryptocurrencymobileappkotlin.R
import kotlinx.android.synthetic.main.recycler_row.view.*

class RecyclerAdapter(private val cryptoList:ArrayList<CryptoModel>,private val listener:Listener): RecyclerView.Adapter<RecyclerAdapter.RowHolder>() {

    interface Listener{
        fun onItemClick(cryptoModel: CryptoModel)
    }
    private val colors:Array<String> = arrayOf("#CCFFCC","#FFCCCC","#606060")
    class RowHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(cryptoModel:CryptoModel,colors:Array<String>,position: Int,listener:Listener){
            itemView.setOnClickListener{
                listener.onItemClick(cryptoModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position%3]))
            itemView.text_name.text=cryptoModel.currency
            itemView.text_price.text=cryptoModel.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(cryptoList[position],colors,position, listener)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }
}