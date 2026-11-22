package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import logica.Validaciones;
import modelo.Producto;
import modelo.Venta;
import persistencia.GestorCSV;

public class VentanaPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final GestorCSV gestorCSV = new GestorCSV();
    private final List<Producto> productos = new ArrayList<>();
    private final List<Venta> ventas = new ArrayList<>();

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStockActual;
    private JTextField txtStockMinimo;
    private JTextField txtBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;
    private JButton btnGuardarNuevo;
    private JButton btnGuardarCambios;

    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidadVenta;
    private JTextField txtFechaVenta;
    private JLabel lblTotalCalculado;
    private JTable tablaVentas;
    private DefaultTableModel modeloVentas;

    public VentanaPrincipal() {
        initComponents();
        configurarInterfazCafeStock();
        cargarDatosDesdeCSV();
    }

    private void configurarInterfazCafeStock() {
        setTitle("CaféStock - Equipo 2 (Escenario A)");
        setSize(920, 620);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Productos (HU-A1 / HU-A2)", crearPanelProductos());
        pestanas.addTab("Ventas (HU-A3)", crearPanelVentas());

        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pestanas, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void cargarDatosDesdeCSV() {
        productos.clear();
        productos.addAll(gestorCSV.cargarProductos());

        ventas.clear();
        ventas.addAll(gestorCSV.cargarVentas());

        filtrarTablaProductos();
        refrescarComboProductos();
        refrescarTablaVentas();
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Registro y Edición de Productos"));

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtStockActual = new JTextField();
        txtStockMinimo = new JTextField();

        form.add(new JLabel("Código (único):"));
        form.add(txtCodigo);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Precio de venta (USD):"));
        form.add(txtPrecio);
        form.add(new JLabel("Stock actual:"));
        form.add(txtStockActual);
        form.add(new JLabel("Stock mínimo:"));
        form.add(txtStockMinimo);

        btnGuardarNuevo = new JButton("Registrar Producto");
        btnGuardarCambios = new JButton("Guardar Cambios");
        JButton btnLimpiar = new JButton("Limpiar / Nuevo");
        btnGuardarCambios.setEnabled(false);

        btnGuardarNuevo.addActionListener(e -> registrarProductoUI());
        btnGuardarCambios.addActionListener(e -> guardarCambiosProductoUI());
        btnLimpiar.addActionListener(e -> limpiarCamposProducto());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardarCambios);
        panelBotones.add(btnGuardarNuevo);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(form, BorderLayout.CENTER);
        superior.add(panelBotones, BorderLayout.SOUTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar (código o nombre):"));
        txtBuscar = new JTextField(24);
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarTablaProductos(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarTablaProductos(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarTablaProductos(); }
        });
        panelBusqueda.add(txtBuscar);

        modeloProductos = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Precio (USD)", "Stock Actual", "Stock Mínimo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloProductos);
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarProductoParaEditar();
            }
        });

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelBusqueda, BorderLayout.NORTH);
        centro.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formVenta = new JPanel(new GridLayout(4, 2, 8, 8));
        formVenta.setBorder(BorderFactory.createTitledBorder("Registro de Ventas"));

        comboProductos = new JComboBox<>();
        txtCantidadVenta = new JTextField();
        txtFechaVenta = new JTextField(LocalDate.now().format(DATE_FMT));
        lblTotalCalculado = new JLabel("$0.00");
        lblTotalCalculado.setFont(lblTotalCalculado.getFont().deriveFont(Font.BOLD, 15f));

        comboProductos.addActionListener(e -> calcularTotalVenta());
        txtCantidadVenta.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { calcularTotalVenta(); }
            @Override
            public void removeUpdate(DocumentEvent e) { calcularTotalVenta(); }
            @Override
            public void changedUpdate(DocumentEvent e) { calcularTotalVenta(); }
        });

        formVenta.add(new JLabel("Producto:"));
        formVenta.add(comboProductos);
        formVenta.add(new JLabel("Cantidad:"));
        formVenta.add(txtCantidadVenta);
        formVenta.add(new JLabel("Fecha (AAAA-MM-DD):"));
        formVenta.add(txtFechaVenta);
        formVenta.add(new JLabel("Total calculado (USD):"));
        formVenta.add(lblTotalCalculado);

        JButton btnRegistrarVenta = new JButton("Registrar Venta");
        btnRegistrarVenta.addActionListener(e -> registrarVentaUI());

        JPanel panelBtnVenta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnVenta.add(btnRegistrarVenta);

        JPanel superiorVenta = new JPanel(new BorderLayout(5, 5));
        superiorVenta.add(formVenta, BorderLayout.CENTER);
        superiorVenta.add(panelBtnVenta, BorderLayout.SOUTH);

        modeloVentas = new DefaultTableModel(
                new Object[]{"Número", "Fecha", "Código", "Producto", "Cantidad", "Total (USD)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVentas = new JTable(modeloVentas);

        panel.add(superiorVenta, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaVentas), BorderLayout.CENTER);
        return panel;
    }

    private void registrarProductoUI() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            if (Validaciones.esTextoVacio(codigo)
                    || Validaciones.esTextoVacio(nombre)
                    || Validaciones.esTextoVacio(txtPrecio.getText())
                    || Validaciones.esTextoVacio(txtStockActual.getText())
                    || Validaciones.esTextoVacio(txtStockMinimo.getText())) {
                mostrarAviso("Complete todos los campos del producto.");
                return;
            }

            for (Producto p : productos) {
                if (p.getCodigo().equalsIgnoreCase(codigo)) {
                    mostrarAviso("Ya existe un producto con el código " + codigo + ".");
                    return;
                }
            }

            double precio = Double.parseDouble(txtPrecio.getText().trim().replace(',', '.'));
            int stockActual = Integer.parseInt(txtStockActual.getText().trim());
            int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());

            if (!Validaciones.precioValido(precio)
                    || !Validaciones.stockValido(stockActual)
                    || !Validaciones.stockValido(stockMinimo)) {
                mostrarAviso("El precio debe ser mayor que 0 y los stocks deben ser números enteros desde 0.");
                return;
            }

            Producto nuevo = new Producto(codigo, nombre, precio, stockActual, stockMinimo);
            productos.add(nuevo);
            gestorCSV.guardarProductos(productos);

            filtrarTablaProductos();
            refrescarComboProductos();
            limpiarCamposProducto();
            JOptionPane.showMessageDialog(this, "Producto guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            mostrarAviso("Revise que el precio y los stocks tengan números válidos.");
        }
    }

    private void seleccionarProductoParaEditar() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        String codigo = modeloProductos.getValueAt(fila, 0).toString();
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                txtCodigo.setText(p.getCodigo());
                txtCodigo.setEditable(false);
                txtNombre.setText(p.getNombre());
                txtPrecio.setText(String.valueOf(p.getPrecioVenta()));
                txtStockActual.setText(String.valueOf(p.getStockActual()));
                txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
                btnGuardarNuevo.setEnabled(false);
                btnGuardarCambios.setEnabled(true);
                break;
            }
        }
    }

    private void guardarCambiosProductoUI() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            if (Validaciones.esTextoVacio(codigo)
                    || Validaciones.esTextoVacio(nombre)
                    || Validaciones.esTextoVacio(txtPrecio.getText())
                    || Validaciones.esTextoVacio(txtStockActual.getText())
                    || Validaciones.esTextoVacio(txtStockMinimo.getText())) {
                mostrarAviso("Complete todos los campos.");
                return;
            }

            double precio = Double.parseDouble(txtPrecio.getText().trim().replace(',', '.'));
            int stockActual = Integer.parseInt(txtStockActual.getText().trim());
            int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());

            if (!Validaciones.precioValido(precio)
                    || !Validaciones.stockValido(stockActual)
                    || !Validaciones.stockValido(stockMinimo)) {
                mostrarAviso("El precio debe ser mayor que 0 y los stocks deben ser números enteros desde 0.");
                return;
            }

            for (Producto p : productos) {
                if (p.getCodigo().equals(codigo)) {
                    p.setNombre(nombre);
                    p.setPrecioVenta(precio);
                    p.setStockActual(stockActual);
                    p.setStockMinimo(stockMinimo);
                    break;
                }
            }

            gestorCSV.guardarProductos(productos);
            filtrarTablaProductos();
            refrescarComboProductos();
            limpiarCamposProducto();
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            mostrarAviso("Revise que el precio y los stocks tengan números válidos.");
        }
    }

    private void limpiarCamposProducto() {
        txtCodigo.setText("");
        txtCodigo.setEditable(true);
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStockActual.setText("");
        txtStockMinimo.setText("");
        tablaProductos.clearSelection();
        btnGuardarNuevo.setEnabled(true);
        btnGuardarCambios.setEnabled(false);
    }

    private void filtrarTablaProductos() {
        if (modeloProductos == null) {
            return;
        }
        String filtro = txtBuscar != null ? txtBuscar.getText().trim().toLowerCase() : "";
        modeloProductos.setRowCount(0);
        for (Producto p : productos) {
            if (filtro.isEmpty()
                    || p.getCodigo().toLowerCase().contains(filtro)
                    || p.getNombre().toLowerCase().contains(filtro)) {
                modeloProductos.addRow(new Object[]{
                        p.getCodigo(),
                        p.getNombre(),
                        String.format("%.2f", p.getPrecioVenta()),
                        p.getStockActual(),
                        p.getStockMinimo()
                });
            }
        }
    }

    private void refrescarComboProductos() {
        if (comboProductos == null) {
            return;
        }
        Producto anterior = (Producto) comboProductos.getSelectedItem();
        String codigoAnterior = anterior != null ? anterior.getCodigo() : null;

        comboProductos.removeAllItems();
        for (Producto p : productos) {
            comboProductos.addItem(p);
            if (codigoAnterior != null && codigoAnterior.equals(p.getCodigo())) {
                comboProductos.setSelectedItem(p);
            }
        }
        calcularTotalVenta();
    }

    private void calcularTotalVenta() {
        if (comboProductos == null || txtCantidadVenta == null || lblTotalCalculado == null) {
            return;
        }
        Producto prod = (Producto) comboProductos.getSelectedItem();
        String cantTexto = txtCantidadVenta.getText().trim();
        if (prod == null || Validaciones.esTextoVacio(cantTexto)) {
            lblTotalCalculado.setText("$0.00");
            return;
        }
        try {
            int cant = Integer.parseInt(cantTexto);
            if (cant <= 0) {
                lblTotalCalculado.setText("$0.00");
                return;
            }
            lblTotalCalculado.setText(String.format("$%.2f", prod.getPrecioVenta() * cant));
        } catch (NumberFormatException ex) {
            lblTotalCalculado.setText("Inválido");
        }
    }

    private void registrarVentaUI() {
        Producto prod = (Producto) comboProductos.getSelectedItem();
        if (prod == null) {
            mostrarAviso("Seleccione un producto.");
            return;
        }

        String fecha = txtFechaVenta.getText().trim();
        if (Validaciones.esTextoVacio(fecha)) {
            mostrarAviso("Ingrese una fecha.");
            return;
        }
        try {
            LocalDate.parse(fecha, DATE_FMT);
        } catch (DateTimeParseException ex) {
            mostrarAviso("La fecha debe tener formato AAAA-MM-DD (ej. 2026-11-20).");
            return;
        }

        try {
            int cant = Integer.parseInt(txtCantidadVenta.getText().trim());
            if (cant <= 0) {
                mostrarAviso("La cantidad debe ser mayor que 0.");
                return;
            }
            if (!Validaciones.cantidadVentaValida(cant, prod.getStockActual())) {
                mostrarAviso("Stock insuficiente. Disponible: " + prod.getStockActual());
                return;
            }

            double total = Math.round(prod.getPrecioVenta() * cant * 100.0) / 100.0;
            int maxNumero = 0;
            for (Venta v : ventas) {
                if (v.getNumero() > maxNumero) {
                    maxNumero = v.getNumero();
                }
            }
            int nuevoNumero = maxNumero + 1;

            prod.setStockActual(prod.getStockActual() - cant);
            Venta nuevaVenta = new Venta(
                    nuevoNumero,
                    fecha,
                    prod.getCodigo(),
                    prod.getNombre(),
                    cant,
                    total
            );
            ventas.add(nuevaVenta);

            gestorCSV.guardarProductos(productos);
            gestorCSV.guardarVentas(ventas);

            txtCantidadVenta.setText("");
            filtrarTablaProductos();
            refrescarComboProductos();
            refrescarTablaVentas();
            JOptionPane.showMessageDialog(this, "Venta registrada y stock actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            mostrarAviso("Ingrese una cantidad numérica válida.");
        }
    }

    private void refrescarTablaVentas() {
        if (modeloVentas == null) {
            return;
        }
        modeloVentas.setRowCount(0);
        for (Venta v : ventas) {
            modeloVentas.addRow(new Object[]{
                    v.getNumero(),
                    v.getFecha(),
                    v.getCodigoProducto(),
                    v.getNombreProducto(),
                    v.getCantidad(),
                    String.format("%.2f", v.getTotal())
            });
        }
    }

    private void mostrarAviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
