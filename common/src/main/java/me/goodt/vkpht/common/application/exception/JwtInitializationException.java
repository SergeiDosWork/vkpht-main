package me.goodt.vkpht.common.application.exception;

public class JwtInitializationException extends RuntimeException {

    public JwtInitializationException(Throwable e) {
        super("Something went wong while reading public key!", e);
    }
}
