package com.atoudeft.controleur;

import com.atoudeft.client.Client;
import com.atoudeft.vue.PanneauDepot;
import com.atoudeft.vue.PanneauFacture;
import com.atoudeft.vue.PanneauRetrait;
import com.atoudeft.vue.PanneauTransfert;

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

                dialogDepot.setSize(370, 110);
                dialogDepot.setLocationRelativeTo(fenetre);

                dialogDepot.setVisible(true);
                client.envoyer("DEPOT " + panneauDepot.getMontantDepot());
                break;

            case "RETRAIT":
                PanneauRetrait panneauRetrait = new PanneauRetrait();

                JDialog dialogRetrait = new JDialog(fenetre, "Effectuer un retrait", true);
                dialogRetrait.setLayout(new BorderLayout());
                dialogRetrait.add(panneauRetrait, BorderLayout.CENTER);

                dialogRetrait.setSize(370, 110);
                dialogRetrait.setLocationRelativeTo(fenetre);

                dialogRetrait.setVisible(true);
                client.envoyer("RETRAIT " + panneauRetrait.getMontantRetrait());
                break;
            case "TRANSFER":
                PanneauTransfert panneauTransfert = new PanneauTransfert();

                JDialog dialogTransfert = new JDialog(fenetre, "Paiement de la facture", true);
                dialogTransfert.setLayout(new BorderLayout());
                dialogTransfert.add(panneauTransfert, BorderLayout.CENTER);

                dialogTransfert.setSize(480, 140);
                dialogTransfert.setLocationRelativeTo(fenetre);

                dialogTransfert.setVisible(true);
                client.envoyer("TRANSFER " + panneauTransfert.getMontantFacture() + " " + panneauTransfert.getNumeroFacture());
                break;
            case "FACTURE":
                PanneauFacture panneauFacture = new PanneauFacture();

                JDialog dialogFacture = new JDialog(fenetre, "Paiement de la facture", true);
                dialogFacture.setLayout(new BorderLayout());
                dialogFacture.add(panneauFacture, BorderLayout.CENTER);

                dialogFacture.setSize(400, 160);
                dialogFacture.setLocationRelativeTo(fenetre);

                dialogFacture.setVisible(true);
                client.envoyer("FACTURE " + panneauFacture.getMontantFacture() + " " + panneauFacture.getNumeroFacture() + " "+ panneauFacture.getDescriptionFacture());
                break;
            case "HIST":
                client.envoyer("HIST");
                break;
            case "EPARGNE":
                client.envoyer("EPARGNE");
        }
    }
}
