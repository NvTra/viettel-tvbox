package com.viettel.tvbox.models

data class Categories(
    val html5: List<CategoryItem>? = emptyList(),
    val cloud: List<CategoryItem>? = emptyList(),
)

data class CategoryItem(
    val id: String? = "",
    val icon: String? = "",
    val type: String? = "",
    val typeCode: String? = "",
    val image: String? = "",
    val chanel: String? = "",
    val games: List<GameRelation>? = emptyList()
)


data class DetailCategoryResponse(
    val responseMessage: String? = "",
    val responseCode: Int? = null,
    val responseDate: String? = "",
    val data: List<CategoryItem>? = null,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val hasNext: Boolean = false
)
