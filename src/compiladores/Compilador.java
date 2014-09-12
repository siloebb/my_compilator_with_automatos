package compiladores;

import compiladores.automato.Automata;
import compiladores.enums.Primitiva;
import compiladores.enums.TipoToken;
import java.util.ArrayList;

/**
 *
 * @author Siloe
 */
public class Compilador {

    private Automata automata;
    private ArrayList<Erro> listaErros;
    private ArrayList<Token> listaTokens;
    

    public Compilador() {        
        iniciarAutomata();
    }

    public void iniciarAutomata() {

        System.out.println("Iniciando Automata...");        
        
        automata = new Automata();

        automata.setState(0);
        automata.setState(1);
        automata.setState(2, new Erro());
        automata.setState(3);
        automata.setState(4, new Token(TipoToken.CARACTERE_CONSTANTE));

        automata.setStartState(0);
        automata.setFinalstate(4);

        automata.setTransition(0, 1, Primitiva.LETRA);
        automata.setTransition(1, 2, "b");
        automata.setTransition(2, 3, Primitiva.LETRA);
        automata.setTransition(3, 4, Primitiva.DIGITO);

        System.out.println("Automata iniciada!");

        //automata.executeAutomata(symbol);
    }

    public void executar(String texto) {
        listaErros = new ArrayList<>();
        listaTokens = new ArrayList<>();
        
        automata.executeAutomata(texto, listaErros, listaTokens);
    }

    public ArrayList<Erro> getListaErros() {
        return listaErros;
    }

    public void setListaErros(ArrayList<Erro> listaErros) {
        this.listaErros = listaErros;
    }

    public ArrayList<Token> getListaTokens() {
        return listaTokens;
    }

    public void setListaTokens(ArrayList<Token> listaTokens) {
        this.listaTokens = listaTokens;
    }

}
