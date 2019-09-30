/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifce.view;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Eduardo
 */
public class PainelImagemQuadra extends javax.swing.JPanel {
    String diretorio = "/br/edu/ifce/view/imagens/quadra_basquete_rua.jpg";
    ImageIcon fundo = new ImageIcon(getClass().getResource(diretorio));
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Image img = fundo.getImage();
        g.drawImage(img, 0, 0, this);
    }
}
