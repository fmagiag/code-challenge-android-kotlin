package com.arctouch.codechallenge.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arctouch.codechallenge.AppApplication.Companion.api
import com.arctouch.codechallenge.util.Constants.Companion.API_KEY
import com.arctouch.codechallenge.util.Constants.Companion.DEFAULT_LANGUAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.TopRatedMoviesResponse
import com.arctouch.codechallenge.util.Constants.Companion.DEFAULT_REGION
import com.arctouch.codechallenge.util.Constants.Companion.PAGE_START

class HomeViewModel : ViewModel(){

    var isLoading = false
    var isLastPage = false
    var totalPages = 0
    var currentPage = PAGE_START
    val onMoviesResult = MutableLiveData<MutableList<Movie>>()

    fun startService(){
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

    fun loadPage() {
        api.topRatedMovies(API_KEY, DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (currentPage == PAGE_START) {
                        totalPages = it.totalPages
                    } else {
                        isLoading = false
                    }
                    checkPage()
                    currentPage += 1
                    onMoviesResult.postValue(getMoviesWithGenres(it))
                }
    }

    private fun checkPage() {
        if (currentPage >= totalPages)
            isLastPage = true
    }

    private fun getMoviesWithGenres(it: TopRatedMoviesResponse): MutableList<Movie> {
        return it.results.map { movie ->
            movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
        }.toMutableList()
    }

}