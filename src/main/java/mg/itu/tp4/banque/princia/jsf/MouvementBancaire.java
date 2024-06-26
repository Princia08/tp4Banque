/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package mg.itu.tp4.banque.princia.jsf;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import mg.itu.tp4.banque.princia.entities.CompteBancaire;
import mg.itu.tp4.banque.princia.jsf.utilitaire.Util;
import mg.itu.tp4.banque.princia.services.GestionnaireCompte;

/**
 *
 * @author princ
 */
@Named(value = "mouvementBancaire")
@ViewScoped
public class MouvementBancaire implements Serializable {

    private Long idCompte;
    private CompteBancaire compte;

    @PositiveOrZero
    private int montant;

    @Inject
    private GestionnaireCompte gc;

    /**
     * Creates a new instance of MouvementBancaire
     */
    public MouvementBancaire() {
    }

    public Long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Long idCompte) {
        this.idCompte = idCompte;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public CompteBancaire getCompte() {
        return compte;
    }

    public void setCompte(CompteBancaire compte) {
        this.compte = compte;
    }

    public String retirer() {
        try {
            if (montant > compte.getSolde()) {
                Util.messageErreur("Votre solde est insuffisant", "Votre solde est insuffisant");
                return null;
            } else {
                gc.retirer(compte, montant);
                Util.addFlashInfoMessage("Retrait de " + montant + " EUR du compte de " + compte.getNom() + " correctement effectué.");
                return "listeComptes?faces-redirect=true";
            }
        } catch (OptimisticLockException ex) {
            Util.messageErreur("Le compte de " + compte.getNom()
                    + " a été modifié ou supprimé par un autre utilisateur !");
            return null; // pour rester sur la page s'il y a une exception
        } // end try
    }

    public String verser() {
        try {
            gc.verser(compte, montant);
            Util.addFlashInfoMessage("Versement de " + montant + " EUR du compte de " + compte.getNom() + " correctement effectué.");
            return "listeComptes?faces-redirect=true";
        } catch (OptimisticLockException ex) {
            Util.messageErreur("Le compte de " + compte.getNom()
                    + " a été modifié ou supprimé par un autre utilisateur !");
            return null; // pour rester sur la page s'il y a une exception
        } // end try
    }

    public void chargerCompte() {
        this.compte = gc.getCompte(idCompte);
    }
}
