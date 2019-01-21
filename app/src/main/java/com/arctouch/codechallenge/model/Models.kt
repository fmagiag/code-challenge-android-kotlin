package com.arctouch.codechallenge.model

import com.squareup.moshi.Json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GenreResponse(val genres: MutableList<Genre>)

@Parcelize
data class Genre(val id: Int, val name: String): Parcelable

@Parcelize
data class UpcomingMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
): Parcelable

@Parcelize
data class TopRatedMoviesResponse(
        val page: Int,
        val results: List<Movie>,
        @Json(name = "total_pages") val totalPages: Int,
        @Json(name = "total_results") val totalResults: Int
): Parcelable

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val overview: String?,
    val genres: List<Genre>?,
    @Json(name = "genre_ids") val genreIds: List<Int>?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "release_date") val releaseDate: String?
)  : Parcelable