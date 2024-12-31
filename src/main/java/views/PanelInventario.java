/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import Utils.ImportExcel;
import Utils.mathOpereations;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Categoria;
import models.Cliente;
import models.Creditos;
import models.Facturas;
import models.Lotes;
import models.Productos;
import models.UnidadesMultiples;
import models.Ventas;
import models.VentasProducto;
import models.ViewModels.TipoPago;
import models.ViewModels.VistaInventario;
import net.sf.jasperreports.engine.JRException;
import services.CategoriaServices;
import services.ClienteServices;
import services.CreditoServices;
import services.FacturaServices;
import services.FacturasClienteServices;
import services.InventarioServices;
import services.LoteServices;
import services.ProductoService;
import services.ReportesServices;
import services.UnidadesMayoresServices;
import services.UsuariosServices;
import services.VentasProductosServices;
import services.VentasServices;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.TimerTask;
import javax.swing.WindowConstants;
import models.CajaModels.Caja;
import models.CajaModels.Turno;
import models.Micelanea.Micelanea;
import models.Micelanea.VentasMicelanea;
import services.Caja.MovimientoServices;
import services.Micelanea.MiscelaneaServices;
import services.Micelanea.ventasMiscelaneaServices;

/**
 *
 * @author jason
 */
public class PanelInventario extends javax.swing.JPanel {

    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel listModel = new DefaultTableModel();
    private DefaultTableModel modelCliente = new DefaultTableModel();
    private DefaultTableModel excelTable;
    private DefaultTableModel ventaMicelaneaModel = new DefaultTableModel();
    List<Productos> listaAuxiliar = new ArrayList<>();
    private double Subtotal;
    private double Total;

    private static boolean isNotificated = false;

    private final String[] Unidades = {"PAQUETE", "CAJA"};

    /**
     * Creates new form PanelInventario
     */
    public PanelInventario() {
        this.Subtotal = 0.0;
        initComponents();

        //Inicializar el inventario
        String[] Columnas = {"Id", "Nombre", "Indicaciones", "Laboratorio", "Lote",
            "Categoria", "Presentación", "Precio", "Cantidad", "Costo", "Total", "Utilidad", "Unidad mayor", "contenido", "Precio mayor", "Vencimiento"};

        model.setColumnIdentifiers(Columnas);
        model.setRowCount(0);

        JTableProductos.setModel(model);

        obtenerDatos();
        obtenerCategorias();

        //Obtener los clientes
        modelCliente.setColumnIdentifiers(new String[]{
            "Id", "Nombre", "Telefono", "Dirección"
        });

        jTableClientes.setModel(modelCliente);

        obtenerDatosCliente();

        //Establecer las columnas de la tabla de las ventas
        listModel.setColumnIdentifiers(new String[]{
            "Id", "Nombre", "Precio", "Cantidad", "Unidad Mayor", "Contenido", "Precio Mayor"});

        jTableVenta.setModel(listModel);

        excelTable = new DefaultTableModel();
        excelTable.setColumnIdentifiers(new String[]{"Nombre", "Indicaciones", "Laboratorio", "Lote",
            "Categoria", "Presentación", "Precio", "Cantidad", "Costo", "Total", "Utilidad", "Unidad mayor", "Precio x Mayor", "Contenido", "Vencimiento"});

        inicializarNotificaciones();

        jComboModoPago.addItem(TipoPago.EFECTIVO);
        jComboModoPago.addItem(TipoPago.TARJETA);
        jComboModoPago.addItem(TipoPago.CREDITO);

        jComboUnidadesMultiples.addItem(Unidades[0]);
        jComboUnidadesMultiples.addItem(Unidades[1]);

        ventaMicelaneaModel.setColumnIdentifiers(new String[]{"Id Producto", "Nombre", "Precio", "Cantidad"});

        jTableVentaMicelanea.setModel(ventaMicelaneaModel);

    }

    private void inicializarNotificaciones() {

        if (isNotificated) {
            return;
        }

        isNotificated = true;

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        java.util.Date primeraEjecucion = calendar.getTime();

        java.util.Timer timer = new java.util.Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int resultado = new ProductoService().comprobarExistencias();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Revisa el sistema de reportes de inventario, \nal parecer hay productos con pocas existencias",
                            "Alerta de inventario", JOptionPane.WARNING_MESSAGE);

                }
            }
        }, primeraEjecucion, 1000 * 60 * 60 * 8);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int resultado = new LoteServices().lotesPorVencer();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Revisa el sistema de reportes de inventario, \nal parecer hay productos por vencer",
                            "Alerta de inventario", JOptionPane.WARNING_MESSAGE);

                }
            }
        }, primeraEjecucion, 1000 * 60 * 60 * 24);

    }

    private void obtenerDatos() {
        try {
            model.setRowCount(0);

            List<VistaInventario> inventario = new InventarioServices().getInventario();

            if (inventario != null) {
                for (VistaInventario p : inventario) {

                    String[] renglon = {
                        String.valueOf(p.getId_producto()),
                        p.getNombre_Producto(),
                        p.getIndicaciones(),
                        p.getLaboratorio(),
                        p.getCodigo_lote(),
                        p.getCategoria(),
                        p.getPresentacion(),
                        String.valueOf(p.getPrecio()),
                        String.valueOf(p.getCantidad()),
                        String.valueOf(p.getPrecio_costo()),
                        String.valueOf(p.getCosto_total()),
                        String.valueOf(p.getUtilidad()),
                        p.getUnidad_mayor(),
                        String.valueOf(p.getConversion()),
                        String.valueOf(p.getPrecio_mayor()),
                        p.getFecha_Vencimiento().toString().split(" ")[0]
                    };

                    model.addRow(renglon);
                }

                jlabelTotal_Inventario.setText("Total de Productos en inventario: "
                        + obtenerTotalProductos() + " Cajas de Productos");

                labelTotalCordobasEnInventario.setText("Total de Cordobas invertidos: "
                        + obtenerTotalCordobas() + " C$");
                labelGanancia.setText("Ganancia total de este inventario: " + obtenerGanancia() + " C$");

                JTableProductos.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private double obtenerGanancia() {
        return new InventarioServices().obtenerGanancias();
    }

    private void obtenerCategorias() {
        jComboCategoriaProducto.removeAllItems();
        try {
            List<Categoria> categorias = new CategoriaServices().obtenerCategorias();

            for (Categoria c : categorias) {
                jComboCategoriaProducto.addItem(new Categoria(c.getId(),
                        c.getNombre_categoria()));

                jComboBoxCategoriasBuscar.addItem(new Categoria(c.getId(),
                        c.getNombre_categoria()));

            }
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

    private void LimpiarCampos() {
        txtIdProducto.setText("");
        txtNombreProducto.setText("");
        txtIndicacionesProducto.setText("");
        txtMarcaProducto.setText("");
        txtPrecioCompraProducto.setText("");
        txtDescuentoUnidadMayor.setText("");
        txtCantidadProducto.setText("");
        txtFecha_Vencimiento_Producto.setText("");
        txtLoteProducto.setText("");
        txtCostoTotalProducto.setText("");
        txtUtilidadProducto.setText("");
        txtPrecioUtilidadProducto.setText("");
        txtPrecioUnidad.setText("");
        txtDescuentoUnidadMayor.setText("");
        txtFactorConversionProducto.setText("");
        txtCantidadLote.setText("");
        txtCantidadMinimaLote.setText("");
    }

    //metodo para mostrar el subtotal de la venta
    private void ObtenerSubtotalVenta() {
        Subtotal = 0.0;
        Total = 0.0;
        double precio;
        double precioMayor;
        int cantidad;
        int conversion;
        for (int i = 0; i < listModel.getRowCount(); i++) {
            precio = Double.parseDouble((String) listModel.getValueAt(i, 2));
            cantidad = Integer.parseInt((String) listModel.getValueAt(i, 3));
            conversion = Integer.parseInt((String) listModel.getValueAt(i, 5));
            precioMayor = Double.parseDouble((String) listModel.getValueAt(i, 6));

            if (cantidad >= conversion) {
                Subtotal += cantidad * precio;
                Total += cantidad * (precioMayor / conversion);
            } else {
                Subtotal += cantidad * precio;
                Total += cantidad * precio;
            }
        }

        jLabelSubTotal.setText("" + Subtotal);

        labelTotalVenta.setText("" + Total);
    }

    private String getSaleType() {
        boolean isMayor = false;
        boolean isIndividual = false;
        for (int i = 0; i < listModel.getRowCount(); i++) {
            int cantidad = Integer.parseInt((String) listModel.getValueAt(i, 3));
            int conversion = Integer.parseInt((String) listModel.getValueAt(i, 5));
            if (cantidad >= conversion) {
                isMayor = true;
            } else {
                isIndividual = true;
            }

            if (isIndividual && isMayor) {
                return "MIXTA";
            }
        }

        return isMayor ? "MAYORISTA" : "INDIVIDUAL";
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
        txtDescuentoUnidadMayor = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtLoteProducto = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
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
        jComboUnidadesMultiples = new javax.swing.JComboBox<>();
        txtCantidadLote = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtCantidadMinimaLote = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        JDialogVenta = new javax.swing.JDialog();
        panelDialogVenta = new javax.swing.JPanel();
        labelGeneric = new javax.swing.JLabel();
        labelCliente = new javax.swing.JLabel();
        btnBorrarProductoVenta = new javax.swing.JButton();
        labelSub = new javax.swing.JLabel();
        btnCancelarVenta = new javax.swing.JButton();
        btnprocesarVenta = new javax.swing.JButton();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableVenta = new javax.swing.JTable();
        jComboModoPago = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        txtIdTurno = new javax.swing.JTextField();
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
        txtDireccionCliente = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        jDialogImportarDatos = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableExcel = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        btnGuardarDatosExcel = new javax.swing.JButton();
        jDialogMicelaneos = new javax.swing.JDialog();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableMiscelanea = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        txtNombreMicelanea = new javax.swing.JTextField();
        txtCantidadMicelanea = new javax.swing.JTextField();
        txtPrecioCostoMicelanea = new javax.swing.JTextField();
        txtPrecioMicelanea = new javax.swing.JTextField();
        jComboBoxTipoMicelanea = new javax.swing.JComboBox<>();
        txtCostoMicelanea = new javax.swing.JTextField();
        txtUtilidadMicelanea = new javax.swing.JTextField();
        btnGuardarMicelanea = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnSeleccionarProducto = new javax.swing.JButton();
        btnVentaMicelanea = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        btnEditarMicelanea = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        txtIdMiscelanea = new javax.swing.JTextField();
        btnLimpiarCampos = new javax.swing.JButton();
        jDialogVentaMicelanea = new javax.swing.JDialog();
        panelDialogVenta1 = new javax.swing.JPanel();
        labelGeneric1 = new javax.swing.JLabel();
        labelCliente1 = new javax.swing.JLabel();
        btnBorrarMicelaneaVenta = new javax.swing.JButton();
        labelSub1 = new javax.swing.JLabel();
        btnCancelarVentaMicelanea = new javax.swing.JButton();
        btnProcesarVentaMicelanea = new javax.swing.JButton();
        txtNombreClienteVentaMicelanea = new javax.swing.JTextField();
        LabelFechaVentaMicelanea = new javax.swing.JLabel();
        jLabelSubTotalMicelanea = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtidClienteVentaMicelanea = new javax.swing.JTextField();
        txtCantidadVentaMicelanea = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        btnEstablecerCantidadMicelanea = new javax.swing.JButton();
        checkGenerarFacturaMicelanea = new javax.swing.JCheckBox();
        jLabel43 = new javax.swing.JLabel();
        labelTotalVentaMicelanea = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableVentaMicelanea = new javax.swing.JTable();
        jComboModoPagoMicelanea = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        txtIdTurnoMicelanea = new javax.swing.JTextField();
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
        labelGanancia = new javax.swing.JLabel();
        btnPanelMiscelaneos = new javax.swing.JButton();
        jComboBoxCategoriasBuscar = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        jDialogProductos.setTitle("Panel de Productos");
        jDialogProductos.setBounds(new java.awt.Rectangle(0, 0, 550, 400));
        jDialogProductos.setResizable(false);

        PanelCrearProducto.setPreferredSize(new java.awt.Dimension(550, 440));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Nombre");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel20.setText("Indicaciones");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setText("Marca/Laboratorio");

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
        jLabel24.setText("Cantidad de Unidades");

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

        jLabel8.setText("Formato: Año-Mes-Dia");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Presentación");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Descuento");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Lote");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Denominación");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("Factor de Conversión");

        txtFactorConversionProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFactorConversionProductoActionPerformed(evt);
            }
        });

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

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel39.setText("Cantidad");

        txtCantidadMinimaLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadMinimaLoteActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel40.setText("Cantidad Minima");

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
                                    .addComponent(txtPrecioUnidad)))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboUnidadesMultiples, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDescuentoUnidadMayor, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel8)))
                                .addGap(18, 18, 18)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCantidadLote))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                                        .addComponent(jLabel40)
                                        .addGap(51, 51, 51))
                                    .addComponent(txtCantidadMinimaLote))))))
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
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLoteProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFecha_Vencimiento_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCrearProductoLayout.createSequentialGroup()
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(jLabel40))
                        .addGap(10, 10, 10)
                        .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCantidadLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidadMinimaLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)))
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelCrearProductoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelCrearProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtDescuentoUnidadMayor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jComboUnidadesMultiples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
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

        txtNombreClienteVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        panelDialogVenta.add(txtNombreClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 130, -1));

        LabelFecha.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta.add(LabelFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 150, 30));

        jLabelSubTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta.add(jLabelSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 130, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Id");
        panelDialogVenta.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 30, 30));

        txtidClienteVenta.setEnabled(false);
        panelDialogVenta.add(txtidClienteVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 70, 30));

        txtCantidadVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCantidadVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadVentaActionPerformed(evt);
            }
        });
        panelDialogVenta.add(txtCantidadVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 120, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Cantidad");
        panelDialogVenta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, -1, -1));

        btnEstablecerCantidad.setText("Establecer Cantidad");
        btnEstablecerCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstablecerCantidadActionPerformed(evt);
            }
        });
        panelDialogVenta.add(btnEstablecerCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 180, -1));

        checkGenerarFactura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        checkGenerarFactura.setText("Generar Factura");
        panelDialogVenta.add(checkGenerarFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, 150, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Total:");
        panelDialogVenta.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        labelTotalVenta.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta.add(labelTotalVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 140, 30));

        jTableVenta.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTableVenta);

        panelDialogVenta.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 680, 300));
        panelDialogVenta.add(jComboModoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 150, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel30.setText("Id Turno");
        panelDialogVenta.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        txtIdTurno.setEnabled(false);
        panelDialogVenta.add(txtIdTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 80, -1));

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

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Id Cliente");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Nombre Cliente");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("Dirección");

        jTableClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTableClientes);

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
                        .addComponent(jLabel6))
                    .addGroup(panelDialogClientesLayout.createSequentialGroup()
                        .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(66, Short.MAX_VALUE))
            .addGroup(panelDialogClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
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
                        .addComponent(btnActualizarCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClienteVenta)
                            .addComponent(jLabel28)))
                    .addGroup(panelDialogClientesLayout.createSequentialGroup()
                        .addGroup(panelDialogClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialogClienteLayout = new javax.swing.GroupLayout(jDialogCliente.getContentPane());
        jDialogCliente.getContentPane().setLayout(jDialogClienteLayout);
        jDialogClienteLayout.setHorizontalGroup(
            jDialogClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogClienteLayout.setVerticalGroup(
            jDialogClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogClienteLayout.createSequentialGroup()
                .addComponent(panelDialogClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTableExcel.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTableExcel);

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel29.setText("Verifique todos los datos antes de ingresar algo al inventario");

        btnGuardarDatosExcel.setText("Guardar Datos");
        btnGuardarDatosExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarDatosExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogImportarDatosLayout = new javax.swing.GroupLayout(jDialogImportarDatos.getContentPane());
        jDialogImportarDatos.getContentPane().setLayout(jDialogImportarDatosLayout);
        jDialogImportarDatosLayout.setHorizontalGroup(
            jDialogImportarDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogImportarDatosLayout.createSequentialGroup()
                .addGroup(jDialogImportarDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(jDialogImportarDatosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addComponent(btnGuardarDatosExcel)))
                .addContainerGap())
        );
        jDialogImportarDatosLayout.setVerticalGroup(
            jDialogImportarDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogImportarDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogImportarDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(btnGuardarDatosExcel))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTableMiscelanea.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableMiscelanea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTableMiscelaneaKeyTyped(evt);
            }
        });
        jScrollPane5.setViewportView(jTableMiscelanea);

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setText("Inventario de Miscelaneos");

        txtPrecioCostoMicelanea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioCostoMicelaneaActionPerformed(evt);
            }
        });

        jComboBoxTipoMicelanea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnGuardarMicelanea.setText("Guardar producto");
        btnGuardarMicelanea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMicelaneaMouseClicked(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMouseClicked(evt);
            }
        });

        btnEliminarProducto.setText("Borrar producto");
        btnEliminarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarProductoMouseClicked(evt);
            }
        });

        btnSeleccionarProducto.setText("Seleccionar");
        btnSeleccionarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarProductoActionPerformed(evt);
            }
        });

        btnVentaMicelanea.setText("Venta");
        btnVentaMicelanea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVentaMicelaneaMouseClicked(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setText("Nombre");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setText("Tipo");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setText("Cantidad");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setText("Precio");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel36.setText("Precio de compra");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setText("Costo de compra total");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel38.setText("Utilidad");

        btnEditarMicelanea.setText("Editar");
        btnEditarMicelanea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMicelaneaMouseClicked(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel45.setText("Id Micelanea");

        txtIdMiscelanea.setEnabled(false);

        btnLimpiarCampos.setText("LimpiarCampos");
        btnLimpiarCampos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarCamposMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jDialogMicelaneosLayout = new javax.swing.GroupLayout(jDialogMicelaneos.getContentPane());
        jDialogMicelaneos.getContentPane().setLayout(jDialogMicelaneosLayout);
        jDialogMicelaneosLayout.setHorizontalGroup(
            jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogMicelaneosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addComponent(jLabel31)
                    .addGroup(jDialogMicelaneosLayout.createSequentialGroup()
                        .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jDialogMicelaneosLayout.createSequentialGroup()
                                    .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtNombreMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel32))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel33)
                                        .addComponent(jComboBoxTipoMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jDialogMicelaneosLayout.createSequentialGroup()
                                    .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPrecioMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel35))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel36)
                                        .addComponent(txtPrecioCostoMicelanea))))
                            .addGroup(jDialogMicelaneosLayout.createSequentialGroup()
                                .addComponent(btnGuardarMicelanea)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnActualizar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEliminarProducto)))
                        .addGap(18, 18, 18)
                        .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialogMicelaneosLayout.createSequentialGroup()
                                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtCantidadMicelanea)
                                        .addComponent(txtCostoMicelanea, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel34))
                                .addGap(18, 18, 18)
                                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel38)
                                    .addComponent(txtUtilidadMicelanea, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                    .addComponent(jLabel45)
                                    .addComponent(txtIdMiscelanea)))
                            .addGroup(jDialogMicelaneosLayout.createSequentialGroup()
                                .addComponent(btnEditarMicelanea)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimpiarCampos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                                .addComponent(btnSeleccionarProducto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVentaMicelanea)))))
                .addContainerGap())
        );
        jDialogMicelaneosLayout.setVerticalGroup(
            jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogMicelaneosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addGap(2, 2, 2)
                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxTipoMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdMiscelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38))
                .addGap(3, 3, 3)
                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioCostoMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCostoMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUtilidadMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioMicelanea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialogMicelaneosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarMicelanea)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminarProducto)
                    .addComponent(btnSeleccionarProducto)
                    .addComponent(btnVentaMicelanea)
                    .addComponent(btnEditarMicelanea)
                    .addComponent(btnLimpiarCampos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelDialogVenta1.setMinimumSize(new java.awt.Dimension(700, 480));
        panelDialogVenta1.setPreferredSize(new java.awt.Dimension(700, 480));
        panelDialogVenta1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelGeneric1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelGeneric1.setText("Fecha: ");
        panelDialogVenta1.add(labelGeneric1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, -1));

        labelCliente1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelCliente1.setText("Cliente: ");
        panelDialogVenta1.add(labelCliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, -1));

        btnBorrarMicelaneaVenta.setText("Borrar Producto");
        btnBorrarMicelaneaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarMicelaneaVentaActionPerformed(evt);
            }
        });
        panelDialogVenta1.add(btnBorrarMicelaneaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 177, -1));

        labelSub1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelSub1.setText("SubTotal: ");
        panelDialogVenta1.add(labelSub1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 80, -1));

        btnCancelarVentaMicelanea.setBackground(new java.awt.Color(255, 0, 0));
        btnCancelarVentaMicelanea.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarVentaMicelanea.setText("Cancelar Venta");
        btnCancelarVentaMicelanea.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelarVentaMicelanea.setBorderPainted(false);
        btnCancelarVentaMicelanea.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarVentaMicelanea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarVentaMicelaneaMouseClicked(evt);
            }
        });
        panelDialogVenta1.add(btnCancelarVentaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 177, -1));

        btnProcesarVentaMicelanea.setText("Procesar Venta");
        btnProcesarVentaMicelanea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarVentaMicelaneaActionPerformed(evt);
            }
        });
        panelDialogVenta1.add(btnProcesarVentaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 177, -1));

        txtNombreClienteVentaMicelanea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        panelDialogVenta1.add(txtNombreClienteVentaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 130, -1));

        LabelFechaVentaMicelanea.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta1.add(LabelFechaVentaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 150, 30));

        jLabelSubTotalMicelanea.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta1.add(jLabelSubTotalMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 130, 30));

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel41.setText("Id");
        panelDialogVenta1.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 30, 30));

        txtidClienteVentaMicelanea.setEnabled(false);
        panelDialogVenta1.add(txtidClienteVentaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 70, 30));

        txtCantidadVentaMicelanea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCantidadVentaMicelanea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadVentaMicelaneaActionPerformed(evt);
            }
        });
        panelDialogVenta1.add(txtCantidadVentaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 120, 30));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel42.setText("Cantidad");
        panelDialogVenta1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, -1, -1));

        btnEstablecerCantidadMicelanea.setText("Establecer Cantidad");
        btnEstablecerCantidadMicelanea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstablecerCantidadMicelaneaActionPerformed(evt);
            }
        });
        panelDialogVenta1.add(btnEstablecerCantidadMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 180, -1));

        checkGenerarFacturaMicelanea.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        checkGenerarFacturaMicelanea.setText("Generar Factura");
        panelDialogVenta1.add(checkGenerarFacturaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, 150, -1));

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel43.setText("Total:");
        panelDialogVenta1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        labelTotalVentaMicelanea.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        panelDialogVenta1.add(labelTotalVentaMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 140, 30));

        jTableVentaMicelanea.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(jTableVentaMicelanea);

        panelDialogVenta1.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 680, 300));
        panelDialogVenta1.add(jComboModoPagoMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 150, -1));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel44.setText("Id Turno");
        panelDialogVenta1.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        txtIdTurnoMicelanea.setEnabled(false);
        panelDialogVenta1.add(txtIdTurnoMicelanea, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 80, -1));

        javax.swing.GroupLayout jDialogVentaMicelaneaLayout = new javax.swing.GroupLayout(jDialogVentaMicelanea.getContentPane());
        jDialogVentaMicelanea.getContentPane().setLayout(jDialogVentaMicelaneaLayout);
        jDialogVentaMicelaneaLayout.setHorizontalGroup(
            jDialogVentaMicelaneaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogVenta1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogVentaMicelaneaLayout.setVerticalGroup(
            jDialogVentaMicelaneaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDialogVenta1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        labelGanancia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelGanancia.setText("Total de Ganancia en C$: ");

        btnPanelMiscelaneos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPanelMiscelaneos.setText("Miscelaneos");
        btnPanelMiscelaneos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPanelMiscelaneosMouseClicked(evt);
            }
        });

        jButton1.setText("Buscar por Categoria");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(labelTotalCordobasEnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelGanancia))
                            .addComponent(jlabelTotal_Inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(103, 103, Short.MAX_VALUE)
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
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton1)
                                    .addComponent(jComboBoxCategoriasBuscar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnQuitarFiltroProducto)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                                .addComponent(btnPanelMiscelaneos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
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
                                        .addComponent(btnCrearProductos)
                                        .addComponent(btnPanelMiscelaneos)))
                                .addGap(9, 9, 9)
                                .addComponent(btnImportarDatos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregarVenta)
                            .addComponent(btnVentas)
                            .addComponent(labelTotalCordobasEnInventario)
                            .addComponent(botonClientes)
                            .addComponent(labelGanancia)))
                    .addGroup(jPanelInventarioLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarProducto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnQuitarFiltroProducto)
                            .addComponent(jComboBoxCategoriasBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
        );

        add(jPanelInventario, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    public boolean validarCamposProducto() {

        return txtNombreProducto.getText().isBlank() || txtIndicacionesProducto.getText().isBlank() || txtMarcaProducto
                .getText().isBlank() || jComboCategoriaProducto.getSelectedIndex() == -1 || txtPrecioCompraProducto
                .getText().isBlank() || txtCantidadProducto.getText().isBlank() || txtFecha_Vencimiento_Producto
                .getText().isBlank() || txtLoteProducto.getText().isBlank() || txtCostoTotalProducto.getText().isBlank()
                || txtUtilidadProducto.getText().isBlank() || txtPrecioUtilidadProducto.getText().isBlank() || txtPrecioUnidad
                .getText().isBlank() || txtFactorConversionProducto.getText().isBlank() || txtCantidadLote.getText().isBlank() || txtCantidadMinimaLote.getText().isBlank();

    }

    private void btnCrearProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCrearProductosMouseClicked
        LimpiarCampos();
        jDialogProductos.setVisible(true);
        jDialogProductos.setSize(723, 540);
        jDialogProductos.setLocationRelativeTo(this);
        labelBtnActualizarProducto.setEnabled(false);
        LabelBtnGuardarProducto.setEnabled(true);
        LabelBtnGuardarProducto.setVisible(true);
        txtCantidadMinimaLote.setText("" + 2);
        txtDescuentoUnidadMayor.setText("" + PanelConfiguraciones.obtenerDescuento());
        obtenerPresentación();
        obtenerCategorias();

    }//GEN-LAST:event_btnCrearProductosMouseClicked

    public void obtenerPresentación() {
        jComboPresentaciones.removeAllItems();
        List<String> presentaciones = new ProductoService().getPresentación();

        for (String p : presentaciones) {
            jComboPresentaciones.addItem(p);
        }
    }

    private void btnEditarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarProductoMouseClicked
        obtenerPresentación();
        obtenerCategorias();

        int fila = this.JTableProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(PanelCrearProducto, "Seleccione un registro de la tabla");
        } else {
            try {
                jDialogProductos.setVisible(true);
                jDialogProductos.setSize(723, 540);
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
                String lote = (String) this.JTableProductos.getValueAt(fila, 4);

                // Categoria y presentacion no necesarios (columna 5 y 6)
                double precio = Double.parseDouble((String) this.JTableProductos.
                        getValueAt(fila, 7).toString());

                int cantidad = Integer.parseInt((String) this.JTableProductos.
                        getValueAt(fila, 8).toString());

                //precio costo, precio de compra total, utilidad del porcentaje elejido por el usuario
                double precioCosto = Double.parseDouble((String) this.JTableProductos.getValueAt(fila, 9));
                double costoTotal = Double.parseDouble((String) this.JTableProductos.getValueAt(fila, 10));
                double utilidad = Double.parseDouble((String) this.JTableProductos.getValueAt(fila, 11));

                int conversion = Integer.parseInt((String) this.JTableProductos.getValueAt(fila, 13));
                double precioMayor = Double.parseDouble((String) this.JTableProductos.
                        getValueAt(fila, 14).toString());

                Date fecha = Date.valueOf((String) this.JTableProductos.
                        getValueAt(fila, 15).toString());

                int procentajeMayor = (int) (100 - ((precioMayor / conversion) * (100 / precio)));

                txtIdProducto.setText("" + id);
                txtNombreProducto.setText(nombre);
                txtIndicacionesProducto.setText(indicaciones);
                txtMarcaProducto.setText(marca);

                // sin categoria y presentacion
                txtPrecioCompraProducto.setText(String.valueOf(precioCosto));
                txtCostoTotalProducto.setText(String.valueOf(costoTotal));
                txtUtilidadProducto.setText("" + utilidad);
                txtPrecioUnidad.setText("" + precio);
                txtPrecioUtilidadProducto.setText(String.valueOf((double) costoTotal + utilidad));

                txtLoteProducto.setText(lote);
                txtDescuentoUnidadMayor.setText(String.valueOf(procentajeMayor));
                txtCantidadProducto.setText(String.valueOf(cantidad));
                txtFactorConversionProducto.setText(String.valueOf(conversion));
                txtFecha_Vencimiento_Producto.setText(fecha.toString());

                txtCantidadMinimaLote.setText("" + 2);

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

                    int id = Integer.parseInt((String) this.JTableProductos.
                            getValueAt(fila, 0).toString());

                    String nombre = (String) this.JTableProductos.getValueAt(fila, 1);
                    double precio = Double.parseDouble((String) this.JTableProductos.
                            getValueAt(fila, 7).toString());

                    int cantidadP = Integer.parseInt((String) this.JTableProductos.
                            getValueAt(fila, 8).toString());

                    String unidadMayor = (String) this.JTableProductos.getValueAt(fila, 12);
                    int contenido = Integer.parseInt((String) this.JTableProductos.getValueAt(fila, 13));
                    double precioMayor = Double.parseDouble((String) this.JTableProductos.getValueAt(fila, 14).toString());
                    // Establezco por predeterminado cantidad en uno
                    int cantidad = 1;

                    String[] renglon = {
                        String.valueOf(id),
                        nombre,
                        String.valueOf(precio),
                        String.valueOf(cantidad),
                        unidadMayor,
                        String.valueOf(contenido),
                        String.valueOf(precioMayor)
                    };

                    listaAuxiliar.add(new Productos(id, cantidadP));

                    listModel.addRow(renglon);

                    JOptionPane.showMessageDialog(labelCliente, "producto agregado: " + nombre);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(panelDialogVenta,
                            "Error al intentar agregar el producto: " + e.getMessage());
                }
            }

        }
    }//GEN-LAST:event_btnAgregarVentaMouseClicked

    private void btnVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentasMouseClicked
        if (jTableVenta.getModel().getRowCount() < 1) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos agregados a la venta");
        } else {
            JDialogVenta.setVisible(true);
            JDialogVenta.setLocationRelativeTo(this);
            JDialogVenta.setSize(700, 480);

            LabelFecha.setText(Date.valueOf(LocalDate.now()).toString());

            ObtenerSubtotalVenta();

            try {
                txtIdTurno.setText("" + PanelCaja.obtenerTurno().getId_turno());
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(JDialogVenta, "Revise si hay un turno abierto! \nEs necesario para procesar la venta");
            }

        }
    }//GEN-LAST:event_btnVentasMouseClicked

    private void btnprocesarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprocesarVentaActionPerformed

        if (listModel.getRowCount() > 0) {

            Date fecha = Date.valueOf(LocalDate.now());
            double subtotal = (Double) Double.parseDouble(jLabelSubTotal.getText());
            Double total = (double) Double.parseDouble(labelTotalVenta.getText());
            Cliente cliente;
            int id = 0;
            Turno turno = PanelCaja.obtenerTurno();
            Caja caja = PanelCaja.obtenerCaja();
            String nombre;
            // se procede con el proceso de venta
            try {
                if (turno == null) {
                    throw new RuntimeException("""
                                           Para Ejecutar una venta tiene que estar registrado el turno de un vendedor 
                                            Empiece un turno en la seccion de caja y vuelva a intentarlo""");
                }

                TipoPago tipo = jComboModoPago.getItemAt(jComboModoPago.getSelectedIndex());
                String tipo_pago = "";
                switch (tipo) {
                    case EFECTIVO -> {
                        tipo_pago = "EFECTIVO";
                        break;
                    }
                    //Se evalua si el pago fue a credito para guardarlo como una venta al credito
                    case CREDITO -> {
                        if (txtidClienteVenta.getText().isEmpty()) {
                            jDialogCliente.setVisible(true);
                            jDialogCliente.setLocationRelativeTo(this);

                            throw new IllegalArgumentException("Para aplicar un 'Credito' seleccione un cliente o cree uno nuevo desde esta ventana");
                        } else {
                            tipo_pago = "CREDITO";
                        }
                        break;
                    }

                    case TARJETA -> {
                        tipo_pago = "TARJETA";
                        break;
                    }

                    default ->
                        throw new Exception("Seleccione un tipo de pago");
                }

                // Si no se encuentra nombre en el campo de cliente se asigna como cliente anonimo
                if (txtNombreClienteVenta.getText().isBlank()) {
                    cliente = new ClienteServices().getIdClienteNoRegistrado();

                } else {
                    // y si el campo tiene nombre quiere decir que esta en la base de datos agregado previamente
                    id = Integer.parseInt((String) txtidClienteVenta.getText());
                    nombre = txtNombreClienteVenta.getText();
                    cliente = new Cliente(id, nombre, "", "");
                }

                //Primero se procede a guardar todos los productos de la venta procesando de igual forma a que precio se vendio
                List<Productos> productos = new ArrayList<>();
                for (int i = 0; i < jTableVenta.getModel().getRowCount(); i++) {
                    // primero capturo el id del producto
                    int idP = Integer.parseInt((String) this.jTableVenta.getValueAt(i, 0));
                    String nombreP = (String) this.jTableVenta.getValueAt(i, 1);
                    // declaro el precio antes de darle valor
                    double precioP;
                    double precioTemp = Double.parseDouble((String) this.jTableVenta.getValueAt(i, 2));
                    // obtengo la cantidad y el tipo de conversion para evaluar si el precio sera el normal 

                    int cantidadP = Integer.parseInt((String) this.jTableVenta.getValueAt(i, 3));
                    int conversion = Integer.parseInt((String) listModel.getValueAt(i, 5));
                    // obtengo el precio mayor para utilizarlo en la venta mayorista: (precioM/conversion) * cantidad
                    double precioM = Double.parseDouble((String) this.jTableVenta.getValueAt(i, 6));
                    if (cantidadP >= conversion) {

                        precioP = (precioM / conversion) * cantidadP;
                    } else {

                        precioP = (double) precioTemp * cantidadP;
                    }

                    int cantidadAuxiliar = listaAuxiliar.stream()
                            .filter(e -> e.getId() == idP)
                            .findFirst()
                            .orElseThrow(() -> new Exception("producto no indexado"))
                            .getCantidad();

                    if (cantidadAuxiliar < cantidadP) {
                        String nombreTemp = (String) this.jTableVenta.getValueAt(i, 1);
                        throw new IllegalArgumentException("La Cantidad de: " + nombreTemp + "\nen inventario es: " + cantidadAuxiliar + " \npor lo tanto baje su cantidad de la venta");
                    }

                    productos.add(new Productos(idP, nombreP, precioP, cantidadP));
                }

                String saleType = getSaleType();
                //1. Se Guarda la venta
                int key = new VentasServices().guardarVentasProducto(new Ventas(fecha,
                        saleType, tipo_pago, turno, subtotal, total));

                //Se registra el movimiento de la venta
                if (!tipo_pago.equalsIgnoreCase("CREDITO")) {
                    String respuesta = new MovimientoServices().resgistrarMovimiento(caja, total, "VENTA", "Venta");

                    System.out.println(respuesta);
                }

                // Luego se guarda la factura
                int fkey = new FacturaServices().
                        createFactura(new Facturas(fecha, key));

                if (fkey == 0) {
                    JOptionPane.showMessageDialog(JDialogVenta,
                            "Error al guardar la factura:");
                }

                // crea un credito si el tipo de venta lo es
                if (!txtidClienteVenta.getText().isBlank() && tipo_pago.contentEquals("CREDITO")) {
                    int interes = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el interes que tendra este credito"));
                    Date fecha_Vencimiento = Date.valueOf(JOptionPane.showInputDialog(JDialogVenta,
                            "Formato: AAAA-MM-DD",
                            "Ingrese la fecha de vencimiento del credito",
                            JOptionPane.PLAIN_MESSAGE));
                    int plazos = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de meses que desea agregar de plazo"));

                    double monto_total = total + ((total * (mathOpereations.integerToPercent(interes))));
                    String responseCredito = new CreditoServices()
                            .guardarCredito(new Creditos(id,
                                    fkey,
                                    Math.ceil(monto_total),
                                    Math.ceil(monto_total),
                                    interes,
                                    plazos,
                                    fecha,
                                    fecha_Vencimiento,
                                    "PENDIENTE"));

                    JOptionPane.showMessageDialog(JDialogVenta, responseCredito);
                }

                //al validar que se guardo correctamente la facutura se guarda la factura del cliente
                String responseF = new FacturasClienteServices().
                        createFacturasCliente(fkey, cliente.getId());

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
                    List<Map<String, Object>> campos = new ArrayList<>();
                    for (Productos p : productos) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("cantidad", p.getCantidad());
                        data.put("nombre", p.getNombre());
                        data.put("precio", p.getPrecio());

                        campos.add(data);

                    }

                    try {
                        reportServices.CrearFactura(parametros, campos, "/Reports/Report _facturacion.jrxml");
                    } catch (JRException ex) {
                        Logger.getLogger(PanelInventario.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                Subtotal = 0.0;

                Total = 0.0;

                jLabelSubTotal.setText("");

                txtCantidadVenta.setText("");

                txtNombreClienteVenta.setText("");

                listModel.setRowCount(0);

                listaAuxiliar.clear();

                obtenerDatos();

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(JDialogVenta, e.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(PanelInventario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(JDialogVenta, "No hay productos agregados, "
                    + "No se puede ejecutar la venta");
        }

    }//GEN-LAST:event_btnprocesarVentaActionPerformed

    private void btnBorrarProductoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarProductoVentaActionPerformed
        int index = jTableVenta.getSelectedRow();

        if (index == -1) {
            JOptionPane.showMessageDialog(JDialogVenta,
                    "Seleccione un producto para su eliminación");
        } else {
            listModel.removeRow(index);
            ObtenerSubtotalVenta();
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
            labelTotalVenta.setText("");

            txtidClienteVenta.setText("");
            txtNombreClienteVenta.setText("");

            listModel.setRowCount(0);

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
        modelCliente.setRowCount(0);
        List<Cliente> clientes = new ClienteServices().obtenerClientes();

        if (clientes != null) {
            for (Cliente c : clientes) {
                String[] renglon = {
                    String.valueOf(c.getId()),
                    c.getNombre(),
                    c.getTelefono(),
                    c.getDireccion()
                };

                modelCliente.addRow(renglon);
            }
        }
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

        try {
            if (txtNombreCliente.getText().isBlank() || txtTelefonoCliente.getText().isBlank() || txtDireccionCliente.getText().isBlank()) {
                throw new IllegalArgumentException("No puede haber ningun campo vacio al momento de ingresar un cliente \nNombre, \nTelefono, \nDireccion");
            }

            String nombre = txtNombreCliente.getText();
            String telefono = txtTelefonoCliente.getText();
            String direccion = txtDireccionCliente.getText();

            Cliente cliente = new Cliente(nombre, telefono, direccion);

            String response = new ClienteServices().crearCliente(cliente);
            JOptionPane.showMessageDialog(jDialogCliente, response);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(jDialogCliente, e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(jDialogCliente, e.getMessage());
        }
        limpiarCamposCliente();
        obtenerDatosCliente();

    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        int row = jTableClientes.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(jTableClientes.getValueAt(row, 0).toString());

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
        int index = jTableClientes.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(jDialogCliente,
                    "Seleccione un cliente para la venta");
        } else {
            int id = Integer.parseInt((String) jTableClientes.getValueAt(index, 0));
            String nombreCliente = (String) jTableClientes.getValueAt(index, 1);

            txtidClienteVenta.setText("" + id);
            txtNombreClienteVenta.setText(nombreCliente);

            txtidClienteVentaMicelanea.setText("" + id);
            txtNombreClienteVentaMicelanea.setText(nombreCliente);

            JOptionPane.showMessageDialog(this, "Cliente agregado: " + nombreCliente);
        }
    }//GEN-LAST:event_btnClienteVentaActionPerformed

    private void btnEstablecerCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstablecerCantidadActionPerformed
        int index = jTableVenta.getSelectedRow();
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

            listModel.setValueAt(cantidad, index, 3);

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
                model.setRowCount(0);

                String clave = txtBuscarProducto.getText();
                List<VistaInventario> producto = new InventarioServices().obtenerProductoByName(clave);

                if (producto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, """
                                                    El producto no existe o 
                                                    No se inserto correctamente el nombre""");
                } else {

                    for (VistaInventario p : producto) {

                        String[] renglon = {
                            String.valueOf(p.getId_producto()),
                            p.getNombre_Producto(),
                            p.getIndicaciones(),
                            p.getLaboratorio(),
                            p.getCodigo_lote(),
                            p.getCategoria(),
                            p.getPresentacion(),
                            String.valueOf(p.getPrecio()),
                            String.valueOf(p.getCantidad()),
                            String.valueOf(p.getPrecio_costo()),
                            String.valueOf(p.getCosto_total()),
                            String.valueOf(p.getUtilidad()),
                            p.getUnidad_mayor(),
                            String.valueOf(p.getConversion()),
                            String.valueOf(p.getPrecio_mayor()),
                            p.getFecha_Vencimiento().toString().split(" ")[0]
                        };

                        model.addRow(renglon);
                    }

                    JTableProductos.setModel(model);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnQuitarFiltroProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuitarFiltroProductoMouseClicked

        obtenerDatos();
        txtBuscarProducto.setText("Busque un producto");
        txtBuscarProducto.setForeground(Color.GRAY);


    }//GEN-LAST:event_btnQuitarFiltroProductoMouseClicked

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
            try {
                excelTable.setRowCount(0);
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());

                // Procesar el archivo Excel
                List<Object[]> objetos = new ImportExcel().leerExcel(selectedFile);

                for (Object[] fila : objetos) {
                    String[] reglon = {
                        String.valueOf(fila[0]),
                        String.valueOf(fila[1]),
                        String.valueOf(fila[2]),
                        String.valueOf(fila[3]),
                        String.valueOf(fila[4]),
                        String.valueOf(fila[5]),
                        String.valueOf(fila[6]),
                        String.valueOf(fila[7]).split("\\.")[0],
                        String.valueOf(fila[8]),
                        String.valueOf(fila[9]),
                        String.valueOf(fila[10]),
                        String.valueOf(fila[11]),
                        String.valueOf(fila[12]),
                        String.valueOf(fila[13]).split("\\.")[0],
                        LocalDate.parse(fila[14].toString()).toString()
                    };

                    excelTable.addRow(reglon);

                }

                jTableExcel.setModel(excelTable);

                jDialogImportarDatos.setVisible(true);
                jDialogImportarDatos.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                jDialogImportarDatos.setBounds(new Rectangle(1280, 540));
                jDialogImportarDatos.setSize(1280, 540);
                jDialogImportarDatos.setLocationRelativeTo(this);

            } catch (IOException ex) {
                Logger.getLogger(PanelInventario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }//GEN-LAST:event_btnImportarDatosActionPerformed

    private void labelBtnActualizarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelBtnActualizarProductoMouseClicked
        if (!validarCamposProducto()) {
            // propiedades del producto
            int idProducto = Integer.parseInt(txtIdProducto.getText());

            String nombre = txtNombreProducto.getText();
            String indicaciones = txtIndicacionesProducto.getText();
            String presentacion = jComboPresentaciones.getItemAt(jComboPresentaciones.getSelectedIndex());
            String marca = txtMarcaProducto.getText();
            int categoria_id = jComboCategoriaProducto.
                    getItemAt(jComboCategoriaProducto.getSelectedIndex()).getId();
            double precio = Double.parseDouble(txtPrecioUnidad.getText());

            double costoCompra = Double.parseDouble((String) txtPrecioCompraProducto.getText());
            double costoTotal = Double.parseDouble((String) txtCostoTotalProducto.getText());
            double utilidad = Double.parseDouble((String) txtUtilidadProducto.getText());

            //propiedades del lote
            String codigoLote = txtLoteProducto.getText();
            int cantidad = Integer.parseInt(txtCantidadProducto.getText());
            double cantidadLote = Double.parseDouble((String) txtCantidadLote.getText());
            double cantidadMinima = Double.parseDouble((String) txtCantidadMinimaLote.getText());
            Date fecha_vencimiento = Date.valueOf(LocalDate.parse(txtFecha_Vencimiento_Producto.getText()));

            //propiedades de unidades multiples
            int descuento = Integer.parseInt(txtDescuentoUnidadMayor.getText());
            String descripcion = jComboUnidadesMultiples.getItemAt(jComboUnidadesMultiples.getSelectedIndex());
            int factorConversion = Integer.parseInt((String) txtFactorConversionProducto.getText());

            double precioUnidadMultiple = ((double) precio * factorConversion) - (precio * factorConversion * (mathOpereations
                    .integerToPercent(descuento)));

            Productos producto = new Productos(idProducto,
                    nombre,
                    indicaciones,
                    presentacion,
                    marca,
                    categoria_id,
                    costoCompra,
                    costoTotal,
                    utilidad,
                    precio,
                    cantidad);

            Lotes lote = new Lotes(idProducto,
                    codigoLote,
                    fecha_vencimiento,
                    (double) cantidad / factorConversion,
                    cantidadLote, cantidadMinima);

            UnidadesMultiples unidadMayor = new UnidadesMultiples(idProducto,
                    descripcion,
                    factorConversion,
                    precioUnidadMultiple);

            try {
                Productos message1 = new ProductoService().actualizarProducto(producto);

                String message2 = new LoteServices().actualizarLote(lote, producto.getCantidad());

                String message3 = new UnidadesMayoresServices().actualizarUnidadMayor(unidadMayor);

                StringBuilder response = new StringBuilder();

                response.append(message1.getNombre()).append("\n")
                        .append(message2).append("\n")
                        .append(message3).append("\n");

                JOptionPane.showMessageDialog(this, response);

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
            try {
                String nombre = txtNombreProducto.getText();
                String indicaciones = txtIndicacionesProducto.getText();
                String presentacion = jComboPresentaciones.getItemAt(jComboPresentaciones.getSelectedIndex());
                String marca = txtMarcaProducto.getText();
                int categoria_id = jComboCategoriaProducto.
                        getItemAt(jComboCategoriaProducto.getSelectedIndex()).getId();
                double precio = Double.parseDouble(txtPrecioUnidad.getText());

                double costoCompra = Double.parseDouble((String) txtPrecioCompraProducto.getText());
                double costoTotal = Double.parseDouble((String) txtCostoTotalProducto.getText());
                double utilidad = Double.parseDouble((String) txtUtilidadProducto.getText());

                int descuento = Integer.parseInt(txtDescuentoUnidadMayor.getText());

                //propiedades del lote
                String codigoLote = txtLoteProducto.getText();
                int cantidad = Integer.parseInt(txtCantidadProducto.getText());
                double cantidadLote = Double.parseDouble((String) txtCantidadLote.getText());
                double cantidadMinima = Double.parseDouble((String) txtCantidadMinimaLote.getText());
                Date fecha_vencimiento = Date.valueOf(LocalDate.parse(txtFecha_Vencimiento_Producto.getText()));

                //propiedades de unidades multiples
                String descripcion = jComboUnidadesMultiples.getItemAt(jComboUnidadesMultiples.getSelectedIndex());
                int factorConversion = Integer.parseInt((String) txtFactorConversionProducto.getText());

                double precioUnidadMultiple = ((double) precio * factorConversion) - ((precio * factorConversion) * (mathOpereations.integerToPercent(descuento)));

                //---------------------------------------------------------------------
                //Construccion del objeto
                Productos producto = new Productos(nombre, indicaciones, presentacion, marca, categoria_id, costoCompra, costoTotal, utilidad, precio, cantidad);

                String message1 = new ProductoService().crearProducto(producto);

                int id_producto = producto.getId();

                //guardado del lote
                String message2 = new LoteServices().guardarLote(new Lotes(id_producto,
                        codigoLote, fecha_vencimiento, cantidadLote, cantidadLote, cantidadMinima));

                String message3 = new UnidadesMayoresServices().guardarUnidadMayor(new UnidadesMultiples(id_producto,
                        descripcion, factorConversion, precioUnidadMultiple));

                StringBuilder sBuilder = new StringBuilder();

                sBuilder.append(message1).append("\n").
                        append(message2).append("\n").
                        append(message3);

                JOptionPane.showMessageDialog(this, sBuilder.toString());

                LimpiarCampos();
                obtenerDatos();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(PanelCrearProducto,
                        "Estas Ingresando una letra en un campo de numeros, revisa los datos antes de proceder" + e.getMessage());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(PanelCrearProducto,
                        "Estas Ingresando mal la fecha de vencimiento" + e.getMessage());
            }

        }
    }//GEN-LAST:event_LabelBtnGuardarProductoMouseClicked

    private void txtCostoTotalProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoTotalProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoTotalProductoActionPerformed

    private void txtPrecioCompraProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioCompraProductoActionPerformed

        double precioCompra = Double.parseDouble((String) txtPrecioCompraProducto.getText());

        int cantidad = Integer.parseInt((String) txtCantidadProducto.getText());

        double precioCompraTotal = (double) precioCompra * cantidad;

        //Obtener la utilidad desde las configuraciones
        int utilidadConf = PanelConfiguraciones.obtenerUtilidad();

        double utilidad = precioCompraTotal * (mathOpereations.integerToPercent(utilidadConf));

        double precioUtilidad = precioCompraTotal + utilidad;

        double precioUnitario = Math.ceil(precioUtilidad / cantidad); //(precioUtilidad / cantidad);

        txtCostoTotalProducto.setText("" + precioCompraTotal);

        txtUtilidadProducto.setText("" + utilidad);

        txtPrecioUtilidadProducto.setText("" + precioUtilidad);

        txtPrecioUnidad.setText("" + precioUnitario);

    }//GEN-LAST:event_txtPrecioCompraProductoActionPerformed

    private void btnGuardarDatosExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarDatosExcelActionPerformed
        int filas = excelTable.getRowCount();

        int columnas = excelTable.getColumnCount();

        int indice = 0;

        List<Object[]> lista = new ArrayList<>();

        try {
            for (int row = 0; row < filas; row++) {
                indice = row;
                Object[] objeto = new Object[columnas];

                objeto[0] = this.jTableExcel.getValueAt(row, 0); // Assuming column 0 holds a String
                objeto[1] = this.jTableExcel.getValueAt(row, 1);
                objeto[2] = this.jTableExcel.getValueAt(row, 2);
                objeto[3] = this.jTableExcel.getValueAt(row, 3);
                objeto[4] = this.jTableExcel.getValueAt(row, 4);
                objeto[5] = this.jTableExcel.getValueAt(row, 5);

                objeto[6] = this.jTableExcel.getValueAt(row, 6); // Assuming column 6 holds a Double
                objeto[7] = this.jTableExcel.getValueAt(row, 7); // Assuming column 7 holds an Integer

                objeto[8] = this.jTableExcel.getValueAt(row, 8);
                objeto[9] = this.jTableExcel.getValueAt(row, 9);
                objeto[10] = this.jTableExcel.getValueAt(row, 10);

                objeto[11] = this.jTableExcel.getValueAt(row, 11);
                objeto[12] = this.jTableExcel.getValueAt(row, 12);
                objeto[13] = this.jTableExcel.getValueAt(row, 13);
                objeto[14] = Date.valueOf(this.jTableExcel.getValueAt(row, 14).toString());

                lista.add(objeto);
            }

            String message = new InventarioServices().guardarFromExcel(lista);

            obtenerDatos();

            JOptionPane.showMessageDialog(this, message);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en uno de los valores numericos de la tabla en el indice: " + indice);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Error en uno de los valores de fecha de la tabla en el indice: " + indice);
        }
    }//GEN-LAST:event_btnGuardarDatosExcelActionPerformed

    private void txtFactorConversionProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFactorConversionProductoActionPerformed
        try {
            if (txtCantidadLote.getText().isBlank()) {
                JOptionPane.showMessageDialog(jDialogProductos, "Inserte un dato en el campo de cantidad Lote");
            } else {
                int cantidadLote = Integer.parseInt((String) this.txtCantidadLote.getText());
                int conversion = Integer.parseInt((String) this.txtFactorConversionProducto.getText());

                txtCantidadProducto.setText("" + (cantidadLote * conversion));
            }
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(jDialogProductos, "Ingrese los datos numericos correctos: \nFator Conversion, \nCantidad Lote");
        }
    }//GEN-LAST:event_txtFactorConversionProductoActionPerformed

    private void txtCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadProductoActionPerformed
        int cantidadUnidades = Integer.parseInt((String) txtCantidadProducto.getText());
        int conversion = Integer.parseInt((String) txtFactorConversionProducto.getText());

        double cantidadLote = (double) cantidadUnidades / conversion;

        txtCantidadLote.setText("" + cantidadLote);
    }//GEN-LAST:event_txtCantidadProductoActionPerformed

    private void txtCantidadMinimaLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadMinimaLoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadMinimaLoteActionPerformed

    private void btnEstablecerCantidadMicelaneaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstablecerCantidadMicelaneaActionPerformed

    }//GEN-LAST:event_btnEstablecerCantidadMicelaneaActionPerformed

    private void btnGuardarMicelaneaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMicelaneaMouseClicked
        try {
            if (validarCamposMicelanea()) {
                JOptionPane.showMessageDialog(jDialogMicelaneos, "Rellene todos los campos para guardar el producto");
            } else if (!txtIdMiscelanea.getText().isBlank()) {
                JOptionPane.showMessageDialog(jDialogMicelaneos, "Lo que quieres es actualizar producto, por que ese producto ya existe");
            } else {
                String nombre = (String) txtNombreMicelanea.getText();
                String tipo = jComboBoxTipoMicelanea.getItemAt(jComboBoxTipoMicelanea.getSelectedIndex());
                int cantidad = Integer.parseInt((String) txtCantidadMicelanea.getText());
                double precioUnidad = Double.parseDouble((String) txtPrecioMicelanea.getText());
                double precioCompra = Double.parseDouble((String) txtPrecioCostoMicelanea.getText());
                double totalCompra = Double.parseDouble((String) txtCostoMicelanea.getText());
                double utilidadMicelanea = Double.parseDouble((String) txtUtilidadMicelanea.getText());

                Micelanea micelanea = new Micelanea(nombre,
                        tipo, cantidad, precioUnidad,
                        precioCompra, totalCompra, utilidadMicelanea);

                String message = new MiscelaneaServices().guardarMicelanea(micelanea);
                JOptionPane.showMessageDialog(jDialogMicelaneos, message);
                obtenerDatosMicelanea();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(jDialogMicelaneos, e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarMicelaneaMouseClicked

    private void btnPanelMiscelaneosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPanelMiscelaneosMouseClicked
        jDialogMicelaneos.setBounds(new Rectangle(960, 580));
        jDialogMicelaneos.setLocationRelativeTo(this);
        jDialogMicelaneos.setVisible(true);
        obtenerDatosMicelanea();
    }//GEN-LAST:event_btnPanelMiscelaneosMouseClicked

    private void txtPrecioCostoMicelaneaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioCostoMicelaneaActionPerformed
        double precioCompra = Double.parseDouble((String) txtPrecioCostoMicelanea.getText());
        int cantidad = Integer.parseInt((String) txtCantidadMicelanea.getText());

        double costoTotal = (double) precioCompra * cantidad;
        double utilidad = (double) costoTotal * 0.20;
        txtCostoMicelanea.setText("" + costoTotal);
        txtUtilidadMicelanea.setText("" + utilidad);

        double precioUnidad = Math.ceil((double) (costoTotal + utilidad) / cantidad);

        txtPrecioMicelanea.setText("" + precioUnidad);
    }//GEN-LAST:event_txtPrecioCostoMicelaneaActionPerformed

    private void btnEditarMicelaneaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMicelaneaMouseClicked
        if (jTableMiscelanea.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(jDialogMicelaneos, "Seleccione un producto de la tabla");
        } else {
            int row = jTableMiscelanea.getSelectedRow();

            int id_micelanea = Integer.parseInt((String) jTableMiscelanea.getValueAt(row, 0));
            String nombre = (String) jTableMiscelanea.getValueAt(row, 1);
            double precioUnitario = Double.parseDouble((String) jTableMiscelanea.getValueAt(row, 3));
            int cantidad = Integer.parseInt((String) jTableMiscelanea.getValueAt(row, 4));
            double precioCompra = Double.parseDouble((String) jTableMiscelanea.getValueAt(row, 5));
            double totalCompra = Double.parseDouble((String) jTableMiscelanea.getValueAt(row, 6));
            double utilidad = Double.parseDouble((String) jTableMiscelanea.getValueAt(row, 7));

            txtIdMiscelanea.setText("" + id_micelanea);
            txtNombreMicelanea.setText("" + nombre);
            txtCantidadMicelanea.setText("" + cantidad);
            txtPrecioMicelanea.setText("" + precioUnitario);
            txtPrecioCostoMicelanea.setText("" + precioCompra);
            txtCostoMicelanea.setText("" + totalCompra);
            txtUtilidadMicelanea.setText("" + utilidad);

        }
    }//GEN-LAST:event_btnEditarMicelaneaMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        try {
            if (!txtIdMiscelanea.getText().isBlank()) {
                int id_micelanea = Integer.parseInt((String) txtIdMiscelanea.getText());
                String nombre = (String) txtNombreMicelanea.getText();
                String tipo = jComboBoxTipoMicelanea.getItemAt(jComboBoxTipoMicelanea.getSelectedIndex());
                int cantidad = Integer.parseInt((String) txtCantidadMicelanea.getText());
                double precioUnidad = Double.parseDouble((String) txtPrecioMicelanea.getText());
                double precioCompra = Double.parseDouble((String) txtPrecioCostoMicelanea.getText());
                double totalCompra = Double.parseDouble((String) txtCostoMicelanea.getText());
                double utilidadMicelanea = Double.parseDouble((String) txtUtilidadMicelanea.getText());

                Micelanea micelanea = new MiscelaneaServices().
                        actualizarMicelanea(new Micelanea(id_micelanea, nombre, tipo, cantidad, precioUnidad, precioCompra, totalCompra, utilidadMicelanea));
                if (micelanea != null) {
                    JOptionPane.showMessageDialog(jDialogMicelaneos, "producto actualizado con exito");
                }
                LimpiarCamposMicelanea();
                obtenerDatosMicelanea();
            } else {
                JOptionPane.showMessageDialog(jDialogMicelaneos, "La acción que quieres realizar es guardar, ya que este producto no presenta un id");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(jDialogMicelaneos, "Ingresa los campos numericos de manera correcta");
        }
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarProductoMouseClicked
        if (jTableMiscelanea.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(jDialogMicelaneos, "Selecciona un producto de la tabla para poder eliminarlo");
        } else {
            int opcion = JOptionPane.showConfirmDialog(jDialogMicelaneos, "Estas seguro de eliminar este producto",
                    "Eliminacion de producto", JOptionPane.YES_NO_OPTION);
            if (opcion == 0) {
                try {
                    int row = jTableMiscelanea.getSelectedRow();

                    int id_micelanea = Integer.parseInt((String) jTableMiscelanea.getValueAt(row, 0));

                    String message = new MiscelaneaServices().eliminarMicelanea(id_micelanea);

                    JOptionPane.showMessageDialog(jDialogMicelaneos, message);

                    obtenerDatosMicelanea();
                } catch (HeadlessException | NumberFormatException e) {
                    JOptionPane.showMessageDialog(jDialogMicelaneos, e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(jDialogMicelaneos, "Operacion cancelada");
            }
        }
    }//GEN-LAST:event_btnEliminarProductoMouseClicked

    private void btnLimpiarCamposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarCamposMouseClicked
        LimpiarCamposMicelanea();
    }//GEN-LAST:event_btnLimpiarCamposMouseClicked

    private void btnVentaMicelaneaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentaMicelaneaMouseClicked
        if (jTableVentaMicelanea.getModel().getRowCount() < 1) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos agregados a la venta");
        } else {
            try {
                txtIdTurnoMicelanea.setText("" + PanelCaja.obtenerTurno().getId_turno());
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(JDialogVenta, "Revise si hay un turno abierto! \nEs necesario para procesar la venta");
            }
            jComboModoPagoMicelanea.removeAllItems();

            jComboModoPagoMicelanea.addItem(TipoPago.EFECTIVO);
            jComboModoPagoMicelanea.addItem(TipoPago.TARJETA);
            jComboModoPagoMicelanea.addItem(TipoPago.CREDITO);

            jDialogVentaMicelanea.setVisible(true);
            jDialogVentaMicelanea.setLocationRelativeTo(this);
            jDialogVentaMicelanea.setBounds(new Rectangle(700, 480));

            LabelFechaVentaMicelanea.setText(Date.valueOf(LocalDate.now()).toString());

            obtenerSubtotalMicelanea();

        }
    }//GEN-LAST:event_btnVentaMicelaneaMouseClicked

    public void obtenerSubtotalMicelanea() {
        Subtotal = 0.0;
        Total = 0.0;
        double precio;
        int cantidad;
        for (int i = 0; i < ventaMicelaneaModel.getRowCount(); i++) {
            precio = Double.parseDouble((String) ventaMicelaneaModel.getValueAt(i, 2));
            cantidad = Integer.parseInt((String) ventaMicelaneaModel.getValueAt(i, 3));

            Subtotal += cantidad * precio;
            Total += cantidad * precio;

        }

        jLabelSubTotalMicelanea.setText("" + Subtotal);

        labelTotalVentaMicelanea.setText("" + Total);
    }

    private void btnSeleccionarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarProductoActionPerformed
        seleccionarProducto();
    }//GEN-LAST:event_btnSeleccionarProductoActionPerformed

    private void jTableMiscelaneaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableMiscelaneaKeyTyped
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            seleccionarProducto();
        }
    }//GEN-LAST:event_jTableMiscelaneaKeyTyped

    private void txtCantidadVentaMicelaneaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadVentaMicelaneaActionPerformed
        int index = jTableVentaMicelanea.getSelectedRow();
        String cantidad = "";
        if (index == -1) {
            JOptionPane.showMessageDialog(jDialogVentaMicelanea,
                    "Por favor seleccione un producto para establecer su cantidad");
        } else {

            try {
                if (txtCantidadVentaMicelanea.getText().contentEquals("")) {
                    JOptionPane.showMessageDialog(jDialogVentaMicelanea,
                            "Ingrese una cantidad para el producto");
                } else {
                    cantidad = txtCantidadVentaMicelanea.getText();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(jDialogVentaMicelanea, e.getMessage());
            }

            ventaMicelaneaModel.setValueAt(cantidad, index, 3);

            obtenerSubtotalMicelanea();

        }
    }//GEN-LAST:event_txtCantidadVentaMicelaneaActionPerformed

    private void btnBorrarMicelaneaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarMicelaneaVentaActionPerformed
        int index = jTableVentaMicelanea.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(jDialogVentaMicelanea, "Seleccione un producto de la lista a eliminar");
        } else {
            int decision = JOptionPane.showConfirmDialog(jDialogVentaMicelanea, "Desea eliminar este producto de la venta ?",
                    "Eliminar Producto Venta", JOptionPane.YES_NO_OPTION);
            if (decision == 0) {
                ventaMicelaneaModel.removeRow(index);
                obtenerSubtotalMicelanea();
            } else {
                JOptionPane.showMessageDialog(jDialogVentaMicelanea, "Opereacion cancelada");
            }
        }
    }//GEN-LAST:event_btnBorrarMicelaneaVentaActionPerformed

    private void btnCancelarVentaMicelaneaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarVentaMicelaneaMouseClicked
        int decision = JOptionPane.showConfirmDialog(jDialogVentaMicelanea, "Desea cancelar esta venta ?",
                "Cancelar Venta", JOptionPane.YES_NO_OPTION);

        if (decision == 0) {
            ventaMicelaneaModel.setRowCount(0);
            jComboModoPagoMicelanea.removeAllItems();
            jDialogVentaMicelanea.dispose();
        }
    }//GEN-LAST:event_btnCancelarVentaMicelaneaMouseClicked

    private void btnProcesarVentaMicelaneaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarVentaMicelaneaActionPerformed
        if (ventaMicelaneaModel.getRowCount() > 0) {
            try {
                Date fecha = Date.valueOf(LocalDate.now());
                double subtotal = (Double) Double.parseDouble(jLabelSubTotalMicelanea.getText());
                Double total = (double) Double.parseDouble(labelTotalVentaMicelanea.getText());
                Cliente cliente;
                int id = 0;
                Turno turno = PanelCaja.obtenerTurno();
                Caja caja = PanelCaja.obtenerCaja();
                String nombre;

                if (turno == null) {
                    throw new RuntimeException("""
                                           Para Ejecutar una venta tiene que estar registrado el turno de un vendedor 
                                            Empiece un turno en la seccion de caja y vuelva a intentarlo""");
                }

                //Obtener el tipo de pago
                TipoPago tipo = jComboModoPago.getItemAt(jComboModoPago.getSelectedIndex());
                String tipo_pago = "";
                switch (tipo) {
                    case EFECTIVO -> {
                        tipo_pago = "EFECTIVO";
                        break;
                    }
                    //Se evalua si el pago fue a credito para guardarlo como una venta al credito
                    case CREDITO -> {
                        if (txtidClienteVentaMicelanea.getText().isEmpty()) {
                            jDialogCliente.setVisible(true);
                            jDialogCliente.setLocationRelativeTo(this);

                            throw new IllegalArgumentException("Para aplicar un 'Credito' seleccione un cliente o cree uno nuevo desde esta ventana");
                        } else {
                            tipo_pago = "CREDITO";
                        }
                        break;
                    }

                    case TARJETA -> {
                        tipo_pago = "TARJETA";
                        break;
                    }

                    default ->
                        throw new Exception("Seleccione un tipo de pago");
                }

                //evaluar si es un cliente registrado y no fue de tipo credito
                // Si no se encuentra nombre en el campo de cliente se asigna como cliente anonimo
                if (txtNombreClienteVentaMicelanea.getText().isBlank()) {
                    cliente = new ClienteServices().getIdClienteNoRegistrado();

                } else {
                    // y si el campo tiene nombre quiere decir que esta en la base de datos agregado previamente
                    id = Integer.parseInt((String) txtidClienteVentaMicelanea.getText());
                    nombre = txtNombreClienteVentaMicelanea.getText();
                    cliente = new Cliente(id, nombre, "", "");
                }

                //Se crea la lista de las miscelaneas a vender
                List<Micelanea> miscelaneas = new ArrayList<>();
                for (int i = 0; i < jTableVentaMicelanea.getModel().getRowCount(); i++) {
                    int id_micelanea = Integer.parseInt((String) jTableVentaMicelanea.getValueAt(i, 0));
                    String nombreMicelanea = (String) jTableVentaMicelanea.getValueAt(i, 1);

                    double precio = Double.parseDouble((String) jTableVentaMicelanea.getValueAt(i, 2));
                    int cantidad = Integer.parseInt((String) jTableVentaMicelanea.getValueAt(i, 3));

                    miscelaneas.add(new Micelanea(id_micelanea, nombreMicelanea, cantidad, precio));
                }

                //proceder con el guardado de la venta y las facturas
                //1. Se Guarda la venta
                int key = new VentasServices().guardarVentasProducto(new Ventas(fecha,
                        "INDIVIDUAL", tipo_pago, turno, subtotal, total));

                //Se registra el movimiento de la venta
                if (!tipo_pago.equalsIgnoreCase("CREDITO")) {
                    String respuesta = new MovimientoServices().resgistrarMovimiento(caja, total, "VENTA", "Venta");
                    System.out.println(respuesta);
                }

                // Luego se guarda la factura
                int fkey = new FacturaServices().
                        createFactura(new Facturas(fecha, key));

                if (fkey == 0) {
                    JOptionPane.showMessageDialog(jDialogVentaMicelanea,
                            "Error al guardar la factura:");
                }

                // crea un credito si el tipo de venta lo es
                if (!txtidClienteVentaMicelanea.getText().isBlank() && tipo_pago.contentEquals("CREDITO")) {
                    int interes = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el interes que tendra este credito"));
                    Date fecha_Vencimiento = Date.valueOf(JOptionPane.showInputDialog(jDialogVentaMicelanea,
                            "Formato: AAAA-MM-DD",
                            "Ingrese la fecha de vencimiento del credito",
                            JOptionPane.PLAIN_MESSAGE));
                    int plazos = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de meses que desea agregar de plazo"));

                    double monto_total = total + ((total * (mathOpereations.integerToPercent(interes))));
                    String responseCredito = new CreditoServices()
                            .guardarCredito(new Creditos(id,
                                    fkey,
                                    Math.ceil(monto_total),
                                    Math.ceil(monto_total),
                                    interes,
                                    plazos,
                                    fecha,
                                    fecha_Vencimiento,
                                    "PENDIENTE"));

                    JOptionPane.showMessageDialog(jDialogVentaMicelanea, responseCredito);
                }

                //al validar que se guardo correctamente la facutura se guarda la factura del cliente
                String responseF = new FacturasClienteServices().
                        createFacturasCliente(fkey, cliente.getId());

                System.out.println(responseF);

                //Se crea la lista de ventas Micelanea
                List<VentasMicelanea> listVentas = new ArrayList<>();

                for (Micelanea m : miscelaneas) {
                    listVentas.add(new VentasMicelanea(key, m, m.getCantidad(), m.getPrecio()));
                }

                String response = new ventasMiscelaneaServices().GuardarVentaMicelanea(listVentas);

                StringBuilder sb = new StringBuilder();

                //Creo el mensaje con informacion sobre el proceso
                sb.append("Venta id: ").append(key);
                sb.append("\n").append(responseF).append("\n").append(response);

                String message = sb.toString();

                //Lo muestro en un solo JOptionPane
                JOptionPane.showMessageDialog(jDialogVentaMicelanea, message);

                //verifico si el check de generar facura esta marcado o no para iniciar el proceso de impirmir una factura
                if (checkGenerarFacturaMicelanea.isSelected()) {
                    String nombreCliente = cliente.getNombre();

                    double subTotal = subtotal;

                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("NombreCliente", nombreCliente);
                    parametros.put("SubTotal", subTotal);
                    parametros.put("Total", Total);

                    ReportesServices reportServices = new ReportesServices();

                    List<Map<String, Object>> campos = new ArrayList<>();
                    for (Micelanea m : miscelaneas) {
                        Map<String, Object> data = new HashMap<>();

                        data.put("cantidad", m.getCantidad());
                        data.put("nombre", m.getNombre());
                        data.put("precio", m.getPrecio());

                        campos.add(data);
                    }

                    try {
                        reportServices.CrearFactura(parametros, campos, "/Reports/Report _facturacion.jrxml");
                    } catch (JRException ex) {
                        Logger.getLogger(PanelInventario.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                Subtotal = 0.0;

                Total = 0.0;

                jLabelSubTotalMicelanea.setText("");

                txtCantidadVentaMicelanea.setText("");

                txtNombreClienteVentaMicelanea.setText("");

                ventaMicelaneaModel.setRowCount(0);

                obtenerDatosMicelanea();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(jDialogVentaMicelanea, e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(jDialogVentaMicelanea, "No hay productos para realizar la venta");
        }
    }//GEN-LAST:event_btnProcesarVentaMicelaneaActionPerformed

    private void txtCantidadVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadVentaActionPerformed
        int index = jTableVenta.getSelectedRow();
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

            listModel.setValueAt(cantidad, index, 3);

            ObtenerSubtotalVenta();

        }
    }//GEN-LAST:event_txtCantidadVentaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            model.setRowCount(0);

            String clave = jComboBoxCategoriasBuscar.getItemAt(jComboBoxCategoriasBuscar.getSelectedIndex()).getNombre_categoria();
            List<VistaInventario> producto = new InventarioServices().obtenerProductoByCategoria(clave);

            if (producto.isEmpty()) {
                JOptionPane.showMessageDialog(this, """
                                                    El producto no existe o 
                                                    No se inserto correctamente el nombre""");
            } else {

                for (VistaInventario p : producto) {

                    String[] renglon = {
                        String.valueOf(p.getId_producto()),
                        p.getNombre_Producto(),
                        p.getIndicaciones(),
                        p.getLaboratorio(),
                        p.getCodigo_lote(),
                        p.getCategoria(),
                        p.getPresentacion(),
                        String.valueOf(p.getPrecio()),
                        String.valueOf(p.getCantidad()),
                        String.valueOf(p.getPrecio_costo()),
                        String.valueOf(p.getCosto_total()),
                        String.valueOf(p.getUtilidad()),
                        p.getUnidad_mayor(),
                        String.valueOf(p.getConversion()),
                        String.valueOf(p.getPrecio_mayor()),
                        p.getFecha_Vencimiento().toString().split(" ")[0]
                    };

                    model.addRow(renglon);
                }

                JTableProductos.setModel(model);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    public void seleccionarProducto() {
        int fila = this.jTableMiscelanea.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(jDialogMicelaneos,
                    "Por favor seleccione un producto para agregar a venta");
        } else {
            int existencia = Integer.parseInt(this.jTableMiscelanea.getValueAt(fila, 4).toString());
            if (existencia == 0) {
                JOptionPane.showMessageDialog(this, "no hay mas existencias de este producto en el inventario");
            } else {
                try {

                    int id = Integer.parseInt((String) this.jTableMiscelanea.
                            getValueAt(fila, 0).toString());

                    String nombre = (String) this.jTableMiscelanea.getValueAt(fila, 1);
                    double precio = Double.parseDouble((String) this.jTableMiscelanea.
                            getValueAt(fila, 3).toString());

                    // Establezco por predeterminado cantidad en uno
                    int cantidad = 1;

                    String[] renglon = {
                        String.valueOf(id),
                        nombre,
                        String.valueOf(precio),
                        String.valueOf(cantidad)
                    };

                    ventaMicelaneaModel.addRow(renglon);

                    JOptionPane.showMessageDialog(jDialogMicelaneos, "producto agregado: " + nombre);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(jDialogMicelaneos,
                            "Error al intentar agregar el producto: " + e.getMessage());
                }
            }

        }
    }

    private void obtenerDatosMicelanea() {
        DefaultTableModel modelMicelanea = new DefaultTableModel();
        String[] columnas = {"Id miscelanea", "Nombre", "Tipo", "Precio Unitario", "Cantidad", "Precio compra", "Total Compra", "Utilidad"};
        modelMicelanea.setColumnIdentifiers(columnas);

        jComboBoxTipoMicelanea.removeAllItems();

        jComboBoxTipoMicelanea.addItem("GALLETA");
        jComboBoxTipoMicelanea.addItem("BEBIDA");
        jComboBoxTipoMicelanea.addItem("DULCE");
        jComboBoxTipoMicelanea.addItem("COSMETICO");

        try {
            modelMicelanea.setRowCount(0);
            List<Micelanea> objetos = new MiscelaneaServices().obtenerProductos();

            if (objetos != null) {
                for (Micelanea m : objetos) {
                    String[] renglon = {
                        String.valueOf(m.getIdMicelanea()),
                        m.getNombre(),
                        m.getTipo(),
                        String.valueOf(m.getPrecio()),
                        String.valueOf(m.getCantidad()),
                        String.valueOf(m.getPrecioCosto()),
                        String.valueOf(m.getCostoTotal()),
                        String.valueOf(m.getUtilidad())
                    };

                    modelMicelanea.addRow(renglon);
                }
                jTableMiscelanea.setModel(modelMicelanea);
            }

        } catch (Exception e) {
            jTableMiscelanea.setModel(modelMicelanea);
        }
    }

    public void LimpiarCamposMicelanea() {
        txtNombreMicelanea.setText("");
        txtCantidadMicelanea.setText("");
        txtPrecioMicelanea.setText("");
        txtPrecioCostoMicelanea.setText("");
        txtCostoMicelanea.setText("");
        txtUtilidadMicelanea.setText("");

        txtIdMiscelanea.setText("");
    }

    public boolean validarCamposMicelanea() {
        return txtNombreMicelanea.getText().isBlank() || txtCantidadMicelanea.getText().isBlank()
                || txtPrecioMicelanea.getText().isBlank() || txtPrecioCostoMicelanea.getText().isBlank()
                || txtCostoMicelanea.getText().isBlank()
                || txtUtilidadMicelanea.getText().isBlank();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog JDialogVenta;
    private javax.swing.JTable JTableProductos;
    private javax.swing.JLabel LabelBtnGuardarProducto;
    private javax.swing.JLabel LabelFecha;
    private javax.swing.JLabel LabelFechaVentaMicelanea;
    private javax.swing.JPanel PanelCrearProducto;
    private javax.swing.JButton botonClientes;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnActualizarCliente;
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JButton btnAgregarVenta;
    private javax.swing.JButton btnBorrarMicelaneaVenta;
    private javax.swing.JButton btnBorrarProductoVenta;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnCancelarVentaMicelanea;
    private javax.swing.JButton btnClienteVenta;
    private javax.swing.JButton btnCrearProductos;
    private javax.swing.JButton btnEditarMicelanea;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnEliminarProductos;
    private javax.swing.JButton btnEstablecerCantidad;
    private javax.swing.JButton btnEstablecerCantidadMicelanea;
    private javax.swing.JButton btnGuardarDatosExcel;
    private javax.swing.JButton btnGuardarMicelanea;
    private javax.swing.JButton btnImportarDatos;
    private javax.swing.JButton btnLimpiarCampos;
    private javax.swing.JButton btnPanelMiscelaneos;
    private javax.swing.JButton btnProcesarVentaMicelanea;
    private javax.swing.JButton btnQuitarFiltroProducto;
    private javax.swing.JButton btnSeleccionarProducto;
    private javax.swing.JButton btnVentaMicelanea;
    private javax.swing.JButton btnVentas;
    private javax.swing.JButton btnprocesarVenta;
    private javax.swing.JCheckBox checkGenerarFactura;
    private javax.swing.JCheckBox checkGenerarFacturaMicelanea;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<Categoria> jComboBoxCategoriasBuscar;
    private javax.swing.JComboBox<String> jComboBoxTipoMicelanea;
    private javax.swing.JComboBox<Categoria> jComboCategoriaProducto;
    private javax.swing.JComboBox<TipoPago> jComboModoPago;
    private javax.swing.JComboBox<TipoPago> jComboModoPagoMicelanea;
    private javax.swing.JComboBox<String> jComboPresentaciones;
    private javax.swing.JComboBox<String> jComboUnidadesMultiples;
    private javax.swing.JDialog jDialogCliente;
    private javax.swing.JDialog jDialogImportarDatos;
    private javax.swing.JDialog jDialogMicelaneos;
    private javax.swing.JDialog jDialogProductos;
    private javax.swing.JDialog jDialogVentaMicelanea;
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
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelSubTotal;
    private javax.swing.JLabel jLabelSubTotalMicelanea;
    private javax.swing.JPanel jPanelInventario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTable jTableExcel;
    private javax.swing.JTable jTableMiscelanea;
    private javax.swing.JTable jTableVenta;
    private javax.swing.JTable jTableVentaMicelanea;
    private javax.swing.JLabel jlabelTotal_Inventario;
    private javax.swing.JLabel labelBtnActualizarProducto;
    private javax.swing.JLabel labelCliente;
    private javax.swing.JLabel labelCliente1;
    private javax.swing.JLabel labelGanancia;
    private javax.swing.JLabel labelGeneric;
    private javax.swing.JLabel labelGeneric1;
    private javax.swing.JLabel labelSub;
    private javax.swing.JLabel labelSub1;
    private javax.swing.JLabel labelTotalCordobasEnInventario;
    private javax.swing.JLabel labelTotalVenta;
    private javax.swing.JLabel labelTotalVentaMicelanea;
    private javax.swing.JPanel panelDialogClientes;
    private javax.swing.JPanel panelDialogVenta;
    private javax.swing.JPanel panelDialogVenta1;
    private javax.swing.JTextField txtBuscarProducto;
    private javax.swing.JTextField txtCantidadLote;
    private javax.swing.JTextField txtCantidadMicelanea;
    private javax.swing.JTextField txtCantidadMinimaLote;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCantidadVentaMicelanea;
    private javax.swing.JTextField txtCostoMicelanea;
    private javax.swing.JTextField txtCostoTotalProducto;
    private javax.swing.JTextField txtDescuentoUnidadMayor;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtFactorConversionProducto;
    private javax.swing.JTextField txtFecha_Vencimiento_Producto;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdMiscelanea;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdTurno;
    private javax.swing.JTextField txtIdTurnoMicelanea;
    private javax.swing.JTextField txtIndicacionesProducto;
    private javax.swing.JTextField txtLoteProducto;
    private javax.swing.JTextField txtMarcaProducto;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreClienteVentaMicelanea;
    private javax.swing.JTextField txtNombreMicelanea;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioCompraProducto;
    private javax.swing.JTextField txtPrecioCostoMicelanea;
    private javax.swing.JTextField txtPrecioMicelanea;
    private javax.swing.JTextField txtPrecioUnidad;
    private javax.swing.JTextField txtPrecioUtilidadProducto;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtUtilidadMicelanea;
    private javax.swing.JTextField txtUtilidadProducto;
    public static javax.swing.JTextField txtidClienteVenta;
    public static javax.swing.JTextField txtidClienteVentaMicelanea;
    // End of variables declaration//GEN-END:variables
}
