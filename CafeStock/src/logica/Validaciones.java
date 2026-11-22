package logica;

public class Validaciones {

    public static boolean esTextoVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static boolean precioValido(double precio) {
        return !Double.isNaN(precio) && !Double.isInfinite(precio) && precio > 0;
    }

    public static boolean stockValido(int stock) {
        return stock >= 0;
    }

    public static boolean cantidadVentaValida(int cantidad, int stockDisponible) {
        return cantidad > 0 && cantidad <= stockDisponible;
    }
}
