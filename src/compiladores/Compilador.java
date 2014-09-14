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
            if (Identificador.verificarPalavraReservada(token.getLexema())) {
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

    public void setStates(Automata automata) {
        automata.setState(0, true);
//       automata.setState(1);
//       automata.setState(2, new Token(TipoToken.IDENTIFICADOR));
//       automata.setState(3, new Erro(TipoErro.IDENTIFICADOR_MAL_FORMADO));
//       automata.setState(4);
        automata.setState(1);
        automata.setState(2, new Token(TipoToken.IDENTIFICADOR));
        automata.setState(3, new Erro(TipoErro.IDENTIFICADOR_MAL_FORMADO));
        automata.setState(4);
        automata.setState(5);
        automata.setState(6, new Token(TipoToken.NUMERO_REAL));
        automata.setState(7, new Token(TipoToken.NUMERO_INTEIRO));
        automata.setState(8, new Erro(TipoErro.NUMERO_MAL_FORMADO));
        automata.setState(9);

        automata.setState(10, new Token(TipoToken.CADEIA_CONSTANTE));
        automata.setState(11, new Erro(TipoErro.CADEIA_INCOMPLETA));
        automata.setState(12, new Erro(TipoErro.SIMBOLO_NAO_IDENTIFICADO));
        automata.setState(13);
        automata.setState(14);
        automata.setState(15, new Erro(TipoErro.CARACTERE_VAZIO));
        automata.setState(16, new Erro(TipoErro.SIMBOLO_NAO_IDENTIFICADO));
        automata.setState(17, new Erro(TipoErro.SIMBOLO_NAO_IDENTIFICADO));
        automata.setState(18, new Erro(TipoErro.CADEIA_INCOMPLETA));
        automata.setState(19, new Token(TipoToken.CARACTERE_CONSTANTE));

        automata.setState(20, new Erro(TipoErro.QUANTIDADE_DE_CARACTERES_INVALIDO));
        automata.setState(21, new Token(TipoToken.DELIMITADOR_FIM_LINHA));
        automata.setState(22, new Token(TipoToken.DELIMITADOR_SEPARADOR));
        automata.setState(23, new Token(TipoToken.DELIMITADOR_ABRIR_PARENTESE));
        automata.setState(24, new Token(TipoToken.DELIMITADOR_FECHAR_PARENTESE));
        automata.setState(25, new Token(TipoToken.DELIMITADOR_ABRIR_CHAVES));
        automata.setState(26, new Token(TipoToken.DELIMITADOR_FECHAR_CHAVES));
        automata.setState(27, new Token(TipoToken.DELIMITADOR_ABRIR_COLCHETE));
        automata.setState(28, new Token(TipoToken.DELIMITADOR_FECHAR_COLCHETE));
        automata.setState(29);

        automata.setState(30);
        automata.setState(31);
        automata.setState(32, new Token(TipoToken.COMENTARIO));
        automata.setState(33);
        automata.setState(34, new Token(TipoToken.COMENTARIO));
        automata.setState(35, new Token(TipoToken.OPERADOR_ARITMETICO_BINARIO));
        automata.setState(36);
        automata.setState(37, new Token(TipoToken.OPERADOR_ARITMETICO_UNARIO));
        automata.setState(38, new Token(TipoToken.OPERADOR_ARITMETICO_BINARIO));
        automata.setState(39, new Erro(TipoErro.SIMBOLO_NAO_IDENTIFICADO));

        automata.setState(40);
        automata.setState(41, new Token(TipoToken.OPERADOR_ARITMETICO_UNARIO));
        automata.setState(42, new Token(TipoToken.OPERADOR_ARITMETICO_BINARIO));
        automata.setState(43);
        automata.setState(44, new Token(TipoToken.OPERADOR_ARITMETICO_BINARIO));
        automata.setState(45);
        automata.setState(46, new Token(TipoToken.OPERADOR_ARITMETICO_ATRIBUICAO));
        automata.setState(47, new Token(TipoToken.OPERADOR_RELACIONAL));
        automata.setState(48);
        automata.setState(49, new Token(TipoToken.OPERADOR_RELACIONAL));

        automata.setState(50, new Token(TipoToken.OPERADOR_RELACIONAL));
        automata.setState(51);
        automata.setState(52, new Token(TipoToken.OPERADOR_ARITMETICO_LOGICO));
        automata.setState(53, new Erro(TipoErro.OPERADOR_MAL_FORMADO));
        automata.setState(54);
        automata.setState(55, new Token(TipoToken.OPERADOR_ARITMETICO_LOGICO));
        automata.setState(56);
        automata.setState(57, new Token(TipoToken.OPERADOR_ARITMETICO_ACESSO));
        automata.setState(58, new Erro(TipoErro.OPERADOR_MAL_FORMADO));
        automata.setState(59, new Erro(TipoErro.SIMBOLO_NAO_IDENTIFICADO));

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
