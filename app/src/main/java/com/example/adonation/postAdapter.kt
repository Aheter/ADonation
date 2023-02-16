package com.example.adonation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adonation.data.FavoriteList
import com.example.adonation.data.Post
import com.example.adonation.data.PostsList
import com.example.adonation.databinding.PostLayoutBinding


class postAdapter(val posts:List<Post>):RecyclerView.Adapter<postAdapter.PostViewHolder>() {

    class PostViewHolder(private val binding: PostLayoutBinding):RecyclerView.ViewHolder(binding.root){
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
                    Toast.makeText(binding.root.context, "added to favorite", Toast.LENGTH_LONG).show()
                } else {
                    FavoriteList.remove(this.adapterPosition)
                    Toast.makeText(binding.root.context, "removed", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
       PostViewHolder(PostLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun onBindViewHolder(holder: PostViewHolder, position: Int)=
        holder.bind(posts[position])


    override fun getItemCount()=
        posts.size

}