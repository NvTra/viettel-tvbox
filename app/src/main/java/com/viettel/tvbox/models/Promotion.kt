package com.viettel.tvbox.models

// Represents a list of promotions with pagination and status info
data class Promotions(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val data: List<DataItem>? = null,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val hasNext: Boolean = false
)

// Represents a single promotion item in the list
data class DataItem(
    val id: String = "",
    val type: String = "",
    val description: String = "",
    val title: String = "",
    val image: String = ""
)

// Represents detailed info about a promotion, including related users
data class PromotionsDetail(
    val responseMessage: String? = "",
    val responseCode: Int? = 0,
    val responseDate: String? = "",
    val promotionRelationUsers: List<Item>? = emptyList(),
    val promotionDetailUser: ItemDetail? = null
)

// Represents a related user or item in a promotion
data class Item(
    val id: String = "",
    val description: String = "",
    val title: String = "",
    val image: String = "",
    val type: String = "",
    val announceDate: String = ""
)

// Represents detailed information about a promotion item
data class ItemDetail(
    val id: String = "",
    val content: String = "",
    val title: String = "",
    val image: String = "",
    val announceDate: String = ""
)
