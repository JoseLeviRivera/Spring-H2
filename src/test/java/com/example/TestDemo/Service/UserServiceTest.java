package com.example.TestDemo.Service;

import com.example.TestDemo.Model.User;
import com.example.TestDemo.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void whenGetAllUsers() {
        List<User> listaUserTest = new ArrayList<>();
        listaUserTest.add(new User(1L, "Jose", "890ad"));
        listaUserTest.add(new User(2L, "Antonio", "890ad"));
        listaUserTest.add(new User(3L, "Carlos", "890ad"));
        Mockito.when(userRepository.findAll())
                .thenReturn(listaUserTest);
        List<User> listaUsers = userService.getAllUsers();
        assertNotNull(listaUsers);
        assertEquals(3, listaUsers.size());
    }


    @Test
    void whenFindById() {
        User usuario = new User(1L, "Jose", "890ad");
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(usuario));
        User usuarioResponse = userService.getUserById(1L).orElseGet(()-> null);

        Mockito
                .verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());

        assertNotNull(usuarioResponse);
        assertNotNull("Jose", usuarioResponse.getName());
    }

    @Test
    void whenSaveUser() {
        User usuario = new User(1L, "Jose", "890ad");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(usuario);
        User usuarioResponse = userService.save(usuario);
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
        assertNotNull(usuarioResponse);
        assertNotNull("Jose", usuarioResponse.getName());
    }

    @Test
    void whenSaveUserViolateConstraint() throws Exception {
        User usuario = new User(1L, "Jose", "890ad");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new RuntimeException("Error al guardar un usuario"));
        User usuarioResponse = userService.save(usuario);
        Mockito
                .verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
        assertNull(usuarioResponse);

    }

    @Test
    void whenDeleteUser() {
        Mockito
                .doNothing()
                .when(userRepository)
                .deleteById(Mockito.anyLong());
        userService.deleteUser(1L);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(Mockito.anyLong());
    }

    @Test
    void whenUpdateUser() {
        User usuario = new User(1L, "Jose", "890ad");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(usuario);
        User usuarioResponse = userService.updateUser(usuario);
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
        assertNotNull(usuarioResponse);
        assertEquals("Jose", usuarioResponse.getName());
    }
}