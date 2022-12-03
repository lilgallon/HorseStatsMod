package dev.gallon.horsestatsmod.domain

import java.util.UUID

interface UserRepository {
    suspend fun fetchUsernameFromUUID(uuid: UUID): String?
}
