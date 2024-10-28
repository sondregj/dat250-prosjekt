package no.hvl.dat250.polls.Error;

/**
 * ResourceNOtFoundException
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
