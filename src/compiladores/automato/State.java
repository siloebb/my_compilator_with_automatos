/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiladores.automato;

import compiladores.Erro;
import compiladores.Token;
import java.util.Objects;

/**
 *
 * @author Siloe
 */
public class State {
    
    private int id;
    private String name;
    private String label;
    private Token token;
    private Erro erro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Erro getErro() {
        return erro;
    }

    public void setErro(Erro erro) {
        this.erro = erro;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.label);
        return hash;
    }
    
}
