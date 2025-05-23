package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.CompatibleUsersResponse
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.UserExistenceResponse
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.domain.model.User
import java.util.UUID

interface UserRepository {
	suspend fun getUserById(id: UUID?): HttpResult<User?>
	suspend fun getCurrentUser(userCreds: UserPreferences?): HttpResult<User?>
	suspend fun updateUser(user: User?): HttpResult<User>
	suspend fun addFriend(friendId: UUID): HttpResult<User>
	suspend fun deleteFriend(friendId: UUID): HttpResult<User>
	suspend fun getRecommendedFriends(): HttpResult<List<User>>
	suspend fun getUsersCompatibilityPercentage(
		userIds: List<UUID>
	): HttpResult<CompatibleUsersResponse>

	suspend fun checkUserExistenceByEmail(email: String): HttpResult<UserExistenceResponse>

	suspend fun getFriendsByUserId(userId: UUID): HttpResult<List<User>>
	suspend fun searchUserByFullName(name: String): HttpResult<List<User>>
	suspend fun updateUserOnline(online: Boolean): HttpResult<User>
}