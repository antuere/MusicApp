package com.example.musicapp.database.util

interface EntityMapper <T, Entity> {

    fun mapToEntity(t: T): Entity

    fun mapFromEntity(entity: Entity): T
}