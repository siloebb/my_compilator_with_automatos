package compiladores;

import compiladores.automato.Automata;
import compiladores.enums.Primitiva;
import compiladores.enums.TipoErro;
import compiladores.enums.TipoToken;
import compiladores.semantico.ErroSemantico;
import compiladores.sintatico.AnalisadorSintatico;
import compiladores.sintatico.EditorDeNaoTerminais;
import compiladores.sintatico.ErroSintatico;
import compiladores.utils.Identificador;
import java.util.ArrayList;

/**
 *
 * @author Siloe
 */
public class Compilador {

    private Automata automata;
    private ArrayList<Erro> listaErrosLexicos;
    private ArrayList<ErroSintatico> listaErrosSintaticos;
    private ArrayList<ErroSemantico> listaErrosSemanticos;
    private ArrayList<Token> listaTokens;
    private AnalisadorSintatico analisadorSintatico;

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
        //Analise Léxica
        listaErrosLexicos = new ArrayList<>();
        listaTokens = new ArrayList<>();

        automata.executeAutomata(texto, listaErrosLexicos, listaTokens);
        for (Token token : listaTokens) {
            if (Identificador.verificarPalavraReservada(token.getLexema())) {
                token.setTipoToken(TipoToken.PALAVRA_RESERVADA);
            }
        }

        //Analise Sintática
        //removendo comentários
        ArrayList<Token> listaTokensSintatico = new ArrayList<>();
        listaTokensSintatico.addAll(listaTokens);
        
        int i = 0;
        while (i < listaTokensSintatico.size()) {
            Token token = listaTokensSintatico.get(i);
            if (token.getTipoToken() == TipoToken.COMENTARIO) {
                listaTokensSintatico.remove(i);
            } else {
                i++;
            }
        }

        analisadorSintatico = new AnalisadorSintatico(listaTokensSintatico);

        listaErrosSintaticos = EditorDeNaoTerminais.setNaoTerminais(analisadorSintatico);
        
        
        analisadorSintatico.setStarter(analisadorSintatico.getListaNaoTerminal().get("lista_registros"));
        analisadorSintatico.executar();
        
        listaErrosSemanticos = EditorDeNaoTerminais.getListaErrosSemanticos();

    }

    

    public ArrayList<Erro> getListaErros() {
        return listaErrosLexicos;
    }

    public void setListaErros(ArrayList<Erro> listaErros) {
        this.listaErrosLexicos = listaErros;
    }

    public ArrayList<Token> getListaTokens() {
        return listaTokens;
    }

    public ArrayList<ErroSintatico> getListaErrosSintaticos() {
        return listaErrosSintaticos;
    }

    public ArrayList<ErroSemantico> getListaErrosSemanticos() {
        return listaErrosSemanticos;
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
        automata.setState(91);
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
        
        automata.setState(80);

        automata.setStartState(0);
        automata.setFinalstate(0);

    }

    private void setTransitions(Automata automata) {
        automata.setTransition(0, 0, Primitiva.FIM_DE_LINHA);
        automata.setTransition(0, 0, Primitiva.ESPACO);
        automata.setTransition(0, 59, Primitiva.SIMBOLO);
        automata.setTransition(0, 17, Primitiva.OUTROS);
        automata.setTransition(17, 0, Primitiva.LAMBIDA);

        //IDENTIFICADOR
        automata.setTransition(0, 1, Primitiva.LETRA);
        automata.setTransition(1, 1, Primitiva.LETRA);
        automata.setTransition(1, 1, Primitiva.DIGITO);
        automata.setTransition(1, 1, "_");
        automata.setTransition(1, 2, Primitiva.ESPACO, false);
        automata.setTransition(1, 2, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(1, 2, Primitiva.SIMBOLO, false);
        automata.setTransition(1, 2, Primitiva.OUTROS, false);
        automata.setTransition(1, 3, "\"", false);
        automata.setTransition(1, 3, "\'", false);
        automata.setTransition(2, 0, Primitiva.LAMBIDA);
        automata.setTransition(3, 0, Primitiva.LAMBIDA);
        //NUMEROS
        automata.setTransition(0, 4, Primitiva.DIGITO);
        automata.setTransition(4, 4, Primitiva.DIGITO);
        automata.setTransition(4, 91, ".");
        automata.setTransition(91, 5, Primitiva.DIGITO);
        automata.setTransition(91, 8, Primitiva.LETRA);
        automata.setTransition(91, 8, Primitiva.SIMBOLO);
        automata.setTransition(91, 8, Primitiva.ESPACO, false);
        automata.setTransition(91, 8, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(4, 7, Primitiva.ESPACO, false);
        automata.setTransition(4, 7, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(4, 7, Primitiva.SIMBOLO, false);
        automata.setTransition(4, 8, Primitiva.LETRA, false);
        automata.setTransition(4, 8, Primitiva.OUTROS);
        automata.setTransition(5, 5, Primitiva.DIGITO);
        automata.setTransition(5, 8, Primitiva.LETRA);
        automata.setTransition(5, 6, Primitiva.SIMBOLO, false);
        automata.setTransition(5, 6, Primitiva.ESPACO, false);
        automata.setTransition(5, 6, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(6, 0, Primitiva.LAMBIDA);
        automata.setTransition(7, 0, Primitiva.LAMBIDA);
        automata.setTransition(8, 0, Primitiva.LAMBIDA);

        //CADEIA CONSTANTE
        automata.setTransition(0, 9, "\"");
        automata.setTransition(9, 9, Primitiva.LETRA);
        automata.setTransition(9, 9, Primitiva.DIGITO);
        automata.setTransition(9, 9, Primitiva.SIMBOLO);
        automata.setTransition(9, 9, Primitiva.ESPACO);
        automata.setTransition(9, 10, "\"");
        automata.setTransition(9, 11, Primitiva.FIM_DE_LINHA);
        automata.setTransition(9, 12, Primitiva.OUTROS);
        automata.setTransition(10, 0, Primitiva.LAMBIDA);
        automata.setTransition(11, 0, Primitiva.LAMBIDA);
        automata.setTransition(12, 0, Primitiva.LAMBIDA);

        //CARACTERE CONSTANTE
        automata.setTransition(0, 13, "\'");
        automata.setTransition(13, 14, Primitiva.LETRA);
        automata.setTransition(13, 14, Primitiva.DIGITO);
        automata.setTransition(13, 14, Primitiva.SIMBOLO);
        automata.setTransition(13, 14, " ");
        automata.setTransition(13, 15, "\'");
        automata.setTransition(13, 16, Primitiva.OUTROS);
        automata.setTransition(14, 19, "\'");
        automata.setTransition(14, 16, Primitiva.OUTROS);
        automata.setTransition(14, 18, Primitiva.LETRA);
        automata.setTransition(14, 18, Primitiva.DIGITO);
        automata.setTransition(14, 18, Primitiva.SIMBOLO);
        automata.setTransition(15, 0, Primitiva.LAMBIDA);
        automata.setTransition(16, 16, Primitiva.LETRA);
        automata.setTransition(16, 16, Primitiva.DIGITO);
        automata.setTransition(16, 16, Primitiva.SIMBOLO);
        automata.setTransition(16, 16, Primitiva.LETRA);
        automata.setTransition(16, 14, Primitiva.LAMBIDA);
        automata.setTransition(18, 18, Primitiva.LETRA);
        automata.setTransition(18, 18, Primitiva.DIGITO);
        automata.setTransition(18, 18, Primitiva.SIMBOLO);
        automata.setTransition(18, 20, "\'");
        automata.setTransition(18, 17, Primitiva.OUTROS);
        automata.setTransition(19, 0, Primitiva.LAMBIDA);
        automata.setTransition(20, 0, Primitiva.LAMBIDA);

        //DELIMITADORES
        automata.setTransition(0, 21, ";");
        automata.setTransition(0, 22, ",");
        automata.setTransition(0, 23, "(");
        automata.setTransition(0, 24, ")");
        automata.setTransition(0, 25, "{");
        automata.setTransition(0, 26, "}");
        automata.setTransition(0, 27, "[");
        automata.setTransition(0, 28, "]");
        automata.setTransition(0, 80, "!");

        automata.setTransition(21, 0, Primitiva.LAMBIDA);
        automata.setTransition(22, 0, Primitiva.LAMBIDA);
        automata.setTransition(23, 0, Primitiva.LAMBIDA);
        automata.setTransition(24, 0, Primitiva.LAMBIDA);
        automata.setTransition(25, 0, Primitiva.LAMBIDA);
        automata.setTransition(26, 0, Primitiva.LAMBIDA);
        automata.setTransition(27, 0, Primitiva.LAMBIDA);
        automata.setTransition(28, 0, Primitiva.LAMBIDA);

        //OPERADORES
        automata.setTransition(0, 29, "/");
        automata.setTransition(29, 30, "*");
        automata.setTransition(29, 33, "/");
        automata.setTransition(29, 35, Primitiva.LETRA, false);
        automata.setTransition(29, 35, Primitiva.DIGITO, false);
        automata.setTransition(29, 35, Primitiva.ESPACO, false);
        automata.setTransition(29, 39, Primitiva.OUTROS);
        automata.setTransition(30, 30, Primitiva.LETRA);
        automata.setTransition(30, 30, Primitiva.SIMBOLO);
        automata.setTransition(30, 30, Primitiva.DIGITO);
        automata.setTransition(30, 30, Primitiva.OUTROS);
        automata.setTransition(30, 30, Primitiva.ESPACO);
        automata.setTransition(30, 30, Primitiva.FIM_DE_LINHA);
        automata.setTransition(30, 31, "*");
        automata.setTransition(31, 31, "*");
        automata.setTransition(31, 30, Primitiva.LETRA);
        automata.setTransition(31, 30, Primitiva.SIMBOLO);
        automata.setTransition(31, 30, Primitiva.DIGITO);
        automata.setTransition(31, 30, Primitiva.OUTROS);
        automata.setTransition(31, 30, Primitiva.ESPACO);
        automata.setTransition(31, 30, Primitiva.FIM_DE_LINHA);
        automata.setTransition(31, 32, "/");
        automata.setTransition(32, 0, Primitiva.LAMBIDA);
        automata.setTransition(33, 33, Primitiva.LETRA);
        automata.setTransition(33, 33, Primitiva.DIGITO);
        automata.setTransition(33, 33, Primitiva.SIMBOLO);
        automata.setTransition(33, 33, Primitiva.OUTROS);
        automata.setTransition(33, 33, Primitiva.ESPACO);
        automata.setTransition(33, 34, Primitiva.FIM_DE_LINHA);
        automata.setTransition(34, 0, Primitiva.LAMBIDA);
        automata.setTransition(35, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 36, "+");
        automata.setTransition(36, 37, "+");
        automata.setTransition(36, 38, Primitiva.DIGITO, false);
        automata.setTransition(36, 38, Primitiva.SIMBOLO, false);
        automata.setTransition(36, 38, Primitiva.LETRA, false);
        automata.setTransition(36, 38, Primitiva.ESPACO, false);
        //automata.setTransition(36, 38, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(36, 39, Primitiva.OUTROS);
        automata.setTransition(37, 0, Primitiva.LAMBIDA);
        automata.setTransition(38, 0, Primitiva.LAMBIDA);
        automata.setTransition(39, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 40, "-");
        automata.setTransition(40, 41, "-");
        automata.setTransition(40, 39, Primitiva.OUTROS);
        automata.setTransition(40, 42, Primitiva.LETRA, false);
        automata.setTransition(40, 42, Primitiva.SIMBOLO, false);
        automata.setTransition(40, 42, Primitiva.DIGITO, false);
        automata.setTransition(40, 42, Primitiva.ESPACO, false);
        automata.setTransition(41, 0, Primitiva.LAMBIDA);
        automata.setTransition(42, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 43, "*");
        automata.setTransition(43, 44, Primitiva.DIGITO, false);
        automata.setTransition(43, 44, Primitiva.LETRA, false);
        automata.setTransition(43, 44, Primitiva.ESPACO, false);
        automata.setTransition(43, 44, Primitiva.SIMBOLO, false);
        automata.setTransition(43, 44, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(44, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 45, "=");
        automata.setTransition(45, 46, Primitiva.DIGITO, false);
        automata.setTransition(45, 46, Primitiva.LETRA, false);
        automata.setTransition(45, 46, Primitiva.ESPACO, false);
        automata.setTransition(45, 46, Primitiva.SIMBOLO, false);
        automata.setTransition(45, 46, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(45, 47, "=");
        automata.setTransition(46, 0, Primitiva.LAMBIDA);
        automata.setTransition(47, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 48, "<");
        automata.setTransition(48, 49, Primitiva.DIGITO, false);
        automata.setTransition(48, 49, Primitiva.LETRA, false);
        automata.setTransition(48, 49, Primitiva.ESPACO, false);
        automata.setTransition(48, 49, Primitiva.SIMBOLO, false);
        automata.setTransition(48, 49, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(48, 50, "=");
        automata.setTransition(49, 0, Primitiva.LAMBIDA);
        automata.setTransition(50, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 48, ">");
        automata.setTransition(48, 49, Primitiva.DIGITO, false);
        automata.setTransition(48, 49, Primitiva.LETRA, false);
        automata.setTransition(48, 49, Primitiva.ESPACO, false);
        automata.setTransition(48, 49, Primitiva.SIMBOLO, false);
        automata.setTransition(48, 49, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(48, 50, "=");
        automata.setTransition(49, 0, Primitiva.LAMBIDA);
        automata.setTransition(50, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 51, "|");
        automata.setTransition(51, 52, "|");
        automata.setTransition(51, 53, Primitiva.DIGITO, false);
        automata.setTransition(51, 53, Primitiva.SIMBOLO, false);
        automata.setTransition(51, 53, Primitiva.LETRA, false);
        automata.setTransition(51, 53, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(51, 53, Primitiva.ESPACO, false);
        automata.setTransition(51, 53, Primitiva.OUTROS, false);
        automata.setTransition(52, 0, Primitiva.LAMBIDA);
        automata.setTransition(53, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 54, "&");
        automata.setTransition(54, 55, "&");
        automata.setTransition(54, 53, Primitiva.DIGITO, false);
        automata.setTransition(54, 53, Primitiva.SIMBOLO, false);
        automata.setTransition(54, 53, Primitiva.LETRA, false);
        automata.setTransition(54, 53, Primitiva.FIM_DE_LINHA, false);
        automata.setTransition(54, 53, Primitiva.ESPACO, false);
        automata.setTransition(54, 53, Primitiva.OUTROS, false);
        automata.setTransition(55, 0, Primitiva.LAMBIDA);

        automata.setTransition(0, 56, ".");
        automata.setTransition(56, 57, Primitiva.LETRA, false);
        automata.setTransition(56, 57, Primitiva.DIGITO, false);
        automata.setTransition(56, 58, Primitiva.SIMBOLO, false);
        automata.setTransition(56, 58, Primitiva.ESPACO);
        automata.setTransition(56, 58, Primitiva.FIM_DE_LINHA);
        automata.setTransition(57, 0, Primitiva.LAMBIDA);
        automata.setTransition(58, 0, Primitiva.LAMBIDA);

        automata.setTransition(59, 0, Primitiva.LAMBIDA);
        
        automata.setTransition(80, 47, "=");

    }

}
