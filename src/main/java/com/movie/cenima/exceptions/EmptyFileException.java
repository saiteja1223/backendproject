package com.movie.cenima.exceptions;

public class EmptyFileException extends RuntimeException{
    public EmptyFileException(String messege){
        super(messege);
    }
}
