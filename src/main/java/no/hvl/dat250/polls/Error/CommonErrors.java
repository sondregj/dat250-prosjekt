package no.hvl.dat250.polls.Error;

/**
 * CommonErrors
 */
public class CommonErrors {
    public static final ResourceNotFoundException USER_NOT_FOUND
        = new ResourceNotFoundException("Could not retrieve user");

    public static final ResourceNotFoundException VOTE_NOT_FOUND
        = new ResourceNotFoundException("Could not retrieve vote");

    public static final OperationFailedError WRONG_USER
        = new OperationFailedError("Can only remove users own votes");

    public static final OperationFailedError NOT_DELETED
        = new OperationFailedError("Could not delete entity");

    public static final AccessDeniedException NOT_AUTHORIZED
        = new AccessDeniedException("Could not authorize request");
    public static final ResourceNotFoundException POLL_NOT_FOUND
        = new ResourceNotFoundException("Could not find poll");
}
