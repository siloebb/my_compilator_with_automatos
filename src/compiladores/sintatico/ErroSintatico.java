
package compiladores.sintatico;

/**
 *
 * @author Siloé
 */
public class ErroSintatico {
    
    private int linha;
    private String encontrado;
    private String esperado;

    public ErroSintatico(int linha, String encontrado, String esperado) {
        this.linha = linha;
        this.encontrado = encontrado;
        this.esperado = esperado;
    }
        
    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public String getEncontrado() {
        return encontrado;
    }

    public void setEncontrado(String encontrado) {
        this.encontrado = encontrado;
    }

    public String getEsperado() {
        return esperado;
    }

    public void setEsperado(String esperado) {
        this.esperado = esperado;
    }
    
}
