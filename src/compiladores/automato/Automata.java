package compiladores.automato;

import compiladores.Erro;
import compiladores.Token;
import compiladores.enums.Primitiva;
import compiladores.enums.TipoErro;
import compiladores.exceções.NoDestinyToSymbolException;
import compiladores.exceções.NoFinishiInFinalStateException;
import compiladores.utils.Identificador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Siloe
 */
public class Automata {

    private static int startStateLimit = 0;

    private HashMap<Integer, State> states;
    private HashMap<Integer, State> finalStates;
    private State startState;
    private HashSet<Transition> transitions;

    public Automata() {
        states = new HashMap<>();
        finalStates = new HashMap<>();
        transitions = new HashSet<>();
    }

    public void setState(int id) {
        State s = new State();
        s.setId(id);
        s.setLabel("q" + id);
        states.put(id, s);
    }

    public void setState(int id, boolean principal) {
        State s = new State();
        s.setId(id);
        s.setLabel("q" + id);
        s.setPrincipal(principal);
        states.put(id, s);
    }

    public void setState(int id, Token tot) {
        State s = new State();
        s.setId(id);
        s.setLabel("q" + id);
        s.setToken(tot);
        states.put(id, s);
    }

    public void setState(int id, Erro toe) {
        State s = new State();
        s.setId(id);
        s.setLabel("q" + id);
        s.setErro(toe);
        states.put(id, s);
    }

    public void setFinalstate(int id) {
        State get = states.get(id);
        finalStates.put(id, get);
    }

    public void setStartState(int id) {
        State get = states.get(id);
        finalStates.put(id, get);
    }

    public void setTransition(int origin, int destiny, String symbol) {
        State stateOrigin = states.get(origin);
        State stateDestiny = states.get(destiny);
        Transition t = new Transition(stateOrigin, stateDestiny);
        t.setSymbol(symbol);

        transitions.add(t);
    }

    public void setTransition(int origin, int destiny, String symbol, boolean consume) {
        State stateOrigin = states.get(origin);
        State stateDestiny = states.get(destiny);
        Transition t = new Transition(stateOrigin, stateDestiny);
        t.setSymbol(symbol);
        t.setConsume(consume);

        transitions.add(t);
    }

    public void setTransition(int origin, int destiny, Primitiva symbol) {
        State stateOrigin = states.get(origin);
        State stateDestiny = states.get(destiny);
        Transition t = new Transition(stateOrigin, stateDestiny);
        t.setSymbol(symbol);

        transitions.add(t);
    }

    public void setTransition(int origin, int destiny, Primitiva symbol, boolean consume) {
        State stateOrigin = states.get(origin);
        State stateDestiny = states.get(destiny);
        Transition t = new Transition(stateOrigin, stateDestiny);
        t.setSymbol(symbol);
        t.setConsume(consume);

        transitions.add(t);
    }

    public Transition getTransition(int origin, String symbol) {
        State stateOrigin = states.get(origin);
        for (Transition transition : transitions) {
            if (transition.getSymbol() != null) {
                if ((transition.getOrigin().equals(stateOrigin)) && (transition.getSymbol().equals(symbol))) {
                    return transition;
                }
            }

        }
        return null;
    }

    public Transition getTransition(int origin, Primitiva symbol) {
        State stateOrigin = states.get(origin);
        for (Transition transition : transitions) {
            if (transition.getSymbolPrimitiva() != null) {
                if ((transition.getOrigin().equals(stateOrigin)) && (transition.getSymbolPrimitiva().equals(symbol))) {
                    return transition;
                }
            }
        }
        return null;
    }

    public Transition getTransitionLambda(int origin) {
        State stateOrigin = states.get(origin);
        for (Transition transition : transitions) {
            if (transition.getSymbolPrimitiva() != null) {
                if ((transition.getOrigin().equals(stateOrigin))
                        && (transition.getSymbolPrimitiva().equals(Primitiva.LAMBIDA))) {
                    return transition;
                }
            }
        }
        return null;
    }

    public State getStartState() {
        return startState;
    }

    public State getFinalState(int id) {
        return finalStates.get(id);
    }

    public int getFinalStateSize() {
        return finalStates.size();
    }

    public State getState(int id) {
        return states.get(id);
    }

    public boolean isStartState(int id) {
        return startState.equals(states.get(id));
    }

    public boolean isFinalState(int id) {
        State get = finalStates.get(id);
        if (get == null) {
            return false;
        }
        return true;
    }

    private void message(String msg) {
        System.out.println("message: " + msg);
    }

    public void executeAutomata(String texto, ArrayList<Erro> listaErros, ArrayList<Token> listaTokens) {
        try {

            int origin = 0;

            String[] lines = texto.split("\n");
//            for (String strings : lines) {
//                strings = strings + "¿";
//            }

            String tempToken = "";

            for (int c = 0; lines.length > c; c++) {
                String linha = lines[c];
//                System.out.println("Estou na linha " + c);
                int i = 0;

                //Adicionando símbolo de enter
                linha = linha + "¶";

                try {

                    while (i < linha.length()) {

                        char charAt = linha.charAt(i);

                        //Pegando a transição. A prioridade é de um catactere especifico
                        Transition transition = null;
                        //transition = getTransitionLambda(origin);
                        if (transition == null) {
                            transition = getTransition(origin, "" + charAt);
                        }
                        if (transition == null) {
                            Primitiva primitiva = Identificador.getPrimitiva(charAt);
                            transition = getTransition(origin, primitiva);
                        }
                        //acumulando caracteres se forem consumivéis
                        if (transition != null) {
                            if (transition.isConsume()) {
                                tempToken += charAt;
                            }
                        }

                        //Se recuperando de um estado que não trata o simbolo recebido
                        if (transition == null) {
                            throw new NoDestinyToSymbolException("Sem destino para o simbolo \'"
                                    + charAt + "\' no estado " + getState(origin).getLabel());
                        }

                        //Pegando estado destino
                        State destiny = transition.getDestiny();

                        //Verificando se o estado irá informar alguma informação
                        if (destiny.getToken() != null) {
                            //System.out.println("" + destiny.getToken());
                            destiny.getToken().setLexema("" + tempToken.trim());
                            destiny.getToken().setLinha(c + 1);
                            listaTokens.add(destiny.getToken());
                            destiny.setToken(new Token(destiny.getToken().getTipoToken()));
                        }
                        if (destiny.getErro() != null) {
                            destiny.getErro().setLexema("" + tempToken.trim());
                            destiny.getErro().setLinha(c + 1);
                            listaErros.add(destiny.getErro());
                            destiny.setErro(new Erro(destiny.getErro().getTipoErro()));
                        }

                        //Mudando nova origem
                        origin = destiny.getId();

//                        System.out.println("Leu o simbolo \" " + charAt + " \" foi para o "
//                                + this.getState(origin).getLabel());

                        //reiniciando o armazenador de token
                        if (this.getState(origin).isPrincipal()) {
                            tempToken = "";
                        }

                        if (transition.isConsume()) {
                            i++;
                        }

                        //Verificando transições lambdas
                        while (true) {
                            //State stateOrigin = this.getState(origin);
                            Transition transitionLambda = getTransitionLambda(origin);

                            if (transitionLambda != null) {
                                origin = transitionLambda.getDestiny().getId();
//                                System.out.println("Transição lambda, passando para estado "
//                                        + this.getState(origin).getLabel());
                                //reiniciando o armazenador de token para lambidas
                                if (this.getState(origin).isPrincipal()) {
                                    tempToken = "";
                                }
                            } else {
                                break;
                            }
                        }
                    }
                } catch (NoDestinyToSymbolException ex) {
                    System.out.println("Entrando em um estado que não trata o simbolo recebido!");
                    ex.printStackTrace();

                    Erro erro = new Erro(TipoErro.ERRO_NAO_IDENTIFICADO);
                    erro.setLexema("" + tempToken.trim());
                    erro.setLinha(c + 1);
                    listaErros.add(erro);

                    tempToken = "";

                    origin = 0;
                }
            }

            State ultimoState = finalStates.get(origin);
            if (ultimoState != null) {
                System.out.println("Terminou em um estado final");
            } else {
                System.out.println("Terminou em um estado não-final");

                //Verfica se finalizou o comentário e lança o erro
                if (origin != 30 && origin != 31) {
                    throw new NoFinishiInFinalStateException("O automato não terminou em um estado final! q"+origin);
                }else{
                    Erro erro = new Erro(TipoErro.COMENTARIO_NAO_FINALIZADO);
                    erro.setLexema("" + tempToken.trim());
                    erro.setLinha(lines.length +1);
                    listaErros.add(erro);
                }

            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
