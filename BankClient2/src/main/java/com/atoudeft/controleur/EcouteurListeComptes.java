package com.atoudeft.controleur;

import com.atoudeft.client.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-11-01
 */
public class EcouteurListeComptes extends MouseAdapter {

    private Client client;
    public EcouteurListeComptes(Client client) {
        this.client = client;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            JList<String> list = (JList<String>) evt.getSource();
            int selectedIndex = list.getSelectedIndex();

            if (selectedIndex != -1) {
                String typeCompte = "";
                if (selectedIndex == 0) {
                    typeCompte = "cheque";
                    client.envoyer("SELECT " + typeCompte);
                }
                else if (selectedIndex == 1) {
                    typeCompte = "epargne";
                    client.envoyer("SELECT " + typeCompte);
                }
            } else {
                JOptionPane.showMessageDialog(list, "Aucun compte sélectionné.");
            }
        }
    }
}
