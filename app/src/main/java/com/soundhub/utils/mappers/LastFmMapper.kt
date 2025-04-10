package com.soundhub.utils.mappers

import com.soundhub.data.api.responses.lastfm.LastFmSessionBody
import com.soundhub.domain.model.LastFmUser
import com.soundhub.domain.model.LastFmUserDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers

@Mapper
interface LastFmMapper {
	@Mapping(target = "sessionKey", source = "key")
	fun lastFmSessionBodyToLastFmUser(session: LastFmSessionBody?): LastFmUser?

	fun mergeUserDtos(
		@MappingTarget user: LastFmUserDto,
		userResponse: LastFmUserDto
	): LastFmUserDto

	companion object {
		val impl: LastFmMapper = Mappers.getMapper(LastFmMapper::class.java)
	}
}