package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session hiver 2021, à l'ÉTS.
 *
 * @author Xavier Provençal
 *
 * Modifications par les étudiant.e.s : 
 *  - TODO inscrivez vos noms ici.
 *  - TODO inscrivez vos noms ici.
 *  - TODO inscrivez vos noms ici.
 */

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.IllegalArgumentException;
import java.io.IOException;
import java.io.File;


/**
 * Représente une carte sur laquelle on calcule des chemins de longueur
 * minimums.
 * 
 * Une carte est formée par une grille rectangulaire donc chacune des cases
 * possède un type de terrain. Un coût de déplacement est associé à chaque type
 * de terrain.
 *
 * @author Xavier Provençal
 */
public class Carte {

    // 
    // Classes internes : 
    //  - HeuristiquePourAStar
    //  - Couleur
    //  - Case
    //  - Coordonnee
    //  - Dessinateur
    //  - DessinateurModePixel
    //  - DessinateurModeGraphique
    //
    //  Note : à l'exeption de HeuristiquePourAStar, toutes ces classes sont
    //  implémentées à la fin de ce fichier.
    //

    //
    // Variables de classe
    //

    public static final double COUT_DEPLACEMENT_EAU      = 1e6;
    public static final double COUT_DEPLACEMENT_PLAINE   = 1.0;
    public static final double COUT_DEPLACEMENT_COLLINE  = 2.0;
    public static final double COUT_DEPLACEMENT_MONTAGNE = 5.0;

    // 
    // Variables d'instance
    //

    private Case[][] cases; // les cases de carte
    private int nbLignes, nbColonnes; // les dimensions de la carte



    /**
     * Une heuristique produit estimation plutôt qu'une valeur exacte. 
     *
     * L'algorithme A-star utilise une telle heuristique qui, étant donné deux
     * cases d'une carte, produit un estimation INFÉRIEURE OU ÉGALE au coût
     * d'un chemin minimum entre les deux.
     */
    public class HeuristiquePourAStar {

        /**
         * Constructeur par vide, ne fait rien.
         */
        public HeuristiquePourAStar() {}

        /**
         * Produit une sous-estimation du coût d'un chemin allant du la case A
         * à la case B.
         *
         * Plus précisément, le nombre retournée est inférieur ou égal au coût
         * d'un chemin de A à B.
         *
         * Complexité : O(1)
         *
         * @param  A case de départ
         * @param  B case de départ
         * @return une borne inférieur au coût d'un chemin de A à B.
         */
        double coutMinimum(Coordonnee A, Coordonnee B) {
            //
            // BONUS
            //
            return 0.0;
        }


        /**
         * Produit une sous-estimation du coût d'un chemin allant du la i-eme case à 
         * à la j-ième case de la carte. 
         *
         * @param  i case de départ
         * @param  j case de départ
         * @return une borne inférieur au coût d'un chemin de A à B.
         */
        double coutMinimum(int i, int j) {
            return coutMinimum(intToCoordonnee(i), intToCoordonnee(j));
        }
    }


    // 
    // Constructeur
    //


    /**
     * Constructeur à partir d'une image.
     *
     * Chaque pixel de l'image définit une des cases de la carte.
     * La couleur du pixel indique le type de terrain de la case.
     *
     * @param  fichier nom du fichier contenant l'image
     */
    public Carte(String fichier) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fichier));
            nbLignes = img.getHeight();
            nbColonnes = img.getWidth();

            cases = new Case[nbLignes][nbColonnes];
            for (int y = 0; y < nbLignes; ++y) {
                for (int x = 0; x < nbColonnes; ++x) {
                    Couleur couleur = new Couleur(img.getRGB(x, y)); 
                    cases[y][x] = new Case(couleur);
                }
            }
        } catch (IOException e) {
            System.err.println("ERREUR fichier carte ``" + fichier + "`` invalide.");
            System.exit(1);
        }
    }


    // 
    // Méthodes
    //


    /**
     * @return  le nombre de lignes de la grille formée par la carte
     */
    public int getNbLignes() {
        return nbLignes;
    }

    /**
     * @return  le nombre de colonnes de la grille formée par la carte
     */
    public int getNbColonnes() {
        return nbColonnes;
    }


    /**
     * Effectue la conversion Coordonnee vers entier
     *
     * Les cases de la carte sont numérotées de 0 à (nombre de cases - 1)
     * Cette fonction effectue la conversion dans un sens alors que la fonction
     * intToCoordonne effectue la conversion dans l'autre sens.
     *
     * @param  c coordonnée de la case
     * @return l'indice de la case de 0 à nbCases-1
    */
    public int coordonneeToInt(Coordonnee c) {
        return c.ligne*nbColonnes + c.colonne;
    }

    /**
     * Effectue la conversion Entier vers Coordonnee
     *
     * Les cases de la carte sont numérotées de 0 à (nombre de cases - 1)
     * Cette fonction effectue la conversion dans un sens alors que la fonction
     * coordonneeToInt effectue la conversion dans l'autre sens.
     *
     * @param  x l'indice de la case, de 0 à nbCases-1
     * @return les coordonnées de la case
    */
    public Coordonnee intToCoordonnee(int x) {
        Coordonnee c = null;
        if (x >= 0 && x < nbLignes*nbColonnes) {
            int ligne = x / nbColonnes;
            int colonne = x % nbColonnes;
            c = new Coordonnee(ligne, colonne);
        }
        return c;
    }

    /**
     * Teste si la coordonnée désigne un case valide de la carte.
     *
     * @param  c des coordonnées
     * @return vrai si ces corrdonnées correspondent à une case de la case, faux sinon
     */
    public boolean estValide(Coordonnee c) {
        return  0 <= c.ligne   && c.ligne   < nbLignes
            &&  0 <= c.colonne && c.colonne < nbColonnes;
    }

    /**
     * Retourne un itérateur sur les cases voisines d'une case.
     *
     * Pour une case située dans un coin, l'itérateur prend 3 valeurs.
     * Pour une case située le long d'un bord, l'itérateur prend 5 valeurs.
     * Pour une case qui n'est pas au bord, l'itérateur prend 8 valeurs.
     *
     * @param  c les coordonnes d'une cases
     * @return un itérateur sur les coordonnées des cases voisines
     */
    private Iterator<Coordonnee> voisins(Coordonnee c) {
        ArrayList<Coordonnee> v = new ArrayList<Coordonnee>();
        Coordonnee[] t = new Coordonnee[8];
        t[0] = new Coordonnee(c.ligne,   c.colonne-1); // case à l'ouest
        t[1] = new Coordonnee(c.ligne,   c.colonne+1); // case à l'est
        t[2] = new Coordonnee(c.ligne-1, c.colonne);   // case au nord
        t[3] = new Coordonnee(c.ligne+1, c.colonne);   // case au sud
        t[4] = new Coordonnee(c.ligne-1, c.colonne-1); // case au nord-ouest
        t[5] = new Coordonnee(c.ligne-1, c.colonne+1); // case au nord-est
        t[6] = new Coordonnee(c.ligne+1, c.colonne-1); // case au sud-ouest
        t[7] = new Coordonnee(c.ligne+1, c.colonne+1); // case au sud-est
        for (int i=0; i<8; ++i) {
            if (estValide(t[i])) {
                v.add(t[i]);
            }
        }
        return v.iterator();
    }

    /**
     * Calcul de la distance euclidienne entre les centres de deux cases de la
     * carte.
     *
     * Utilise le théorème de Pythagore.
     *
     * @param  a coordonnées de la première case
     * @param  b coordonnées de la deuxième case
     * @return la distance euclidienne d'une case à l'autre
     */
    public static double distanceEuclidienne(Coordonnee a, Coordonnee b) {
        double dy = a.ligne - b.ligne;
        double dx = a.colonne - b.colonne;
        return Math.sqrt(dx*dx + dy*dy);
    }

    /**
     * Calcul du coût de déplacement entre deux cases adjacentes.
     *
     * Le coût de déplacement est déterminé par le type de terrain sur la case
     * destination.
     *
     * De plus, si les cases sont adjacentes par un coin alors un facteur
     * sqrt(2) multiplie le coût de déplacement de manière à reproduire une
     * norme euclidienne.
     *
     * Si les cases ne sont pas adjacentes, la valeur retournée est l'infini
     * positif.
     *
     * @param  a coordonnées de la case de départ
     * @param  b coordonnées de la case destination
     * @return  le coût d'un déplacement d'une case à l'autre.
     */
    public double coutDeplacement(Coordonnee a, Coordonnee b) {
        double d = distanceEuclidienne(a, b);
        double cout;
        double epsilon = 1e-10;
        if (d > Math.sqrt(2.0)+epsilon) {
            cout = Double.POSITIVE_INFINITY;
        } else {
            cout = cases[b.ligne][b.colonne].getCout() * d;
        }
        return cout;
    }

    /**
     * Retourne la somme des coûts de déplacement le long d'un chemin.
     *
     * @param  it itérateur sur les coordonnées des cases empruntées le
     *         long du chemin.  
     * @return le coût total du déplacement le long du chemin
     */
    public double coutChemin(Iterator<Coordonnee> it) {
        if (!it.hasNext()) {
            return 0.0;
        }
        double cout = 0.0;
        Coordonnee A,B;
        A = it.next();
        while (it.hasNext()) {
            B = it.next();
            cout += coutDeplacement(A, B);
            A = B;
        }
        return cout;
    }

    /**
     * Construit un graphe orienté et pondéré correspondant à la carte.
     *
     * Chaque case correspond à un sommet. 
     * Deux cases adjacentes sont reliées par des arcs.
     * La pondération des arcs correspond au coût de déplacement d'une case à
     * l'autre.
     *
     * @param  representation le mode de représentation du graphe
     * @return la représentation de la carte sous la forme d'un graphe
     */
    public Graphe convertirEnGraphe(Graphe.Representation representation) {
        int nbSommets = nbLignes*nbColonnes;
        Graphe g = null;

        // Création du graphe
        switch (representation) {
            case Listes:
                g = new GrapheParListes(nbSommets, Double.POSITIVE_INFINITY);
                break;
            case Matrice:
                g = new GrapheParMatrice(nbSommets, Double.POSITIVE_INFINITY);
                break;
            default:
                break;
        }

        // Ajout des arêtes pondérées en fonction du terrain
        Coordonnee c = new Coordonnee(0,0);
        for (c.ligne=0; c.ligne < nbLignes; ++c.ligne) {
            for (c.colonne=0; c.colonne < nbColonnes; ++c.colonne) {
                Iterator<Coordonnee> it = voisins(c);
                while (it.hasNext()) {
                    Coordonnee v = it.next();
                    if (v != null) {
                        int depart = coordonneeToInt(c);
                        int arrivee = coordonneeToInt(v);
                        double cout = coutDeplacement(c, v);
                        g.ajouterArc(depart, arrivee, cout);
                    }
                }
            }
        }
        return g;
    }

    /**
     * Le type énumératif Algorithme permet de choisir un algorithme pour
     * effectuer la recherche d'un chemin de coût minimum entre deux cases de
     * la carte.
     */
    public static enum Algorithme {
        DijkstraParArrayList("DijkstraParArrayList"), 
        DijkstraParTreeSet("DijkstraParTreeSet"),
        DijkstraParBooleanArray("DijkstraParBooleanArray"),
        Astar("Astar");

        /**
         * @param  nom le nom de l'algorithme
         */
        Algorithme(String nom) {
            this.nom = nom;
        }
        String nom;
    }

    /**
     * Calcul d'un chemin de coût minimum entre deux cases de la carte.
     *
     * La carte est d'abord convertie en un graphe orienté et pondéré. La
     * recherche du chemin minimum est effectuée dans ce graphe.
     *
     * @param  A coordonnes du départ
     * @param  B coordonnes de l'arrivée
     * @param  representation type de représentation pour le graphe
     * @param  algo l'algorithme de recherche du chemin minimum utilisé
     * @return un chemin de coût minimum de A à B sous la forme d'un tableau de
     *         coordonnées
     */
    public ArrayList<Coordonnee> cheminMin(Coordonnee A, Coordonnee B,
            Graphe.Representation representation, Algorithme algo) {

        Chronometre chrono = null;
        long duree = 0L;
        System.out.print("# Construction du graphe... ");
        chrono = new Chronometre();
        Graphe graphe = convertirEnGraphe(representation);
        duree = chrono.duree();
        System.out.println("terminée en " + Chronometre.formatTemps(duree) + ".");

        int depart = coordonneeToInt(A);
        int destination = coordonneeToInt(B);
        ArrayList<Integer> cheminDansGraphe = null;

        System.out.print("# Calcul du chemin minimum de " + A + " à " + B + "... ");
        chrono = new Chronometre();
        switch (algo) {
            case DijkstraParArrayList:
                cheminDansGraphe = graphe.DijkstraParArrayList(depart, destination);
                break;
            case DijkstraParTreeSet:
                cheminDansGraphe = graphe.DijkstraParTreeSet(depart, destination);
                break;
            case DijkstraParBooleanArray:
                cheminDansGraphe = graphe.DijkstraParBooleanArray(depart, destination);
                break;
            case Astar:
                cheminDansGraphe = graphe.Astar(depart, destination, new HeuristiquePourAStar());
                break;
            default:
                break;
        }
        duree = chrono.duree();
        System.out.println("terminé en " + Chronometre.formatTemps(duree) + ".");

        // On a calculé un chemin définit sur les sommets du graphe. Ces
        // sommets sont identifiés par des entiers qui correspondent aux
        // indices des cases de la carte.
        //
        // Il faut convertir ces indices en coordonnées pour obtenir un chemin
        // sur la carte.
        ArrayList<Coordonnee> cheminDansCarte = new ArrayList<Coordonnee>();
        for (Integer i : cheminDansGraphe) {
            cheminDansCarte.add(intToCoordonnee(i));
        }
        return cheminDansCarte;
    }


    /**
     * Le type énumératif Affichage décrit les différentes manières de générer
     * une image à partir d'une carte.
     *
     * Mode `Pixel` : 
     *
     * Chaque case de la carte est représentée par un pixel. La couleur de ce
     * pixel est la couleur associée au type de terrain de cette case. On
     * retrouve ainsi une image similaire à celle utilisée pour définir la
     * carte. Ensuite, les pixels correspondant aux cases empruntées par le
     * chemin sont affichées en rouge.
     *
     *
     * Mode `Graphique` :
     *
     * Chaque case est représentée par une tuile illustrant ce type de terrain.
     * L'image obtenue est significativement plus grande que celle utilisée
     * pour définir la carte. Ensuite, le chemin est tracé en rouge sur ces
     * tuiles.
     */
    public static enum Affichage {
        Pixel("pixel"), 
        Graphique("graphique");

        Affichage(String n) {
            nom = n;
        }
        String nom;
    }


    /**
     * Génère une image à partir de la carte et trace le chemin spécifié 
     * en rouge sur cette image.
     *
     * L'image est sauvegardée dans un fichier au format PNG.
     *
     * @param  chemin le chemin à tracer sur la carte
     * @param  fichier nom du fichier dans lequel l'image est sauvegardée
     * @param  affichage le type d'affichage : Pixel ou Graphique.
     */
    public void tracerCheminEtSauvegarder(ArrayList<Coordonnee> chemin, 
            String fichier, Affichage affichage) {
        Dessinateur dessinateur = null;
        switch (affichage) {
            case Pixel:
                dessinateur = new DessinateurModePixel();
                break;
            case Graphique:
                dessinateur = new DessinateurModeGraphique();
                break;
            default:
                break;
        }
        dessinateur.tracerChemin(chemin);
        dessinateur.sauvegarder(fichier);
    }


    /**
     * La classe abstraite Dessinateur représente un outil permettant de
     * dessiner une carte dans une image.
     */
    private abstract class Dessinateur {

        // 
        // Variable d'instance
        //

        // Image dans laquelle la carte est dessinée
        protected BufferedImage dessin;

        // 
        // Méthodes
        //

        /**
         * Trace le chemin spécifié en rouge sur la carte.
         *
         * @param  chemin le chemin à tracer sur la carte
         */
        public abstract void tracerChemin(ArrayList<Coordonnee> chemin);

        /**
         * Sauvegarde l'image dans le fichier spécifiée au format PNG.
         *
         * @param  fichier le nom du fichier
         */
        public void sauvegarder(String fichier) {
            try {
                File sortie = new File(fichier);
                ImageIO.write(dessin, "png", sortie);
            } 
            catch (IOException e) {
                System.err.println("ERREUR, incapable de sauvegarder la carte dans le fichier `" + fichier + "`.");
                System.exit(1);
            }
        }

    }

    /**
     * Implémente un Dessinateur pour le mode d'affichage "Pixel"
     */
    private class DessinateurModePixel extends Dessinateur {

        /**
         * Constructeur à partir d'une carte.
         *
         * @param  carte la carte à dessiner.
         */
        public DessinateurModePixel() {
            dessin = new BufferedImage(nbColonnes, nbLignes, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < nbLignes; ++y) {
                for (int x = 0; x < nbColonnes; ++x) {
                    Couleur couleur = cases[y][x].getCouleur();
                    dessin.setRGB(x, y, couleur.getRGB());
                }
            }
        }

        /**
         * {@inheritdoc}
         */
        @Override
        public void tracerChemin(ArrayList<Coordonnee> chemin) {
            for (Coordonnee c : chemin) {
                dessin.setRGB(c.colonne, c.ligne, Couleur.Rouge.getRGB());
            }
        }

    }

    /**
     * Implémente un Dessinateur pour le mode d'affichage "Graphique"
     *
     * Les tuiles utilisées sont définies à partir d'images contenues dans des
     * fichiers.
     *
     * Le chemin vers ces fichiers est donnée par la variable `cheminVersImages`.
     */
    private class DessinateurModeGraphique extends Dessinateur {

        // 
        // Variables de classe
        //

        // chemin et noms des fichiers des images utilisées comme tuiles
        private static final String cheminVersImages = "./tuiles/";
        private static final String fichierPlaine    = cheminVersImages + "plaine.png";
        private static final String fichierColline   = cheminVersImages + "colline.png";
        private static final String fichierMontagne  = cheminVersImages + "montagne.png";
        private static final String fichierEau       = cheminVersImages + "eau.png";


        // 
        // Variables d'instance
        //

        // Les images des tuiles pour chacun des types de terrains
        private BufferedImage casePlaine;
        private BufferedImage caseColline;
        private BufferedImage caseMontagne;
        private BufferedImage caseEau;

        // les dimensions des tuiles
        private int largeurDesCases;
        private int hauteurDesCases;

        // la largeur du trait utilisée pour illustré le chemin sur la carte
        private final int LARGEURDUTRAIT = 4;


        // 
        // Constructeur
        //


        /**
         * Constructeur à partir d'une carte
         * 
         * @param  carte la carte à dessiner
         */
        public DessinateurModeGraphique() {

            try {
                casePlaine   = ImageIO.read(new File(fichierPlaine));
                caseColline  = ImageIO.read(new File(fichierColline));
                caseMontagne = ImageIO.read(new File(fichierMontagne));
                caseEau      = ImageIO.read(new File(fichierEau));

                // Les quatre images sont supposées avoir la même taille Par
                // précaution, on prend le max sur toutes les images
                largeurDesCases = casePlaine.getWidth();
                largeurDesCases = Math.max(largeurDesCases, caseColline.getWidth()); 
                largeurDesCases = Math.max(largeurDesCases, caseMontagne.getWidth()); 
                largeurDesCases = Math.max(largeurDesCases, caseEau.getWidth()); 
                hauteurDesCases = casePlaine.getHeight();
                hauteurDesCases = Math.max(hauteurDesCases, caseColline.getHeight()); 
                hauteurDesCases = Math.max(hauteurDesCases, caseMontagne.getHeight()); 
                hauteurDesCases = Math.max(hauteurDesCases, caseEau.getHeight()); 

                dessin = new BufferedImage(
                        nbColonnes * largeurDesCases, 
                        nbLignes   * hauteurDesCases,
                        BufferedImage.TYPE_INT_RGB);

                for (int i=0; i<nbLignes; ++i) {
                    for (int j=0; j<nbColonnes; ++j) {
                        Terrain t = cases[i][j].terrain;
                        switch (t) {
                            case Plaine :
                                dessineCase(i, j, casePlaine);
                                break;
                            case Colline :
                                dessineCase(i, j, caseColline);
                                break;
                            case Montagne :
                                dessineCase(i, j, caseMontagne);
                                break;
                            case Eau :
                                dessineCase(i, j, caseEau);
                                break;
                            default:
                                throw new IllegalArgumentException("Type de terrain inconnu.");
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("ERREUR incapable de lire les tuiles pour l'affichage graphique");
                System.exit(1);
            }
        }


        //
        // Méthodes
        //


        /**
         * Dessine une case de la carte à l'endroit correspondant dans l'image.
         *
         * @param  ligneCarte numéro de la ligne de la carte où se situe la case
         * @param  colonneCarte numéro de la colonne de la carte où se situe la case
         * @param  imageCase image de la tuile qui illustre le type de terrain de cette case
         */
        private void dessineCase(int ligneCarte, int colonneCarte, BufferedImage imageCase) {
            int ligneImage = ligneCarte * hauteurDesCases;
            int colonneImage = colonneCarte * largeurDesCases;
            for (int i=0; i<hauteurDesCases; ++i) {
                for (int j=0; j<largeurDesCases; ++j) {
                    dessin.setRGB(colonneImage+j, ligneImage+i, imageCase.getRGB(j, i));
                }
            }
        }

        /**
         * Calcule le numéro de la ligne de l'image correspondant au centre de
         * la case spécifiée.
         *
         * @param  c coordonnées de la case sur la carte
         * @return le numéro de la ligne du centre de cette case dans l'image
         */
        private int numLigneDuCentre(Coordonnee c) {
            return hauteurDesCases*c.ligne + (hauteurDesCases / 2);
        }

        /**
         * Calcule le numéro de la colonne de l'image correspondant au centre
         * de la case spécifiée.
         *
         * @param  c coordonnées de la case sur la carte
         * @return le numéro de la colonne du centre de cette case dans l'image
         */
        private int numColonneDuCentre(Coordonnee c) {
            return largeurDesCases*c.colonne + (largeurDesCases / 2);
        }

        /**
         * {@inheritdoc}
         */
        @Override
        public void tracerChemin(ArrayList<Coordonnee> chemin) {
            for (int i=0; i<chemin.size()-1; i++) {
                Coordonnee A = chemin.get(i);
                Coordonnee B = chemin.get(i+1);
                int ligneDepart    = numLigneDuCentre(A);
                int colonneDepart  = numColonneDuCentre(A);
                int ligneArrivee   = numLigneDuCentre(B);
                int colonneArrivee = numColonneDuCentre(B);
                int nbPas = Math.max(Math.abs(ligneDepart-ligneArrivee),
                        Math.abs(colonneDepart-colonneArrivee));
                for (int j=0; j<=nbPas; ++j) {
                    // version simplifié et moins efficace de l'algo. de Bresenham
                    int ligne   = ((nbPas-j)*ligneDepart   + j*ligneArrivee)   / nbPas; 
                    int colonne = ((nbPas-j)*colonneDepart + j*colonneArrivee) / nbPas;
                    // Pour faire un trait large, on trace une croix sur chacun
                    // de points du chemin
                    for (int k=-(LARGEURDUTRAIT-1); k<LARGEURDUTRAIT; ++k) {
                        dessin.setRGB(colonne+k, ligne, Couleur.Rouge.getRGB());
                        dessin.setRGB(colonne, ligne+k, Couleur.Rouge.getRGB());
                    }
                }
            }
        }

    }

    /**
     * Cette classe implémente une couleur au format RGB.
     */
    public static class Couleur {
        // 
        // Variables de classe
        //

        /**
         * Couleur prédéfinies
         */
        public static final Couleur Bleu = new Couleur(0,0,255);
        public static final Couleur Vert = new Couleur(0,255,0);
        public static final Couleur Brun = new Couleur(165,42,42);
        public static final Couleur Noir = new Couleur(0,0,0);
        public static final Couleur Rouge = new Couleur(255,0,0);

        // 
        // Variables d'instance
        //

        /**
         * Valeur des trois couleur primaires : rouge, vert, bleu, encodée sur 24 bits.
         */
        private int rgb;

        // 
        // Constructeurs
        //

        /**
         * Constructeur à partir de trois entiers.
         *
         * @param  r valeur du rouge (0 à 255).
         * @param  g valeur du vert (0 à 255).
         * @param  b valeur du bleu (0 à 255).
         */
        public Couleur(int r, int g, int b) {
            rgb = ((0xff & r) << 16) | ((0xff & g) << 8) | (0xff & b);
    }
   
        /**
         * Constructeur à partir d'un seul entier RGB correspondant à l'encodage
         * BufferedImage.TYPE_INT_RGB.
         *
         * Les 8 bits les moins significatifs contiennent la valeur du bleu (0 à 255)
         * Les 8 bits suivants contiennent la valeur du vert (0 à 255)
         * Les 8 bits suivants contiennent la valeur du rouge (0 à 255)
         *
         * @param  rgb  la couleur encodée sur 24 bits
         */
        public Couleur(int rgb) {
            this.rgb = rgb & 0x00ffffff; // on force les 8 premiers bits à zéro
        }


        // 
        // Méthodes
        //


        /**
         * Constructeur de copie
         *
         * @param  autre couleur copiée
         */
        public boolean equals(Couleur autre) {
            return rgb == autre.rgb;
        }

        /**
         * Retourne la couleur sous la forme d'un seul entier, conforme à
         * l'encodate BufferedImage.TYPE_INT_RGB 
         *
         * @return  la couleur au format RGB codée sur 24 bits
         */
        int getRGB() {
            return rgb;
        }

        public String toString() {
            int r = (rgb & 0x00ff0000) >> 16;
            int g = (rgb & 0x0000ff00) >> 8;
            int b = (rgb & 0x000000ff);
            return "Couleur(" + r + ", " + g + ", " + b + ")";
        }

    }


    /**
     * Le type énumératif Terrain représente chacun de types de terrains qu'on
     * peut trouver sur une case de la carte.
     *
     * À chaque type de terrain correspond une couleur et un coût de
     * déplacement.
     */
    public static enum Terrain {
        Eau(Couleur.Bleu, COUT_DEPLACEMENT_EAU),
        Plaine(Couleur.Vert, COUT_DEPLACEMENT_PLAINE),
        Colline(Couleur.Brun, COUT_DEPLACEMENT_COLLINE),
        Montagne(Couleur.Noir, COUT_DEPLACEMENT_MONTAGNE);

        /**
         * @param  couleur couleur de la case
         * @param  coutDeplacement coût du déplacement
         */
        Terrain(Couleur couleur, double coutDeplacement) {
            this.couleur = couleur;
            this.coutDeplacement = coutDeplacement;
        }

        Couleur couleur;
        double coutDeplacement;
    }


    /**
     * La classe Case représente un case sur une carte. 
     *
     * Un case est initialisée à partir d'une couleur. Cette couleur doit
     * obligatoirement correspondre à une des types de terrain défini par le
     * type énumératif Terrain.
     */
    public static class Case {

        // 
        // Variable d'instance
        //

        Terrain terrain;

        // 
        // Constructeur
        //

        /**
         * Constructeur à partir d'une couleur.
         *
         * @param  c couleur de la case
         * @throws IllegalArgumentException si la couleur spécifiée ne
         *         correspond pas à un des types de terrain 
         */
        private Case(Couleur c) {
            terrain = null;
            for (Terrain t : Terrain.values()) {
                if (c.equals(t.couleur)) {
                    terrain = t;
                }
            }
            if (terrain == null) {
                System.out.println("Plaine : " + Terrain.Plaine.couleur);
                System.out.println("" + Terrain.Plaine.couleur.rgb + ", " + c.rgb);

                throw new IllegalArgumentException("Couleur (= " + c + ") invalide");
            }
        }

        // 
        // Méthodes
        //

        /**
         * @return  la couleur de la case
         */
        public Couleur getCouleur() {
            return terrain.couleur;
        }

        /**
         * @return  le coût de déplacement associé au type de terrain de la
         *          case
         */
        public double getCout() {
            return terrain.coutDeplacement;
        }
    }; 

    /**
     * La classe Coordonnee donne la position d'une case dans la grille formée
     * par la carte.
     *
     * Il s'agit tout simplement d'un couple (y, x) où y est le numéro de la
     * ligne et x le numéro de la colonne.
     *
     * CONVENTION : toujours "ligne" avant "colonne".
     */
    public static class Coordonnee {

        // 
        // Variables d'instance
        //
        public int ligne, colonne;

        // 
        // Constructeurs
        //

        /**
         * Constructeur de copie
         *
         * @param autre  coordonnées à copier
         */
        public Coordonnee(Coordonnee autre) {
            ligne = autre.ligne;
            colonne = autre.colonne;
        }

        /**
         * Constructeur à partir d'un couple d'entiers ligne, colonne.
         *
         * @param  ligne le numéro de la ligne
         * @param  colonne le numéro de la colonne
         */
        public Coordonnee(int ligne, int colonne) {
            this.ligne = ligne;
            this.colonne = colonne;
        }

        // 
        // Méthodes
        //

        /**
         * @return  une représentation textuelle de la coordonnée
         */
        public String toString() {
            return "(" + ligne + ", " + colonne + ")";
        }

    }

}


