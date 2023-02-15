package com.example.adonation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adonation.data.FavoriteList
import com.example.adonation.data.Post
import com.example.adonation.databinding.FavoriteBinding
import com.example.adonation.databinding.FavoriteLayoutBinding


class FavsAdapter (val posts:List<Post>):RecyclerView.Adapter<FavsAdapter.FavViewHolder>() {

    class FavViewHolder(private val binding: FavoriteLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(post:Post){
            binding.userName.text=post.userName
            binding.textInPost.text=post.textInPost
            binding.postTime.text=post.postTime
            binding.isFavorite.isChecked=post.isFavorite

            binding.isFavorite.setOnClickListener {
                post.isFavorite = ! post.isFavorite
                binding.isFavorite.isChecked=post.isFavorite
                if (post.isFavorite) {
                    FavoriteList.add(post)
                } else {
                    FavoriteList.favs.remove(post)
                    //FavoriteList.remove(this.adapterPosition)
                    Toast.makeText(binding.root.context, "remove", Toast.LENGTH_LONG).show()
                }


            }
        }

        /*

                    favBtn.setOnClickListener {
                val position: Int = getAdapterPosition()
                val coffeeItem: CoffeeItem = coffeeItems[position]
                likeClick(coffeeItem, favBtn, likeCountTextView)
            }
         */

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
    FavViewHolder(FavoriteLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: FavViewHolder, position: Int)=
        holder.bind(posts[position])


    override fun getItemCount()=
        posts.size

}