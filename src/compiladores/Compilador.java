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

        /*automata.setState(0);
        automata.setState(1);
        automata.setState(2, new Erro());
        automata.setState(3);
        automata.setState(4, new Token(TipoToken.CARACTERE_CONSTANTE));
        automata.setState(5);

        automata.setStartState(0);
        //automata.setFinalstate(4);
        automata.setFinalstate(0);

        automata.setTransition(0, 1, Primitiva.LETRA);
        automata.setTransition(1, 2, "b");
        automata.setTransition(2, 3, Primitiva.LETRA);
        automata.setTransition(3, 4, Primitiva.DIGITO);
        automata.setTransition(4, 0, "1");*/
        setStates(automata);
        setTransitions(automata);
        

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
    
    public void setStates(Automata automata){
       automata.setState(0,true);
       automata.setState(1);
       automata.setState(2, new Token(TipoToken.IDENTIFICADOR));
       automata.setState(3, new Erro());
       automata.setState(4);
       
       automata.setStartState(0);
       automata.setFinalstate(0);
    }

    private void setTransitions(Automata automata) {
        automata.setTransition(0, 0, "\n");
        automata.setTransition(0, 1, Primitiva.LETRA);
        automata.setTransition(1, 1, Primitiva.LETRA);
        automata.setTransition(1, 1, Primitiva.DIGITO);
        automata.setTransition(1, 1, "_");
        automata.setTransition(1, 2, Primitiva.ESPACO);
        automata.setTransition(1, 3, Primitiva.SIMBOLO);
        automata.setTransition(1, 3, Primitiva.OUTROS);
        automata.setTransition(2, 0, Primitiva.LAMBIDA);
        automata.setTransition(3, 0, Primitiva.LAMBIDA);
        
       
    }

}
