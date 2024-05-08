package edu.upvictoria.fpoo.Exceptions;

public class InvalidSentenceFormatException extends RuntimeException{
    
    public InvalidSentenceFormatException(){
        super("\nEl Formato de la sentencia no es valido");
    }

}
