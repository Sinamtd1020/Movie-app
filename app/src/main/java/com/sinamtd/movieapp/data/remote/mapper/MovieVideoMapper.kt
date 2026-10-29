package com.sinamtd.movieapp.data.remote.mapper

import android.util.Log
import com.sinamtd.movieapp.data.remote.dto.MovieVideoDto
import com.sinamtd.movieapp.data.remote.dto.MovieVideosResponseDto
import com.sinamtd.movieapp.domain.model.MovieVideo

fun MovieVideoDto.toDomain(): MovieVideo? {

    val videoKey = key ?: return null
    val videoName = name ?: return null
    val videoSite = site ?: return null
    val videoType = type ?: return null

    return MovieVideo(
        id = id,
        key = videoKey,
        name = videoName,
        site = videoSite,
        type = videoType,
        official = official ?: false
    )
}
fun MovieVideosResponseDto.toTrailer(): MovieVideo? {



    return results
        .orEmpty()
        .firstOrNull { video ->

            video.site.equals(
                "YouTube",
                ignoreCase = true
            ) &&
                    video.type.equals(
                        "Trailer",
                        ignoreCase = true
                    ) &&
                    !video.key.isNullOrBlank()
        }
        ?.let { video ->

            val key = video.key
                ?: return@let null

            MovieVideo(
                id = video.id,
                key = key,
                name = video.name ?: "Official Trailer",
                site = video.site ?: "YouTube",
                type = video.type ?: "Trailer",
                official = video.official ?: false
            )
        }
}