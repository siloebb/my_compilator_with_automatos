package compiladores.utils;

import compiladores.Erro;
import compiladores.sintatico.ErroSintatico;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Siloe
 */
public class ErroSintaticoTableModel extends AbstractTableModel {

    String[] colunas = {"Linha", "Encontrado", "Esperado"};
    ArrayList<ErroSintatico> listaErro;

    public ErroSintaticoTableModel(ArrayList<ErroSintatico> listaTokens) {
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
                return listaErro.get(rowIndex).getLinha();
            case 1:
                return listaErro.get(rowIndex).getEncontrado();
            case 2:
                return listaErro.get(rowIndex).getEsperado();
            default:
                return "";
        }

    }

}
