package com.gofore.movie.application.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class MoviePersonId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "movie_id")
	private Long movieId;

	@Column(name = "person_id")
	private Long personId;
	
	@Column(name = "role")
	private String role;

	@Override
	public int hashCode() {
		return Objects.hash(movieId, personId, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MoviePersonId)) {
			return false;
		}
		MoviePersonId other = (MoviePersonId) obj;
		return Objects.equals(movieId, other.movieId) && Objects.equals(personId, other.personId)
				&& Objects.equals(role, other.role);
	}
	
	


}
