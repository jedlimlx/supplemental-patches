package io.github.jedlimlx.supplemental_patches.shaders

val COLOURS = arrayListOf<Colour>()
val TINTS = arrayListOf<Colour>()
val COLOUR_INJECTIONS = arrayListOf<String>()

class Colour(
    val index: Int = -1,
    val code: String = "",
    val tint: Boolean = false
) {
    override fun toString(): String = code
}