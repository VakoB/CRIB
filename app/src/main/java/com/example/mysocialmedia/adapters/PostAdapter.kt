package com.example.mysocialmedia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysocialmedia.R
import com.example.mysocialmedia.models.Post

class PostAdapter(private var posts: MutableList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    fun updatePost(post: Post) {
        for (i in posts.indices) {
            if (posts[i].id == post.id) {
                posts[i] = post
                break
            }
        }
        notifyDataSetChanged()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.titleTextView.text = post.title
        Glide.with(holder.itemView.context).load(post.image).into(holder.photoImageView)
        holder.postDate.text = post.time.toString()
        holder.authorName.text = post.author
    }

    override fun getItemCount() = posts.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView = itemView.findViewById<ImageView>(R.id.postImage)
        val titleTextView = itemView.findViewById<TextView>(R.id.postMessage)
        val postDate = itemView.findViewById<TextView>(R.id.postDate)
        val authorName = itemView.findViewById<TextView>(R.id.authorName)
    }


}

