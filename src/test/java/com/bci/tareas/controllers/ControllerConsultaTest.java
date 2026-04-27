package com.bci.tareas.controllers;

import com.bci.tareas.dto.Login;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.security.JwtService;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControllerConsulta.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerConsultaTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistraUsuarioServices registraUsuarioServices;

    @MockBean
    private ConsultaUsuarioService consultaUsuarioService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /allUsuarios - Debería retornar lista de usuarios")
    void getAllUsuarios_Success() throws Exception {
        // Given
        UsuarioDTO user = new UsuarioDTO(1, "Augusto", "pass123");
        System.out.println("nombre " + user.getName());
        List<UsuarioDTO> list = Arrays.asList(user);
        when(consultaUsuarioService.findAllUsuario()).thenReturn(list);

        // When & Then
        mockMvc.perform(get("/consulta/usuarios/allUsuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

    }

    @Test
    @DisplayName("GET /login/{id} - Usuario encontrado")
    void getUsuariosById_Found() throws Exception {
        // Given
        String id = "123";
        String uuid = UUID.nameUUIDFromBytes(id.getBytes()).toString();
        UsuarioDTO user = new UsuarioDTO(1, "Augusto", "pass123");

        when(consultaUsuarioService.findUsuario(uuid)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/consulta/usuarios/login/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /login/{id} - Usuario no encontrado")
    void getUsuariosById_NotFound() throws Exception {
        // Given
        String id = "999";
        String uuid = UUID.nameUUIDFromBytes(id.getBytes()).toString();
        when(consultaUsuarioService.findUsuario(uuid)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/consulta/usuarios/login/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /login-auth - Autenticación exitosa")
    void authenticateWithBody_Success() throws Exception {
        // Given
        Login loginRequest = new Login("123", "pass123");
        String uuid = UUID.nameUUIDFromBytes("123".getBytes()).toString();
        UsuarioDTO user = new UsuarioDTO(1, "Augusto", "pass123");
        user.setId("1");
        user.setName("Augusto");
        user.setPassword("pass123");
        when(consultaUsuarioService.findUsuario(uuid)).thenReturn(user);
        when(jwtService.generateToken(anyString())).thenReturn("mocked-jwt-token");

        // When & Then
        mockMvc.perform(post("/consulta/usuarios/login-auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    @DisplayName("POST /login-auth - Credenciales incorrectas")
    void authenticateWithBody_Unauthorized() throws Exception {
        // Given
        Login loginRequest = new Login("123", "wrong-pass");
        String uuid = UUID.nameUUIDFromBytes("123".getBytes()).toString();
        UsuarioDTO user = new UsuarioDTO(1, "Augusto", "pass123");
        user.setId("1");
        user.setName("Augusto");
        user.setPassword("pass123");
        when(consultaUsuarioService.findUsuario(uuid)).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/consulta/usuarios/login-auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("ID o password incorrectos"));
    }
}