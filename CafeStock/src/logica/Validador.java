package logica;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import modelo.Producto;

public class Validador {

    public static String validarProducto(String codigo, String nombre, String precioStr,
                                         String stockStr, String stockMinStr,
                                         List<Producto> existentes, boolean esEdicion) {
        if (codigo == null || codigo.trim().isEmpty() ||
                nombre == null || nombre.trim().isEmpty() ||
                precioStr == null || precioStr.trim().isEmpty() ||
                stockStr == null || stockStr.trim().isEmpty() ||
                stockMinStr == null || stockMinStr.trim().isEmpty()) {
            return "Todos los campos son obligatorios.";
        }

        if (codigo.contains(";") || nombre.contains(";")) {
            return "El codigo y el nombre no pueden contener punto y coma (;).";
        }

        if (!esEdicion) {
            for (Producto p : existentes) {
                if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                    return "El codigo '" + codigo.trim() + "' ya existe.";
                }
            }
        }

        try {
            double precio = Double.parseDouble(precioStr.trim().replace(",", "."));
            if (Double.isNaN(precio) || Double.isInfinite(precio) || precio <= 0) {
                return "El precio de venta debe ser mayor que 0.";
            }
        } catch (NumberFormatException e) {
            return "El precio debe ser un numero valido (ej. 1.50).";
        }

        try {
            int stock = Integer.parseInt(stockStr.trim());
            int stockMin = Integer.parseInt(stockMinStr.trim());
            if (stock < 0 || stockMin < 0) {
                return "El stock actual y minimo deben ser enteros desde 0.";
            }
        } catch (NumberFormatException e) {
            return "El stock actual y minimo deben ser numeros enteros.";
        }

        return null;
    }

    public static String validarVenta(Producto producto, String cantidadStr, String fechaStr) {
        if (producto == null) {
            return "Debe seleccionar un producto.";
        }
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return "La fecha es obligatoria.";
        }
        try {
            LocalDate.parse(fechaStr.trim());
        } catch (DateTimeParseException e) {
            return "La fecha debe tener el formato AAAA-MM-DD (ej. 2026-11-21).";
        }

        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return "Ingrese una cantidad.";
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr.trim());
        } catch (NumberFormatException e) {
            return "La cantidad debe ser un numero entero valido.";
        }

        if (cantidad <= 0) {
            return "La cantidad debe ser mayor que 0.";
        }
        if (cantidad > producto.getStockActual()) {
            return "Stock insuficiente. Disponible: " + producto.getStockActual() + " unidades.";
        }

        return null;
    }
}