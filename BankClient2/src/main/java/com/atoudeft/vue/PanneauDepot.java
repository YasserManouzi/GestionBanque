package com.atoudeft.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanneauDepot extends JPanel {
    private JButton bDepot;
    private JLabel lblMontantDepot;
    private JTextField txtMontantDepot;

    public PanneauDepot() {
        bDepot = new JButton("Déposer");
        lblMontantDepot = new JLabel("Montant à déposer : ");
        txtMontantDepot = new JTextField(20);

        bDepot.setActionCommand("DEPOT");

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(lblMontantDepot);
        this.add(txtMontantDepot);
        this.add(bDepot);

        bDepot.addActionListener(e -> {
            Window window = SwingUtilities.windowForComponent(this);
            if (window != null) {
                window.dispose();
            }
        });
    }

    public String getMontantDepot() {
        return txtMontantDepot.getText().trim();
    }
}
