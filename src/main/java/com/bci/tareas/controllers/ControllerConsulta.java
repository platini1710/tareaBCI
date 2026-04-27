package com.bci.tareas.controllers;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.bci.tareas.dto.Login;
import com.bci.tareas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;




@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/consulta/usuarios")
@RequiredArgsConstructor
public class ControllerConsulta {

    // 1. Declarar como private final
    private final RegistraUsuarioServices registraUsuarioServices;
    private final ConsultaUsuarioService consultaUsuarioService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(ControllerConsulta.class);
    DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a z");
    String str = "";

    @RequestMapping(method = RequestMethod.GET, value = "/allUsuarios")
    @ResponseBody
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        logger.info("Obteniendo todos los usuarios");

        // Formatear fecha si la necesitas para logs o auditoría
        String str = ZDT_FORMATTER.format(ZonedDateTime.now());

        // Obtener la lista desde el servicio
        List<UsuarioDTO> listAllUsuarios = consultaUsuarioService.findAllUsuario();

        // Retornar la lista con el status 200 OK
        return ResponseEntity.ok(listAllUsuarios);


    }


    /**
     * Gets users by id.
     *
     * @param userId the user id
     * @return the users by id
     * @throws ResourceNotFoundException the resource not found exception
     */

    @GetMapping("/login/{id}")
    public ResponseEntity<UsuarioDTO> getUsuariosById(@PathVariable String id) {
        logger.info("Iniciando búsqueda para ID: {}", id);

        // 1. Generación de UUID delegada o simplificada
        String uuid = UUID.nameUUIDFromBytes(id.getBytes()).toString();
        logger.info("UUID generado: {}", uuid);

        // 2. Uso de Optional para evitar if-else manual y retornar 404 limpio
        return Optional.ofNullable(consultaUsuarioService.findUsuario(uuid))
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElseGet(() -> {
                    logger.warn("Usuario no encontrado para UUID: {}", uuid);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping(value = "/login-auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateWithBody(@RequestBody Login authRequest) {
        logger.info("Intento de autenticación vía Body para ID: {}", authRequest.id());

        // 1. Generar el UUID basado en el ID del body
        String uuid = UUID.nameUUIDFromBytes(authRequest.id().getBytes()).toString();

        // 2. Buscar el usuario
        UsuarioDTO userFound = consultaUsuarioService.findUsuario(uuid);

        // 3. Validar credenciales
        if (userFound != null && userFound.getPassword().equals(authRequest.password())) {

            // 4. Generar el Token
            String token = jwtService.generateToken(userFound.getPassword());

            // 5. Construir respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("name", userFound.getName());
            response.put("lastLogin", ZDT_FORMATTER.format(ZonedDateTime.now()));

            return ResponseEntity.ok(response);
        }
        // 6. Error de autenticación
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensaje", "ID o password incorrectos"));
    }
}