package com.bci.tareas.controllers;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.helper.ErrorResp;
import com.bci.tareas.helper.Utils;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;

import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/consulta/usuarios")

public class ControllerConsulta {

	@Autowired
	private RegistraUsuarioServices registraUsuarioServices;
	@Autowired
	private ConsultaUsuarioService consultaUsuarioService;

	private static final Logger logger = LoggerFactory.getLogger(ControllerConsulta.class);
	DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a z");
	String str = "";
	@ApiOperation(value = "Find todos los Usuarios", notes = "Return clase Respuesta " + "resultado ")
	@RequestMapping(method = RequestMethod.GET,  value = "/allUsuarios")
	@ResponseBody
	public ResponseEntity<List<RespuestaDTO>>  getAllUsuarios() {
		logger.info("todo los usuarios");
		 str = ZDT_FORMATTER.format(ZonedDateTime.now());
		 List<RespuestaDTO> listAllUsuarios = new ArrayList<>();

		try {
			listAllUsuarios = consultaUsuarioService.findAllUsuario();


			

		} catch (Exception e) {
			RespuestaDTO respuestaDTO=new RespuestaDTO();
			respuestaDTO.setMsg("se ha producido un error ");
			listAllUsuarios.add(respuestaDTO);
			logger.error(e.getMessage(), e);
			return new ResponseEntity<List<RespuestaDTO>>(listAllUsuarios, HttpStatus.INTERNAL_SERVER_ERROR); // XXX
		
		}
		Optional<List<RespuestaDTO>> list=Optional.of(listAllUsuarios);
		if (listAllUsuarios==null)
			return new ResponseEntity<List<RespuestaDTO>>(listAllUsuarios, HttpStatus.NOT_FOUND);
		else
		return new ResponseEntity<List<RespuestaDTO>>(listAllUsuarios, HttpStatus.OK); // XXX
	}


	/**
	 * Gets users by id.
	 *
	 * @param userId the user id
	 * @return the users by id
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@ApiOperation(value = "Find un tarea por  id de la tarea", notes = "Return tarea "
			+ "resultado en campoProducto maneja su propias excepcion")
	@GetMapping("/login/{id}")
	@RequestMapping(method = RequestMethod.GET,  value = "/login/{id}")
	@ResponseBody
	public ResponseEntity<UsuarioDTO> getUsuariosById(@PathVariable(value ="id", required = true) String id) {
		str = ZDT_FORMATTER.format(ZonedDateTime.now());
		logger.info("id  <:::" + id);
		HttpHeaders headers = new HttpHeaders();
		UsuarioDTO usuario=new UsuarioDTO();
		Usuario user = new Usuario(id);
		try {
			
			RespuestaDTO r=new RespuestaDTO();

		    
	
			byte[] byte_id=user.getId().getBytes();
			UUID uuid = UUID.nameUUIDFromBytes(byte_id);
	
			logger.info("uuid id  <:::" + uuid.toString());
			logger.info("user  <:::" + user);
		 usuario=consultaUsuarioService.findUsuario(uuid.toString());
		    if (usuario==null) {
		    	logger.info("user = null :::"  );
				return 	 new ResponseEntity<>(
						usuario, headers, HttpStatus.NOT_FOUND);
		    } else 
			return 	 new ResponseEntity<>(
					usuario, headers, HttpStatus.OK);
		} 

		catch (Exception e) {

			logger.error(e.getMessage(),e);
			return new ResponseEntity<>(
					usuario,  HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	

}
