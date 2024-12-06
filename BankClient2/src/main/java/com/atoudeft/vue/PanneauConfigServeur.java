package com.atoudeft.vue;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-11-01
 */
public class PanneauConfigServeur extends JPanel {
    private JTextField txtAdrServeur, txtNumPort;

    public PanneauConfigServeur(String adr, int port) {
        this.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel lblAdrServeur = new JLabel("Adresse IP:");
        txtAdrServeur = new JTextField(adr);

        JLabel lblNumPort = new JLabel("Port:");
        txtNumPort = new JTextField(String.valueOf(port));

        this.add(lblAdrServeur);
        this.add(txtAdrServeur);
        this.add(lblNumPort);
        this.add(txtNumPort);

    }
    public String getAdresseServeur() {
        return txtAdrServeur.getText();
    }
    public String getPortServeur() {
        return txtNumPort.getText();
    }
}
