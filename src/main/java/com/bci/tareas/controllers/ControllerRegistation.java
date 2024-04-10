package com.bci.tareas.controllers;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.exception.ResourceFoundException;
import com.bci.tareas.helper.Constantes;
import com.bci.tareas.helper.EmailValidatorSimple;
import com.bci.tareas.helper.ErrorException;
import com.bci.tareas.helper.PasswordValidator;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/registro/usuario")

public class ControllerRegistation {

	@Autowired
	private RegistraUsuarioServices registraUsuarioServices;
	@Autowired
	private ConsultaUsuarioService consultaUsuarioService;

	DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");
	private static final Logger logger = LoggerFactory.getLogger(ControllerRegistation.class);

	/**
	 * Update user response entity.
	 *
	 * @param producto the user id
	 * @return the response entity
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@ApiOperation(value = "guarda un registro de la tarea  en caso de que exista mandara el respectivo msg ", notes = "Return clase Respuesta "
			+ " retorna el resultado en campo Msg ")
	@RequestMapping(value = "/sign-up", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<RespuestaDTO> saveUsuario(@RequestBody UsuarioDTO usuario) {
		logger.info("grabar tareas");
		RespuestaDTO response = new RespuestaDTO();		
		try {
			// valida nombre
			if (usuario.getId() == null || "".equals(usuario.getId().trim())) {
				logger.debug("id  es null");
				throw new ResourceFoundException(Constantes.ID_NULL);
			}
			byte[] byte_id = usuario.getId().getBytes();
			UUID uuid = UUID.nameUUIDFromBytes(byte_id);

			usuario.setId(uuid.toString());
			logger.info("uuid " + uuid.toString());

			UsuarioDTO user = consultaUsuarioService.findUsuario(uuid.toString());

			logger.info("In prod" + user);

			/*
			 * validacion de parametros de entrada
			 */

			if (user != null) {
				logger.debug("usuario existe");
				throw new ResourceFoundException(Constantes.USER_EXIST);
			}
			// valida nombre
			if (usuario.getName() == null || "".equals(usuario.getName().trim())) {
				logger.debug("nombre  es null");
				throw new ResourceFoundException(Constantes.NAME_NULL);
			}
			if (usuario.getPassword() == null || "".equals(usuario.getPassword().trim())) {
				logger.debug("password es null");
				throw new ResourceFoundException(Constantes.PWD_NULL);
			}
			// es valido el email
			if (usuario.getEmail() == null || "".equals(usuario.getEmail().trim())) {
				logger.debug("email null");
				throw new ResourceFoundException(Constantes.EMAIL_NULL);
			}
			// es valido el email
			if (!EmailValidatorSimple.isValid(usuario.getEmail())) {
				logger.debug("email invalido");
				throw new ResourceFoundException(Constantes.EMAIL_INVALID);
			}

			// existe ya el email
			if (consultaUsuarioService.findEmail(usuario.getEmail()) != null
					&& consultaUsuarioService.findEmail(usuario.getEmail()).size() > 0) {
				logger.debug("email ya existe");
				throw new ResourceFoundException(Constantes.EMAIL_EXIST);
			}

			if (!PasswordValidator.isValid(usuario.getPassword())) {
				logger.debug("password  invalida");
				throw new ErrorException(Constantes.PASSWORD_INVALID);
			}

			if (usuario.getPhone() != null) {
				usuario.getPhone().setId(uuid.toString());
			}
			/*
			 * si todo esta ok se guarda registro en la tabla
			 */
			response = registraUsuarioServices.saveUsuario(usuario);
		} catch (ResourceFoundException e) {
			response.setMsg(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		} catch (ErrorException e) {
			response.setMsg(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);

		} catch (Exception e) {
			logger.debug("error" + e.getMessage());
			response.setMsg( e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		/*
		 * se responde 201 de que se ha creado un nuevo registro
		 * 
		 */
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

}
