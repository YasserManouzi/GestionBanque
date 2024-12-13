package com.atoudeft.vue;

import javax.swing.*;
import java.awt.*;

public class PanneauRetrait extends JPanel {
    private JButton bRetrait;
    private JLabel lblMontantRetrait;
    private JTextField txtMontantRetrait;

    public PanneauRetrait() {
        bRetrait = new JButton("Faire un retrait");
        lblMontantRetrait = new JLabel("Montant de retrait : ");
        txtMontantRetrait = new JTextField(20);

        bRetrait.setActionCommand("RETRAIT");

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(lblMontantRetrait);
        this.add(txtMontantRetrait);
        this.add(bRetrait);

        bRetrait.addActionListener(e -> {
            Window window = SwingUtilities.windowForComponent(this);
            if (window != null) {
                window.dispose();
            }
        });
    }

    public String getMontantRetrait() {
        return txtMontantRetrait.getText().trim();
    }
}
