
package compiladores.sintatico;

import compiladores.Token;
import java.util.ArrayList;

/**
 *
 * @author Siloé
 */
public interface ExecutorNaoTerminal {
    
    public void executarLeitura(ArrayList<Token> listaTokens);
    
}
