/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import models.Categoria;
import models.ViewModels.Presentacion;
import services.CategoriaServices;
import services.ProductoService;
import services.UsuariosServices;

/**
 *
 * @author jason
 */
public class PanelConfiguraciones extends javax.swing.JPanel {

    /**
     * Creates new form PanelConfiguraciones
     */
    public PanelConfiguraciones() {
        initComponents();
        ObtenerUsuarios();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtNombreCategoria = new javax.swing.JTextField();
        btnAgregarCategoria = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombreAdmin = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPasswordAdmin = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txtRolAdmin = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnGuardarAdmin = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaUsuarios = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtnombrePresentacion = new javax.swing.JTextField();
        btngaregarPresentacion = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Bienvenido a tu panel de configuración.");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Agregacion de Categorias.");

        btnAgregarCategoria.setText("Agregar");
        btnAgregarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCategoriaActionPerformed(evt);
            }
        });

        jLabel3.setText("Nombre de la Categoria");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Configuraciones de Administrador");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Nombre");

        txtPasswordAdmin.setText("jPasswordField1");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Contraseña");

        txtRolAdmin.setText("Admin");
        txtRolAdmin.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Rol");

        btnGuardarAdmin.setText("Guardar Admin");
        btnGuardarAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAdminActionPerformed(evt);
            }
        });

        jLabel8.setText("Crear Adminsitrador");

        txtAreaUsuarios.setColumns(20);
        txtAreaUsuarios.setRows(5);
        jScrollPane1.setViewportView(txtAreaUsuarios);

        jLabel9.setText("Agregación de presentaciones");

        jLabel10.setText("Nombre Presentación");

        btngaregarPresentacion.setText("Agregar");
        btngaregarPresentacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngaregarPresentacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(9, 9, 9)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(btnAgregarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(76, 76, 76)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(btngaregarPresentacion)
                                                    .addComponent(txtnombrePresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addGap(0, 36, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarAdmin)
                            .addComponent(txtRolAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtPasswordAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                .addComponent(txtNombreAdmin)))
                        .addGap(52, 52, 52)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10)
                    .addComponent(txtnombrePresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCategoria)
                    .addComponent(btngaregarPresentacion))
                .addGap(36, 36, 36)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreAdmin))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPasswordAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRolAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addComponent(btnGuardarAdmin)
                        .addContainerGap(82, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(16, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void ObtenerUsuarios(){
        try {
            List<String> nombres = new UsuariosServices().getUsuarios();
            
            txtAreaUsuarios.setEditable(false);
            StringBuilder contenidoArea = new StringBuilder();
            
            for(String nombre: nombres){
                contenidoArea.append(nombre).append("\n");
            }
            
            txtAreaUsuarios.setText(contenidoArea.toString());
        } catch (Exception e) {
            txtAreaUsuarios.setText(e.getMessage());
        }
    }
    
    private void btnAgregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCategoriaActionPerformed
        if (txtNombreCategoria.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "ingrese el nombre de la categoria\n ¡por favor!");
        } else {
            try {
                String nombre = txtNombreCategoria.getText();

                String response = new CategoriaServices().crearCategoria(new Categoria(nombre));

                JOptionPane.showMessageDialog(this, response);

                txtNombreCategoria.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnAgregarCategoriaActionPerformed

    private void btnGuardarAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAdminActionPerformed
        if (txtNombreAdmin.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un Nombre de administrador");
        } else if (txtPasswordAdmin.getPassword().length == -1) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese una contraseña para el administrador");
        } else {
            try {
                String a_nombre = txtNombreAdmin.getText();

                char[] passChars = txtPasswordAdmin.getPassword();

                String password = new String(passChars);

                Arrays.fill(passChars, '\u0000');

                String rol = txtRolAdmin.getText();

                String response = new UsuariosServices().crearUsuario(a_nombre, password, rol);

                JOptionPane.showMessageDialog(this, "a " + response);
                
                txtNombreAdmin.setText("");
                txtPasswordAdmin.setText("");
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnGuardarAdminActionPerformed

    private void btngaregarPresentacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngaregarPresentacionActionPerformed
        if(txtnombrePresentacion.getText().isBlank()){
            JOptionPane.showMessageDialog(this, "ingrese un nombre de producto");
        } else {
            String presentacion = txtnombrePresentacion.getText();
            
            
            
            ProductoService ps = new ProductoService();
            
            ps.ingresarPresentacion(new Presentacion(presentacion));
            
            txtnombrePresentacion.setText("");
        }
    }//GEN-LAST:event_btngaregarPresentacionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCategoria;
    private javax.swing.JButton btnGuardarAdmin;
    private javax.swing.JButton btngaregarPresentacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea txtAreaUsuarios;
    private javax.swing.JTextField txtNombreAdmin;
    private javax.swing.JTextField txtNombreCategoria;
    private javax.swing.JPasswordField txtPasswordAdmin;
    private javax.swing.JTextField txtRolAdmin;
    private javax.swing.JTextField txtnombrePresentacion;
    // End of variables declaration//GEN-END:variables
}
