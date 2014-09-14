/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiladores;

import compiladores.enums.TipoErro;
import compiladores.enums.TipoToken;
import java.util.Objects;

/**
 *
 * @author Siloe
 */
public class Erro {
    
    private TipoErro tipoErro;
    private String lexema;
    private int linha;

    public Erro(TipoErro tipoErro) {
        this.tipoErro = tipoErro;
    }

    public TipoErro getTipoErro() {
        return tipoErro;
    }

    public void setTipoErro(TipoErro tipoErro) {
        this.tipoErro = tipoErro;
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
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.tipoErro);
        hash = 23 * hash + Objects.hashCode(this.lexema);
        hash = 23 * hash + this.linha;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Erro other = (Erro) obj;
        if (this.tipoErro != other.tipoErro) {
            return false;
        }
        if (!Objects.equals(this.lexema, other.lexema)) {
            return false;
        }
        if (this.linha != other.linha) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Erro{" + "tipoErro=" + tipoErro + ", lexema=" + lexema + ", linha=" + linha + '}';
    }
    
}
