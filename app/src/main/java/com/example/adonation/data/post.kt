package com.example.adonation.data

data class Post (val userName:String, val textInPost:String, val post_image:String, var isFavorite:Boolean, val postTime: String)

object PostsList{
    val posts : MutableList<Post> = mutableListOf()
    fun add(post: Post){
        posts.add(0, post)
    }
    fun remove(index:Int){
        posts.removeAt(index)
    }
}

object FavoriteList{
    val posts : MutableList<Post> = mutableListOf()
    fun add(post: Post){
        posts.add( post)
    }
    fun remove(post: Post){
        posts.remove(post)
    }
    fun remove(index: Int){
        posts.removeAt(index)
    }
}