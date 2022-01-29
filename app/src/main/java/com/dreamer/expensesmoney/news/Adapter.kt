package com.dreamer.expensesmoney.news

import android.content.Context
import com.dreamer.expensesmoney.news.models.Article
import androidx.recyclerview.widget.RecyclerView
import com.dreamer.expensesmoney.news.Adapter.MyViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.dreamer.expensesmoney.R
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import android.widget.TextView
import android.widget.ProgressBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target
import java.lang.StringBuilder

class Adapter(private val articles: List<Article>, private val context: Context) :
    RecyclerView.Adapter<MyViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false)
        return MyViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holders: MyViewHolder, position: Int) {
        val model = articles[position]
        val requestOptions = RequestOptions()
        requestOptions.placeholder(Utils.randomDrawbleColor)
        requestOptions.error(Utils.randomDrawbleColor)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()
        Glide.with(context)
            .load(model.urlToImage)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    holders.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holders.progressBar.visibility = View.GONE
                    return false
                }
            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holders.imageView)
        holders.title.text = model.title
        holders.desc.text = model.description
        holders.source.text = model.source?.name
        //        holder.time.setText(" \u2022 "+ Utils.DateToTimeFormat(model.getPublishAt()));
//        holder.publish.setText(Utils.DateFormat(model.getPublishAt()));
        holders.time.text = StringBuilder().append(" \u2022 ")
            .append(Utils.DateToTimeFormat(model.publishedAt))
            .toString()
        holders.publish.text = Utils.DateFormat(model.publishedAt)
        holders.author.text = model.author
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    inner class MyViewHolder(itemView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title: TextView
        var desc: TextView
        var author: TextView
        var publish: TextView
        var source: TextView
        var time: TextView
        var imageView: ImageView
        var progressBar: ProgressBar
        var onItemClickListener: OnItemClickListener?
        override fun onClick(v: View) {
            onItemClickListener!!.onItemClick(v, bindingAdapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
            title = itemView.findViewById(R.id.news_title)
            desc = itemView.findViewById(R.id.Desc)
            author = itemView.findViewById(R.id.author)
            publish = itemView.findViewById(R.id.publishAt)
            source = itemView.findViewById(R.id.source)
            time = itemView.findViewById(R.id.time)
            imageView = itemView.findViewById(R.id.img)
            progressBar = itemView.findViewById(R.id.progress_bar)
            this.onItemClickListener = onItemClickListener
        }
    }
}