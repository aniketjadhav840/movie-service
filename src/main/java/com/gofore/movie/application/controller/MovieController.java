package com.gofore.movie.application.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gofore.movie.application.dto.MovieDto;
import com.gofore.movie.application.dto.SearchDto;
import com.gofore.movie.application.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	MovieService movieService;

	@GetMapping("/")
	public ResponseEntity<List<MovieDto>> getAllMovies() {

		List<MovieDto> movies = movieService.getAllMovies();

		if (!movies.isEmpty()) {
			return new ResponseEntity<List<MovieDto>>(movies, HttpStatus.OK);
		}
		return new ResponseEntity<List<MovieDto>>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/movie")
	public ResponseEntity<String> saveMovie(@Valid @RequestBody MovieDto movie) {
		movieService.saveMovie(movie);
		return new ResponseEntity<String>("Data Saved Successfully.", HttpStatus.CREATED);
	}
	
	@PostMapping("/")
	public ResponseEntity<List<MovieDto>> searchMovies(@RequestBody SearchDto searchTerm) {

		List<MovieDto> movies = movieService.searchMovies(searchTerm);

		if (!movies.isEmpty()) {
			return new ResponseEntity<List<MovieDto>>(movies, HttpStatus.OK);
		}
		return new ResponseEntity<List<MovieDto>>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/movie/{movieId}")
	public ResponseEntity<String> deleteMovie(@PathVariable(name = "movieId") Long movieId) {
		movieService.deleteMovie(movieId);
		return new ResponseEntity<String>("Data Deleted Successfully.", HttpStatus.OK);
	}

}
