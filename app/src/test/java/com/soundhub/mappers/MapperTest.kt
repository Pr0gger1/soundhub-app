package com.soundhub.mappers

import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.ui.post_editor.PostEditorState
import com.soundhub.ui.states.UserFormState
import com.soundhub.utils.mappers.MessageMapper
import com.soundhub.utils.mappers.PostMapper
import com.soundhub.utils.mappers.RegisterDataMapper
import com.soundhub.utils.mappers.UserMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapperTest: BaseMapperTest() {
    // user form state mapper tests
    @Test
    fun mapToUserFromFormStateTest() {
        val result: User = UserMapper.impl.mergeUserWithFormState(userFormState, user)
        assertEquals(user, result)
    }

    @Test
    fun mapToFormStateFromUserInstanceTest() {
        val result: UserFormState = UserMapper.impl.toFormState(user)
        assertEquals(userFormState, result)
    }

    @Test
    fun mapToUserFromRegistrationTest() {
        val result: User = UserMapper.impl.fromRegistrationState(registerState)
        assertEquals(registerUser, result)
    }

    // register state and register body mapper tests
    @Test
    fun mapToRegisterRequestBodyTest() {
        val result: RegisterRequestBody = RegisterDataMapper
            .impl.registerStateToRegisterRequestBody(registerState)
        assertEquals(registerRequestBody, result)
    }

    @Test
    fun mapToRequestBodyIfStateIsNullTest() {
        val result: RegisterRequestBody = RegisterDataMapper
            .impl.registerStateToRegisterRequestBody(null)
        assertNull(result)
    }

    // message mapper tests
    @Test
    fun mapToSendMessageRequestTest() {
        val result: SendMessageRequest? = MessageMapper
            .impl.toSendMessageRequest(chat, user.id, messageContent)

        assertEquals(result, messageRequest)
    }

    // post mapper tests
    @Test
    fun mapToPostTest() {
        val result: Post = PostMapper
            .impl.fromPostEditorStateToPost(postEditorState)

        assertEquals(result, post)
    }

    @Test
    fun mapToPostEditorState() {
        val result: PostEditorState = PostMapper
            .impl.fromPostToPostEditorState(post)

        assertEquals(result, postEditorState)
    }
}