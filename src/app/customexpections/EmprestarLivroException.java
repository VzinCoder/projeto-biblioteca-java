package app.customexpections;

public class EmprestarLivroException extends Exception {
    public EmprestarLivroException(String message){
        super(message);
    }
}
