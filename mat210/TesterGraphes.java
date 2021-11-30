package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * @author Xavier Provençal
 */

import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * Cette classe sert à tester une implémentation de la classe Graphe.
 *
 * Une fonction `main` permet d'initialiser un graphe et de tester l'ajout et
 * le retrait d'arcs pondérés.
 */
public class TesterGraphes {

    public static void main(String[] args) {
        args = "--graphe Listes".split(" ");
        //args = "--graphe Matrice".split(" ");
        String nomRepresentation = null;
        Graphe.Representation representation = null;

        // Lecture des arguments
        for (int i=0; i<args.length; ++i){
            if (args[i].equals("--graphe")) {
                nomRepresentation = args[++i];
            } else if (args[i].equals("--help")) {
                usage();
                System.exit(0);
            } else {
                System.out.println("Paramètre invalide : " + args[i]);
                usage();
                System.exit(1);
            }
        }

        if (nomRepresentation == null) {
            System.out.println("ERREUR : argument obligatoire --graphe non spécifié, utilisez --help pour de l'aide.");
            System.exit(1);
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

        int nbTests = 0;
        int nbSommets;
        int nbArcs;
        double ponderationArcsAbsents;
        boolean succes;

        ++nbTests;
        nbSommets = 10;
        nbArcs = 20;
        ponderationArcsAbsents = Double.POSITIVE_INFINITY;
        System.out.println("");
        System.out.println("# Test " + nbTests + ", " + nbSommets + " sommets, " + nbArcs 
                + " arcs, la pondération des arcs absents est " + ponderationArcsAbsents);
        succes = testGraphe(representation, nbSommets, nbArcs, ponderationArcsAbsents);
        if (!succes) {
            System.out.println("Échec d'au moins un test.");
            System.exit(-1);
        }

        ++nbTests;
        nbSommets = 10;
        nbArcs = 20;
        ponderationArcsAbsents = Double.POSITIVE_INFINITY;
        System.out.println("");
        System.out.println("# Test " + nbTests + " : " + nbSommets + " sommets, " + nbArcs 
                + " arcs, la pondération des arcs absents est " + ponderationArcsAbsents);
        succes = testGraphe(representation, nbSommets, nbArcs, ponderationArcsAbsents);
        if (!succes) {
            System.out.println("Échec d'au moins un test.");
            System.exit(-1);
        }

        ++nbTests;
        nbSommets = 20;
        nbArcs = 50;
        ponderationArcsAbsents = 1000.0;
        System.out.println("");
        System.out.println("# Test " + nbTests + " : " + nbSommets + " sommets, " + nbArcs 
                + " arcs, la pondération des arcs absents est " + ponderationArcsAbsents);
        succes = testGraphe(representation, nbSommets, nbArcs, ponderationArcsAbsents);
        if (!succes) {
            System.out.println("Échec d'au moins un test.");
            System.exit(-1);
        }

        ++nbTests;
        nbSommets = 50;
        nbArcs = 500;
        ponderationArcsAbsents = -1;
        System.out.println("");
        System.out.println("# Test " + nbTests + " : " + nbSommets + " sommets, " + nbArcs 
                + " arcs, la pondération des arcs absents est " + ponderationArcsAbsents);
        succes = testGraphe(representation, nbSommets, nbArcs, ponderationArcsAbsents);
        if (!succes) {
            System.out.println("Échec d'au moins un test.");
            System.exit(-1);
        }

        ++nbTests;
        nbSommets = 100;
        nbArcs = 1000;
        ponderationArcsAbsents = Double.NEGATIVE_INFINITY;
        System.out.println("");
        System.out.println("# Test " + nbTests + " : " + nbSommets + " sommets, " + nbArcs 
                + " arcs, la pondération des arcs absents est " + ponderationArcsAbsents);
        succes = testGraphe(representation, nbSommets, nbArcs, ponderationArcsAbsents);
        if (!succes) {
            System.out.println("Échec d'au moins un test.");
            System.exit(-1);
        }

        ++nbTests;
        nbSommets = 200;
        nbArcs = 20000;
        ponderationArcsAbsents = Double.POSITIVE_INFINITY;
        System.out.println("");
        System.out.println("# Test " + nbTests + " : " + nbSommets + " sommets, " + nbArcs 
                + " arcs, la pondération des arcs absents est " + ponderationArcsAbsents);
        succes = testGraphe(representation, nbSommets, nbArcs, ponderationArcsAbsents);
        if (!succes) {
            System.out.println("Échec d'au moins un test.");
            System.exit(-1);
        }

        ++nbTests;
        nbSommets = 20;
        nbArcs = 50;
        ponderationArcsAbsents = 0.0;
        System.out.println("");
        System.out.println("# Test " + nbTests + " : " + nbSommets + " sommets, " + nbArcs 
                + " arcs, la pondération des arcs absents est " + ponderationArcsAbsents);
        succes = testGraphe(representation, nbSommets, nbArcs, ponderationArcsAbsents);
        if (!succes) {
            System.out.println("Échec d'au moins un test.");
            System.exit(-1);
        }
    }

    /**
     * Affiche l'aide pour l'utilisation en ligne de commande
     */
    public static void usage() {
        System.out.println("\nUsage :\n" 
                + "java mat210.TestGraphe --graphe REPRESENTATION\n\n"
                + "     Les représentataions supportés sont :");
        for (Graphe.Representation r : Graphe.Representation.values()) {
            System.out.println("       " + r.nom );
        }
        System.out.println("\n"
                + "  --help\n"
                + "     Affiche cet aide.\n");
    }

    /**
     * Sélectionnne un arc de manière aléatoire parmi l'ensemble d'arcs en paramètre.
     *
     * @param  s un ensemble d'arcs
     * @return un arc choisi aléatoirement
     */
    private static Graphe.Arc choisirArcAleatoirement(Set<Graphe.Arc> s) {
        int k = 0;
        int rand = ThreadLocalRandom.current().nextInt(0, s.size());
        for (Graphe.Arc a : s) {
            if (k == rand) {
                return a;
            }
            ++k;
        }
        return null;
    }

    /**
     * Initialise un graphe et test les différentes fonctionnalités de celui-ci.
     *
     * @param  r la preprésentation du graphe
     * @param  nbSommets le nombre de sommet du graphe
     * @param  nbArcs le nombre d'arcs du graphe
     * @return  true si tous les tests ont été effectués avec succès, false sinon.
     */
    public static boolean testGraphe(Graphe.Representation r, int nbSommets, int nbArcs, double valeurArcsAbsents) {
        Chronometre chrono;
        long duree;
        Set<Graphe.Arc> arcsPresents = new HashSet<Graphe.Arc>();
        Set<Graphe.Arc> arcsAbsents = new HashSet<Graphe.Arc>();

        if (nbArcs > nbSommets*nbSommets) {
            throw new IllegalArgumentException("Le nombre d'arcs ne peut pas" + 
                    " être supérieur au carré du nombre de sommets");
        }
        // Initialisation du graphe
        System.out.print("#   Génération d'un graphe à " + nbSommets + " sommets et " + nbArcs + " arcs... ");
        chrono = new Chronometre();
        Graphe g = null;
        switch (r) {
            case Listes :
                g = new GrapheParListes(nbSommets, valeurArcsAbsents);
                break;
            case Matrice :
                g = new GrapheParMatrice(nbSommets, valeurArcsAbsents);
                break;
            default:
                break;
        }

        for (int i=0; i<nbSommets; ++i){
            for (int j=0; j<nbSommets; ++j){
                arcsAbsents.add( g.new Arc(i, j, valeurArcsAbsents));
            }
        }

        for (int i=0; i<nbArcs; ++i) {
            Graphe.Arc a = choisirArcAleatoirement(arcsAbsents);
            Graphe.Arc b = g.new Arc(a.initial, a.terminal,
                    ThreadLocalRandom.current().nextDouble(1e10));
            arcsAbsents.remove(a);
            arcsPresents.add(b);
            g.ajouterArc(b.initial, b.terminal, b.ponderation);
        }
        duree = chrono.duree();
        System.out.println("terminé en " + Chronometre.formatTemps(duree) + ".");

        System.out.print("#   Test des fonctionnalités de base... ");
        chrono = new Chronometre();
        if (g.getNbSommets() != nbSommets) {
            System.out.println("\n[Erreur] getNbSommets()\n");
            return false;
        }
        if (g.getPonderationArcsAbsents() != valeurArcsAbsents) {
            System.out.println("\n[Erreur] getPonderationArcAbsent()\n");
            return false;
        }

        for (Graphe.Arc a : arcsAbsents) {
            double d = g.getPonderation(a.initial, a.terminal);
            if (d != valeurArcsAbsents) {
                System.out.println("\n[Erreur] getPonderation( " + a.initial + ", " + a.terminal + ")\n");
                return false;
            }
        }
        for (Graphe.Arc a : arcsPresents) {
            double d = g.getPonderation(a.initial, a.terminal);
            if (d != a.ponderation) {
                System.out.println("\n[Erreur] getPonderation( " + a.initial + ", " + a.terminal + ")\n");
                return false;
            }
        }
        duree = chrono.duree();
        System.out.println(" terminé avec succès en " + Chronometre.formatTemps(duree) + ".");

        System.out.print("#   Test des itérateurs (getArcs)... ");
        chrono = new Chronometre();
        for (int i=0; i<nbSommets; ++i) {
            Iterator<Graphe.Arc> it = g.getArcs(i);
            while (it.hasNext()) {
                Graphe.Arc a = it.next();
                if (arcsPresents.contains(a)) {
                    arcsPresents.remove(a);
                } else {
                    System.out.println("\n[Erreur] getArcs(" + i + ") contient des arcs invalides\n");
                    return false;
                }
            }
        }
        if (arcsPresents.size() != 0) {
            System.out.println("\n[Erreur] getArcs, des arcs sont ignorés\n");
            return false;
        }
        duree = chrono.duree();
        System.out.println(" terminé avec succès en " + Chronometre.formatTemps(duree) + ".");
        System.out.println("#   Tous les tests ont été réalisés avec succès.\n");
        return true;
    }

}
