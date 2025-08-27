package com.viettel.tvbox.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.Categories
import com.viettel.tvbox.models.CategoryItem
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class CategoryViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)

    var error by mutableStateOf<String?>(null)

    var categories by mutableStateOf<Categories?>(null)

    var featuredCategories by mutableStateOf<List<CategoryItem>?>(null)

    var otherCategory by mutableStateOf<List<CategoryItem>?>(null)
    var category by mutableStateOf<CategoryItem?>(null)

    val typeCodeFeature = listOf("Family", "Kids", "Multiplayer")
    private val categoryService = RetrofitInstance.categoryService


    suspend fun fetchCategories(type: String? = "", titleId: String? = "") {
        isLoading = true
        error = null
        try {
            val response = categoryService.getCategories(type, titleId).awaitResponse()

            if (response.isSuccessful) {
                categories = response.body()

                featuredCategories =
                    categories?.cloud?.filter { it.typeCode in typeCodeFeature } ?: emptyList()

                otherCategory =
                    categories?.cloud?.filter { it.typeCode !in typeCodeFeature } ?: emptyList()

            } else {
                error = "Error: ${response.code()}"
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    fun getDetailCategory(
        id: String = "",
        page: Int = 0,
        pageSize: Int = Int.MAX_VALUE,
        type: String? = ""
    ) {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response =
                    categoryService.getDetailCategory(id, page, pageSize, type).awaitResponse()
                if (response.isSuccessful) {
                    category = response.body()?.data?.firstOrNull()
                } else {
                    error = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
}