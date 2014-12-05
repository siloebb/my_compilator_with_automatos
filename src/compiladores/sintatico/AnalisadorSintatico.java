package compiladores.sintatico;

import compiladores.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Siloé
 */
public class AnalisadorSintatico {

    private HashMap<String, NaoTerminal> listaNaoTerminal;
    private NaoTerminal starter;
    private ArrayList<Token> listaTokens;

    public AnalisadorSintatico(ArrayList<Token> listaTokens) {
        this.listaTokens = listaTokens;
        listaNaoTerminal = new HashMap<>();
    }

    public void executar() {
        try {
            starter.executar(listaTokens);
            
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void addNaoTerminal(NaoTerminal nt) {
        listaNaoTerminal.put(nt.getNome(), nt);
    }

    public NaoTerminal getNaoTerminal(String nome) {
        return listaNaoTerminal.get(nome);
    }

    public HashMap<String, NaoTerminal> getListaNaoTerminal() {
        return listaNaoTerminal;
    }

    public void setStarter(NaoTerminal starter) {
        this.starter = starter;
    }

    public NaoTerminal getStarter() {
        return starter;
    }

    public ArrayList<Token> getListaTokens() {
        return listaTokens;
    }

}
