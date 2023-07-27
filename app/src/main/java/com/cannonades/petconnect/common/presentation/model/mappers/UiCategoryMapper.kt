package com.cannonades.petconnect.common.presentation.model.mappers

import com.cannonades.petconnect.common.domain.model.Category
import com.cannonades.petconnect.common.presentation.model.UICategory
import javax.inject.Inject

class UiCategoryMapper @Inject constructor(): UiMapper<Category, UICategory> {
    override fun mapToView(input: Category): UICategory {
        return UICategory(
            id = input.id.toString(),
            name = input.name,
            checked = input.isChecked
        )
    }
}