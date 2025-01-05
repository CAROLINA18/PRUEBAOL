package com.pruebaOL.demo.utils;

public class ComercianteException extends RuntimeException {
    private final int codigoError;

    public ComercianteException(int codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public int getCodigoError() {
        return codigoError;
    }
}
