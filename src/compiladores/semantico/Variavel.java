
package compiladores.semantico;

import java.util.ArrayList;

/**
 *
 * @author Siloé
 */
public class Variavel {
    
    private String nome;
    private String tipo;
    private String escopoFuncao;
    private boolean constante = false;
    private boolean registro = false;
    private String nomeRegistro;

    public Variavel(String nome, String tipo, String escopoFuncao) {
        this.nome = nome;
        this.tipo = tipo;
        this.escopoFuncao = escopoFuncao;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEscopoFuncao() {
        return escopoFuncao;
    }

    public void setEscopoFuncao(String escopoFuncao) {
        this.escopoFuncao = escopoFuncao;
    }

    public boolean isConstante() {
        return constante;
    }

    public void setConstante(boolean constante) {
        this.constante = constante;
    }

    public boolean isRegistro() {
        return registro;
    }

    public void setRegistro(boolean registro) {
        this.registro = registro;
    }

    public String getNomeRegistro() {
        return nomeRegistro;
    }

    public void setNomeRegistro(String nomeRegistro) {
        this.nomeRegistro = nomeRegistro;
    }

    @Override
    public String toString() {
        return "Variavel{" + "nome=" + nome + ", tipo=" + tipo + ", escopoFuncao=" + escopoFuncao + ", constante=" + constante + ", registro=" + registro + ", nomeRegistro=" + nomeRegistro + '}';
    }
    
}
