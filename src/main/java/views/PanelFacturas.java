/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.ViewModels.VistaFacturas;
import models.ViewModels.VistaFacturasCliente;
import services.FacturasClienteServices;

/**
 *
 * @author jason
 */
public class PanelFacturas extends javax.swing.JPanel {

    /**
     * Creates new form PanelFacturas
     */
    public PanelFacturas() {
        initComponents();
        ObtenerFacturas();
    }

    private void ObtenerFacturas() {
        List<VistaFacturas> listaFactura = new FacturasClienteServices().obtenerVistaFactura();

        DefaultTableModel model = new DefaultTableModel();

        String[] columnas = {
            "Nombre Cliente",
            "Cliente Id",
            "Factura Id"
        };

        model.setColumnIdentifiers(columnas);

        for (VistaFacturas vf : listaFactura) {
            String[] renglon = {
                vf.getNombre_cliente(),
                String.valueOf(vf.getCliente_id()),
                String.valueOf(vf.getFactura_id())
            };

            model.addRow(renglon);
        }

        JtableFacturas.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JtableFacturas = new javax.swing.JTable();
        txtBuscarFactura = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnBuscarFactura = new javax.swing.JButton();
        btnQuitarFiltro = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaFactura = new javax.swing.JTextArea();
        btnVerFactura = new javax.swing.JButton();
        btnEliminarVista = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        JtableFacturas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(JtableFacturas);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Nombre");

        btnBuscarFactura.setBackground(new java.awt.Color(204, 204, 255));
        btnBuscarFactura.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBuscarFactura.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscarFactura.setText("Buscar");
        btnBuscarFactura.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnBuscarFactura.setBorderPainted(false);
        btnBuscarFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBuscarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFacturaActionPerformed(evt);
            }
        });

        btnQuitarFiltro.setBackground(java.awt.Color.red);
        btnQuitarFiltro.setForeground(new java.awt.Color(255, 255, 255));
        btnQuitarFiltro.setText("Quitar Filtro");
        btnQuitarFiltro.setToolTipText("");
        btnQuitarFiltro.setActionCommand("Quitar");
        btnQuitarFiltro.setBorderPainted(false);
        btnQuitarFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarFiltroActionPerformed(evt);
            }
        });

        txtAreaFactura.setColumns(20);
        txtAreaFactura.setRows(5);
        jScrollPane2.setViewportView(txtAreaFactura);

        btnVerFactura.setText("Ver");
        btnVerFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerFacturaActionPerformed(evt);
            }
        });

        btnEliminarVista.setBackground(java.awt.Color.red);
        btnEliminarVista.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarVista.setText("Eliminar vista");
        btnEliminarVista.setBorderPainted(false);
        btnEliminarVista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVistaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel1)
                        .addGap(6, 6, 6)
                        .addComponent(txtBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(btnVerFactura)
                        .addGap(12, 12, 12)
                        .addComponent(btnEliminarVista)))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnQuitarFiltro)
                    .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(369, 399, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(txtBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(btnBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnQuitarFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnVerFactura)
                            .addComponent(btnEliminarVista))))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFacturaActionPerformed
        if (txtBuscarFactura.getText().isBlank()) {
            JOptionPane.showMessageDialog(jPanel1,
                    "Escriba un nombre para poder realizar una busqueda");
        } else {
            String clave = txtBuscarFactura.getText();

            List<VistaFacturas> listaFactura = new FacturasClienteServices().obtenerVistaFacturaByName(clave);

            DefaultTableModel model = new DefaultTableModel();

            String[] columnas = {
                "Nombre Cliente",
                "Cliente Id",
                "Factura Id"
            };

            model.setColumnIdentifiers(columnas);

            for (VistaFacturas vf : listaFactura) {
                String[] renglon = {
                    vf.getNombre_cliente(),
                    String.valueOf(vf.getCliente_id()),
                    String.valueOf(vf.getFactura_id())
                };

                model.addRow(renglon);
            }

            JtableFacturas.setModel(model);
        }


    }//GEN-LAST:event_btnBuscarFacturaActionPerformed

    private void btnQuitarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarFiltroActionPerformed
        ObtenerFacturas();
        txtBuscarFactura.setText("");
    }//GEN-LAST:event_btnQuitarFiltroActionPerformed

    private void btnVerFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerFacturaActionPerformed
        int fila = JtableFacturas.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla");
        } else {
            int facturaId = Integer.parseInt((String) JtableFacturas.getValueAt(fila, 2));

            try {
                List<VistaFacturasCliente> listaFactura = new FacturasClienteServices().obtenerVistaFacturaByFactura(facturaId);
                txtAreaFactura.setEditable(false);

                if (!listaFactura.isEmpty()) {
                    StringBuilder contenido = new StringBuilder();

                    for (VistaFacturasCliente facturas : listaFactura) {
                        contenido.append(facturas.toString()).append("\n");
                    }

                    txtAreaFactura.setText(contenido.toString());
                } else {
                    txtAreaFactura.setText("Esta factura esta vacia debido a que \n los productos relacionados a ella fueron eliminados");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnVerFacturaActionPerformed

    private void btnEliminarVistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVistaActionPerformed
        txtAreaFactura.setText("");
    }//GEN-LAST:event_btnEliminarVistaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable JtableFacturas;
    private javax.swing.JButton btnBuscarFactura;
    private javax.swing.JButton btnEliminarVista;
    private javax.swing.JButton btnQuitarFiltro;
    private javax.swing.JButton btnVerFactura;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtAreaFactura;
    private javax.swing.JTextField txtBuscarFactura;
    // End of variables declaration//GEN-END:variables
}
