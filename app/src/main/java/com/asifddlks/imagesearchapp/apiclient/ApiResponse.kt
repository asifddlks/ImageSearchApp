package com.asifddlks.imagesearchapp.apiclient

import com.asifddlks.imagesearchapp.model.ImageModel

data class ApiResponse(
    val results: List<ImageModel>
)