package com.cannonades.petconnect.common.domain.model.pagination

data class Pagination(
    val currentPage: Int,
    val totalPages: Int
) {

  companion object {
    const val DEFAULT_PAGE_SIZE = 30
  }
}
