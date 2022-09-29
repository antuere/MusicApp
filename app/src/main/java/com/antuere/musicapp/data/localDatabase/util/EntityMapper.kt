package com.antuere.musicapp.data.localDatabase.util

interface EntityMapper <T, Entity> {

    fun mapToEntity(t: T): Entity

    fun mapFromEntity(entity: Entity): T
}