package compiladores.utils;

import compiladores.semantico.ErroSemantico;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Siloe
 */
public class ErroSemanticoTableModel extends AbstractTableModel {

    String[] colunas = {"Linha", "Erro"};
    ArrayList<ErroSemantico> listaErro;

    public ErroSemanticoTableModel(ArrayList<ErroSemantico> listaTokens) {
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
                return listaErro.get(rowIndex).getErro();
            default:
                return "";
        }
    }

}
