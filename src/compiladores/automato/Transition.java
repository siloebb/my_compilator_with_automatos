package compiladores.automato;

import compiladores.enums.Primitiva;
import java.util.Objects;

/**
 *
 * @author Siloe
 */
public class Transition {

    private State origin;
    private State destiny;
    private String symbol;
    private Primitiva symbolPrimitiva;
    private boolean consume = true;

    public Transition(State origin, State destiny) {
        this.origin = origin;
        this.destiny = destiny;
    }

    public Transition(State origin, State destiny, String symbol, boolean consume) {
        this.origin = origin;
        this.destiny = destiny;
        this.symbol = symbol;
        this.consume = consume;
    }
    
    public Transition(State origin, State destiny, Primitiva symbolPrimitiva, boolean consume) {
        this.origin = origin;
        this.destiny = destiny;
        this.symbolPrimitiva = symbolPrimitiva;
        this.consume = consume;
    }

    public State getOrigin() {
        return origin;
    }

    public void setOrigin(State origin) {
        this.origin = origin;
    }

    public State getDestiny() {
        return destiny;
    }

    public void setDestiny(State destiny) {
        this.destiny = destiny;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public Primitiva getSymbolPrimitiva() {
        return symbolPrimitiva;
    }

    public void setSymbol(Primitiva symbolPrimitiva) {
        this.symbolPrimitiva = symbolPrimitiva;
    }

    public boolean isConsume() {
        return consume;
    }

    public void setConsume(boolean consume) {
        this.consume = consume;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public String toString() {
        return "Transition{" + "origin=" + origin + ", destiny=" + destiny + ", symbol=" + symbol + ", symbolPrimitiva=" + symbolPrimitiva + ", consume=" + consume + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transition other = (Transition) obj;
        if (!Objects.equals(this.origin, other.origin)) {
            return false;
        }
        if (!Objects.equals(this.destiny, other.destiny)) {
            return false;
        }
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        if (this.symbolPrimitiva != other.symbolPrimitiva) {
            return false;
        }
        if (this.consume != other.consume) {
            return false;
        }
        return true;
    }

}
