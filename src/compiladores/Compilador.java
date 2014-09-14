package compiladores;

import compiladores.automato.Automata;
import compiladores.enums.Primitiva;
import compiladores.enums.TipoErro;
import compiladores.enums.TipoToken;
import compiladores.utils.Identificador;
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
        
        setStates(automata);
        setTransitions(automata);
        

        System.out.println("Automata iniciada!");

        //automata.executeAutomata(symbol);
    }

    public void executar(String texto) {
        listaErros = new ArrayList<>();
        listaTokens = new ArrayList<>();
        
        automata.executeAutomata(texto, listaErros, listaTokens);
        for (Token token : listaTokens) {
            if(Identificador.verificarPalavraReservada(token.getLexema())){
                token.setTipoToken(TipoToken.PALAVRA_RESERVADA);
            }
        }
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
//       automata.setState(1);
//       automata.setState(2, new Token(TipoToken.IDENTIFICADOR));
//       automata.setState(3, new Erro(TipoErro.IDENTIFICADOR_MAL_FORMADO));
//       automata.setState(4);
       automata.setStartState(1);
       automata.setStartState(2);
       automata.setStartState(3);
       automata.setStartState(4);
       automata.setStartState(5);
       automata.setStartState(6);
       automata.setStartState(7);
       automata.setStartState(8);
       automata.setStartState(9);
       
       automata.setStartState(10);       
       automata.setStartState(11);
       automata.setStartState(12);
       automata.setStartState(13);
       automata.setStartState(14);
       automata.setStartState(15);
       automata.setStartState(16);
       automata.setStartState(17);
       automata.setStartState(18);
       automata.setStartState(19);
       
       automata.setStartState(20);       
       automata.setStartState(21);
       automata.setStartState(22);
       automata.setStartState(23);
       automata.setStartState(24);
       automata.setStartState(25);
       automata.setStartState(26);
       automata.setStartState(27);
       automata.setStartState(28);
       automata.setStartState(29);
       
       automata.setStartState(30);       
       automata.setStartState(31);
       automata.setStartState(32);
       automata.setStartState(33);
       automata.setStartState(34);
       automata.setStartState(35);
       automata.setStartState(36);
       automata.setStartState(37);
       automata.setStartState(38);
       automata.setStartState(39);
       
       automata.setStartState(40);       
       automata.setStartState(41);
       automata.setStartState(42);
       automata.setStartState(43);
       automata.setStartState(44);
       automata.setStartState(45);
       automata.setStartState(46);
       automata.setStartState(47);
       automata.setStartState(48);
       automata.setStartState(49);
       
       automata.setStartState(50);       
       automata.setStartState(51);
       automata.setStartState(52);
       automata.setStartState(53);
       automata.setStartState(54);
       automata.setStartState(55);
       automata.setStartState(56);
       automata.setStartState(57);
       automata.setStartState(58);
       automata.setStartState(59);
       
       automata.setStartState(0);
       automata.setFinalstate(0);
       
    }

    private void setTransitions(Automata automata) {
        automata.setTransition(0, 0, "\n");
        automata.setTransition(0, 0, Primitiva.ESPACO);
        automata.setTransition(0, 1, Primitiva.LETRA);
        automata.setTransition(1, 1, Primitiva.LETRA);
        automata.setTransition(1, 1, Primitiva.DIGITO);
        automata.setTransition(1, 1, "_");
        automata.setTransition(1, 2, Primitiva.ESPACO);
        automata.setTransition(1, 2, Primitiva.FIM_DE_LINHA);
        automata.setTransition(1, 3, Primitiva.SIMBOLO);
        automata.setTransition(1, 3, Primitiva.OUTROS);
        automata.setTransition(2, 0, Primitiva.LAMBIDA);
        automata.setTransition(3, 0, Primitiva.LAMBIDA);
        
       
    }

}
