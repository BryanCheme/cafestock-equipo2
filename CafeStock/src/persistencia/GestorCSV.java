package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;
import modelo.Venta;

public class GestorCSV {
    private static final String ARCHIVO_PRODUCTOS = "productos.csv";
    private static final String ARCHIVO_VENTAS = "ventas.csv";

    public List<Producto> cargarProductos() {
        List<Producto> lista = new ArrayList<>();
        File file = new File(ARCHIVO_PRODUCTOS);
        if (!file.exists()) {
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length == 5) {
                    lista.add(new Producto(
                            p[0],
                            p[1],
                            Double.parseDouble(p[2]),
                            Integer.parseInt(p[3]),
                            Integer.parseInt(p[4])
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error leyendo productos.csv: " + e.getMessage());
        }
        return lista;
    }

    public void guardarProductos(List<Producto> productos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_PRODUCTOS))) {
            for (Producto p : productos) {
                bw.write(String.join(";",
                        p.getCodigo(),
                        p.getNombre().replace(";", ","),
                        String.valueOf(p.getPrecioVenta()),
                        String.valueOf(p.getStockActual()),
                        String.valueOf(p.getStockMinimo())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error guardando productos.csv: " + e.getMessage());
        }
    }

    public List<Venta> cargarVentas() {
        List<Venta> lista = new ArrayList<>();
        File file = new File(ARCHIVO_VENTAS);
        if (!file.exists()) {
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] v = linea.split(";");
                if (v.length == 6) {
                    lista.add(new Venta(
                            Integer.parseInt(v[0]),
                            v[1],
                            v[2],
                            v[3],
                            Integer.parseInt(v[4]),
                            Double.parseDouble(v[5])
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error leyendo ventas.csv: " + e.getMessage());
        }
        return lista;
    }

    public void guardarVentas(List<Venta> ventas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_VENTAS))) {
            for (Venta v : ventas) {
                bw.write(String.join(";",
                        String.valueOf(v.getNumero()),
                        v.getFecha(),
                        v.getCodigoProducto(),
                        v.getNombreProducto().replace(";", ","),
                        String.valueOf(v.getCantidad()),
                        String.valueOf(v.getTotal())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error guardando ventas.csv: " + e.getMessage());
        }
    }
}
