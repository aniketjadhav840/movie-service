package com.gofore.movie.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gofore.movie.application.entity.Genres;

@Repository
public interface GenresRepository extends JpaRepository<Genres, Long> {
	
	List<Genres> findByValueIn(List<String> genresList);

}
