package be.howest.ti.mars.logic.exceptions;

public class MarsResourceNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 10000;

    public MarsResourceNotFoundException(String msg) {
        super(msg);
    }

}
