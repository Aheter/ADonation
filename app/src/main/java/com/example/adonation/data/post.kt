package com.example.adonation.data

data class Post (val userName:String , val textInPost:String, val post_image:String?)

object PostsList{
    val posts : MutableList<Post> = mutableListOf()

        fun add(post: Post){
            posts.add(post)
        }
        fun remove(index:Int){
            posts.removeAt(index)
        }
}