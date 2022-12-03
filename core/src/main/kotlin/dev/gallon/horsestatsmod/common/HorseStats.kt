package dev.gallon.horsestatsmod.common

data class HorseStats(
    val health: Double,
    val jump: Double,
    val speed: Double,
    val inventory: Int?,
    val owner: String?
)
