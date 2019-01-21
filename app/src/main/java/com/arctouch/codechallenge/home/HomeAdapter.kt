package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.detail.DetailActivity
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants.Companion.ARGS_MOVIE
import com.arctouch.codechallenge.util.buildPosterUrl
import com.arctouch.codechallenge.util.redirectToActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*

class HomeAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var movies: MutableList<Movie>? = null
    private var isLoading = false

    fun addMovie(movie: Movie) {
        this.movies?.add(movie)
        notifyItemInserted(this.movies?.size!!.minus(1))
    }

    fun addMovies(movies: MutableList<Movie>) {
        if (this.movies == null)
            this.movies = movies
        else
            movies.forEach { addMovie(it) }
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoading = true
    }

    fun removeLoadingFooter() {
        isLoading = false

        val position = movies!!.size - 1
        val result = getItem(position)

        if (result != null) {
            movies!!.removeAt(position)
            notifyItemRemoved(position)
            addMovie(result)
        }
    }

    fun getItem(position: Int): Movie {
        return movies!!.get(position)
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie, isLoading: Boolean) {
            if (isLoading) {
                itemView.card.visibility = View.GONE
                itemView.loading.visibility = View.VISIBLE
            } else {
                itemView.card.visibility = View.VISIBLE
                itemView.loading.visibility = View.GONE
                itemView.titleTextView.text = movie.title
                itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
                itemView.releaseDateTextView.text = movie.releaseDate

                Glide.with(itemView)
                        .load(movie.posterPath?.let { buildPosterUrl(it) })
                        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(itemView.posterImageView)

                val bundle = Bundle()
                bundle.putParcelable(ARGS_MOVIE, movie)

                itemView.card.setOnClickListener { redirectToActivity(DetailActivity::class.java, it.context, bundle, true) }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = if (movies == null) 0 else movies!!.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies!![position], isLoadingItem(position))

    private fun isLoadingItem(position: Int): Boolean {
        return isLoading && position == movies!!.size - 1
    }
}
