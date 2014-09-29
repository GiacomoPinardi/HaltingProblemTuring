/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haltingproblemturing;

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author giacomo
 */
public class Gestione {
    
    // Tramite il costruttore creo due nuovi vettori:
    
    // "stato" contiene la configurazione che la macchina deve assumere (stato "a" o stato "b")
    // Il valore "0" indica lo stato "a", il valore "1" indica lo stato "b"
        
    // "shift" contiene il valore dello spostamento che la testina della macchina deve compiere (stato "a" o stato "b")
    
    int[][] stato;
    int[][] shift;
    
    // "nastro" diventa l'ononimo Vettore "nastro" della classe "FermataTuring" che l'utente riempie;
    Vector nastro;
    // il vettore result contiene tutti i risultati della simulazione, ovvero l'index delle celle per le quali la macchina partendo, 
    // in questa determinata configurazione, si ferma (finendo cioe' nella cella 0)
    Vector result;
    
    // La variabile statoIniziale indica lo stato iniziale della macchina (o 'a' o 'b' che equivarranno "0" e a "1"):
    int statoIniziale;       
    
    public Gestione(Vector v) {
        this.stato = new int[3][2];
        this.shift = new int[3][2]; 
        
        this.nastro = v;
        result = new Vector(nastro.size());
    }
    
    // Inizializzo il vettore "stato" passando al metodo 6 valori:
    //      c0rA: stato relativo al valore "0" e alla configurazione "a"
    //      c1rA: stato relativo al valore "1" e alla configurazione "a"
    //      c2rA: stato relativo al valore "2" e alla configurazione "a"
    //      c0rB: stato relativo al valore "0" e alla configurazione "b"
    //      c1rB: stato relativo al valore "1" e alla configurazione "b"
    //      c2rB: stato relativo al valore "2" e alla configurazione "b"
    public void inizializzaStato(String c0rA, String c1rA, String c2rA, String c0rB, String c1rB, String c2rB) {
        
        this.traduttore(c0rA, 0, 0);
        this.traduttore(c1rA, 1, 0);
        this.traduttore(c2rA, 2, 0);
        this.traduttore(c0rB, 0, 1);
        this.traduttore(c1rB, 1, 1);
        this.traduttore(c2rB, 2, 1);
    }
    
    // Inizializzo il vettore "shift" passando al metodo 6 valori:
    //      c0rA: spostamento relativo al valore "0" e alla configurazione "a"
    //      c1rA: spostamento relativo al valore "1" e alla configurazione "a"
    //      c2rA: spostamento relativo al valore "2" e alla configurazione "a"
    //      c0rB: spostamento relativo al valore "0" e alla configurazione "b"
    //      c1rB: spostamento relativo al valore "1" e alla configurazione "b"
    //      c2rB: spostamento relativo al valore "2" e alla configurazione "b"
    public void inizializzaShift(int c0rA, int c1rA, int c2rA, int c0rB, int c1rB, int c2rB) {
        shift[0][0] = c0rA;
        shift[1][0] = c1rA;
        shift[2][0] = c2rA;
        
        shift[0][1] = c0rB;
        shift[1][1] = c1rB;
        shift[2][1] = c2rB;
    }
    
    // Con questo metodo viene settato lo stato iniziale della macchina. '1' indica 'a', mentre '2' indica 'b'
    public void setStatoIniziale (String s) {
        if ("a".equalsIgnoreCase(s)) {
            this.statoIniziale = 0;
        }
        else {
            this.statoIniziale = 1;
        }
        this.statoCorrente = this.statoIniziale;
    }    
    
    // Traduce la "a" o la "b" in "0" e "1":
    private void traduttore (String s, int x, int y) {
        if (s.equalsIgnoreCase("a")) {
            stato[x][y] = 0;
        }
        else {
            stato[x][y] = 1;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // inizializzo le variabili ed i valori:
        // stato corrente indica lo stato attuale della macchina (a=1, b=2)
        int statoCorrente;
        // cella corrente indica l'index della cella corrente
        int indexCorrente;        
        // creo un HashMap che mi permettera' di capire se entra in loop infinito
        // key: cellaCorrente   -   value: statoCorrente
        HashMap hm = new HashMap();
    
    // Metodo che avvia la simulazione della macchina
    public void start () {        
        // avvio la simulazione per ogni cella del nastro
        for (int i = 0; i < nastro.size(); i++) {
            // Pulisco l'hashmap da tutti i valori:
            hm.clear();
            // Pulisco lo stato corrente:
            this.statoCorrente = this.statoIniziale;
            
            if (this.simula(i)) {
                // se la macchina si ferma il valore di i (ovvero al cella di partenza) viene aggiunta alle soluzioni
                result.addElement(i);
            }
        }       
    }
    
    // Il metodo simula permette di simulare la configurazione partendo dalla cella nell'argomento e restituisce true se si ferma e false altrimenti
    private boolean simula (int indexIniziale) {
        // indica se e' avvenuto un loop infinito:
        boolean loop = false;
        
        // cellaCorrente assume il valore della cella da cui deve iniziare la simulazione della macchina
        this.indexCorrente = indexIniziale;
        
        while (this.indexCorrente != 0) {
            // memorizzo all'interno di hm la cella corrente e lo stato. Se la macchina torna in questo punto significa che si sta ripetendo
            hm.put(this.indexCorrente, this.statoCorrente);
            
            int newStato = this.statoReturner(this.cellaReader(this.indexCorrente), this.statoCorrente);
            int newShift = this.shiftReturner(this.cellaReader(this.indexCorrente), this.statoCorrente);
            this.indexCorrente += newShift;
            this.statoCorrente = newStato;
            
            // hm interrompe il ciclo se la macchina si ripete
            if (hm.containsKey(indexCorrente)) {
                if ((int) hm.get(indexCorrente) == statoCorrente) {
                    loop = true;
                    break;
                }
            }
            this.simula(indexCorrente);
        }
        return !loop;
    }
    
    // Metodo che legge la configurazione e restituisce lo shift
    private int shiftReturner (int v, int s) {
        return this.shift[v][s];
    }
    
    // Metodo che legge la configurazione e restituisce lo stato
    private int statoReturner (int v, int s) {        
        return this.stato[v][s];
    }
    
    // legge il valore contenuto nella cella indicata
    private int cellaReader (int cella) {
        return Integer.valueOf((String) nastro.elementAt(cella));        
    }
    
    public Vector getSolution () {
        return result;
    }
    
    public void scriviTutto () {
        System.out.println("Stato Iniziale: " + this.statoIniziale);
        System.out.println("");
        
        System.out.println("Configurazione stato:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print("Stato[" + i + "][" + j + "]: " + stato[i][j]);
                System.out.println(" - ");
            }
        }
        System.out.println("");
        System.out.println("Configurazione shift:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print("Shift[" + i + "][" + j + "]: " + shift[i][j]);
                System.out.println(" - ");
            }
        }
        System.out.println("");
        System.out.println("Configurazione Nastro:");
        for (int i = 0; i < nastro.size(); i++) {
            System.out.print(nastro.elementAt(i));
            System.out.println(" - ");
        }
        System.out.println("");
    }
}
