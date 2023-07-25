package com.cannonades.petconnect.common.presentation.model

data class UICategory(
    override val id: String,
    override val name: String,
    val checked: Boolean = false
) : UIItem