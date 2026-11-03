package pe.commerce.app.tienda;

/** Regla de negocio no cumplida; la API responde 409 con {"mensaje": "..."}. */
public class VentaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VentaException(String mensaje) {
        super(mensaje);
    }
}
