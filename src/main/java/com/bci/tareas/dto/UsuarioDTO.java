package com.bci.tareas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Setter
@EqualsAndHashCode()
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
	@NonNull
	private String id;

	private String name;

	private String password;

	private String email;

	private String created;

	private String lastLogin;

	private String token;

	private boolean isActive;

	private PhoneDTO phone;
}
