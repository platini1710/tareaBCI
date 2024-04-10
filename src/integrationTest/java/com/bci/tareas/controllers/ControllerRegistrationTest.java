package com.bci.tareas.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServicesImpl;
import com.google.gson.Gson;

public class ControllerRegistrationTest {

	private MockMvc mockMvc;

	UsuarioDTO usuario1 = new UsuarioDTO("98c6f2c2-287f-3c73-8ea3-d40ae7ec3ff2", "augusto Espinoza", "A23bbbccc@",
			"aespinoza3010@gmail.com", "12-08-1987", "ass", "assa", true,null);
	RespuestaDTO respuestaDTO = new RespuestaDTO();

	@Mock
	private RegistraUsuarioServicesImpl registraUsuarioServicesImpl;

	@InjectMocks
	private ControllerRegistation controllerRegistation;
	@Mock
	private ConsultaUsuarioService consultaUsuarioService;

	List<RespuestaDTO> listAllUsuarios = new ArrayList<>();

	ResponseEntity<List<RespuestaDTO>> respuesta;

	@BeforeEach
	public void setup() {
		respuestaDTO.setCreated("12-08-1987");
		respuestaDTO.setName("augusto Espinoza");
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controllerRegistation).build();
	}

	/*
	 * test de integracion exitoso
	 */
	@Test
	void saveRegistro_integrationOK() throws Exception {
		Gson gson = new Gson();
		String json = gson.toJson(usuario1);
		Mockito.when(consultaUsuarioService.findUsuario("98c6f2c2-287f-3c73-8ea3-d40ae7ec3ff2")).thenReturn(null);
		Mockito.when(registraUsuarioServicesImpl.saveUsuario(any(UsuarioDTO.class))).thenReturn(respuestaDTO);
		mockMvc.perform(MockMvcRequestBuilders.post("/registro/usuario/sign-up").contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isCreated());

	}

	/*
	 * ttest exitoso
	 */
	@Test
	void saveRecordSucesfull_201OK() {
		Mockito.when(consultaUsuarioService.findUsuario("98c6f2c2-287f-3c73-8ea3-d40ae7ec3ff2")).thenReturn(null);
		Mockito.when(registraUsuarioServicesImpl.saveUsuario(any(UsuarioDTO.class))).thenReturn(respuestaDTO);
		ResponseEntity<RespuestaDTO> respuesta = controllerRegistation.saveUsuario(usuario1);
		assertEquals(201, respuesta.getStatusCodeValue());
	}

	/*
	 * test que lanza error 400 cuando el nombre es nulo
	 */
	@Test
	void saveRecordNameNullFault_400NOK() {
		usuario1.setName(null);
		Mockito.when(consultaUsuarioService.findUsuario("98c6f2c2-287f-3c73-8ea3-d40ae7ec3ff2")).thenReturn(null);
		Mockito.when(registraUsuarioServicesImpl.saveUsuario(any(UsuarioDTO.class))).thenReturn(respuestaDTO);
		ResponseEntity<RespuestaDTO> respuesta = controllerRegistation.saveUsuario(usuario1);
		assertEquals(400, respuesta.getStatusCodeValue());
	}
	/*
	 * test que lanza error 400 cuando el email es no valido
	 */
	@Test
	void saveRecordEmailErrorFault_400NOK() {
		usuario1.setEmail("aespinoza@gm@l.com");
		Mockito.when(consultaUsuarioService.findUsuario("98c6f2c2-287f-3c73-8ea3-d40ae7ec3ff2")).thenReturn(null);
		Mockito.when(registraUsuarioServicesImpl.saveUsuario(any(UsuarioDTO.class))).thenReturn(respuestaDTO);
		ResponseEntity<RespuestaDTO> respuesta = controllerRegistation.saveUsuario(usuario1);
		assertEquals(400, respuesta.getStatusCodeValue());
	}
}