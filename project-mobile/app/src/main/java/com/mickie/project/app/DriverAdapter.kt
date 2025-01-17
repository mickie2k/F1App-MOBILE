package com.mickie.project.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mickie.project.R
import com.mickie.project.databinding.ActivityMainBinding
import com.mickie.project.databinding.ViewReviewItemBinding

class DriverAdapter(private val dataList: List<F1Fetch.DriverStanding>): RecyclerView.Adapter<DriverAdapter.ViewHolderClass>(){

    class ViewHolderClass(val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root)

//    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
//        val rvDriverName: TextView = itemView.findViewById(R.id.driverName)
//        val rvDriverPoint: TextView = itemView.findViewById(R.id.driverPoint)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolderClass(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.binding.driverName.text = currentItem.Driver.givenName
        holder.binding.driverPoint.text = holder.itemView.context.getString(R.string.points , currentItem.points)
        holder.binding.driverTeam.text = currentItem.Constructors.first().name
        holder.binding.driverLastName.text = currentItem.Driver.familyName
        holder.binding.driverStanding.text = currentItem.position
    }
}
