package com.atoudeft.controleur;

import com.atoudeft.client.Client;
import com.atoudeft.vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurOperationsCompte implements ActionListener {
    private Client client;
    private JFrame fenetre;

    public EcouteurOperationsCompte(Client client) {
        this.client = client;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "DEPOT":
                PanneauDepot panneauDepot = new PanneauDepot();

                JDialog dialogDepot = new JDialog(fenetre, "Effectuer un dépôt", true);
                dialogDepot.setLayout(new BorderLayout());
                dialogDepot.add(panneauDepot, BorderLayout.CENTER);

                dialogDepot.setSize(300, 150);
                dialogDepot.setLocationRelativeTo(fenetre);

                dialogDepot.setVisible(true);
                client.envoyer("DEPOT " + panneauDepot.getMontantDepot());
                break;
            case "EPARGNE":
                client.envoyer("EPARGNE");
        }
    }

}
