package com.sa.foodapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sa.foodapp.R
import com.sa.foodapp.databinding.ItemMealBinding
import com.sa.foodapp.domain.model.Meal
import com.sa.foodapp.domain.util.setResizableText

class MealListAdapter :ListAdapter<Meal?,MealListAdapter.ViewHolder>(DIFF_UTIL){
    companion object{
        private val DIFF_UTIL=object :DiffUtil.ItemCallback<Meal?>(){
            override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return false
            }

        }
    }
    inner class ViewHolder(private val binding:ItemMealBinding):RecyclerView.ViewHolder(binding.root){
        fun  bindData(){
            binding.apply {
                val meal=getItem(adapterPosition)
                tvMealName.text=meal?.strMeal
                tvCategory.text=meal?.strCategory
                if (!meal?.strTags.isNullOrEmpty()){
                    tvTags.text=meal?.strTags
                    tvTags.isVisible=true
                }else{
                    tvTags.isVisible=false
                }
                tvInstruction.setResizableText(meal?.strInstructions?:"", 3, true)
                Glide.with(imageViewMeal)
                    .load(meal?.strMealThumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.image_place_holder)
                    .error(R.drawable.image_place_holder)
                    .into(imageViewMeal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemMealBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindData()
    }
}