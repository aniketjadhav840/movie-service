package com.gofore.movie.application.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long movieId;

	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@NotNull(message = "Year is mandatory")
	@Positive(message = "Invalid data for year")
	private Long year;
	
	private List<String> genres;
	
	private Integer ageLimit;
	
	private Integer rating;
	
	private String synopsis;
	
	@NotNull(message = "Information for director is mandatory")
	private PersonDto director;
	
	@NotEmpty(message = "Information for Actors is mandatory")
	private List<PersonDto> actors;
	
	

}
