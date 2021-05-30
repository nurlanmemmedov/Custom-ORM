package exceptions;

public class NoEntityAnnotation extends Exception {
    public NoEntityAnnotation(String errorMessage) {
        super(errorMessage);
    }
}