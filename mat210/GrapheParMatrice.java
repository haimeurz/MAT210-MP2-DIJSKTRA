package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * @author Xavier Provençal
 *
 * Modifications par les étudiant.e.s : 
 *  - TODO inscrivez vos noms ici.
 *  - TODO inscrivez vos noms ici.
 *  - TODO inscrivez vos noms ici.
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implémentation de graphes représentés par des listes d'adjacences.
 */
public class GrapheParMatrice extends Graphe {

    /**
     * Constructeur
     *
     * Initialise un graphe dont le nombre de sommet est `nbSommets` qui ne
     * contient aucun arc.
     * 
     * @param nbSommets le nombre de sommet du graphe
     * @param ponderationArcsAbsents la pondération affectée aux arcs qui ne
     *        font pas partie du graphe
     */
    public GrapheParMatrice(int nbSommets, double ponderationArcsAbsents) {
        // 
        // Exercice 1
        //
    }


    /**
     * {@inheritdoc}
     */
    @Override
    public void ajouterArc(int initial, int terminal, double ponderation) {
        // 
        // Exercice 1
        //
    }


    /**
     * {@inheritdoc}
     */
    @Override
    public Iterator<Arc> getArcs(int sommet) {
        // 
        // Exercice 1
        //
        return null;
    }


    /**
     * {@inheritdoc}
     */
    @Override
    public double getPonderation(int initial, int terminal) {
        // 
        // Exercice 1
        //
        return 0.0;
    }


    /**
     * Produit une représentation textuelle du graphe où toutes les entrées de
     * la matrice d'adjacence sont affichés.
     *
     * À n'utiliser que pour de petits graphes.
     *
     * @return représentation textuelle du graphe.
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.0");
        int n = nbSommets;
        String[][] asString = new String[n][n];
        int[] maxColonne = new int[n];
        for (int i=0; i<n; ++i) {
            maxColonne[i] = 0;
        }
        for (int i=0; i<n; ++i) {
            for (int j=0; j<n; ++j) {
                asString[i][j] = df.format(m[i][j]);
                maxColonne[j] = Math.max(maxColonne[j], asString[i][j].length());
            }
        }
        for (int i=0; i<n; ++i) {
            for (int j=0; j<n; ++j) {
                int lngActuelle = asString[i][j].length();
                int lngCible = maxColonne[j];
                String pad = new String();
                for (int k=lngActuelle; k<lngCible; ++k) {
                    pad = pad + " ";
                }
                asString[i][j] = pad + asString[i][j];
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i=0; i<n; ++i) {
            sb.append("[");
            sb.append(asString[i][0]);
            for (int j=1; j<n; ++j) {
                sb.append(" ");
                sb.append(asString[i][j]);
            }
            sb.append("]\n");
        }
        return sb.toString();
    }



    // Pour une implémentation par matrice d'adjacence, on utilise une matrice
    // dont les cases sont les pondérations des arêtes. 
    //
    // ``m[i][j]`` est la pondération de l'arête dont le sommet initial est
    // ``i`` et le sommet terminal est ``j``.
    //
    double[][] m;

}
