package com.udacity.asteroidradar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.AsteroidRadarAdapter.*
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding
import com.udacity.asteroidradar.models.Asteroid

class AsteroidRadarAdapter(val clickListener: AsteroidRadarClickListener)
                           : ListAdapter<Asteroid, ViewHolder>(AsteroidRadarDiffCallBack()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var asteroid= getItem(position)
        holder.itemView.setOnClickListener{
            clickListener.onClick(asteroid)
        }
        holder.bind(asteroid)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }






class ViewHolder private constructor(val  binding:ListItemAsteroidBinding)
                                          :RecyclerView.ViewHolder(binding.root){



    fun bind( item: Asteroid) {
        binding.asteroid = item

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)

            return ViewHolder(binding)
        }
    }
                                          }








}
class AsteroidRadarDiffCallBack:DiffUtil.ItemCallback<Asteroid>(){

    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return  oldItem.id== newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
      return  oldItem==newItem
    }


}
class AsteroidRadarClickListener(val clickListener: (asteroid:Asteroid)-> Unit){
    fun onClick(asteroid: Asteroid) =clickListener(asteroid)
}