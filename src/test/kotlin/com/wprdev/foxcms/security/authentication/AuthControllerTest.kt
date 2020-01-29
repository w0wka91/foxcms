package com.wprdev.foxcms.security.authentication

import com.wprdev.foxcms.domain.project.ProjectService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*


@RunWith(MockitoJUnitRunner::class)
@SpringBootTest
class AuthControllerTest {

    private lateinit var mvc: MockMvc

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var encoder: PasswordEncoder

    @Mock
    private lateinit var projectService: ProjectService

    @Mock
    private lateinit var authManager: AuthenticationManager

    @InjectMocks
    private lateinit var authController: AuthController

    @Before
    fun setup() {
        mvc = MockMvcBuilders.standaloneSetup(authController).build()
    }

    @Test
    fun register_userDoesNotExist_shouldReturn201() {
        given(encoder.encode(anyString())).willReturn("encodedPw")
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty())
        given(userRepository.save(any(User::class.java)))
                .willReturn(User("savedUser", "encodedPw"))

        mvc.perform(
                post("/register")
                        .param("username", "peter")
                        .param("password", "Test12345!")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated)
        verify(projectService, times(1)).createProject(anyString())
    }

    @Test
    fun register_userExistsAlready_shouldReturn409() {
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.of(User("user", "pw")))

        mvc.perform(
                post("/register")
                        .param("username", "user")
                        .param("password", "Test12345!")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict)
    }

    @Test
    fun register_weakPassword_shouldReturn406() {
        mvc.perform(
                post("/register")
                        .param("username", "user")
                        .param("password", "a")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotAcceptable)
    }
}