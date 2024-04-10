package com.bci.tareas.services;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bci.tareas.controllers.ControllerRegistation;
import com.bci.tareas.dto.PhoneDTO;
import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.exception.ResourceFoundException;
import com.bci.tareas.helper.Constantes;
import com.bci.tareas.helper.ErrorException;
import com.bci.tareas.helper.ErrorResp;
import com.bci.tareas.helper.Utils;
import com.bci.tareas.model.Phone;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.repositorio.PhoneDataRestRepository;
import com.bci.tareas.repositorio.UsuarioDataRestRepository;

@Component
public class RegistraUsuarioServicesImpl implements RegistraUsuarioServices {
	@Autowired
	private UsuarioDataRestRepository usuarioDataRestRepository;
	@Autowired
	private PhoneDataRestRepository phoneDataRestRepository;
	private static final Logger logger = LoggerFactory.getLogger(RegistraUsuarioServicesImpl.class);
	DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");

	public RespuestaDTO saveUsuario(UsuarioDTO usuarioDto) {
		// TODO Auto-generated method stub
		String str = ZDT_FORMATTER.format(ZonedDateTime.now());
		byte[] byte_id = usuarioDto.getId().getBytes();
		UUID uuid = UUID.nameUUIDFromBytes(byte_id);
		RespuestaDTO response = new RespuestaDTO();
		logger.debug("antes de grabar");
		usuarioDto.setCreated(str);
		logger.debug("str " + str);
		usuarioDto.setLastLogin(str);
		logger.debug("uuid  " + uuid.toString());
		usuarioDto.setToken(Utils.getJWTToken(usuarioDto.getName()));
		logger.debug("token " + Utils.getJWTToken(usuarioDto.getName()));
		usuarioDto.setActive(true);
		response.setUuid(uuid.toString());
		// response.setMsg(Constantes.insert);
		response.setPassword(usuarioDto.getPassword());
		response.setName(usuarioDto.getName());
		response.setLastLogin(usuarioDto.getLastLogin());
		response.setCreated(str);
		response.setEmail(usuarioDto.getEmail());
		response.setToken(usuarioDto.getToken());
		response.setIsActive("true");
		Phone phone =new Phone();
		phone.setId(usuarioDto.getId());
		if (usuarioDto.getPhone() != null) {
			phone.setNumber(usuarioDto.getPhone().getNumber());
			phone.setCityCode(usuarioDto.getPhone().getCityCode());
			phone.setCountryCode(usuarioDto.getPhone().getCountryCode());
		}
		/*
		 * guardo en base de datos ,transformo el objeto dto a objeto de base de datos
		 */
		

		Usuario user=new Usuario(usuarioDto.getId(),usuarioDto.getName(),usuarioDto.getPassword(),usuarioDto.getEmail(),usuarioDto.getCreated(),
				usuarioDto.getLastLogin(),usuarioDto.getToken(),usuarioDto.isActive(),phone);
			usuarioDataRestRepository.save(user);
			// registraUsuarioServices.save(usuario);

	
		response.setMsg(Constantes.INSERTSUCESSFUL);
		return response;
	}

	public Phone update(Phone phone) {
		// TODO Auto-generated method stub

		return phoneDataRestRepository.save(phone);
	}

	public void delete(String id) {
		// TODO Auto-generated method stub
		usuarioDataRestRepository.deleteById(id);
	}

}