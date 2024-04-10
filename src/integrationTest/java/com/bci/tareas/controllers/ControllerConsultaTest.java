package  com.bci.tareas.controllers;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.model.Phone;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.ConsultaUsuarioServiceImplement;
import com.bci.tareas.services.RegistraUsuarioServicesImpl;

public class ControllerConsultaTest   {
	
	private MockMvc mockMvc;

	Usuario usuario1=new Usuario("98c6f2c2-287f-3c73-8ea3-d40ae7ec3ff2","augusto Espinoza","A23bbbccc@","aespinoza3010@gmail.com","12-08-1987","ass","assa",true,new Phone());
	RespuestaDTO respuestaDTO=new RespuestaDTO();
	@InjectMocks                         
	private ControllerConsulta controllerConsulta;
	@Mock                         
	private RegistraUsuarioServicesImpl registraUsuarioServicesImpl;

	@InjectMocks
	private ControllerRegistation controllerRegistation;
	@Mock
    private ConsultaUsuarioService consultaUsuarioServiceImplement;
	
	 List<RespuestaDTO> listAllUsuarios = new ArrayList<>();
//	@InjectMocks
	//private ControllerRegistation controllerRegistation;
	 ResponseEntity<List<RespuestaDTO>> respuesta;
	@BeforeEach
	public  void setup() {
		respuestaDTO.setCreated("12-08-1987");
		respuestaDTO.setName("augusto Espinoza");
		listAllUsuarios.add(respuestaDTO)	;

		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders
		        .standaloneSetup(controllerConsulta)
		        .build();

	}
	

/*
 * test de integracion
 */
	@Test
	 void getAllRecordSucesfull_integrationOK() throws Exception{

		Mockito.when(consultaUsuarioServiceImplement.findAllUsuario()).thenReturn(listAllUsuarios);
		mockMvc.perform(MockMvcRequestBuilders.get("/consulta/usuarios/allUsuarios").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())  
		.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))  ;

	}
	@Test
	 void getAllRecordSucesfull_200OK() throws Exception{
	List<Usuario> records=new ArrayList<>(Arrays.asList(usuario1));
		Mockito.when(consultaUsuarioServiceImplement.findAllUsuario()).thenReturn(listAllUsuarios);
		 ResponseEntity<List<RespuestaDTO>> respuesta=	controllerConsulta.getAllUsuarios();
		 assertEquals(200, respuesta.getStatusCodeValue());
	}


}