package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.PaginationScrollListener
import kotlinx.android.synthetic.main.home_activity.*
import androidx.lifecycle.ViewModelProviders


class HomeActivity : AppCompatActivity() {

    private var adapter = HomeAdapter()
    lateinit var viewModel : HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        configRecyclerView()
        viewModel.onMoviesResult.observe(this, Observer<MutableList<Movie>> { this.onMoviesResult(it) })
        viewModel.startService()
    }

    private fun onMoviesResult(it: MutableList<Movie>) {
        if (progressBar.visibility == View.VISIBLE) {
            progressBar.visibility = View.GONE
        } else {
            adapter.removeLoadingFooter()
        }
        adapter.addMovies(it)
        if (!viewModel.isLastPage) {
            adapter.addLoadingFooter()
        }
    }

    private fun configRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                viewModel.isLoading = true
                viewModel.loadPage()
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
            }
        })
    }
}
