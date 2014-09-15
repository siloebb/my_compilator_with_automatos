/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.utils;

import compiladores.Erro;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Siloe
 */
public class ErroTableModel extends AbstractTableModel {

    String[] colunas = {"Tipo Erro", "Lexema", "Linha"};
    ArrayList<Erro> listaErro;

    public ErroTableModel(ArrayList<Erro> listaTokens) {
        this.listaErro = listaTokens;
    }

    @Override
    public int getRowCount() {
        return listaErro.size();
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
                return listaErro.get(rowIndex).getTipoErro();
            case 1:
                return listaErro.get(rowIndex).getLexema();
            case 2:
                return listaErro.get(rowIndex).getLinha();
            default:
                return "";
        }

    }

}
