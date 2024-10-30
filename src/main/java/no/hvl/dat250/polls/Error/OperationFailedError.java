package no.hvl.dat250.polls.Error;

/**
 * OperationFailedError
 */
public class OperationFailedError extends RuntimeException{
    public OperationFailedError(String message){
        super(message);
    }
}
