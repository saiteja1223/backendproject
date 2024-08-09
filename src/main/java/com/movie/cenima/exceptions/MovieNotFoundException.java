package com.movie.cenima.exceptions;

public class MovieNotFoundException extends RuntimeException{
    public MovieNotFoundException(String messege){
        super(messege);
    }
}
