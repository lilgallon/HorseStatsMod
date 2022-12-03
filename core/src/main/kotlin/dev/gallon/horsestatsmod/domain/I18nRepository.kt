package dev.gallon.horsestatsmod.domain

interface I18nRepository {
    fun get(key: String): String
}
