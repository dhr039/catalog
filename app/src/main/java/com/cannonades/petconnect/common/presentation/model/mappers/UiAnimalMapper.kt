package com.cannonades.petconnect.common.presentation.model.mappers

import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.presentation.model.UIAnimal
import javax.inject.Inject

class UiAnimalMapper @Inject constructor(): UiMapper<Animal, UIAnimal> {

  override fun mapToView(input: Animal): UIAnimal {
    return UIAnimal(
        id = input.id,
        name = input.photo.url,//TODO: change to something that makes sense
        photo = input.photo.url
    )
  }
}
