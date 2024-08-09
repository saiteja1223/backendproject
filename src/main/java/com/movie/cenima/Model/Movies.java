package com.movie.cenima.Model;

import jakarta.persistence.*;

import  java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor


public class Movies {
   @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   private Integer movieId;

   @Column(nullable = false, length = 200)
   @NotBlank(message = "Please provide movie's title!")
   private String title;
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's director!")
   private  String director;

    @Column(nullable = false)  // database level validation
    @NotBlank(message = "Please provide movie's studio!")   //serever level validation
   private  String studio;
    @ElementCollection
    @CollectionTable(name = "movie_cast")
   private Set<String> movieCast;
    @Column(nullable = false)
   private Integer releaseYear;
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's poster!")
   private String poster;



}
