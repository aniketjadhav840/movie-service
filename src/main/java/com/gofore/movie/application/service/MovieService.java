package com.gofore.movie.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gofore.movie.application.dto.MovieDto;
import com.gofore.movie.application.dto.PersonDto;
import com.gofore.movie.application.dto.SearchDto;
import com.gofore.movie.application.entity.Genres;
import com.gofore.movie.application.entity.Movie;
import com.gofore.movie.application.entity.MoviePersonId;
import com.gofore.movie.application.entity.MoviePersons;
import com.gofore.movie.application.entity.Person;
import com.gofore.movie.application.repository.GenresRepository;
import com.gofore.movie.application.repository.MovieRepository;
import com.gofore.movie.application.repository.PersonRepository;

@Service
public class MovieService {

	private static final String ACTOR_ROLE = "Actor";

	private static final String DIRECTOR_ROLE = "Director";

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	PersonRepository personRepository;

	@Autowired
	GenresRepository genresRepository;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<MovieDto> getAllMovies() {
		List<Movie> movies = movieRepository.findAll();
		return mapMovieEntityToDto(movies);
	}

	private List<MovieDto> mapMovieEntityToDto(List<Movie> movies) {
		return movies.stream().map(movie -> MovieDto.builder().movieId(movie.getMovieId()).name(movie.getName())
				.year(movie.getYear())
				.genres(movie.getGenres().stream().map(Genres::getValue).collect(Collectors.toList()))
				.ageLimit(movie.getAgeLimit()).rating(movie.getRating()).synopsis(movie.getSynopsis())
				.actors(new ArrayList<>(movie.getPeople().stream()
						.filter(personRole -> ACTOR_ROLE.equals(personRole.getMoviePersonsId().getRole()))
						.map(person -> PersonDto.builder().firstName(person.getPerson().getFirstName())
								.lastName(person.getPerson().getLastName()).build())
						.collect(Collectors.toList())))
				.director(movie.getPeople().stream()
						.filter(personRole -> DIRECTOR_ROLE.equals(personRole.getMoviePersonsId().getRole()))
						.map(person -> PersonDto.builder().firstName(person.getPerson().getFirstName())
								.lastName(person.getPerson().getLastName()).build())
						.findFirst().orElse(null))
				.build()).collect(Collectors.toList());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveMovie(MovieDto movie) {

		List<Person> peoples = personRepository.findAll();

		List<Genres> genres = genresRepository.findByValueIn(movie.getGenres());

		Map<String, Person> personsMappedByFirstNameAndLastName = peoples.stream()
				.collect(Collectors.toMap(p -> p.getFirstName() + "_" + p.getLastName(), p -> p));

		Movie newMovie = Movie.builder().name(movie.getName()).year(movie.getYear()).ageLimit(movie.getAgeLimit())
				.rating(movie.getRating()).synopsis(movie.getSynopsis()).build();

		populateGenres(newMovie, movie.getGenres(),
				genres.stream().collect(Collectors.toMap(Genres::getValue, Function.identity())));

		populateActors(movie, personsMappedByFirstNameAndLastName, newMovie);

		populateDirector(movie, personsMappedByFirstNameAndLastName, newMovie);

		movieRepository.save(newMovie);

	}

	public void deleteMovie(Long movieId) {
		movieRepository.deleteById(movieId);

	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<MovieDto> searchMovies(SearchDto searchTerm) {

		Movie movieExample = Movie.builder().name(searchTerm.getName()).year(searchTerm.getYear())
				.ageLimit(searchTerm.getAgeLimit()).rating(searchTerm.getRating()).synopsis(searchTerm.getSynopsis())
				.build();

		if (searchTerm.getGenres() != null) {
			movieExample.setGenres(searchTerm.getGenres().stream().map(genres -> Genres.builder().value(genres).build())
					.collect(Collectors.toSet()));
		}

		if (searchTerm.getActors() != null) {
			Set<MoviePersons> moviePersons = new HashSet<>();
			searchTerm.getActors().forEach(actor -> {
				MoviePersons moviePerson = new MoviePersons();
				MoviePersonId moviePersonId = new MoviePersonId();
				moviePersonId.setRole(ACTOR_ROLE);
				Person person = Person.builder().firstName(actor.getFirstName()).lastName(actor.getLastName()).build();
				moviePerson.setPerson(person);
				moviePersons.add(moviePerson);
			});
			movieExample.setPeople(moviePersons);
		}

		PersonDto director = searchTerm.getDirector();
		if (director != null) {
			Set<MoviePersons> moviePersons = movieExample.getPeople();
			if (moviePersons == null) {
				moviePersons = new HashSet<>();
			}

			MoviePersons moviePerson = new MoviePersons();
			MoviePersonId moviePersonId = new MoviePersonId();
			moviePersonId.setRole(DIRECTOR_ROLE);
			Person person = Person.builder().firstName(director.getFirstName()).lastName(director.getLastName())
					.build();
			moviePerson.setPerson(person);
			moviePersons.add(moviePerson);

		}

		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
		Example<Movie> exampleQuery = Example.of(movieExample, matcher);
		List<Movie> results = movieRepository.findAll(exampleQuery);

		return mapMovieEntityToDto(results);
	}

	private void populateGenres(Movie newMovie, List<String> inputGenresList, Map<String, Genres> existingGenresMap) {
		Set<Genres> genresListToSave = new LinkedHashSet<>();

		inputGenresList.forEach(inputGenres -> {
			Genres existingGenres = existingGenresMap.get(inputGenres);
			if (existingGenres != null) {
				if (existingGenres.getMovies() == null) {
					existingGenres.setMovies(new HashSet<>());
				}
				genresListToSave.add(existingGenres);
				existingGenres.getMovies().add(newMovie);
			} else {
				Genres newGenres = new Genres();
				newGenres.setValue(inputGenres);
				genresListToSave.add(newGenres);
			}
		});
		newMovie.setGenres(genresListToSave);
	}

	private void populateActors(MovieDto movie, Map<String, Person> personsMappedByFirstNameAndLastName,
			Movie newMovie) {

		Set<MoviePersons> people = new LinkedHashSet<>();

		movie.getActors().forEach(inActor -> {

			MoviePersons movieActor = new MoviePersons();

			MoviePersonId moviePersonId = new MoviePersonId();
			moviePersonId.setRole(ACTOR_ROLE);
			movieActor.setMoviePersonsId(moviePersonId);

			Person existingActor = personsMappedByFirstNameAndLastName
					.get(inActor.getFirstName() + "_" + inActor.getLastName());

			if (existingActor != null) {
				movieActor.setPerson(existingActor);
				existingActor.getMovies().add(movieActor);
			} else {
				Person newActor = new Person();
				newActor.setFirstName(inActor.getFirstName());
				newActor.setLastName(inActor.getLastName());
				movieActor.setPerson(newActor);
			}

			movieActor.setMovie(newMovie);
			people.add(movieActor);

		});

		newMovie.setPeople(people);
	}

	private MoviePersons populateDirector(MovieDto movie, Map<String, Person> personsMappedByFirstNameAndLastName,
			Movie newMovie) {

		PersonDto inputDirector = movie.getDirector();

		MoviePersons movieDirector = new MoviePersons();

		MoviePersonId moviePersonId = new MoviePersonId();
		moviePersonId.setRole(DIRECTOR_ROLE);
		movieDirector.setMoviePersonsId(moviePersonId);

		Person existingDirector = personsMappedByFirstNameAndLastName
				.get(inputDirector.getFirstName() + "_" + inputDirector.getLastName());
		if (existingDirector != null) {
			movieDirector.setPerson(existingDirector);
			existingDirector.getMovies().add(movieDirector);
		} else {
			Person newDirector = new Person();
			newDirector.setFirstName(inputDirector.getFirstName());
			newDirector.setLastName(inputDirector.getLastName());
			movieDirector.setPerson(newDirector);
		}
		movieDirector.setMovie(newMovie);
		newMovie.getPeople().add(movieDirector);
		return movieDirector;
	}

}
