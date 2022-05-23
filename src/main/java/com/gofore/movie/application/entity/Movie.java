
package com.gofore.movie.application.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "movies", name = "movie")
public class Movie implements Serializable {

	private final static long serialVersionUID = -1136708134650214362L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "movie_id")
	private Long movieId;

	@Column(name = "name")
	private String name;

	@Column(name = "year")
	private Long year;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(schema = "movies", name = "movie_genres", 
		joinColumns = { @JoinColumn(name = "movie_id") }, 
		inverseJoinColumns = {	@JoinColumn(name = "genres_id") })
	private Set<Genres> genres;

	@Column(name = "age_limit")
	private Integer ageLimit;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "synopsis")
	private String synopsis;

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
	Set<MoviePersons> people;

}
