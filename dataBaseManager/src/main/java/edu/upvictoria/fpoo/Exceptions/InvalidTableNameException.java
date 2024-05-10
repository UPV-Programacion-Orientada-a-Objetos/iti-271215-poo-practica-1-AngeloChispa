package edu.upvictoria.fpoo.Exceptions;

public class InvalidTableNameException extends RuntimeException{
    
    public InvalidTableNameException(){

        super("El nombre de la tabla no es valido");

    }

}
