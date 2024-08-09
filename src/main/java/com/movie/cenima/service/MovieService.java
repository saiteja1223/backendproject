package com.movie.cenima.service;

import com.movie.cenima.Model.Movies;
import com.movie.cenima.Repository.MovieRepository;
import com.movie.cenima.dto.MoviePageResponce;
import com.movie.cenima.dto.Moviesdto;
import com.movie.cenima.exceptions.FileExistsException;
import com.movie.cenima.exceptions.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    @Value("${projects.poster}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;
    @Autowired
    private FileService fileService;
    @Autowired
    private MovieRepository movieRepository;
    public Moviesdto addMovie(Moviesdto moviesdto, MultipartFile file) throws Exception {
        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new FileExistsException("File already exists! Please enter another file name!");
        }
       String uploadedFileName=fileService.uploadfile(path,file);
        moviesdto.setPoster(uploadedFileName);
        Movies movie=new Movies();
        movie.setMovieId(null);
        movie.setTitle(moviesdto.getTitle());
        movie.setDirector(moviesdto.getDirector());
        movie.setStudio(moviesdto.getStudio());
        movie.setMovieCast(moviesdto.getMovieCast());
        movie.setReleaseYear(moviesdto.getReleaseYear());
        movie.setPoster(moviesdto.getPoster());

        Movies savedMovie=movieRepository.save(movie);
        String posterUrl = baseUrl + "/file/" + uploadedFileName;
        System.out.println(posterUrl+"get"+baseUrl+"set "+uploadedFileName);
        Moviesdto response=new Moviesdto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );

        return response;

    }

    public Moviesdto getMovie(Integer movieId){
        Movies movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        Moviesdto response = new Moviesdto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;
    }

    public List<Moviesdto> getAllMovies(){
      List<Movies> movies=movieRepository.findAll();
      List<Moviesdto> movie=new ArrayList<>();
      for(Movies mov:movies){
          String posterUrl= baseUrl + "/file/" + mov.getPoster();
          Moviesdto moviesdto=new Moviesdto(
                  mov.getMovieId(),
                  mov.getTitle(),
                  mov.getDirector(),
                  mov.getStudio(),
                  mov.getMovieCast(),
                  mov.getReleaseYear(),
                  mov.getPoster(),
                  posterUrl
          );
          movie.add(moviesdto);
      }
      return movie;
    }
    public Moviesdto updateMovie(Integer movieId,Moviesdto moviesdto,MultipartFile newFile) throws Exception {
        Movies mv=movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("movie not found with id"+movieId));
        String oldFileName=mv.getPoster();
        if(oldFileName!=null){
            Files.deleteIfExists(Paths.get(path + File.separator + oldFileName));
            oldFileName = fileService.uploadfile(path, newFile);
        }
        moviesdto.setPoster(oldFileName);
        Movies movie = new Movies();
                movie.setMovieId(mv.getMovieId());

               movie.setTitle( moviesdto.getTitle());
               movie.setDirector(moviesdto.getDirector());
               movie.setStudio( moviesdto.getStudio());
               movie.setMovieCast( moviesdto.getMovieCast());
            movie.setReleaseYear(moviesdto.getReleaseYear());
             movie.setPoster(moviesdto.getPoster());

        Movies updatedMovie=movieRepository.save(movie);
        String posterUrl = baseUrl + "/file/" + oldFileName;
        Moviesdto response = new Moviesdto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;
    }
    public MoviePageResponce getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movies> moviePages = movieRepository.findAll(pageable);
        List<Movies> movies = moviePages.getContent();

        List<Moviesdto> movieDtos = new ArrayList<>();


        for(Movies movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            Moviesdto movieDto = new Moviesdto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }


        return new MoviePageResponce(movieDtos, pageNumber, pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }

    public String handleDelete(Integer movieId) throws IOException {
        Movies mv=movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("movie not found with na id"+movieId));
        Integer id=mv.getMovieId();
        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));
        movieRepository.delete(mv);
        return "Movie deleted with id = " + id;

    }
    public MoviePageResponce getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize,
                                                                  String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movies> moviePages = movieRepository.findAll(pageable);
        List<Movies> movies = moviePages.getContent();

        List<Moviesdto> movieDtos = new ArrayList<>();

        // 2. iterate through the list, generate posterUrl for each movie obj,
        // and map to MovieDto obj
        for (Movies movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            Moviesdto movieDto = new Moviesdto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }


        return new MoviePageResponce(movieDtos, pageNumber, pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }
}

