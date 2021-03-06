package com.example.myfavdish.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfavdish.R
import com.example.myfavdish.databinding.ItemDishLayoutBinding
import com.example.myfavdish.model.entities.FavDish
import com.example.myfavdish.utils.Constants
import com.example.myfavdish.view.activities.AddUpdateDishActivity
import com.example.myfavdish.view.fragments.AllDishesFragment
import com.example.myfavdish.view.fragments.FavoriteDishesFragment

class FavDishAdapter(private val fragment: Fragment):
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()

        class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root){
            val ivDishImage = view.ivDishImage
            val tvTitle = view.tvDishTitle
            val ibMore = view.ibMore
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
        when(fragment){
            is AllDishesFragment -> holder.ibMore.visibility = View.VISIBLE

            is FavoriteDishesFragment -> holder.ibMore.visibility = View.GONE
        }

        holder.ibMore.setOnClickListener{
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
               if(it.itemId == R.id.action_edit){
                  val intent = Intent(fragment.requireContext(), AddUpdateDishActivity::class.java)
                   intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                   fragment.requireActivity().startActivity(intent)


               }else{
                   (fragment as AllDishesFragment).deleteDish(dish)

               }
                true
            }
            popup.show()

        }

        holder.itemView.setOnClickListener{
            when(fragment){
                is AllDishesFragment -> fragment.dishDetails(dish)

                is FavoriteDishesFragment -> fragment.favoriteDishDetails(dish)
            }
        }
    }

    override fun getItemCount(): Int {
       return dishes.size
    }

    fun dishesList(list: List<FavDish>){
        dishes = list
        notifyDataSetChanged()
    }
}