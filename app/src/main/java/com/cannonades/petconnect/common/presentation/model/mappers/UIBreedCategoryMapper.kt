package com.cannonades.petconnect.common.presentation.model.mappers

import com.cannonades.petconnect.breeds.presentation.UIBreedCategory
import com.cannonades.petconnect.common.domain.model.BreedCategory
import javax.inject.Inject

class UIBreedCategoryMapper @Inject constructor(): UiMapper<BreedCategory, UIBreedCategory> {
    override fun mapToView(input: BreedCategory): UIBreedCategory {
        return UIBreedCategory(
            id = input.id,
            name = input.name,
        )
    }
}