package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.RepresentativeListItemBinding
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter :
    ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class RepresentativeViewHolder(val binding: RepresentativeListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Representative) {
        binding.representative = item
        showSocialLinks(item)

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RepresentativeListItemBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }

    private fun showSocialLinks(representative: Representative) {

        val facebookUrl = getFacebookUrl(representative)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.facebook, facebookUrl)
        }

        val twitterUrl = getTwitterUrl(representative)
        if (!twitterUrl.isNullOrBlank()) {
            enableLink(binding.twitter, twitterUrl)
        }

        val wwwUrl = getWwwUrl(representative)
        if (!wwwUrl.isNullOrBlank()) {
            enableLink(binding.www, wwwUrl)
        }
    }

    private fun getWwwUrl(representative: Representative): String? {
        return representative.official.urls?.firstOrNull()
    }

    private fun getFacebookUrl(representative: Representative): String? {
        var facebookUrl: String? = null
        representative.official.channels?.let { channel ->
            facebookUrl = channel.filter { it.type == "Facebook" }
                .map { "https://www.facebook.com/${it.id}" }
                .firstOrNull()
        }

        return facebookUrl
    }

    private fun getTwitterUrl(representative: Representative): String? {
        var twitterUrl: String? = null
        representative.official.channels?.let { channel ->
            twitterUrl = channel.filter { it.type == "Twitter" }
                .map { "https://www.twitter.com/${it.id}" }
                .firstOrNull()

        }
        return twitterUrl
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

}

class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative) =
        oldItem.official.name == newItem.official.name

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative) =
        oldItem == newItem
}