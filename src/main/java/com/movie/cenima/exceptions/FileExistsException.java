package com.movie.cenima.exceptions;

public class FileExistsException extends RuntimeException{
    public FileExistsException(String messege){
        super(messege);
    }
}
