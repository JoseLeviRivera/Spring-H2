package com.example.TestDemo.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Test
    void testUserConstructorAndGetters() {
        // Datos de prueba
        Long id = 1L;
        String name = "John Doe";
        String email = "john@example.com";

        // Crear un objeto User
        User user = new User(id, name, email);

        // Verificar los valores a través de los getters
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    void testUserSetter() {
        // Datos de prueba
        User user = new User();
        Long id = 1L;
        String name = "John Doe";
        String email = "john@example.com";

        // Establecer los valores a través de los setters
        user.setId(id);
        user.setName(name);
        user.setEmail(email);

        // Verificar los valores a través de los getters
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    void testUserEqualsAndHashCode() {
        // Datos de prueba
        Long id = 1L;
        String name = "John Doe";
        String email = "john@example.com";

        // Crear dos objetos User con los mismos valores
        User user1 = new User(id, name, email);
        User user2 = new User(id, name, email);

        // Verificar que los objetos sean iguales
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

}