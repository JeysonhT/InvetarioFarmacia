/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import Utils.ImportExcel;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Categoria;
import models.Cliente;
import models.Facturas;
import models.Lotes;
import models.Productos;
import models.UnidadesMultiples;
import models.Ventas;
import models.VentasProducto;
import net.sf.jasperreports.engine.JRException;
import services.CategoriaServices;
import services.ClienteServices;
import services.FacturaServices;
import services.FacturasClienteServices;
import services.LoteServices;
import services.ProductoService;
import services.ReportesServices;
import services.UsuariosServices;
import services.VentasProductosServices;
import services.VentasServices;

/**
 *
 * @author jason
 */
public class PanelInventario extends javax.swing.JPanel {

    private DefaultTableModel tableModel = new DefaultTableModel();
    private DefaultListModel<Productos> listModel = new DefaultListModel<>();
    private DefaultListModel<Cliente> modelCliente = new DefaultListModel<>();
    private double Subtotal;
    private double Total;

    /**
     * Creates new form PanelInventario
     */
    public PanelInventario() {
        this.Subtotal = 0.0;
        initComponents();
        obtenerDatos();
        obtenerDatosCliente();

        tableModel = (DefaultTableModel) JTableProductos.getModel();

        modelCliente = (DefaultListModel<Cliente>) jlistClientes.getModel();

        jListVenta.setModel(listModel);
    }

    private List<Lotes> obtenerLotes() {
        try {
            return new LoteServices().obtenerLotes();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<UnidadesMultiples> obtenerunidades() {
        try {
            return new ProductoService().obtenerUnidades();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void obtenerDatos() {
        Map<Integer, String> categorias = obtenerCategorias();
        List<Lotes> lotes = obtenerLotes();
        List<UnidadesMultiples> unidades = obtenerunidades();
        List<Productos> productos = new ProductoService().obtenerProductos();
        DefaultTableModel model = new DefaultTableModel();
        String[] Columnas = {"Id", "Nombre", "Indicaciones", "Marca", "Lote",
            "Categoria", "Presentación", "Precio", "Cantidad", "Unidad mayor", "Precio mayor", "Fecha_caducidad"};

        model.setColumnIdentifiers(Columnas);

        for (Productos p : productos) {
            String nombreCategoria = categorias.get(p.getCategoria_id());
            String codigoLote = "Sin Lote";
            String fechaVecimiento = "no tiene";
            String UnidadMayor = "Caja";
            double precioM = 0.0;

            try {
                codigoLote = lotes.stream()
                        .filter(e -> e.getId_producto() == p.getId())
                        .findFirst()
                        .orElseThrow(() -> new Exception("Lote no encontrado"))
                        .getLote();

                fechaVecimiento = lotes.stream()
                        .filter(e -> e.getId_producto() == p.getId())
                        .findFirst()
                        .orElseThrow(() -> new Exception("No tiene"))
                        .getFecha_vencimiento().toString();
                
                UnidadMayor = unidades.stream()
                        .filter(u -> u.getId_Producto() == p.getId())
                        .findFirst()
                        .orElseThrow(() -> new Exception("No hay unidad mayor"))
                        .getDescripcion();
                
                precioM = unidades.stream()
                        .filter(u -> u.getId_Producto() == p.getId())
                        .findFirst()
                        .orElseThrow(() -> new Exception("No tiene precio mayor"))
                        .getPrecio_Unitario();
                        

            } catch (Exception e) {
                System.err.println("Error al obtener el lote: " + e.getMessage());
            }

            String[] renglon = {
                String.valueOf(p.getId()),
                p.getNombre(),
                p.getIndicaciones(),
                p.getMarca(),
                codigoLote,
                nombreCategoria,
                p.getPresentacion(),
                String.valueOf(p.getPrecio()),
                String.valueOf(p.getCantidad()),
                UnidadMayor,
                String.valueOf(precioM),
                fechaVecimiento.split(" ")[0]
            };

            model.addRow(renglon);
        }

        jlabelTotal_Inventario.setText("Total de Productos en inventario: "
                + obtenerTotalProductos() + " Productos");

        labelTotalCordobasEnInventario.setText("Total de Cordobas en inventario: "
                + obtenerTotalCordobas() + " C$");

        JTableProductos.setModel(model);

    }

    private Map<Integer, String> obtenerCategorias() {
        try {
            List<Categoria> categorias = new CategoriaServices().obtenerCategorias();
            Map<Integer, String> categoriasMap = new HashMap<>();

            for (Categoria c : categorias) {
                jComboCategoriaProducto.addItem(new Categoria(c.getId(),
                        c.getNombre_categoria()));

                int id = c.getId();
                String nombre = c.getNombre_categoria();
                categoriasMap.put(id, nombre);
            }
            return categoriasMap;
        } catch (Exception e) {
            throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    private int obtenerTotalProductos() {
        return new ProductoService().obtenerCantidadProductosTotal();
    }

    private double obtenerTotalCordobas() {
        return new ProductoService().totalCordobasInvetario();
    }

    private Categoria[] ObtenerArrayCategorias() {
        try {
            List<Categoria> categorias = new CategoriaServices().obtenerCategorias();
            Categoria[] categoria = new Categoria[categorias.size() - 1];

            for (int i = 0; i < categorias.size() - 1; i++) {
                categoria[i] = categorias.get(i);
            }

            return categoria;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelDialogVenta, e.getMessage());
            throw new RuntimeException("error en el metodo: " + e.getMessage());
        }
    }

    private void LimpiarCampos() {
        txtIdProducto.setText("");
        txtNombreProducto.setText("");
        txtIndicacionesProducto.setText("");
        txtMarcaProducto.setText("");
        txtPrecioCompraProducto.setText("");
        txtPrecioMayorProducto.setText("");
        txtCantidadProducto.setText("");
        txtFecha_Vencimiento_Producto.setText("");
    }

    //metodo para mostrar el subtotal de la venta
    private void ObtenerSubtotalVenta() {
        Subtotal = 0.0;

        for (int i = 0; i < listModel.getSize(); i++) {
            Subtotal += listModel.get(i).getPrecio() * listModel.get(i).getCantidad();
        }

        jLabelSubTotal.setText("" + Subtotal);

        Total = Subtotal;

        labelTotalVenta.setText("" + Total);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogProductos = new javax.swing.JDialog();
        PanelCrearProducto = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtIndicacionesProducto = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtMarcaProducto = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jComboCategoriaProducto = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        txtPrecioCompraProducto = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtFecha_Vencimiento_Producto = new javax.swing.JTextField();
        LabelBtnGuardarProducto = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIdProducto = new javax.swing.JTextField();
        labelBtnActualizarProducto = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboPresentaciones = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtPrecioMayorProducto = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtLoteProducto = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtFactorConversionProducto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        txtCostoTotalProducto = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtUtilidadProducto = new javax.swing.JTextField();
        txtPrecioUtilidadProducto = new javax.swing.JTextField();
        txtPrecioUnidad = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        JDialogVenta = new javax.swing.JDialog();
        panelDialogVenta = new javax.swing.JPanel();
        labelGeneric = new javax.swing.JLabel();
        labelCliente = new javax.swing.JLabel();
        btnBorrarProductoVenta = new javax.swing.JButton();
        labelSub = new javax.swing.JLabel();
        btnCancelarVenta = new javax.swing.JButton();
        btnprocesarVenta = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListVenta = new javax.swing.JList<>();
        txtNombreClienteVenta = new javax.swing.JTextField();
        LabelFecha = new javax.swing.JLabel();
        jLabelSubTotal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtidClienteVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnEstablecerCantidad = new javax.swing.JButton();
        checkGenerarFactura = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        labelTotalVenta = new javax.swing.JLabel();
        btnVentaMayor = new javax.swing.JButton();
        jDialogCliente = new javax.swing.JDialog();
        panelDialogClientes = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        btnAgregarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnActualizarCliente = new javax.swing.JButton();
        btnClienteVenta = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jlistClientes = new javax.swing.JList<Cliente>();
        jPanelInventario = new javax.swing.JPanel();
        jlabelTotal_Inventario = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableProductos = new javax.swing.JTable();
        btnCrearProductos = new javax.swing.JButton();
        btnEliminarProductos = new javax.swing.JButton();
        btnEditarProducto = new javax.swing.JButton();
        labelTotalCordobasEnInventario = new javax.swing.JLabel();
        btnAgregarVenta = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnVentas = new javax.swing.JButton();
        botonClientes = new javax.swing.JButton();
        txtBuscarProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        btnQuitarFiltroProducto = new javax.swing.JButton();
        btnImportarDatos = new javax.swing.JButton();

        jDialogProductos.setTitle("Panel de Productos");
        jDialogProductos.setBounds(new java.awt.Rectangle(0, 0, 550, 400));
        jDialogProductos.setResizable(false);

        PanelCrearProducto.setPreferredSize(new java.awt.Dimension(550, 440));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Nombre");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel20.setText("Indicaciones");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setText("Marca");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel22.setText("Categoria");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setText("Precio de compra");

        txtPrecioCompraProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioCompraProductoActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setText("Cantidad");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setText("Fecha de Vencimiento");

        LabelBtnGuardarProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LabelBtnGuardarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/disco.png"))); // NOI18N
        LabelBtnGuardarProducto.setText("Guardar Producto");
        LabelBtnGuardarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LabelBtnGuardarProductoMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Id");

        txtIdProducto.setEnabled(false);

        labelBtnActualizarProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelBtnActualizarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/respaldo.png"))); // NOI18N
        labelBtnActualizarProducto.setText("Actualizar Producto");
        labelBtnActualizarProducto.setEnabled(false);
        labelBtnActualizarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelBtnActualizarProductoMouseClicked(evt);
            }
        });

        jLabel8.setText("Formato: Año-Mes-Dia");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Presentación");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Precio Mayor");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Lote");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Descripción");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("Factor de Conversión");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Datos de Unidades mayores");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Datos de Lote");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Utilidad");

        txtCostoTotalProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoTotalProductoActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setText("Costo Total");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel26.setText("Precio Con utilidad");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setText("Precio de Unidad Final");

        javax.swing.GroupLayout PanelCrearProductoLayout = new javax.swing.GroupLayout(PanelCrearProducto);
        PanelCrearProducto.setLayout(PanelCrearProductoLayout);
        PanelCrearProductoLayout.setHorizontalGroup(
            PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4)
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCrearProductoLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator3))
                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2))
                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(LabelBtnGuardarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelBtnActualizarProducto))
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPrecioMayorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtFactorConversionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel19))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel20)
                                                    .addComponent(txtIndicacionesProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboCategoriaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel22))
                                                .addGap(18, 18, 18)
                                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel9)
                                                    .addComponent(jComboPresentaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(36, 36, 36)
                                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel21)
                                            .addComponent(txtMarcaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel24)))
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel12)
                                            .addComponent(txtLoteProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(39, 39, 39)
                                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel25)
                                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                                .addComponent(txtFecha_Vencimiento_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel8)))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                                    .addComponent(txtPrecioCompraProducto))
                                .addGap(18, 18, 18)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtUtilidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCostoTotalProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(12, 12, 12)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel26))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecioUtilidadProducto)
                                    .addComponent(txtPrecioUnidad, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        PanelCrearProductoLayout.setVerticalGroup(
            PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(jLabel21)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIndicacionesProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMarcaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel9)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboCategoriaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboPresentaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel18)
                    .addComponent(txtCostoTotalProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioUtilidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtUtilidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioCompraProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(14, 14, 14)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLoteProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFecha_Vencimiento_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtPrecioMayorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFactorConversionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelBtnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelBtnGuardarProducto))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogProductosLayout = new javax.swing.GroupLayout(jDialogProductos.getContentPane());
        jDialogProductos.getContentPane().setLayout(jDialogProductosLayout);
        jDialogProductosLayout.setHorizontalGroup(
            jDialogProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelCrearProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
        );
        jDialogProductosLayout.setVerticalGroup(
            jDialogProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelCrearProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );

        JDialogVenta.setTitle("Venta de Producto");
        JDialogVenta.setBounds(new java.awt.Rectangle(0, 0, 530, 400));
        JDialogVenta.setMinimumSize(new java.awt.Dimension(700, 480));
        JDialogVenta.setResizable(false);

        panelDialogVenta.setMinimumSize(new java.awt.Dimension(700, 480));
        panelDialogVenta.setPreferredSize(new java.awt.Dimension(700, 480));
        panelDialogVenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelGeneric.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelGeneric.setText("Fecha: ");
        panelDialogVenta.add(labelGeneric, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, -1));

        labelCliente.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelCliente.setText("Cliente: ");
        panelDialogVenta.add(labelCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, -1));

        btnBorrarProductoVenta.setText("Borrar Producto");
        btnBorrarProductoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarProductoVentaActionPerformed(evt);
            }
        });
        panelDialogVenta.add(btnBorrarProductoVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 177, -1));

        labelSub.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelSub.setText("SubTotal: ");
        panelDialogVenta.add(labelSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 80, -1));

        btnCancelarVenta.setBackground(new java.awt.Color(255, 0, 0));
        btnCancelarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarVenta.setText("Cancelar Venta");
        btnCancelarVenta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelarVenta.setBorderPainted(false);
        btnCancelarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarVentaActionPerformed(evt);
            }
        });
        panelDialogVenta.add(btnCancelarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 177, -1));

        btnprocesarVenta.setText("Procesar Venta");
        btnprocesarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprocesarVentaActionPerformed(evt);
            }
        });
        panelDialogVenta.add(btnprocesarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 177, -1));

        jListVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jScrollPane2.setViewportView(jListVenta);

        panelDialogVenta.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 700, 270));

        txtNombreClienteVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        panelDialogVenta.add(txtNombreClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 210, -1));

        LabelFecha.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta.add(LabelFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 170, 30));

        jLabelSubTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta.add(jLabelSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 130, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Id");
        panelDialogVenta.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 30, 30));

        txtidClienteVenta.setEnabled(false);
        panelDialogVenta.add(txtidClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 120, 30));
        panelDialogVenta.add(txtCantidadVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, 120, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Cantidad");
        panelDialogVenta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, -1, -1));

        btnEstablecerCantidad.setText("Establecer Cantidad");
        btnEstablecerCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstablecerCantidadActionPerformed(evt);
            }
        });
        panelDialogVenta.add(btnEstablecerCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 180, -1));

        checkGenerarFactura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        checkGenerarFactura.setText("Generar Factura");
        panelDialogVenta.add(checkGenerarFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 150, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Total:");
        panelDialogVenta.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        labelTotalVenta.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta.add(labelTotalVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 146, 140, 20));

        btnVentaMayor.setText("Venta Mayor");
        btnVentaMayor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaMayorActionPerformed(evt);
            }
        });
        panelDialogVenta.add(btnVentaMayor, new org.netbeans.lib.awtextra.AbsoluteConstraints(321, 150, 120, -1));

        javax.swing.GroupLayout JDialogVentaLayout = new javax.swing.GroupLayout(JDialogVenta.getContentPane());
        JDialogVenta.getContentPane().setLayout(JDialogVentaLayout);
        JDialogVentaLayout.setHorizontalGroup(
            JDialogVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JDialogVentaLayout.setVerticalGroup(
            JDialogVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogCliente.setTitle("Panel de Clientes");
        jDialogCliente.setBounds(new java.awt.Rectangle(0, 0, 745, 395));
        jDialogCliente.setResizable(false);

        jLabel4.setText("Id Cliente");

        jLabel5.setText("Nombre Cliente");

        jLabel6.setText("Telefono");

        txtIdCliente.setEnabled(false);

        btnAgregarCliente.setText("Agregar");
        btnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarClienteActionPerformed(evt);
            }
        });

        btnEliminarCliente.setText("Eliminar");
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });

        btnActualizarCliente.setText("Actualizar");

        btnClienteVenta.setText("Seleccionar para venta");
        btnClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteVentaActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jlistClientes);

        javax.swing.GroupLayout panelDialogClientesLayout = new javax.swing.GroupLayout(panelDialogClientes);
        panelDialogClientes.setLayout(panelDialogClientesLayout);
        panelDialogClientesLayout.setHorizontalGroup(
            panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDialogClientesLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogClientesLayout.createSequentialGroup()
                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogClientesLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnClienteVenta)
                            .addGroup(panelDialogClientesLayout.createSequentialGroup()
                                .addComponent(btnAgregarCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminarCliente))))
                    .addGroup(panelDialogClientesLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(btnActualizarCliente)))
                .addContainerGap(97, Short.MAX_VALUE))
            .addGroup(panelDialogClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        panelDialogClientesLayout.setVerticalGroup(
            panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDialogClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCliente)
                    .addComponent(btnEliminarCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDialogClientesLayout.createSequentialGroup()
                        .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDialogClientesLayout.createSequentialGroup()
                        .addComponent(btnActualizarCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClienteVenta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogClienteLayout = new javax.swing.GroupLayout(jDialogCliente.getContentPane());
        jDialogCliente.getContentPane().setLayout(jDialogClienteLayout);
        jDialogClienteLayout.setHorizontalGroup(
            jDialogClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogClienteLayout.setVerticalGroup(
            jDialogClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setMinimumSize(new java.awt.Dimension(762, 501));
        setLayout(new java.awt.BorderLayout());

        jPanelInventario.setBackground(new java.awt.Color(255, 255, 255));

        jlabelTotal_Inventario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jlabelTotal_Inventario.setText("Total en inventario:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/farmacia.png"))); // NOI18N
        jLabel1.setText("Inventario Farmacia YESHUA");

        JTableProductos.setBackground(new java.awt.Color(204, 204, 255));
        JTableProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        JTableProductos.setGridColor(new java.awt.Color(0, 0, 0));
        JTableProductos.setRowHeight(25);
        jScrollPane1.setViewportView(JTableProductos);

        btnCrearProductos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCrearProductos.setText("Crear");
        btnCrearProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCrearProductosMouseClicked(evt);
            }
        });

        btnEliminarProductos.setBackground(java.awt.Color.red);
        btnEliminarProductos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEliminarProductos.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProductos.setText("Eliminar");
        btnEliminarProductos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarProductos.setBorderPainted(false);
        btnEliminarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductosActionPerformed(evt);
            }
        });

        btnEditarProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditarProducto.setText("Editar");
        btnEditarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarProductoMouseClicked(evt);
            }
        });

        labelTotalCordobasEnInventario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelTotalCordobasEnInventario.setText("Total C$ en inventario:");

        btnAgregarVenta.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAgregarVenta.setText("Agregar a venta");
        btnAgregarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarVentaMouseClicked(evt);
            }
        });

        btnVentas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnVentas.setText("Venta");
        btnVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVentasMouseClicked(evt);
            }
        });

        botonClientes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        botonClientes.setText("Clientes");
        botonClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonClientesMouseClicked(evt);
            }
        });

        txtBuscarProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtBuscarProducto.setForeground(new java.awt.Color(204, 204, 204));
        txtBuscarProducto.setText("Busque un producto");
        txtBuscarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBuscarProductoMouseClicked(evt);
            }
        });

        btnBuscarProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBuscarProducto.setText("Buscar");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        btnQuitarFiltroProducto.setBackground(java.awt.Color.red);
        btnQuitarFiltroProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnQuitarFiltroProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnQuitarFiltroProducto.setText("Quitar Filtro");
        btnQuitarFiltroProducto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnQuitarFiltroProducto.setBorderPainted(false);
        btnQuitarFiltroProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQuitarFiltroProductoMouseClicked(evt);
            }
        });

        btnImportarDatos.setText("Importar Datos");
        btnImportarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportarDatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelInventarioLayout = new javax.swing.GroupLayout(jPanelInventario);
        jPanelInventario.setLayout(jPanelInventarioLayout);
        jPanelInventarioLayout.setHorizontalGroup(
            jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelTotalCordobasEnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlabelTotal_Inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(214, 360, Short.MAX_VALUE)
                        .addComponent(botonClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(btnAgregarVenta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))))
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnQuitarFiltroProducto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(btnCrearProductos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditarProducto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnImportarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanelInventarioLayout.setVerticalGroup(
            jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(23, 23, 23)
                        .addComponent(jlabelTotal_Inventario))
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEliminarProductos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnEditarProducto)
                                .addComponent(btnCrearProductos)))
                        .addGap(9, 9, 9)
                        .addComponent(btnImportarDatos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelInventarioLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarProducto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuitarFiltroProducto)))
                .addGap(8, 8, 8)
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarVenta)
                    .addComponent(btnVentas)
                    .addComponent(labelTotalCordobasEnInventario)
                    .addComponent(botonClientes))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
        );

        add(jPanelInventario, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    public boolean validarCamposProducto() {

        return txtNombreProducto.getText().isBlank() || txtIndicacionesProducto.getText().isBlank() || txtMarcaProducto
                .getText().isBlank() || jComboCategoriaProducto.getSelectedIndex() == -1 || txtPrecioCompraProducto
                .getText().isBlank() || txtCantidadProducto.getText().isBlank() || txtFecha_Vencimiento_Producto
                .getText().isBlank();

    }


    private void btnCrearProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCrearProductosMouseClicked
        LimpiarCampos();
        jDialogProductos.setVisible(true);
        jDialogProductos.setSize(723, 540);
        jDialogProductos.setLocationRelativeTo(this);
        labelBtnActualizarProducto.setEnabled(false);
        LabelBtnGuardarProducto.setEnabled(true);
        LabelBtnGuardarProducto.setVisible(true);
        obtenerPresentación();

    }//GEN-LAST:event_btnCrearProductosMouseClicked

    public void obtenerPresentación() {
        List<String> presentaciones = new ProductoService().getPresentación();

        for (String p : presentaciones) {
            jComboPresentaciones.addItem(p);
        }
    }
    private void btnEditarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarProductoMouseClicked
        obtenerPresentación();

        int fila = this.JTableProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(PanelCrearProducto, "Seleccione un registro de la tabla");
        } else {
            try {
                jDialogProductos.setVisible(true);
                jDialogProductos.setLocationRelativeTo(this);
                labelBtnActualizarProducto.setEnabled(true);
                //metodos para ocultar y desactivar el boton que hace la funcion de guardar el producto
                LabelBtnGuardarProducto.setEnabled(false);
                LabelBtnGuardarProducto.setVisible(false);

                //recoleccion de los datos de la tabla del producto a editar
                int id = Integer.parseInt((String) this.JTableProductos.
                        getValueAt(fila, 0).toString());

                String nombre = (String) this.JTableProductos.getValueAt(fila, 1);
                String indicaciones = (String) this.JTableProductos.getValueAt(fila, 2);

                String marca = (String) this.JTableProductos.getValueAt(fila, 4);
                double precio = Double.parseDouble((String) this.JTableProductos.
                        getValueAt(fila, 6).toString());

                double precioMayor = Double.parseDouble((String) this.JTableProductos.
                        getValueAt(fila, 7).toString());

                int cantidad = Integer.parseInt((String) this.JTableProductos.
                        getValueAt(fila, 8).toString());

                Date fecha = Date.valueOf((String) this.JTableProductos.
                        getValueAt(fila, 9).toString().split(" ")[0]);

                int procentajeMayor = (int) ((1 - (precioMayor / precio)) * 100);

                txtIdProducto.setText("" + id);
                txtNombreProducto.setText(nombre);
                txtIndicacionesProducto.setText(indicaciones);
                txtMarcaProducto.setText(marca);
                txtPrecioCompraProducto.setText(String.valueOf(precio));
                txtPrecioMayorProducto.setText(String.valueOf(procentajeMayor));
                txtCantidadProducto.setText(String.valueOf(cantidad));
                txtFecha_Vencimiento_Producto.setText(fecha.toString());

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(PanelCrearProducto, "error al intentar editar el producto: " + e.getMessage());
            }
        }


    }//GEN-LAST:event_btnEditarProductoMouseClicked

    private void btnAgregarVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarVentaMouseClicked
        int fila = this.JTableProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(panelDialogVenta,
                    "Por favor seleccione un producto para agregar a venta");
        } else {
            int existencia = Integer.parseInt(this.JTableProductos.getValueAt(fila, 8).toString());
            if (existencia == 0) {
                JOptionPane.showMessageDialog(this, "no hay mas existencias de este producto en el inventario");
            } else {
                try {
                    Categoria[] categorias = ObtenerArrayCategorias();

                    int id = Integer.parseInt((String) this.JTableProductos.
                            getValueAt(fila, 0).toString());

                    String nombre = (String) this.JTableProductos.getValueAt(fila, 1);
                    String indicaciones = (String) this.JTableProductos.getValueAt(fila, 2);
                    String presentacion = (String) this.JTableProductos.getValueAt(fila, 3);
                    String marca = (String) this.JTableProductos.getValueAt(fila, 4);

                    String Nombrecategoria = (String) this.JTableProductos.getValueAt(fila, 5);

                    double precio = Double.parseDouble((String) this.JTableProductos.
                            getValueAt(fila, 6).toString());

                    // Establezco por predeterminado cantidad en uno
                    double precioMayor = Double.parseDouble((String) this.JTableProductos.getValueAt(fila, 7).toString());

                    int cantidad = 1;
                    Date fecha = Date.valueOf((String) this.JTableProductos.
                            getValueAt(fila, 9).toString().split(" ")[0]);

                    int categoriaId = 0;
                    //Obtencion de la categoria
                    for (Categoria categoria : categorias) {
                        if (categoria.getNombre_categoria().equalsIgnoreCase(Nombrecategoria)) {
                            categoriaId = categoria.getId();
                            break;
                        } else {
                            categoriaId = 0;
                        }
                    }

                    //Productos producto = new Productos(id, nombre, indicaciones, presentacion, marca, categoriaId, precio, precioMayor, cantidad, fecha);
                    //listModel.addElement(producto);
                    JOptionPane.showMessageDialog(labelCliente, "producto agregado: ");

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(panelDialogVenta,
                            "Error al intentar agregar el producto: " + e.getMessage());
                }
            }

        }
    }//GEN-LAST:event_btnAgregarVentaMouseClicked

    private void btnVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentasMouseClicked
        if (jListVenta.getModel().getSize() < 1) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos agregados a la venta");
        } else {
            JDialogVenta.setVisible(true);
            JDialogVenta.setLocationRelativeTo(this);
            JDialogVenta.setSize(700, 480);

            LabelFecha.setText(Date.valueOf(LocalDate.now()).toString());

            ObtenerSubtotalVenta();
        }
    }//GEN-LAST:event_btnVentasMouseClicked

    private void btnprocesarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprocesarVentaActionPerformed
        if (!listModel.isEmpty()) {

            Date fecha = Date.valueOf(LocalDate.now());
            double subtotal = (Double) Double.parseDouble(jLabelSubTotal.getText());
            Double total = (double) Double.parseDouble(labelTotalVenta.getText());
            Cliente cliente;
            int id;
            String nombre;

            // Si no se encuentra nombre en el campo de cliente se asigna como cliente anonimo
            if (txtNombreClienteVenta.getText().isBlank()) {
                cliente = new ClienteServices().getIdClienteNoRegistrado();

            } else {
                // y si el campo tiene nombre quiere decir que esta en la base de datos agregado previamente
                id = Integer.parseInt((String) txtidClienteVenta.getText());
                nombre = txtNombreClienteVenta.getText();
                cliente = new Cliente(id, nombre, "");
            }

            // se procede con el proceso de venta
            try {
                //1. Se Guarda la venta
                int key = new VentasServices().guardarVentasProducto(new Ventas(fecha, subtotal, total));

                // Luego se guarda la factura
                int fkey = new FacturaServices().
                        createFactura(new Facturas(fecha, key));

                if (fkey == 0) {
                    JOptionPane.showMessageDialog(JDialogVenta,
                            "Error al guardar la factura:");
                }
                //al validar que se guardo correctamente la facutura se guarda la factura del cliente
                String responseF = new FacturasClienteServices().
                        createFacturasCliente(fkey, cliente.getId());

                //Luego se procede a guardar todos los productos de la venta
                List<Productos> productos = new ArrayList<>();
                for (int i = 0; i < jListVenta.getModel().getSize(); i++) {
                    productos.add(jListVenta.getModel().getElementAt(i));
                }

                List<VentasProducto> listVenta = new ArrayList<>();

                for (Productos p : productos) {
                    listVenta.add(new VentasProducto(key,
                            p.getId(), p.getCantidad(), p.getPrecio()));
                }

                String response = new VentasProductosServices().crearVentasProductos(listVenta);

                StringBuilder sb = new StringBuilder();

                //Creo el mensaje con informacion sobre el proceso
                sb.append("Venta id: ").append(key);
                sb.append("\n").append(responseF).append("\n").append(response);

                String message = sb.toString();

                //Lo muestro en un solo JOptionPane
                JOptionPane.showMessageDialog(JDialogVenta, message);

                //verifico si el check de generar facura esta marcado o no para iniciar el proceso de impirmir una factura
                if (checkGenerarFactura.isSelected()) {
                    String nombreCliente = cliente.getNombre();

                    double subTotal = subtotal;

                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("NombreCliente", nombreCliente);
                    parametros.put("SubTotal", subTotal);
                    parametros.put("Total", Total);

                    ReportesServices reportServices = new ReportesServices();

                    try {
                        reportServices.CrearFactura(parametros, productos, "/Reports/Report _facturacion.jrxml");
                    } catch (JRException ex) {
                        Logger.getLogger(PanelInventario.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                subtotal = 0.0;

                Total = 0.0;

                jLabelSubTotal.setText("");

                txtCantidadVenta.setText("");

                txtNombreClienteVenta.setText("");

                listModel.clear();

                obtenerDatos();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(JDialogVenta, e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(JDialogVenta, "No hay productos agregados, "
                    + "No se puede ejecutar la venta");
        }


    }//GEN-LAST:event_btnprocesarVentaActionPerformed

    private void btnBorrarProductoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarProductoVentaActionPerformed
        int index = jListVenta.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(JDialogVenta,
                    "Seleccione un producto para su eliminación");
        } else {
            listModel.removeElementAt(index);
        }
    }//GEN-LAST:event_btnBorrarProductoVentaActionPerformed

    private void btnCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarVentaActionPerformed
        int option = JOptionPane.showConfirmDialog(JDialogVenta,
                "Esta seguro que desea cancelar este venta",
                "Dialogo de Confirmación", JOptionPane.YES_NO_OPTION);
        if (option == 1) {
            JOptionPane.showMessageDialog(JDialogVenta,
                    "Operacion Cancelada");
        } else {
            JOptionPane.showMessageDialog(JDialogVenta, "Venta Cancelada");
            Subtotal = 0.0;

            jLabelSubTotal.setText("");

            listModel.clear();

            JDialogVenta.dispose();
        }
    }//GEN-LAST:event_btnCancelarVentaActionPerformed

    private void btnEliminarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductosActionPerformed
        int fila = JTableProductos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un producto a eliminar");

        } else {
            try {
                int option = JOptionPane.
                        showConfirmDialog(this, """
                                                                 Tome en cuenta que eliminar este producto reduce su existencia a cero del sistema;
                                                Ademas todas las ventas relacionadas y su registro en las ventas del mes seran eliminadas
                                                                  Desea proceder con la eliminacion  despues de saber todo esto? """,
                                "Diagolo de Eliminación", JOptionPane.YES_NO_OPTION);

                if (option != 1) {
                    int idProducto = Integer.parseInt((String) JTableProductos.getValueAt(fila, 0).toString());

                    String name = JOptionPane.showInputDialog(this, "ingrese el nombre del administrador");

                    String pass = JOptionPane.showInputDialog(this, "Ingrese la contraseña de administrador");

                    boolean verificarPass = new UsuariosServices().compararContraseña(name, pass);

                    if (!verificarPass) {
                        JOptionPane.showMessageDialog(this, "Contraseña o usuario incorrecto \n operación cancelada");
                    } else {
                        String response = new ProductoService().eliminarProducto(idProducto);
                        JOptionPane.showMessageDialog(this, response);
                        obtenerDatos();
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Operacion Cancelada");
                }

            } catch (HeadlessException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnEliminarProductosActionPerformed

    private void obtenerDatosCliente() {
        List<Cliente> clientes = new ClienteServices().obtenerClientes();
        DefaultListModel<Cliente> model = new DefaultListModel<>();

        for (Cliente c : clientes) {
            model.addElement(c);
        }

        jlistClientes.setModel(model);
    }

    private void limpiarCamposCliente() {
        txtIdCliente.setText("");
        txtNombreCliente.setText("");
        txtTelefonoCliente.setText("");
    }


    private void botonClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonClientesMouseClicked
        jDialogCliente.setVisible(true);
        jDialogCliente.setLocationRelativeTo(this);
    }//GEN-LAST:event_botonClientesMouseClicked

    private void btnAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarClienteActionPerformed
        String nombre = txtNombreCliente.getText();
        String telefono = txtTelefonoCliente.getText();

        Cliente cliente = new Cliente(nombre, telefono);

        try {
            String response = new ClienteServices().crearCliente(cliente);
            JOptionPane.showMessageDialog(jDialogCliente, response);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(jDialogCliente, e.getMessage());
        }
        limpiarCamposCliente();
        obtenerDatosCliente();
    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        if (jlistClientes.getSelectedIndex() != -1) {
            int id = jlistClientes.getSelectedValue().getId();

            try {
                int option = JOptionPane.showConfirmDialog(jDialogCliente,
                        "Esta seguro de eliminar al Cliente ?",
                        "Eliminacion de Cliente", JOptionPane.YES_NO_OPTION);

                if (option != 0) {
                    JOptionPane.showMessageDialog(jDialogCliente, "operacion cancelada");
                } else {
                    String message = new ClienteServices().eliminarCliente(id);
                    JOptionPane.showMessageDialog(jDialogCliente, message);
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(jDialogCliente, " Error al eliminar el cliente" + e.getMessage());
            }

            obtenerDatosCliente();

        } else {
            JOptionPane.showMessageDialog(jDialogCliente, "Selecione un Cliente de la lista");
        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteVentaActionPerformed
        int index = jlistClientes.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(jDialogCliente,
                    "Seleccione un cliente para la venta");
        } else {
            int id = jlistClientes.getSelectedValue().getId();
            String nombreCliente = jlistClientes.getSelectedValue().getNombre();

            txtidClienteVenta.setText("" + id);
            txtNombreClienteVenta.setText(nombreCliente);
        }
    }//GEN-LAST:event_btnClienteVentaActionPerformed

    private void btnEstablecerCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstablecerCantidadActionPerformed
        int index = jListVenta.getSelectedIndex();
        String cantidad = "";
        if (index == -1) {
            JOptionPane.showMessageDialog(jDialogProductos,
                    "Por favor seleccione un producto para establecer su cantidad");
        } else {

            try {
                if (txtCantidadVenta.getText().contentEquals("")) {
                    JOptionPane.showMessageDialog(jDialogProductos,
                            "Ingrese una cantidad para el producto");
                } else {
                    cantidad = txtCantidadVenta.getText();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(jDialogProductos, e.getMessage());
            }

            Productos produto = jListVenta.getSelectedValue();
            produto.setCantidad(Integer.parseInt((String) cantidad));
            listModel.removeElementAt(index);
            listModel.addElement(produto);

            ObtenerSubtotalVenta();

        }
    }//GEN-LAST:event_btnEstablecerCantidadActionPerformed
    //Metodos para la busqueda de Los productos

    private void txtBuscarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBuscarProductoMouseClicked
        txtBuscarProducto.setText("");
        txtBuscarProducto.setForeground(Color.BLACK);
    }//GEN-LAST:event_txtBuscarProductoMouseClicked

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        if (txtBuscarProducto.getText().contentEquals("")
                || txtBuscarProducto.getText().contentEquals("Busque un producto")) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de un producto");
        } else {
            try {
                String clave = txtBuscarProducto.getText();
                List<Productos> producto = new ProductoService().obtenerProductoByName(clave);

                Map<Integer, String> categorias = obtenerCategorias();

                if (producto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, """
                                                    El producto no existe o 
                                                    No se inserto correctamente el nombre""");
                } else {

                    DefaultTableModel newmodel = new DefaultTableModel();
                    String[] Columnas = {"Id", "Nombre", "Indicaciones", "Marca",
                        "Categoria", "Precio", "Cantidad", "Fecha_caducidad"};

                    newmodel.setColumnIdentifiers(Columnas);

                    for (Productos p : producto) {
                        String nombreCategoria = categorias.get(p.getCategoria_id());
                        String[] renglon = {
                            String.valueOf(p.getId()),
                            p.getNombre(),
                            p.getIndicaciones(),
                            p.getMarca(),
                            nombreCategoria,
                            String.valueOf(p.getPrecio()),
                            String.valueOf(p.getCantidad()),};

                        newmodel.addRow(renglon);
                    }

                    JTableProductos.setModel(newmodel);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnQuitarFiltroProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuitarFiltroProductoMouseClicked
        if (txtBuscarProducto.getText().equalsIgnoreCase("busque un producto")) {
            JOptionPane.showMessageDialog(this, "No hay un filtro en el campo");
            txtBuscarProducto.setText("Busque un producto");
            txtBuscarProducto.setForeground(Color.GRAY);
        } else {
            obtenerDatos();
            txtBuscarProducto.setText("Busque un producto");
            txtBuscarProducto.setForeground(Color.GRAY);
        }

    }//GEN-LAST:event_btnQuitarFiltroProductoMouseClicked

    private void btnVentaMayorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaMayorActionPerformed
        int index = jListVenta.getSelectedIndex();
        Double precio = 0.0;
        if (index == -1) {
            JOptionPane.showMessageDialog(jDialogProductos,
                    "Por favor seleccione un producto para establecer su Precio por mayor");
        } else {

            Productos produto = jListVenta.getSelectedValue();

//            double tempTotal = produto.getCantidad() * produto.getVentaMayor();
            //produto.setCantidad(Integer.parseInt((String) cantidad));
            listModel.removeElementAt(index);
            listModel.addElement(produto);

            ObtenerSubtotalVenta();

            Total -= produto.getCantidad() * produto.getPrecio();

            //          Total += tempTotal;
            labelTotalVenta.setText("" + Total);
        }
    }//GEN-LAST:event_btnVentaMayorActionPerformed

    private void btnImportarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportarDatosActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo Excel");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filtro para aceptar solo archivos Excel
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".xlsx");
            }

            @Override
            public String getDescription() {
                return "Archivos Excel (*.xlsx)";
            }
        });

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());

            // Procesar el archivo Excel
            String message = new ImportExcel().leerExcel(selectedFile, "Productos");

            JOptionPane.showMessageDialog(this, message);
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }//GEN-LAST:event_btnImportarDatosActionPerformed

    private void labelBtnActualizarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelBtnActualizarProductoMouseClicked
        if (!validarCamposProducto()) {
            int id = Integer.parseInt(txtIdProducto.getText());
            String nombre = txtNombreProducto.getText();
            String indicaciones = txtIndicacionesProducto.getText();
            String presentacion = jComboPresentaciones.getItemAt(jComboPresentaciones.getSelectedIndex());
            String marca = txtMarcaProducto.getText();
            int categoria_id = jComboCategoriaProducto.
            getItemAt(jComboCategoriaProducto.getSelectedIndex()).getId();
            double precio = Double.parseDouble(txtPrecioCompraProducto.getText());

            int porcentaje = Integer.parseInt(txtPrecioMayorProducto.getText());

            double precioMayor = precio - (precio * ((double) porcentaje / 100));
            int cantidad = Integer.parseInt(txtCantidadProducto.getText());
            Date fecha_vencimiento = Date.valueOf(txtFecha_Vencimiento_Producto.getText());

            //Productos prod = new Productos(id, nombre, indicaciones, presentacion, marca, categoria_id, precio, cantidad);
            try {
                //Productos response = new ProductoService().actualizarProducto(prod);

                //JOptionPane.showMessageDialog(jDialogProductos,
                    //      response.toString());
                LimpiarCampos();
                obtenerDatos();
                jDialogProductos.dispose();

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(jDialogProductos, e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(jDialogProductos, """
                Rellene todos lo campos de texto del producto
                y seleccione una categoria""");
            }
    }//GEN-LAST:event_labelBtnActualizarProductoMouseClicked

    private void LabelBtnGuardarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LabelBtnGuardarProductoMouseClicked
        // Primero comprobar que cada campo de texto de productos este lleno
        if (validarCamposProducto()) {
            JOptionPane.showMessageDialog(this, "Rellene todos los campos y seleccione una categoria Disponible");
        } else {

            String nombre = txtNombreProducto.getText();
            String indicaciones = txtIndicacionesProducto.getText();
            String presentacion = jComboPresentaciones.getItemAt(jComboPresentaciones.getSelectedIndex());
            String marca = txtMarcaProducto.getText();
            int categoria_id = jComboCategoriaProducto.
            getItemAt(jComboCategoriaProducto.getSelectedIndex()).getId();
            double precio = Double.parseDouble(txtPrecioCompraProducto.getText());
            
            double costoCompra = Double.parseDouble((String) txtPrecioCompraProducto.getText());
            double costoTotal = Double.parseDouble((String) txtCostoTotalProducto.getText());
            double utilidad = Double.parseDouble((String) txtUtilidadProducto.getText());
            
            int porcentajeMayor = Integer.parseInt(txtPrecioMayorProducto.getText());

            //propiedades del lote
            String codigoLote = txtLoteProducto.getText();
            int cantidad = Integer.parseInt(txtCantidadProducto.getText());
            Date fecha_vencimiento = Date.valueOf(LocalDate.parse(txtFecha_Vencimiento_Producto.getText()));

            //propiedades de unidades multiples
            String descripcion = txtDescripcionProducto.getText();
            int factorConversion = Integer.parseInt((String) txtFactorConversionProducto.getText());

            double precioUnidadMultiple = ((double) precio * factorConversion);

            //---------------------------------------------------------------------

            //Construccion del objeto
            Productos producto = new Productos(nombre, indicaciones, presentacion, marca, categoria_id, costoCompra, costoTotal, utilidad, precio, cantidad);

            try {
                String message1 = new ProductoService().crearProducto(producto);

                int id_producto = producto.getId();

                String message2 = new LoteServices().guardarLote(new Lotes(id_producto,
                    codigoLote, fecha_vencimiento, cantidad, cantidad));

            String message3 = new ProductoService().guardarUnidadMayor(new UnidadesMultiples(id_producto,
                descripcion, factorConversion, precioUnidadMultiple));

        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append(message1).append("\n").
        append(message2).append("\n").
        append(message3);

        JOptionPane.showMessageDialog(this, sBuilder.toString());

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(PanelCrearProducto, e.getMessage());
        }

        LimpiarCampos();
        obtenerDatos();
        }

    }//GEN-LAST:event_LabelBtnGuardarProductoMouseClicked

    private void txtCostoTotalProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoTotalProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoTotalProductoActionPerformed

    private void txtPrecioCompraProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioCompraProductoActionPerformed
        double precioCompra = 0.0;
        
        precioCompra = Double.parseDouble((String) txtPrecioCompraProducto.getText());
        
        int cantidad = Integer.parseInt((String) txtCantidadProducto.getText());
        
        double precioCompraTotal = (double) precioCompra * cantidad;
        
        double utilidad = precioCompraTotal * ((double) 20/100); 
        
        double precioUtilidad = precioCompraTotal + utilidad;
        
        double precioUnitario = Math.round(precioUtilidad / cantidad);
        
        txtCostoTotalProducto.setText(""+precioCompraTotal);
        
        txtUtilidadProducto.setText(""+utilidad);
        
        txtPrecioUtilidadProducto.setText(""+precioUtilidad);
        
        txtPrecioUnidad.setText(""+precioUnitario);
        
    }//GEN-LAST:event_txtPrecioCompraProductoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog JDialogVenta;
    private javax.swing.JTable JTableProductos;
    private javax.swing.JLabel LabelBtnGuardarProducto;
    private javax.swing.JLabel LabelFecha;
    private javax.swing.JPanel PanelCrearProducto;
    private javax.swing.JButton botonClientes;
    private javax.swing.JButton btnActualizarCliente;
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JButton btnAgregarVenta;
    private javax.swing.JButton btnBorrarProductoVenta;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnClienteVenta;
    private javax.swing.JButton btnCrearProductos;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProductos;
    private javax.swing.JButton btnEstablecerCantidad;
    private javax.swing.JButton btnImportarDatos;
    private javax.swing.JButton btnQuitarFiltroProducto;
    private javax.swing.JButton btnVentaMayor;
    private javax.swing.JButton btnVentas;
    private javax.swing.JButton btnprocesarVenta;
    private javax.swing.JCheckBox checkGenerarFactura;
    private javax.swing.JComboBox<Categoria> jComboCategoriaProducto;
    private javax.swing.JComboBox<String> jComboPresentaciones;
    private javax.swing.JDialog jDialogCliente;
    private javax.swing.JDialog jDialogProductos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelSubTotal;
    private javax.swing.JList<models.Productos> jListVenta;
    private javax.swing.JPanel jPanelInventario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel jlabelTotal_Inventario;
    private javax.swing.JList<Cliente> jlistClientes;
    private javax.swing.JLabel labelBtnActualizarProducto;
    private javax.swing.JLabel labelCliente;
    private javax.swing.JLabel labelGeneric;
    private javax.swing.JLabel labelSub;
    private javax.swing.JLabel labelTotalCordobasEnInventario;
    private javax.swing.JLabel labelTotalVenta;
    private javax.swing.JPanel panelDialogClientes;
    private javax.swing.JPanel panelDialogVenta;
    private javax.swing.JTextField txtBuscarProducto;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCostoTotalProducto;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtFactorConversionProducto;
    private javax.swing.JTextField txtFecha_Vencimiento_Producto;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIndicacionesProducto;
    private javax.swing.JTextField txtLoteProducto;
    private javax.swing.JTextField txtMarcaProducto;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioCompraProducto;
    private javax.swing.JTextField txtPrecioMayorProducto;
    private javax.swing.JTextField txtPrecioUnidad;
    private javax.swing.JTextField txtPrecioUtilidadProducto;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtUtilidadProducto;
    public static javax.swing.JTextField txtidClienteVenta;
    // End of variables declaration//GEN-END:variables
}
