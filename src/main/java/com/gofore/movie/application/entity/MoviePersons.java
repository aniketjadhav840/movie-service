package com.gofore.movie.application.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "movies", name = "movie_persons")
public class MoviePersons implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MoviePersonId moviePersonsId;

	@ManyToOne
	@MapsId("movieId")
    @JoinColumn(name = "movie_id")
	private Movie movie;

	@ManyToOne
	@MapsId("personId")
    @JoinColumn(name = "person_id")
	private Person person;

	
	
	
	
	
	

}
