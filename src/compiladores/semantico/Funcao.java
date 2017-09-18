
package compiladores.semantico;

import java.util.ArrayList;

/**
 *
 * @author Siloé
 */
public class Funcao {
    
    private String nome;
    private String tipoRetorno;
    private ArrayList<CampoRegistro> listaParametro;

    public Funcao(String nome, String tipoRetorno) {
        this.nome = nome;
        this.tipoRetorno = tipoRetorno;
    }

    public Funcao(String nome, String tipoRetorno, ArrayList<CampoRegistro> listaParametro) {
        this.nome = nome;
        this.tipoRetorno = tipoRetorno;
        this.listaParametro = listaParametro;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(String tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public ArrayList<CampoRegistro> getListaParametro() {
        return listaParametro;
    }

    public void setListaParametro(ArrayList<CampoRegistro> listaParametro) {
        this.listaParametro = listaParametro;
    }
    
}
