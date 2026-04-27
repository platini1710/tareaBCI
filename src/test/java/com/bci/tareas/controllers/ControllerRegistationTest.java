package com.bci.tareas.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.bci.tareas.dto.RespuestaDTO;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.bci.tareas.dto.PhoneDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.exception.EmailYaExisteException;
import com.bci.tareas.exception.RecursoNoEncontradoException;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ControllerRegistationTest {

    @Mock
    private RegistraUsuarioServices registraUsuarioServices;

    @Mock
    private ConsultaUsuarioService consultaUsuarioService;

    @InjectMocks
    private ControllerRegistation controller;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId("12345");
        usuarioDTO.setEmail("augusto@ejemplo.com");

        PhoneDTO phone = new PhoneDTO();
        phone.setNumber(987654321);
        usuarioDTO.setPhone(phone);
    }

    @Test
    @DisplayName("Debe registrar usuario exitosamente y generar UUID")
    void saveUsuario_Success() {
        // GIVEN
        String expectedUuid = UUID.nameUUIDFromBytes("12345".getBytes()).toString();

        // Configuramos mocks para que no encuentren duplicados
        when(consultaUsuarioService.findUsuario(expectedUuid)).thenReturn(null);
        when(consultaUsuarioService.findEmail(anyString())).thenReturn(Collections.emptyList());

        RespuestaDTO mockRespuesta = new RespuestaDTO();
        mockRespuesta.setMsg("Registro exitoso");
        when(registraUsuarioServices.saveUsuario(any(UsuarioDTO.class))).thenReturn(mockRespuesta);

        // WHEN
        ResponseEntity<RespuestaDTO> response = controller.saveUsuario(usuarioDTO);

        // THEN
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registro exitoso", response.getBody().getMsg());

        // Verificamos que el UUID se asignó correctamente al DTO y al teléfono
        assertEquals(expectedUuid, usuarioDTO.getId());
        assertEquals(expectedUuid, usuarioDTO.getPhone().getId());

        verify(registraUsuarioServices, times(1)).saveUsuario(any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("Debe lanzar EmailYaExisteException cuando el correo ya está en la base de datos")
    void saveUsuario_EmailDuplicated_ThrowsException() {
        // GIVEN
        String expectedUuid = UUID.nameUUIDFromBytes("12345".getBytes()).toString();
        when(consultaUsuarioService.findUsuario(expectedUuid)).thenReturn(null);

        // Simulamos que el email ya existe devolviendo una lista con un objeto
        when(consultaUsuarioService.findEmail("augusto@ejemplo.com"))
                .thenReturn(List.of(new Usuario()));

        // WHEN & THEN
        assertThrows(EmailYaExisteException.class, () -> {
            controller.saveUsuario(usuarioDTO);
        });

        // Verificamos que NUNCA se llamó al servicio de guardado
        verify(registraUsuarioServices, never()).saveUsuario(any());
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException cuando el UUID ya existe")
    void saveUsuario_UuidDuplicated_ThrowsException() {
        // GIVEN
        String expectedUuid = UUID.nameUUIDFromBytes("12345".getBytes()).toString();

        // Simulamos que el usuario ya existe por su UUID
        when(consultaUsuarioService.findUsuario(expectedUuid)).thenReturn(new UsuarioDTO());

        // WHEN & THEN
        assertThrows(RecursoNoEncontradoException.class, () -> {
            controller.saveUsuario(usuarioDTO);
        });

        verify(registraUsuarioServices, never()).saveUsuario(any());
    }
}