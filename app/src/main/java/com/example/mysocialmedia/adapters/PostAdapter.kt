package com.example.mysocialmedia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysocialmedia.R
import com.example.mysocialmedia.models.Post
import com.example.mysocialmedia.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(private val context: Context, private val posts: ArrayList<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val POST_WITH_IMAGE = 0
    private val POST_WITHOUT_IMAGE = 1


    override fun getItemViewType(position: Int): Int {
        return if (posts[position].image == "") POST_WITHOUT_IMAGE else POST_WITH_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            POST_WITH_IMAGE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
                return viewHolderWithImage(itemView)
            }
            POST_WITHOUT_IMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.post_another_item, parent, false)
                return viewHolderWithoutImage(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")



        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = posts[position]
        when (holder) {
            is viewHolderWithImage -> {
                holder.titleTextView.text = post.title
                Glide.with(holder.itemView.context).load(post.image).into(holder.photoImageView)
                holder.postDate.text = post.time
                holder.authorName.text = post.author
                Glide.with(holder.itemView.context).load(post.authorImage).into(holder.authorImage)

            }
            is viewHolderWithoutImage -> {
                holder.titleAnotherTextView.text = post.title
                holder.postAnotherDate.text = post.time
                holder.authorAnotherName.text = post.author
                Glide.with(holder.itemView.context).load(post.authorImage).into(holder.authorAnotherImage)
            }
        }


    }

    override fun getItemCount(): Int {
        return posts.size
    }


    public class viewHolderWithImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView = itemView.findViewById<ImageView>(R.id.postImage)
        val titleTextView = itemView.findViewById<TextView>(R.id.postMessage)
        val postDate = itemView.findViewById<TextView>(R.id.postDate)
        val authorName = itemView.findViewById<TextView>(R.id.authorName)
        val authorImage = itemView.findViewById<CircleImageView>(R.id.authorImage)

    }
    public class viewHolderWithoutImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleAnotherTextView = itemView.findViewById<TextView>(R.id.postAnotherMessage)
        val postAnotherDate = itemView.findViewById<TextView>(R.id.postAnotherDate)
        val authorAnotherName = itemView.findViewById<TextView>(R.id.authorAnotherName)
        val authorAnotherImage = itemView.findViewById<CircleImageView>(R.id.authorAnotherImage)
    }


}

