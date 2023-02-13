package com.example.adonation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adonation.data.Post
import com.example.adonation.databinding.PostLayoutBinding


class postAdapter(val posts:List<Post>):RecyclerView.Adapter<postAdapter.PostViewHolder>() {

    class PostViewHolder(private val binding: PostLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(post:Post){
            binding.userName.text=post.userName
            binding.textInPost.text=post.textInPost
            binding.postTime.text=post.postTime



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
       PostViewHolder(PostLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun onBindViewHolder(holder: PostViewHolder, position: Int)=
        holder.bind(posts[position])


    override fun getItemCount()=
        posts.size

}