
package compiladores.semantico;

import java.util.ArrayList;

/**
 *
 * @author Siloé
 */
public class RegistroDeclaracao {
    
    private String nome;
    private ArrayList<CampoRegistro> campos;

    public RegistroDeclaracao(String nome) {
        this.nome = nome;
        campos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<CampoRegistro> getCampo() {
        return campos;
    }

    public void setCampo(ArrayList<CampoRegistro> campo) {
        this.campos = campo;
    }
    
    public void addCampo(CampoRegistro campo) {
        this.campos.add(campo);
    }
    
}
