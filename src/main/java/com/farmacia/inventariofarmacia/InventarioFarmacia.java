/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.farmacia.inventariofarmacia;

import controller.FarmaciaController;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author jason
 */
public class InventarioFarmacia {

    public static void main(String[] args) {
        
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e){
            System.out.println(e.getMessage());
        }
        
        FarmaciaController mainController = new FarmaciaController();
        
        mainController.setVisible(true);
    }
}
