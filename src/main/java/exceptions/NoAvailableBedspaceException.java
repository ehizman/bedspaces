package exceptions;

public class NoAvailableBedspaceException extends HostelManagementException {
    public NoAvailableBedspaceException(String message) {
        super(message);
    }
}
