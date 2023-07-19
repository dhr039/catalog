package com.cannonades.petconnect.common.domain.model


sealed class AppException: Exception()

object NoMoreAnimalsException: AppException()

object NoMoreCategoriesException: AppException()

object NetworkException: AppException()
