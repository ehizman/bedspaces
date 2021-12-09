package exceptions;

import data.repositories.HostelRepository;

public class DuplicateIdException extends HostelManagementException {
    public DuplicateIdException(String message) {
        super(message);
    }
}
