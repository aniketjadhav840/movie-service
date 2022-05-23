
package com.gofore.movie.application.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(schema = "movies", name = "person")
public class Person implements Serializable {

	private final static long serialVersionUID = 2490895805052705235L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "person_id")
	private Long personId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    Set<MoviePersons> movies;

	@Override
	public String toString() {
		return "Person [personId=" + personId + ", firstName=" + firstName + ", lastName=" + lastName + ", movies="
				+ movies + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, movies, personId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Person)) {
			return false;
		}
		Person other = (Person) obj;
		return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(movies, other.movies) && Objects.equals(personId, other.personId);
	}

	

}
