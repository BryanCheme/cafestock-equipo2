package modelo;

public class Venta {
    private final int numero;
    private final String fecha;
    private final String codigoProducto;
    private final String nombreProducto;
    private final int cantidad;
    private final double total;

    public Venta(int numero, String fecha, String codigoProducto, String nombreProducto, int cantidad, double total) {
        this.numero = numero;
        this.fecha = fecha;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.total = total;
    }

    public int getNumero() { return numero; }
    public String getFecha() { return fecha; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public double getTotal() { return total; }
}
