package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.soundhub.utils.constants.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateRoomConverter {
	private val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)

	@TypeConverter
	fun fromLocalDate(date: LocalDate?): String {
		return date.toString()
	}

	@TypeConverter
	fun toLocalDate(stringDate: String): LocalDate? {
		if (stringDate == "null")
			return null

		return LocalDate.parse(stringDate, formatter)
	}
}