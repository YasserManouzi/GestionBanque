package com.atoudeft.vue;

import javax.swing.*;
import java.awt.*;

public class PanneauFacture extends JPanel {
    private JButton bFacture;
    private JLabel lblMontantFacture;
    private JTextField txtMontantFacture, txtNumeroFacture, txtDescriptionFacture;
    private JLabel lblNumeroFacture;
    private JLabel lblDescriptionFacture;

    public PanneauFacture() {
        bFacture = new JButton("Payer votre facture");
        lblMontantFacture = new JLabel("Facture à payer: ");
        txtMontantFacture = new JTextField(20);
        lblNumeroFacture = new JLabel("Numéro de la facture : ");
        txtNumeroFacture = new JTextField((20));
        lblDescriptionFacture = new JLabel("Description de la facture : ");
        txtDescriptionFacture = new JTextField((20));

        bFacture.setActionCommand("FACTURE");

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(lblMontantFacture);
        this.add(txtMontantFacture);
        this.add(lblNumeroFacture);
        this.add(txtNumeroFacture);
        this.add(lblDescriptionFacture);
        this.add(txtDescriptionFacture);
        this.add(bFacture);

        bFacture.addActionListener(e -> {
            Window window = SwingUtilities.windowForComponent(this);
            if (window != null) {
                window.dispose();
            }
        });
    }

    public String getMontantFacture() {
        return txtMontantFacture.getText().trim();
    }
    public String getNumeroFacture() {
        return txtNumeroFacture.getText().trim();
    }
    public String getDescriptionFacture() {
        return txtDescriptionFacture.getText().trim();
    }
}
