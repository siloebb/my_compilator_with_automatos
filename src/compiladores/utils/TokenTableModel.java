/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.utils;

import compiladores.Token;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Siloe
 */
public class TokenTableModel extends AbstractTableModel {

    String[] colunas = {"Tipo Token", "Lexema", "Linha"};
    ArrayList<Token> listaTokens;

    public TokenTableModel(ArrayList<Token> listaTokens) {
        this.listaTokens = listaTokens;
    }

    @Override
    public int getRowCount() {
        return listaTokens.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return listaTokens.get(rowIndex).getTipoToken();
            case 1:
                return listaTokens.get(rowIndex).getLexema();
            case 2:
                return listaTokens.get(rowIndex).getLinha();
            default:
                return "";
        }

    }

}
