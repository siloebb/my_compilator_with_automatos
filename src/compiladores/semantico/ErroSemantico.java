package compiladores.semantico;

/**
 *
 * @author Siloé
 */
public class ErroSemantico {
        
    private TipoDeErroSemantico erro;
    private int linha;

    public ErroSemantico(TipoDeErroSemantico erro, int linha) {
        this.erro = erro;
        this.linha = linha;
    }
    
    public TipoDeErroSemantico getErro() {
        return erro;
    }

    public void setErro(TipoDeErroSemantico erro) {
        this.erro = erro;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
        
}
