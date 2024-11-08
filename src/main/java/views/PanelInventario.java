/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import java.awt.HeadlessException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Categoria;
import models.Cliente;
import models.Facturas;
import models.Producto;
import models.Ventas;
import models.VentasProducto;
import services.CategoriaServices;
import services.ClienteServices;
import services.FacturaServices;
import services.FacturasClienteServices;
import services.ProductoService;
import services.ReportesServices;
import services.VentasProductosServices;
import services.VentasServices;

/**
 *
 * @author jason
 */
public class PanelInventario extends javax.swing.JPanel {

    private DefaultTableModel tableModel = new DefaultTableModel();
    private DefaultListModel<Producto> listModel = new DefaultListModel<>();
    private DefaultListModel<Cliente> modelCliente = new DefaultListModel<>();
    private double Subtotal;

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

    private void obtenerDatos() {
        Map<Integer, String> categorias = obtenerCategorias();

        List<Producto> productos = new ProductoService().obtenerProductos();
        DefaultTableModel model = new DefaultTableModel();
        String[] Columnas = {"Id", "Nombre", "Indicaciones", "Marca",
            "Categoria", "Precio", "Cantidad", "Fecha_caducidad"};

        model.setColumnIdentifiers(Columnas);

        for (Producto p : productos) {
            String nombreCategoria = categorias.get(p.getCategoria_id());
            String[] renglon = {
                String.valueOf(p.getId()),
                p.getNombre(),
                p.getIndicaciones(),
                p.getMarca(),
                nombreCategoria,
                String.valueOf(p.getPrecio()),
                String.valueOf(p.getCantidad()),
                p.getFecha_vencimiento().toString()
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
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        txtFecha_Vencimiento_Producto.setText("");
    }
    
    
    //metodo para mostrar el subtotal de la venta
    private void ObtenerSubtotalVenta(){     
        Subtotal = 0.0;
        
        for(int i = 0; i<listModel.getSize(); i++){
            Subtotal += listModel.get(i).getPrecio() * listModel.get(i).getCantidad();
        }
        
        jLabelSubTotal.setText(""+Subtotal);
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
        txtPrecioProducto = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtFecha_Vencimiento_Producto = new javax.swing.JTextField();
        LabelBtnGuardarProducto = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIdProducto = new javax.swing.JTextField();
        labelBtnActualizarProducto = new javax.swing.JLabel();
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
        btnReportes = new javax.swing.JButton();

        jDialogProductos.setBounds(new java.awt.Rectangle(0, 0, 530, 400));
        jDialogProductos.setResizable(false);

        PanelCrearProducto.setPreferredSize(new java.awt.Dimension(480, 350));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Nombre");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel20.setText("Indicaciones");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setText("Marca");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel22.setText("Categoria");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setText("Precio");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setText("Cantidad");

        txtCantidadProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadProductoActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout PanelCrearProductoLayout = new javax.swing.GroupLayout(PanelCrearProducto);
        PanelCrearProducto.setLayout(PanelCrearProductoLayout);
        PanelCrearProductoLayout.setHorizontalGroup(
            PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCrearProductoLayout.createSequentialGroup()
                                .addComponent(txtIndicacionesProducto)
                                .addGap(18, 18, 18)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(txtMarcaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
                            .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtFecha_Vencimiento_Producto, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                .addGap(192, 192, 192)
                                .addComponent(jLabel20))
                            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LabelBtnGuardarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelBtnActualizarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                            .addComponent(jComboCategoriaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53))
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelCrearProductoLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .addComponent(jLabel19))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PanelCrearProductoLayout.setVerticalGroup(
            PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIndicacionesProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMarcaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel22))
                .addGap(6, 6, 6)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFecha_Vencimiento_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboCategoriaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelBtnGuardarProducto))
                .addGap(18, 18, 18)
                .addComponent(labelBtnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout jDialogProductosLayout = new javax.swing.GroupLayout(jDialogProductos.getContentPane());
        jDialogProductos.getContentPane().setLayout(jDialogProductosLayout);
        jDialogProductosLayout.setHorizontalGroup(
            jDialogProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(jDialogProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PanelCrearProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
        );
        jDialogProductosLayout.setVerticalGroup(
            jDialogProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(jDialogProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(PanelCrearProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
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

        panelDialogVenta.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 700, 330));

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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/farmacia(2).png"))); // NOI18N
        jLabel1.setText("Inventario Farmacia Jeshua");

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
        jScrollPane1.setViewportView(JTableProductos);

        btnCrearProductos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCrearProductos.setText("Crear");
        btnCrearProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCrearProductosMouseClicked(evt);
            }
        });

        btnEliminarProductos.setBackground(new java.awt.Color(255, 0, 0));
        btnEliminarProductos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEliminarProductos.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProductos.setText("Eliminar");
        btnEliminarProductos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
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

        btnReportes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnReportes.setText("Reporte");
        btnReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelInventarioLayout = new javax.swing.GroupLayout(jPanelInventario);
        jPanelInventario.setLayout(jPanelInventarioLayout);
        jPanelInventarioLayout.setHorizontalGroup(
            jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInventarioLayout.createSequentialGroup()
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(labelTotalCordobasEnInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(jlabelTotal_Inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)))
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(jSeparator1)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(btnAgregarVenta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))))
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCrearProductos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditarProducto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanelInventarioLayout.setVerticalGroup(
            jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInventarioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEliminarProductos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnEditarProducto)
                                .addComponent(btnCrearProductos)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlabelTotal_Inventario)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarVenta)
                    .addComponent(btnVentas)
                    .addComponent(labelTotalCordobasEnInventario)
                    .addComponent(botonClientes)
                    .addComponent(btnReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
        );

        add(jPanelInventario, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadProductoActionPerformed

    private void btnCrearProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCrearProductosMouseClicked
        LimpiarCampos();
        jDialogProductos.setVisible(true);
        jDialogProductos.setLocationRelativeTo(this);
        labelBtnActualizarProducto.setEnabled(false);
    }//GEN-LAST:event_btnCrearProductosMouseClicked

    private void LabelBtnGuardarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LabelBtnGuardarProductoMouseClicked

        String nombre = txtNombreProducto.getText();
        String indicaciones = txtIndicacionesProducto.getText();
        String marca = txtMarcaProducto.getText();
        int categoria_id = jComboCategoriaProducto.
                getItemAt(jComboCategoriaProducto.getSelectedIndex()).getId();
        double precio = Double.parseDouble(txtPrecioProducto.getText());
        int cantidad = Integer.parseInt(txtCantidadProducto.getText());
        Date fecha_vencimiento = Date.valueOf(txtFecha_Vencimiento_Producto.getText());

        //Construccion del objeto
        Producto producto = new Producto(nombre,
                indicaciones, marca, categoria_id,
                precio, cantidad, fecha_vencimiento);

        try {
            String response = new ProductoService().crearProducto(producto);
            JOptionPane.showMessageDialog(PanelCrearProducto, response);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(PanelCrearProducto, e.getMessage());
        }

        LimpiarCampos();
        obtenerDatos();
    }//GEN-LAST:event_LabelBtnGuardarProductoMouseClicked

    private void btnEditarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarProductoMouseClicked
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
                String marca = (String) this.JTableProductos.getValueAt(fila, 3);
                double precio = Double.parseDouble((String) this.JTableProductos.
                        getValueAt(fila, 5).toString());

                int cantidad = Integer.parseInt((String) this.JTableProductos.
                        getValueAt(fila, 6).toString());
                Date fecha = Date.valueOf((String) this.JTableProductos.
                        getValueAt(fila, 7).toString().split(" ")[0]);

                txtIdProducto.setText("" + id);
                txtNombreProducto.setText(nombre);
                txtIndicacionesProducto.setText(indicaciones);
                txtMarcaProducto.setText(marca);
                txtPrecioProducto.setText(String.valueOf(precio));
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
            try {
                Categoria[] categorias = ObtenerArrayCategorias();

                int id = Integer.parseInt((String) this.JTableProductos.
                        getValueAt(fila, 0).toString());

                String nombre = (String) this.JTableProductos.getValueAt(fila, 1);
                String indicaciones = (String) this.JTableProductos.getValueAt(fila, 2);
                String marca = (String) this.JTableProductos.getValueAt(fila, 3);

                String Nombrecategoria = (String) this.JTableProductos.getValueAt(fila, 4);

                double precio = Double.parseDouble((String) this.JTableProductos.
                        getValueAt(fila, 5).toString());
                
                // Establezco por predeterminado cantidad en uno
                int cantidad = 1;
                
                Date fecha = Date.valueOf((String) this.JTableProductos.
                        getValueAt(fila, 7).toString().split(" ")[0]);

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

                Producto producto = new Producto(id, nombre, indicaciones, marca, categoriaId, precio, cantidad, fecha);
                
                listModel.addElement(producto);

                JOptionPane.showMessageDialog(labelCliente, "producto agregado: " + producto.getNombre());

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panelDialogVenta,
                        "Error al intentar agregar el producto: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnAgregarVentaMouseClicked

    private void btnVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentasMouseClicked
        if (jListVenta.getModel().getSize() < 1) {
            JOptionPane.showMessageDialog(labelCliente,
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
        Date fecha = Date.valueOf(LocalDate.now());
        double total = (Double) Double.parseDouble(jLabelSubTotal.getText());
        int idCliente = 0;
        if (txtNombreClienteVenta.getText().contentEquals("")) {
            idCliente = new ClienteServices().getIdClienteNoRegistrado();
        } else {
            idCliente = Integer.parseInt((String) txtidClienteVenta.getText());
        }
        try {
            //1. Se Guarda la venta
            int key = new VentasServices().guardarVentasProducto(new Ventas(fecha, total));
            JOptionPane.showMessageDialog(labelCliente, "venta id: " + key);

            // Luego se guarda la factura
            int fkey = new FacturaServices().
                    createFactura(new Facturas(fecha, key));

            if (fkey == 0) {
                JOptionPane.showMessageDialog(labelCliente,
                        "Error al guardar la factura:");
            } else {
                //al validar que se guardo correctamente la facutura se guarda la factura del cliente
                String response = new FacturasClienteServices().
                        createFacturasCliente(fkey, idCliente);
                JOptionPane.showMessageDialog(labelCliente, response);
            }

            //Luego se procede a guardar todos los productos de la venta
            List<Producto> productos = new ArrayList<>();
            for (int i = 0; i < jListVenta.getModel().getSize(); i++) {
                productos.add(jListVenta.getModel().getElementAt(i));
            }

            List<VentasProducto> listVenta = new ArrayList<>();

            for (Producto p : productos) {
                listVenta.add(new VentasProducto(key,
                        p.getId(), p.getCantidad(), p.getPrecio()));
            }

            String response = new VentasProductosServices().crearVentasProductos(listVenta);

            JOptionPane.showMessageDialog(labelCliente, response);

            Subtotal = 0.0;

            jLabelSubTotal.setText("");

            listModel.clear();

            obtenerDatos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(labelCliente, e.getMessage());
        }
    }//GEN-LAST:event_btnprocesarVentaActionPerformed

    private void btnBorrarProductoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarProductoVentaActionPerformed
        int index = jListVenta.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(labelCliente,
                    "Seleccione un producto para su eliminación");
        } else {
            listModel.removeElementAt(index);
        }
    }//GEN-LAST:event_btnBorrarProductoVentaActionPerformed

    private void btnCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarVentaActionPerformed
        int option = JOptionPane.showConfirmDialog(labelCliente,
                "Esta seguro que desea cancelar este venta",
                "Dialogo de Confirmación", JOptionPane.YES_NO_OPTION);
        if (option == 1) {
            JOptionPane.showMessageDialog(labelCliente,
                    "Operacion Cancelada");
        } else {
            JOptionPane.showMessageDialog(labelCliente, "Venta Cancelada");
            Subtotal = 0.0;

            jLabelSubTotal.setText("");

            listModel.clear();

            JDialogVenta.dispose();
        }
    }//GEN-LAST:event_btnCancelarVentaActionPerformed

    private void btnEliminarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductosActionPerformed
        int fila = JTableProductos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(labelCliente,
                    "Por favor seleccione un producto a eliminar");

        } else {
            try {
                int option = JOptionPane.
                        showConfirmDialog(labelCliente, """
                                                                 Tome en cuenta que eliminar este producto reduce su existencia a cero del sistema;
                                                                  Desea proceder con la eliminacion ? """,
                                "Diagolo de Eliminación", JOptionPane.YES_NO_OPTION);

                if (option != 1) {
                    int idProducto = Integer.parseInt((String) JTableProductos.getValueAt(fila, 0).toString());

                    String response = new ProductoService().eliminarProducto(idProducto);
                    JOptionPane.showMessageDialog(labelCliente, response);
                    obtenerDatos();
                } else {
                    JOptionPane.showMessageDialog(labelCliente, "Operacion Cancelada");
                }

            } catch (HeadlessException | NumberFormatException e) {
                JOptionPane.showMessageDialog(labelCliente, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnEliminarProductosActionPerformed

    private void labelBtnActualizarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelBtnActualizarProductoMouseClicked

        int id = Integer.parseInt(txtIdProducto.getText());
        String nombre = txtNombreProducto.getText();
        String indicaciones = txtIndicacionesProducto.getText();
        String marca = txtMarcaProducto.getText();
        int categoria_id = jComboCategoriaProducto.
                getItemAt(jComboCategoriaProducto.getSelectedIndex()).getId();
        double precio = Double.parseDouble(txtPrecioProducto.getText());
        int cantidad = Integer.parseInt(txtCantidadProducto.getText());
        Date fecha_vencimiento = Date.valueOf(txtFecha_Vencimiento_Producto.getText());

        Producto prod = new Producto(id, nombre, indicaciones, marca, categoria_id, precio, cantidad, fecha_vencimiento);

        try {
            Producto response = new ProductoService().actualizarProducto(prod);

            JOptionPane.showMessageDialog(labelCliente,
                    response.toString());

            LimpiarCampos();
            obtenerDatos();
            jDialogProductos.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(labelCliente, e.getMessage());
        }
    }//GEN-LAST:event_labelBtnActualizarProductoMouseClicked

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
            JOptionPane.showMessageDialog(jlistClientes, response);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(jlistClientes, e.getMessage());
        }
        limpiarCamposCliente();
        obtenerDatosCliente();
    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        if (jlistClientes.getSelectedIndex() != -1) {
            int id = jlistClientes.getSelectedValue().getId();

            try {
                int option = JOptionPane.showConfirmDialog(jlistClientes,
                        "Esta seguro de eliminar al Cliente ?",
                        "Eliminacion de Cliente", JOptionPane.YES_NO_OPTION);

                if (option != 0) {
                    JOptionPane.showMessageDialog(jlistClientes, "operacion cancelada");
                } else {
                    String message = new ClienteServices().eliminarCliente(id);
                    JOptionPane.showMessageDialog(jlistClientes, message);
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(jlistClientes, " Error al eliminar el cliente" + e.getMessage());
            }

            obtenerDatosCliente();

        } else {
            JOptionPane.showMessageDialog(jlistClientes, "Selecione un Cliente de la lista");
        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteVentaActionPerformed
        int index = jlistClientes.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(labelCliente,
                    "Sellecione un cliente para la venta");
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
            JOptionPane.showMessageDialog(labelCliente,
                    "Por favor seleccione un producto para establecer su cantidad");
        } else {

            try {
                if (txtCantidadVenta.getText().contentEquals("")) {
                    JOptionPane.showMessageDialog(labelCliente,
                            "Ingrese una cantidad para el producto");
                } else {
                    cantidad = txtCantidadVenta.getText();
                }
            } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(labelCliente, e.getMessage());
            }

            Producto produto = jListVenta.getSelectedValue();
            produto.setCantidad(Integer.parseInt((String) cantidad));
            listModel.removeElementAt(index);
            listModel.addElement(produto);
            
            ObtenerSubtotalVenta();
        }
    }//GEN-LAST:event_btnEstablecerCantidadActionPerformed

    private void btnReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesActionPerformed
        try{
            ReportesServices reporte = new ReportesServices();
            reporte.productosCantidadBaja();
            
        } catch (Exception e){
            JOptionPane.showMessageDialog(labelCliente, e.getMessage());
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_btnReportesActionPerformed


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
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnClienteVenta;
    private javax.swing.JButton btnCrearProductos;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProductos;
    private javax.swing.JButton btnEstablecerCantidad;
    private javax.swing.JButton btnReportes;
    private javax.swing.JButton btnVentas;
    private javax.swing.JButton btnprocesarVenta;
    private javax.swing.JComboBox<Categoria> jComboCategoriaProducto;
    private javax.swing.JDialog jDialogCliente;
    private javax.swing.JDialog jDialogProductos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelSubTotal;
    private javax.swing.JList<Producto> jListVenta;
    private javax.swing.JPanel jPanelInventario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel jlabelTotal_Inventario;
    private javax.swing.JList<Cliente> jlistClientes;
    private javax.swing.JLabel labelBtnActualizarProducto;
    private javax.swing.JLabel labelCliente;
    private javax.swing.JLabel labelGeneric;
    private javax.swing.JLabel labelSub;
    private javax.swing.JLabel labelTotalCordobasEnInventario;
    private javax.swing.JPanel panelDialogClientes;
    private javax.swing.JPanel panelDialogVenta;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtFecha_Vencimiento_Producto;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIndicacionesProducto;
    private javax.swing.JTextField txtMarcaProducto;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtTelefonoCliente;
    public static javax.swing.JTextField txtidClienteVenta;
    // End of variables declaration//GEN-END:variables
}
