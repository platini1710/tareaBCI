package com.bci.tareas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RecursoNoEncontradoException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Recurso no encontrado");
        respuesta.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND); // 404 Bad Request.
    }

    // 2. NUEVO Manejador para Correo Ya Existe (409 Conflict)
    @ExceptionHandler(EmailYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleEmailConflict(EmailYaExisteException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("Error", ex.getMessage());
        // Usamos CONFLICT (409) para duplicados
        return new ResponseEntity<>(respuesta, HttpStatus.CONFLICT);
    }
}
