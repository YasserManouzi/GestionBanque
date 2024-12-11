package com.atoudeft.vue;

import javax.swing.*;
import java.awt.*;

public class PanneauTransfert extends JPanel{
    private JButton bTransfert;
    private JLabel lblMontantTransfert;
    private JTextField txtMontantTransfert, txtNumeroTransfert;
    private JLabel lblNumeroTransfert;

    public PanneauTransfert() {
        bTransfert = new JButton("Transférer le montant");
        lblMontantTransfert = new JLabel("Montant à transférer : ");
        txtMontantTransfert = new JTextField(20);
        lblNumeroTransfert = new JLabel("Numéro du compte pour le transfert : ");
        txtNumeroTransfert = new JTextField((30));

        bTransfert.setActionCommand("TRANSFER");

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(lblMontantTransfert);
        this.add(txtMontantTransfert);
        this.add(lblNumeroTransfert);
        this.add(txtNumeroTransfert);
        this.add(bTransfert);

        bTransfert.addActionListener(e -> {
            Window window = SwingUtilities.windowForComponent(this);
            if (window != null) {
                window.dispose();
            }
        });
    }

    public String getMontantFacture() {
        return txtMontantTransfert.getText().trim();
    }
    public String getNumeroFacture() {
        return txtNumeroTransfert.getText().trim();
    }
}
