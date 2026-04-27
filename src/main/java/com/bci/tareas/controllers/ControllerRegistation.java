package com.bci.tareas.controllers;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.exception.EmailYaExisteException;
import com.bci.tareas.exception.RecursoNoEncontradoException;
import com.bci.tareas.helper.Constantes;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.UUID;


@RestController
@RequestMapping("/registro/usuario")
@RequiredArgsConstructor
public class ControllerRegistation {


    private final RegistraUsuarioServices registraUsuarioServices;
    private final ConsultaUsuarioService consultaUsuarioService;

    DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");
    private static final Logger logger = LoggerFactory.getLogger(ControllerRegistation.class);

    /**
     * Update user response entity.
     *
     * @param producto the user id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */

    private String generarUUID(String idOriginal) {
        byte[] byteId = idOriginal.getBytes();
        return UUID.nameUUIDFromBytes(byteId).toString();
    }

    private void validarDuplicidad(UsuarioDTO usuarioDTO, String uuid) {
        // Verificar si el UUID ya existe
        if (consultaUsuarioService.findUsuario(uuid) != null) {
            throw new RecursoNoEncontradoException(Constantes.USER_EXIST);
        }

        // Verificar si el Email ya existe

        if (!consultaUsuarioService.findEmail(usuarioDTO.getEmail()).isEmpty()) {
            throw new EmailYaExisteException(Constantes.EMAIL_EXIST);
        }

    }

    @Transactional
    public RespuestaDTO processSignUp(UsuarioDTO usuarioDTO) {
        // 1. Generar el UUID basado en el ID original
        String uuid = generarUUID(usuarioDTO.getId());
        usuarioDTO.setId(uuid);

        // 2. Validaciones de Negocio (Duplicidad)
        validarDuplicidad(usuarioDTO, uuid);

        // 3. Preparar datos adicionales (como el teléfono)
        if (usuarioDTO.getPhone() != null) {
            usuarioDTO.getPhone().setId(uuid);
        }

        // 4. Guardar en la base de datos
        // Aquí llamarías a tu lógica de persistencia real
        return registraUsuarioServices.saveUsuario(usuarioDTO);
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RespuestaDTO> saveUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("grabar tareas");

        RespuestaDTO response = processSignUp(usuarioDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
