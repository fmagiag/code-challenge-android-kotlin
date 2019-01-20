package com.arctouch.codechallenge.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants.Companion.ARGS_MOVIE
import com.arctouch.codechallenge.util.Constants.Companion.BUNDLE_ARGS
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        intent.getBundleExtra(BUNDLE_ARGS)?.let {
            it.getParcelable<Movie>(ARGS_MOVIE)?.let {
                title = it.title
                tv_title.text = it.genres?.joinToString(separator = ", ") { it.name }
                tv_summary.text = it.overview
                Glide.with(this)
                        .load(it.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it)})
                        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(iv_cover)
            }
        }
    }
}
