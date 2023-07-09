package com.cannonades.petconnect.common.domain.model

import java.io.IOException


class NoMoreAnimalsException: Exception()

class NoMoreCategoriesException: Exception()

class NetworkUnavailableException : IOException()

class NetworkException: Exception()
