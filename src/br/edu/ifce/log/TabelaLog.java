/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifce.log;

import br.edu.ifce.thread.Crianca;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Eduardo
 */
public class TabelaLog extends AbstractTableModel{

    private List<Crianca> criancas = new ArrayList<>();
    private String[] colunas = {"ID", "NOME", "INICIOU COM BOLA", "TEMPO BRINCANDO", "TEMPO QUIETA"};
    
    @Override
    public String getColumnName(int coluna){
        return colunas[coluna];
    }
    
    @Override
    public int getRowCount() {
        return criancas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch(coluna){
            case 0:
                return criancas.get(linha).getIdCrianca();
            case 1:
                return criancas.get(linha).getNome();
            case 2:
                return criancas.get(linha).getIniciouComBola();
            case 3:
                return criancas.get(linha).getTempoDeBrincar();
            case 4:
                return criancas.get(linha).getTempoQuieta();
        }
        return null;
    }
    
    public void adicionarLinha(Crianca crianca){
        this.criancas.add(crianca);
        this.fireTableDataChanged();
    }
    
}
