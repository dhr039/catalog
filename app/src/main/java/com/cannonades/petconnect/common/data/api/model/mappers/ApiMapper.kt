package com.cannonades.petconnect.common.data.api.model.mappers

/**
 * Having all the mappers follow this interface gives you the advantage of decoupling
 * the mapping. This is useful if you have a lot of mappers and want to make sure they
 * all follow the same contract. (p.66)
 * */
interface ApiMapper<E, D> {

  fun mapToDomain(apiEntity: E): D
}
