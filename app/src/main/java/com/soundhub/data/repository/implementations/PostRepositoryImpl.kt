package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.soundhub.R
import com.soundhub.data.api.PostService
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Post
import com.soundhub.utils.HttpFileUtils
import com.soundhub.data.repository.PostRepository
import com.soundhub.utils.Constants
import com.soundhub.utils.ContentTypes
import com.soundhub.utils.converters.LocalDateAdapter
import com.soundhub.utils.converters.LocalDateTimeAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val context: Context
): PostRepository {

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    override suspend fun getPostById(
        accessToken: String?,
        postId: UUID
    ): HttpResult<Post?> {
        try {
            val response: Response<Post?> = postService.getPostById(
                id = postId,
                accessToken = "Bearer $accessToken"
            )
            Log.d("PostRepository", "getPostById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = response.message()
                    )
                Log.d("PostRepository", "getPostById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.d("PostRepository", "getPostById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getPostsByAuthorId(
        authorId: UUID,
        accessToken: String?
    ): HttpResult<List<Post>> {
        try {
            val response: Response<List<Post>> = postService.getPostsByAuthorId(
                authorId = authorId,
                accessToken = "Bearer $accessToken"
            )
            Log.d("PostRepository", "getPostsByAuthorId[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = response.message()
                    )
                Log.d("PostRepository", "getPostsByAuthorId[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.d("PostRepository", "getPostsByAuthorId[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun addPost(post: Post, accessToken: String?): HttpResult<Post> {
        try {
            val imagesFormDataList: List<MultipartBody.Part?> = post.images?.map {
                HttpFileUtils.prepareMediaFormData(it, context)
            } ?: emptyList()

            val postRequestBody: RequestBody = gson.toJson(post)
                .toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

            val response: Response<Post> = postService.addPost(
                accessToken = "Bearer $accessToken",
                post = postRequestBody,
                images = imagesFormDataList
            )

            Log.d("PostRepository", "response: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_update_error)
                    )

                Log.d("PostRepository", "addPost[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("PostRepository", "addPost[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun toggleLike(accessToken: String?, postId: UUID): HttpResult<Post> {
        try {
            val response: Response<Post> = postService.toggleLike(
                accessToken = "Bearer $accessToken",
                postId = postId
            )
            Log.d("PostRepository", "toggleLike[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.e("PostRepository", "toggleLike[2]: $errorBody")
                return HttpResult.Error(errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("PostRepository", "toggleLike[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun deletePost(accessToken: String?, postId: UUID): HttpResult<RequestBody> {
        try {
            val response: Response<RequestBody> = postService.deletePost(
                accessToken = "Bearer $accessToken",
                postId = postId
            )
            Log.d("PostRepository", "deletePost[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.e("PostRepository", "deletePost[2]: $errorBody")
                return HttpResult.Error(errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("PostRepository", "deletePost[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun updatePost(
        accessToken: String?,
        postId: UUID,
        post: Post,
        deletedImages: List<String>
    ): HttpResult<Post> {
        TODO("Not yet implemented")
    }
}