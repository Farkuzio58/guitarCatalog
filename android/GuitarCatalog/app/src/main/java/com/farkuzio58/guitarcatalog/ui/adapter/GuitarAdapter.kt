/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Adapter para el RecyclerView.
 */

package com.farkuzio58.guitarcatalog.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farkuzio58.guitarcatalog.ImageLoader
import com.farkuzio58.guitarcatalog.data.Guitar
import com.farkuzio58.guitarcatalog.databinding.CardviewLayoutBinding


class GuitarAdapter (private val context: Context,
    val clickListener:(guitar:Guitar) -> Unit
): ListAdapter<Guitar, GuitarAdapter.ListViewHolder>(GUITAR_COMPARATOR) {

    class ListViewHolder(val context: Context, val binding: CardviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bin(guitar: Guitar) {
            binding.txMarca.text = guitar.marca
            binding.txModelo.text = guitar.modelo
            val imageLoader = ImageLoader(context, binding.imageView, guitar.rotacion)
            imageLoader.load(guitar.urlImagen)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(context, CardviewLayoutBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bin(item)
        holder.itemView.setOnClickListener {
            clickListener(item)
        }
    }

    fun sortByBrand(rv: RecyclerView){
        val list = this.currentList.sortedWith(
            compareBy<Guitar> { it.marca }.thenBy { it.modelo }
        )
        submitList(list){
            rv.scrollToPosition(0)
        }
    }

    fun sortByBrandDes(rv: RecyclerView){
        val list = this.currentList.sortedWith(
            compareByDescending<Guitar> { it.marca }.thenBy { it.modelo }
        )
        submitList(list){
            rv.scrollToPosition(0)
        }
    }

    fun sortByPriceDes(rv: RecyclerView){
        val list = this.currentList.sortedByDescending  {
            it.precio
        }
        submitList(list){
            rv.scrollToPosition(0)
        }
    }

    fun sortByPrice(rv: RecyclerView){
        val list = this.currentList.sortedBy {
            it.precio
        }
        submitList(list){
            rv.scrollToPosition(0)
        }
    }

    companion object {
        val GUITAR_COMPARATOR = object : DiffUtil.ItemCallback<Guitar>() {
            override fun areItemsTheSame(oldItem: Guitar, newItem: Guitar): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Guitar, newItem: Guitar): Boolean {
                return oldItem.idExterno == newItem.idExterno
            }

        }
    }
}