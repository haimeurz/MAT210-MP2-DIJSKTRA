package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * @author Xavier Provençal
 */

import java.util.ArrayList;

/**
 * Cette classe charge une carte et y calcule un chemin de coût minimum entre
 * deux points sur cette carte.
 *
 * Le coût de ce chemin minimum est affiché ainsi que le temps de calcul.
 *
 * La carte est ensuite dessinée et sauvegardée dans un fichier au format PNG.
 * Sur ce dessin, le chemin minimum calculé est tracé en rouge.
 */
public class TracerChemin {

    public static void main(String[] args) {

        //args = "--carte ./cartes/3x2.png --algo DijkstraParArrayList --graphe Matrice".split(" ");
        //args = "--carte G:\\Other computers\\My Computer\\AUT2021 ETS\\MAT210\\MAT210-MP2-Dijsktra\\cartes\\40x25.png --algo DijkstraParArrayList --graphe Listes".split(" ");
        //args = "--carte ./cartes/40x25.png --graphe Listes --algo DijkstraParArrayList --depart 3 5 --arrivee 24 39".split(" ");
        //args = "--carte ./cartes/40x25.png --graphe Listes --algo DijkstraParTreeSet --depart 3 5 --arrivee 24 39".split(" ");
        args = "--carte ./cartes/40x25.png --graphe Listes --algo DijkstraParBooleanArray --depart 3 5 --arrivee 24 39".split(" ");
        Carte carte = null;
        String fichierCarte = null;
        String fichierSortie = null;
        Carte.Coordonnee depart = null;
        Carte.Coordonnee arrivee = null;
        String nomRepresentation = null;
        Graphe.Representation representation = null;
        String nomAlgorithme = null;
        Carte.Algorithme algorithme = null;
        String nomAffichage = null;
        Carte.Affichage affichage = null;
        Chronometre chrono;
        long duree;

        // Lecture des arguments
        for (int i=0; i<args.length; ++i){
            if (args[i].equals("--carte")) {
                fichierCarte = args[++i];
            } else if (args[i].equals("--depart")) {
                int ligne = Integer.parseInt(args[++i]);
                int colonne = Integer.parseInt(args[++i]);
                depart = new Carte.Coordonnee(ligne, colonne);
            } else if (args[i].equals("--arrivee")) {
                int ligne = Integer.parseInt(args[++i]);
                int colonne = Integer.parseInt(args[++i]);
                arrivee = new Carte.Coordonnee(ligne, colonne);
            } else if (args[i].equals("--graphe")) {
                nomRepresentation = args[++i];
            } else if (args[i].equals("--algo")) {
                nomAlgorithme = args[++i];
            } else if (args[i].equals("--sortie")) {
                fichierSortie = args[++i];
            } else if (args[i].equals("--affichage")) {
                nomAffichage = args[++i];
            } else if (args[i].equals("--help")) {
                usage();
                System.exit(0);
            } else {
                System.out.println("Paramètre invalide : " + args[i]);
                usage();
                System.exit(1);
            }
        }


        // Validation des arguments obligatoires
        if (fichierCarte == null) {
            System.err.println("ERREUR : argument obligatoire --carte non spécifié, utilisez --help pour de l'aide.");
            System.exit(1);
        }
        if (nomAlgorithme == null) {
            System.err.println("ERREUR : argument obligatoire --algo non spécifié, utilisez --help pour de l'aide.");
            System.exit(1);
        }
        if (nomRepresentation == null) {
            System.err.println("ERREUR : argument obligatoire --graphe non spécifié, utilisez --help pour de l'aide.");
            System.exit(1);
        }


        // Chargement de la carte
        carte = new Carte(fichierCarte);

        // Affectation des valeurs par défaut
        if (depart == null) {
            depart = new Carte.Coordonnee(0, 0);
        }
        if (arrivee == null) {
            arrivee = new Carte.Coordonnee(carte.getNbLignes()-1, carte.getNbColonnes()-1);
        }
        if (fichierSortie == null) {
           fichierSortie = "out.png"; // valeur par defaut
        }
        if (nomAffichage == null) {
            nomAffichage = "pixel";
        }

        // Choix de la méthode de représentation du graphe.
        for (Graphe.Representation r : Graphe.Representation.values()) {
            if (nomRepresentation.equals(r.nom)) {
                representation = r;
            }
        }
        if (representation == null) {
            throw new IllegalArgumentException("Représentation '" + nomRepresentation + 
                    "' invalide, utilisez --help pour de l'aide.");
        }

        // Choix de l'algorithme de recherche de chemins
        for (Carte.Algorithme a : Carte.Algorithme.values()) {
            if (nomAlgorithme.equals(a.nom)) {
                algorithme = a;
            }
        }
        if (algorithme == null) {
            throw new IllegalArgumentException("Algorithme '" + nomAlgorithme + 
                    "' invalide, utilisez --help pour de l'aide.");
        }

        // Choix du type d'affichage
        for (Carte.Affichage a : Carte.Affichage.values()) {
            if (nomAffichage.equals(a.nom)) {
                affichage = a;
            }
        }
        if (affichage == null) {
            throw new IllegalArgumentException("Affichage '" + nomAffichage + 
                    "' invalide, utilisez --help pour de l'aide.");
        }


        System.out.println("#######################################");
        System.out.println("# Carte           : " + fichierCarte);
        System.out.println("# Nombre de cases : " + carte.getNbLignes() * carte.getNbColonnes());
        System.out.println("# Représentation  : " + representation.nom);
        System.out.println("# Algorithme      : " + algorithme.nom);
        System.out.println("# Départ          : " + depart);
        System.out.println("# Arrivée         : " + arrivee);
        System.out.println("# Image en sortie : " + fichierSortie);
        System.out.println("#######################################");

        ArrayList<Carte.Coordonnee> chemin = carte.cheminMin(depart, arrivee, representation, algorithme);
        double cout = carte.coutChemin(chemin.iterator());
        System.out.println("# Cout total du chemin : " + cout);

        System.out.print("# Écriture du résultat dans le fichier ``" + fichierSortie + "``... ");
        chrono = new Chronometre();
        carte.tracerCheminEtSauvegarder(chemin, fichierSortie, affichage);
        duree = chrono.duree();
        System.out.println("terminée en " + Chronometre.formatTemps(duree) + ".");
        System.out.println("#######################################");
    }

    /**
     * Affiche l'aide pour l'utilisation en ligne de commande
     */
    public static void usage() {
        System.out.println("\nUsages :\n" 
                + "java Carte --carte <fichier> --algo <A> --graphe <R> [OPTION]\n\n"
                + "\n"
                + "Paramètre obligatoire :\n\n"
                + "  --carte fichier\n"
                + "     Spécifie une image au format png décrivant une carte.\n\n"
                + "  --graphe representation\n"
                + "     Spécifie le mode de représentation du graphe. Les valeurs\n"
                + "     supportées sont :");
        for (Graphe.Representation r : Graphe.Representation.values()) {
            System.out.println("       " + r.nom);
        }
        System.out.println("\n"
                + "  --algo algorithme\n"
                + "     Spécifie la version de l'algorithme de Dijkstra utilisée\n"
                + "     pour le calcul du chemin minimum. Les valeurs supportées sont :" );
        for (Carte.Algorithme a : Carte.Algorithme.values()) {
            System.out.println("       " + a.nom);
        }
        System.out.println("\n"
                + "Paramètres optionnels :\n\n"
                + "  --depart y x\n"
                + "     Fixe le point de départ en ligne y et colonne x (défaut : coin supérieur gauche).\n\n"
                + "  --arrivee y x\n"
                + "     Fixe le point d'arrivée en ligne y et colonne x (défaut : coin inférieur droit).\n\n"
                + "  --sortie fichier\n"
                + "     Fichier png où la carte et le chemin sont inscrits en sortie (défaut : out.png).\n\n"
                + "  --affichage affichage\n"
                + "     Spécifie la type d'affichage pour le chemin minimal calculé (défaut : Pixel).\n"
                + "     Les valeurs supportées sont :" );
        for (Carte.Affichage a : Carte.Affichage.values()) {
            System.out.println("       " + a.nom);
        }
        System.out.println("\n"
                + "  --help\n"
                + "     Affiche cet aide.\n");
    }


}
