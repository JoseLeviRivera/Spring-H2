package com.example.TestDemo.Controller;

import com.example.TestDemo.Model.User;
import com.example.TestDemo.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Test Listar todo los usuarios")
    void whenFindAllUsers() throws Exception {
        List<User> listaUserTest = new ArrayList<>();
        listaUserTest.add(new User(1L, "Jose", "890ad"));
        listaUserTest.add(new User(2L, "Antonio", "890ad"));
        listaUserTest.add(new User(3L, "Carlos", "890ad"));

        Mockito
                .when(userService.getAllUsers())
                .thenReturn(listaUserTest);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

        Mockito
                .verify(userService, Mockito.times(1))
                .getAllUsers();
    }

    @Test
    @DisplayName("Test busqueda de un id")
    void wheFindUserById() throws Exception {
        User usuario = new User(1L, "Jose", "890ad");
        Mockito
                .when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(Optional.of(usuario));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jose"));
        Mockito
                .verify(userService, Mockito.times(1))
                .getUserById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Test guardar un usuario")
    void whenSaveUser() throws Exception{
        User usuario = new User(1L, "Jose", "890ad");
        Mockito
                .when(userService.save(Mockito.any(User.class)))
                .thenReturn(usuario);

        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuario))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jose"));
        Mockito
                .verify(userService, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Test actualizar a un usuario")
    void whenUpdateUser() throws Exception {
        // Datos de prueba
        Long userId = 1L;
        User existingUser = new User(userId, "John Doe", "john@example.com");
        User updatedUser = new User(userId, "Jane Smith", "jane@example.com");

        // Simular el comportamiento de userService.getUserById()
        Mockito
                .when(userService.getUserById(userId))
                .thenReturn(Optional.of(existingUser));

        // Simular el comportamiento de userService.updateUser()
        Mockito
                .when(userService.updateUser(Mockito.any(User.class)))
                .thenReturn(updatedUser);

        // Realizar la solicitud PUT para actualizar el usuario
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jane Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jane@example.com"));
        Mockito
                .verify(userService, Mockito.times(1))
                .getUserById(Mockito.anyLong());

        Mockito
                .verify(userService, Mockito.times(1))
                .updateUser(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Test cuando un usuario existe, entonces retornar no hay content")
    void whenDeleteExistingUser_thenReturnNoContent() throws Exception {
        // Datos de prueba
        Long userId = 1L;

        User user = new User(userId, "John Doe", "john@example.com");

        // Simular el comportamiento de userService.getUserById()
        Mockito
                .when(userService.getUserById(userId))
                .thenReturn(Optional.of(user));

        // Realizar la solicitud DELETE para eliminar el usuario
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito
                .verify(userService, Mockito.times(1))
                .getUserById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Test cuando un usuario no existe, entonces retornar no funciona")
    void whenDeleteNonExistingUser_thenReturnNotFound() throws Exception {
        // Datos de prueba
        Long userId = 1L;

        // Simular el comportamiento de userService.getUserById()
        Mockito.when(userService.getUserById(userId)).thenReturn(null);

        // Realizar la solicitud DELETE para eliminar el usuario inexistente
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Mockito
                .verify(userService, Mockito.times(1))
                .getUserById(Mockito.anyLong());
    }
}