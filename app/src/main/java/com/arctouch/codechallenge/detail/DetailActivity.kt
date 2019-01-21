package com.arctouch.codechallenge.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants.Companion.ARGS_MOVIE
import com.arctouch.codechallenge.util.Constants.Companion.BUNDLE_ARGS
import com.arctouch.codechallenge.util.buildBackdropUrl
import com.arctouch.codechallenge.util.buildPosterUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        intent.getBundleExtra(BUNDLE_ARGS)?.let {
            it.getParcelable<Movie>(ARGS_MOVIE)?.let {
                tvTitle.text = it.title
                tvGenre.text = it.genres?.joinToString(separator = ", ") { it.name }
                tvSummary.text = it.overview
                tvData.text = it.releaseDate
                Glide.with(this)
                        .load(it.backdropPath?.let { buildBackdropUrl(it) })
                        .into(ivBack)
                Glide.with(this)
                        .load(it.posterPath?.let { buildPosterUrl(it) })
                        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(ivCover)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
