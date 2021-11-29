package mat210;

/**
 * Fichier distribué dans le cadre du cours MAT210, session automne 2021, à l'ÉTS.
 *
 * Représente un chronomètre pour mesurer le temps d'exécution de certains
 * calculs.
 * 
 * @author Xavier Provençal
 */
public class Chronometre {

    // 
    // Variable d'instance
    //
    long depart; // heure à laquelle le chronomètre a été initialisé

    /**
     * Initialise un chronomètre. L'heure de départ est fixée au moment où le
     * chronomètre est construit.
     */
    public Chronometre() {
        this.depart = System.nanoTime();
    }

    /**
     * Le nombre de nano secondes écoutées depuis la créatoin de l'objet.
     */
    public long duree() {
        return System.nanoTime() - depart;
    }

    /**
     * Produit un affichage d'un temps en nano secondes sous une forme
     * compréhensible pour des humains.
     *
     * @param  nanosec le nombre de nano secondes écoutées
     * @return le temps écoulé dans un format lisible pour des humains
     */
    public static String formatTemps(long nanosec) {
        long x = 100*nanosec;
        String s = null;
        String[] prefixes = {"nano", "micro", "milli", ""};
        int i=0;
        while (s == null) {
            if (x < 100000 || i==3) {
                s = "" + (x/100.0) + " " + prefixes[i] + "seconde";
                if (x > 1) {
                    s += "s";
                }
            }
            x = x/1000;
            ++i;
        }
        return s;
    }

}
