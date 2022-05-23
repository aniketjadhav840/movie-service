package com.gofore.movie.application.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is mandatory")
	private String firstName;

	@NotBlank(message = "Name is mandatory")
	private String lastName;

}
