package com.bci.tareas.services;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.model.Phone;

public interface RegistraUsuarioServices {
	public RespuestaDTO saveUsuario(UsuarioDTO usuario);

	public Phone update(Phone phone);

	public void delete(String id);

}