package com.softsandr.dbx.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.Metadata
import com.softsandr.dbx.R
import com.softsandr.dbx.client.PicassoDbxClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.carousel_item.view.*
import java.util.*

class CarouselAdapter(private val picasso: Picasso, private var items: List<Metadata>?) :
        RecyclerView.Adapter<CarouselAdapter.MetadataViewHolder>() {

    fun setItems(items: List<Metadata>) {
        this.items = Collections.unmodifiableList(ArrayList(items))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MetadataViewHolder {
        return MetadataViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.carousel_item, viewGroup, false))
    }

    override fun onBindViewHolder(metadataViewHolder: MetadataViewHolder, i: Int) {
        metadataViewHolder.bind(items!![i])
    }

    override fun getItemId(position: Int): Long {
        return items!![position].pathLower.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    inner class MetadataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var item: Metadata? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            // todo: handle click here
        }

        fun bind(item: Metadata) {
            this.item = item
            itemView.carousel_item_txt.text = this.item!!.name

            if (item is FileMetadata) {
                val mime = MimeTypeMap.getSingleton()
                val ext = item.getName().substring(item.getName().indexOf(".") + 1)
                val type = mime.getMimeTypeFromExtension(ext)
                if (type != null && type.startsWith("image/")) {
                    picasso.load(PicassoDbxClient.ThumbnailRequestHandler.buildPicassoUri(item))
                            .placeholder(R.drawable.placeholder_640_480)
                            .fit()
                            .centerCrop()
                            .into(itemView.carousel_item_img)
                }
            }
        }
    }
}
