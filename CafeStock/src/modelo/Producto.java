package modelo;

public class Producto {
    private final String codigo;
    private String nombre;
    private double precioVenta;
    private int stockActual;
    private int stockMinimo;

    public Producto(String codigo, String nombre, double precioVenta, int stockActual, int stockMinimo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f | Stock: %d)", codigo, nombre, precioVenta, stockActual);
    }
}
