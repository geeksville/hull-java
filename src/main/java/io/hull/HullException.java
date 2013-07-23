package io.hull;

/**
 * Created with IntelliJ IDEA.
 * User: crystal
 * Date: 7/22/13
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class HullException extends RuntimeException {
    public HullException(String message) {
        super(message);
    }

    public HullException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
