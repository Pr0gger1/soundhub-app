package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import java.io.File

interface FileRepository {
	suspend fun getFile(
		folderName: String? = null,
		fileNameUrl: String?,
	): HttpResult<File>
}