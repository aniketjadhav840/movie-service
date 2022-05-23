package com.gofore.movie.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gofore.movie.application.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>  {
	
	@EntityGraph(
		    type = EntityGraphType.FETCH,
		    attributePaths = {
		      "genres",
		      "people",
		      "people.person"
		    }
		  )
	List<Movie> findAll();

}
