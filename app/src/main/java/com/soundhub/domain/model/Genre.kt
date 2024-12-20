package com.soundhub.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity
data class Genre(
	@PrimaryKey
	@SerializedName("id")
	override var id: UUID = UUID.randomUUID(),
	override var name: String? = null,

	@SerializedName("pictureUrl")
	override var cover: String? = null
) : MusicEntity<UUID>()