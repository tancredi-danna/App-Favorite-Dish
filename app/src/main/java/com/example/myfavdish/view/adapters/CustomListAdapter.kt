package com.example.myfavdish.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfavdish.databinding.ItemCustomListBinding
import com.example.myfavdish.view.activities.AddUpdateDishActivity

class CustomListAdapter(
    private val activity: Activity,
    private val listItems: List<String>,
    private val selection: String): RecyclerView.Adapter<CustomListAdapter.ViewHolder>() {

        class ViewHolder(view:ItemCustomListBinding): RecyclerView.ViewHolder(view.root){

           val tvText = view.tvText

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding
            .inflate(LayoutInflater.from(activity),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item = listItems[position]

        holder.tvText.text = item
        holder.tvText.setOnClickListener {

            if(activity is AddUpdateDishActivity){
                activity.selectedListItem(item, selection)
            }

        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }


}