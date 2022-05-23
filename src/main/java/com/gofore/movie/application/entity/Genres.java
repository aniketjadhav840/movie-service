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
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "movies", name = "genres")
public class Genres implements Serializable {

	private final static long serialVersionUID = 2490895805052705235L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "genres_id")
	private Long genresId;

	@Column(name = "value")
	private String value;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "genres")
	@JsonIgnore
	private Set<Movie> movies;

}
