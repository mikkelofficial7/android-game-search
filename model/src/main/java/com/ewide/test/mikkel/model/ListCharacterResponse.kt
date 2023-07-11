package com.ewide.test.mikkel.model

data class ListCharacterResponse(
    var gameID: String? = null,
    var steamAppID: String? = null,
    var cheapest: String? = null,
    var cheapestDealID: String? = null,
    var external: String? = null,
    var internalName: String? = null,
    var thumb: String? = null
)