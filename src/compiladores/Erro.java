/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiladores;

/**
 *
 * @author Siloe
 */
public class Erro {
    
    public final static String nomeErro = "Erro léxico lançado: Da letra";
    public int linha;

    public static String getNomeErro() {
        return nomeErro;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    @Override
    public String toString() {
        return nomeErro+" {" + "linha=" + linha + '}';
    }
        
}
