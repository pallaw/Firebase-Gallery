package com.pallaw.firebasegallery.Util

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pallaw.firebasegallery.R

/**
 * Created by Pallaw Pathak on 21/04/20. - https://www.linkedin.com/in/pallaw-pathak-a6a324a1/
 */

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_loading)
        .error(R.drawable.ic_broken)
        .into(this)
}

fun ImageView.loadImage(uri: Uri) {
    Glide.with(this.context)
        .load(uri)
        .placeholder(R.drawable.ic_loading)
        .error(R.drawable.ic_broken)
        .into(this)
}