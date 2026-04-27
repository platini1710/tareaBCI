package com.bci.tareas.dto;

import com.bci.tareas.helper.Constantes;
import com.bci.tareas.model.Phone;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Setter
@EqualsAndHashCode()
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioDTO {
    @NotBlank(message = Constantes.ID_NULL)
	private String id;
    @NotBlank(message = Constantes.NAME_NULL)
	private String name;
    @NotBlank(message = Constantes.PWD_NULL)
	private String password;
    @NotBlank(message = Constantes.EMAIL_NULL)
// Esto suele ser más riguroso en versiones recientes
    @Email(message = Constantes.EMAIL_INVALID, flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

	private String created;

	private String lastLogin;

	private String token;

	private boolean isActive;

	private PhoneDTO phone;


    public UsuarioDTO(int id, String name, String password) {
    }
}
