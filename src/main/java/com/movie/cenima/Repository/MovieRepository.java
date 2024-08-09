package com.movie.cenima.Repository;

import com.movie.cenima.Model.Movies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movies,Integer> {
}
