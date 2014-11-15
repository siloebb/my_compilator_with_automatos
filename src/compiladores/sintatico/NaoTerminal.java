package compiladores.sintatico;

import compiladores.Token;
import java.util.ArrayList;

/**
 *
 * @author Siloé
 */
public class NaoTerminal {
    
    private String nome;
    private ExecutorNaoTerminal executor;

    public NaoTerminal(String nome, ExecutorNaoTerminal executor) {
        this.nome = nome;
        this.executor = executor;
    }
    
    public void executar(ArrayList<Token> listaTokens){
        executor.executarLeitura(listaTokens);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
        
}
