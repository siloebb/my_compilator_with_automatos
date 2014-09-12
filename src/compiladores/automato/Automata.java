package compiladores.automato;

import compiladores.Erro;
import compiladores.Token;
import compiladores.enums.Primitiva;
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

    public void setTransition(int origin, int destiny, Primitiva symbol) {
        State stateOrigin = states.get(origin);
        State stateDestiny = states.get(destiny);
        Transition t = new Transition(stateOrigin, stateDestiny);
        t.setSymbol(symbol);

        transitions.add(t);
    }

    public Transition getTransition(int origin, String symbol) {
        State stateOrigin = states.get(origin);
        for (Transition transition : transitions) {
            try {
                if ((transition.getOrigin().equals(stateOrigin)) && (transition.getSymbol().equals(symbol))) {
                    return transition;
                }
            } catch (NullPointerException e) {
                return null;
            }
        }
        return null;
    }

    public Transition getTransition(int origin, Primitiva symbol) {
        State stateOrigin = states.get(origin);
        for (Transition transition : transitions) {
            if ((transition.getOrigin().equals(stateOrigin)) && (transition.getSymbolPrimitiva().equals(symbol))) {
                return transition;
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
            int i = 0;
            int origin = 0;

            String[] lines = texto.split("\n");
            for (int c = 0; lines.length > c; c++) {
                String linha = lines[c];

                while (i < linha.length()) {
                    //if (!(("" + linha.charAt(i)).equals(""))) {

                        char charAt = linha.charAt(i);

                        Transition transition = getTransition(origin, "" + charAt);
                        if (transition == null) {
                            Primitiva primitiva = Identificador.getPrimitiva(charAt);
                            transition = getTransition(origin, primitiva);
                        }

                        if (transition == null) {
                            throw new NoDestinyToSymbolException("Sem destino para o simbolo \'"
                                    + charAt + "\' no estado " + getState(origin).getLabel());
                        }

                        State destiny = transition.getDestiny();

                        if (destiny.getToken() != null) {
                            System.out.println("" + destiny.getToken());
                            destiny.getToken().setLexema("" + charAt);
                            destiny.getToken().setLinha(i);
                            listaTokens.add(destiny.getToken());
                        }
                        if (destiny.getErro() != null) {
                            destiny.getErro().setLinha(i);
                            listaErros.add(destiny.getErro());
                        }

                        origin = destiny.getId();

                        System.out.println("Leu o simbolo \" " + charAt + " \" foi para o "
                                + this.getState(origin).getLabel());

                    //}
                    i++;
                }
            }

            State ultimoState = finalStates.get(origin);
            if (ultimoState != null) {
                System.out.println("Terminou em um estado final");
            } else {
                System.out.println("Terminou em um estado não-final");
                throw new NoFinishiInFinalStateException("O automato não terminou em um estado final!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
