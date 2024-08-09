package com.movie.cenima.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.cenima.dto.MoviePageResponce;
import com.movie.cenima.dto.Moviesdto;
import com.movie.cenima.exceptions.EmptyFileException;
import com.movie.cenima.service.MovieService;
import com.movie.cenima.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<Moviesdto>addMovieHandler(@RequestPart String moviesdto, @RequestPart MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty! Please send another file!");
        }
        Moviesdto dto = convertToMovieDto(moviesdto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);


    }
    @GetMapping("/{movieId}")
    public ResponseEntity<Moviesdto> getMovieHandler(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Moviesdto>> getAllMoviesHandler() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    @PutMapping("/update/{movieId}")
    public ResponseEntity<Moviesdto> updateMovieHandler(@PathVariable Integer movieId,
                                                       @RequestPart MultipartFile file,
                                                       @RequestPart String movieDtoObj) throws Exception {
        if (file.isEmpty()) file = null;
        Moviesdto movieDto = convertToMovieDto(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDto, file));
    }
    @DeleteMapping("/delete/{movieId}")
     public ResponseEntity<String> deleteMovie(@PathVariable Integer movieId) throws IOException {
      return ResponseEntity.ok(movieService.handleDelete(movieId));


    }
    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponce> getMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }
    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponce> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir
    ) {
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, dir));
    }
    private Moviesdto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, Moviesdto.class);
    }

}
