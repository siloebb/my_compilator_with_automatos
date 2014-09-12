
package compiladores;

import compiladores.enums.TipoToken;

/**
 *
 * @author Siloe
 */
public class Token {
    
    private TipoToken tipoToken;
    private String lexema;
    private int linha;

    public Token(TipoToken tipoToken) {
        this.tipoToken = tipoToken;
    }    

    public TipoToken getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(TipoToken tipoToken) {
        this.tipoToken = tipoToken;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    @Override
    public String toString() {
        return "Token{" + "tipoToken=" + tipoToken + ", lexema=" + lexema + ", linha=" + linha + '}';
    }
    
}
