package com.gofore.movie.application.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private Long year;
	
	private List<String> genres;
	
	private Integer ageLimit;
	
	private Integer rating;
	
	private String synopsis;
	
	private PersonDto director;
	
	private List<PersonDto> actors;
	
	

}