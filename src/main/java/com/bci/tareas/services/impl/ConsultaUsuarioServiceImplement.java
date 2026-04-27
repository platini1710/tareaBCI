package com.bci.tareas.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bci.tareas.exception.RecursoNoEncontradoException;
import com.bci.tareas.services.ConsultaUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bci.tareas.dto.PhoneDTO;
import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
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

        return usuarioDataRestRepository.findById(id)
                .map(usuarioEntity -> {
                    // Mapeamos el teléfono si existe
                    PhoneDTO phone = Optional.ofNullable(usuarioEntity.getPhone())
                            .map(p -> new PhoneDTO(p.getId(), p.getNumber(), p.getCityCode(), p.getCountryCode()))
                            .orElse(null);

                    // Retornamos el DTO construido
                    return new UsuarioDTO(
                            usuarioEntity.getId(),
                            usuarioEntity.getName(),
                            usuarioEntity.getPassword(),
                            usuarioEntity.getEmail(),
                            usuarioEntity.getCreated(),
                            usuarioEntity.getLastLogin(),
                            usuarioEntity.getToken(),
                            usuarioEntity.getActive(),
                            phone // Aquí pasamos el objeto 'phone' que mapeamos arriba
                    );
                })
                .orElse(null); // Si findById devolvió vacío, todo el flujo salta aquí y retorna null

    }

	@Override
	public List<UsuarioDTO> findAllUsuario() {
		// TODO Auto-generated method stub
		List<UsuarioDTO> listRespuesta = new ArrayList<>();
		List<Usuario> listAllUsuarios = usuarioDataRestRepository.findAll();
		listAllUsuarios.forEach(c -> {
			logger.info("entro al for each:::" + c.getName());
			RespuestaDTO user = new RespuestaDTO();

			if (c.getActive())
				user.setIsActive("true");
			else
				user.setIsActive("false");

			PhoneDTO phone = new PhoneDTO();
			if (c.getPhone() != null) {
				phone.setCityCode(c.getPhone().getCityCode());
				phone.setNumber(c.getPhone().getNumber());
				phone.setCountryCode(c.getPhone().getCountryCode());
			}
			user.setPhone(phone);


            UsuarioDTO usuarioDTO=  new UsuarioDTO(c.getId(), c.getName(),
                    c.getPassword(),
                    c.getEmail(),
                    c.getCreated(),
                    c.getLastLogin(),
                    c.getToken(),
                    c.getActive(),
                    user.getPhone());
			listRespuesta.add(usuarioDTO);
		});
		return listRespuesta;
	}

	@Override
	public List<Usuario> findEmail(String email) {
		// TODO Auto-generated method stub
		return usuarioDataRestRepository.findEmail(email);
	}
}
