package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arctouch.codechallenge.AppApplication.Companion.api
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.TopRatedMoviesResponse
import com.arctouch.codechallenge.util.Constants.Companion.API_KEY
import com.arctouch.codechallenge.util.Constants.Companion.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.util.Constants.Companion.DEFAULT_REGION
import com.arctouch.codechallenge.util.Constants.Companion.PAGE_START
import com.arctouch.codechallenge.util.PaginationScrollListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {

    private var totalPages = 0
    private var currentPage = PAGE_START
    private var isLoading = false
    private var isLastPage = false
    private var adapter = HomeAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        configRecyclerView()
        loadCache()
    }

    private fun loadCache(){
        api.genres(API_KEY, DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    loadPage()
                }
    }

    private fun loadPage() {
        api.topRatedMovies(API_KEY, DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (currentPage == PAGE_START) {
                        totalPages = it.totalPages
                        progressBar.visibility = View.GONE
                    } else {
                        adapter.removeLoadingFooter()
                        isLoading = false
                    }
                    adapter.addMovies(getMoviesWithGenres(it))
                    checkPage()
                }
    }

    private fun checkPage() {
        if (currentPage < totalPages)
            adapter.addLoadingFooter()
        else
            isLastPage = true
    }


    private fun getMoviesWithGenres(it: TopRatedMoviesResponse): MutableList<Movie> {
        return it.results.map { movie ->
            movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
        }.toMutableList()
    }

    private fun configRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                loadPage()
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }
}
