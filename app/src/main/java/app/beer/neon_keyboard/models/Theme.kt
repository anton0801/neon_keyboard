package app.beer.neon_keyboard.models

import androidx.annotation.DrawableRes

data class Theme(
    var name: String = "",
    @DrawableRes
    var photo: Int = 0
)