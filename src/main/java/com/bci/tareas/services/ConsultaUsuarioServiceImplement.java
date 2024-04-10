package com.bci.tareas.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bci.tareas.dto.PhoneDTO;
import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.model.Phone;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.repositorio.UsuarioDataRestRepository;

@Service
public class ConsultaUsuarioServiceImplement implements ConsultaUsuarioService {

	private static final Logger logger = LoggerFactory.getLogger(ConsultaUsuarioServiceImplement.class);
	@Autowired
	private UsuarioDataRestRepository usuarioDataRestRepository;

	@Override
	public UsuarioDTO findUsuario(String id) {
		// TODO Auto-generated method stub
		UsuarioDTO usuarioDTO = new UsuarioDTO();

		if (usuarioDataRestRepository.findById(id).isPresent()) {
			Usuario usuario = usuarioDataRestRepository.findById(id).get();

			usuarioDTO.setToken(usuario.getToken());
			usuarioDTO.setActive(usuario.getActive());
			usuarioDTO.setLastLogin(usuario.getLastLogin());
			usuarioDTO.setName(usuario.getName());
			usuarioDTO.setId(usuario.getId());
			usuarioDTO.setPassword(usuario.getPassword());
			PhoneDTO phoneDTO = new PhoneDTO();
			Phone phone = usuario.getPhone();
			if (phone != null) {

				phoneDTO.setCityCode(phone.getCityCode());
				phoneDTO.setNumber(phone.getNumber());
				phoneDTO.setCountryCode(phone.getCountryCode());
			}
			usuarioDTO.setPhone(phoneDTO);
			usuarioDTO.setCreated(usuario.getCreated());
			return usuarioDTO;
		} else
			return null;
	}

	@Override
	public List<RespuestaDTO> findAllUsuario() {
		// TODO Auto-generated method stub
		List<RespuestaDTO> listRespuesta = new ArrayList<>();
		List<Usuario> listAllUsuarios = usuarioDataRestRepository.findAll();
		listAllUsuarios.forEach(c -> {
			logger.info("entro al for each:::" + c.getName());
			RespuestaDTO r = new RespuestaDTO();
			r.setCreated(c.getCreated());
			r.setEmail(c.getEmail());
			r.setUuid(c.getId());
			if (c.getActive())
				r.setIsActive("true");
			else
				r.setIsActive("false");
			r.setName(c.getName());
			r.setToken(c.getToken());
			r.setLastLogin(c.getLastLogin());
			PhoneDTO phone = new PhoneDTO();
			if (c.getPhone() != null) {
				phone.setCityCode(c.getPhone().getCityCode());
				phone.setNumber(c.getPhone().getNumber());
				phone.setCountryCode(c.getPhone().getCountryCode());
			}
			r.setPhone(phone);
			listRespuesta.add(r);
		});
		return listRespuesta;
	}

	@Override
	public List<Usuario> findEmail(String email) {
		// TODO Auto-generated method stub
		return usuarioDataRestRepository.findEmail(email);
	}
}
