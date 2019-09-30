/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifce.view;

import br.edu.ifce.log.TabelaLog;
import br.edu.ifce.thread.Crianca;
import static java.awt.Color.RED;
import java.util.concurrent.Semaphore;

// Imports para imagens
import java.awt.Dimension;
import javax.swing.*;

/**
 *
 * @author Eduardo
 */
public class TelaPrincipal extends javax.swing.JFrame {

    private long id = 1L;
    private final int capacidadeDoCesto;
    private final int maximaCapacidadeDoCesto = 10;
    private final int maximoNumeroDeCriancas = 10;
    private int quantidadeBolasCesto = 0;
    
    TabelaLog tabelaLog = new TabelaLog();    
    
    Semaphore cesto_full;
    Semaphore cesto_empty;
    Semaphore mutex;
    
    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();
        
        JOptionPane.showMessageDialog(null, "Bem-vindo à Brincadeira de Crianças! Vamos brincar? =D", "Seja bem-vindo!", JOptionPane.INFORMATION_MESSAGE);
        
        this.capacidadeDoCesto = this.lerCapacidadeDoCesto(this.maximaCapacidadeDoCesto);
        
        cesto_full = new Semaphore(0);
        cesto_empty = new Semaphore(this.capacidadeDoCesto);
        mutex = new Semaphore(1);
        
        this.TextField_ID.setText(Long.toString(id));
        this.table_log.setModel(tabelaLog);
        
        if(this.capacidadeDoCesto == 1)
            this.jLabelCapacidade_cesto.setText("Capacidade do Cesto = " + this.capacidadeDoCesto + " bola");
        else
            this.jLabelCapacidade_cesto.setText("Capacidade do Cesto = " + this.capacidadeDoCesto + " bolas");
        
        this.esconderCriancas();
        
        this.getContentPane().setBackground(new java.awt.Color(255, 255, 204));
        
    }
    
    
    /**
     * Faz a leitura da capacidade do cesto de bolas e valida o valor digitado pelo usuário
     */
    private int lerCapacidadeDoCesto(int maximaCapacidadeDoCesto){
        int k = 0;
        boolean valorInvalido = true;
        String capacidadeDoCesto;
        
        while(valorInvalido){
            try{    
                do{
                    capacidadeDoCesto = JOptionPane.showInputDialog(
                                            this,
                                            "Primeiramente, informe quantas bolas o cesto comporta? (máx. " + maximaCapacidadeDoCesto + ")",
                                            "Capacidade do Cesto",
                                            JOptionPane.QUESTION_MESSAGE
                                        );
                    
                    //Usuário clicou em cancelar ou fechar no diálogo da capacidade do cesto
                    if(capacidadeDoCesto==null){
                        int confirmacao = JOptionPane.showConfirmDialog(this, "Desejas fechar o programa?");  //Retorno showConfirmDialog: 0 = sim; 1 = não; 2 = cancelar; -1 = fechar;
                        if(confirmacao==0) System.exit(0); //Fecha o programa;
                        else
                            capacidadeDoCesto = JOptionPane.showInputDialog(
                                                    this,
                                                    "Informe quantas bolas o cesto comporta? (máx. " + maximaCapacidadeDoCesto + ")",
                                                    "Capacidade do Cesto",
                                                    JOptionPane.QUESTION_MESSAGE
                                                );
                    }
                    
                    //Converte o valor obtido da capacidade do cesto para inteiro
                    k = Integer.parseInt(capacidadeDoCesto);
                    
                    //Controle de quantidade de bolas do cesto
                    if(k<=0)
                        JOptionPane.showMessageDialog(this, "Atenção! O valor deve ser maior ou igual a 1.", "Atenção! Valor inválido.", JOptionPane.WARNING_MESSAGE);
                    else if(k > maximaCapacidadeDoCesto)
                        JOptionPane.showMessageDialog(this, "Atenção! O valor é maior do que a capacidade máxima do cesto.", "Atenção! Valor inválido.", JOptionPane.WARNING_MESSAGE);
                    
                } while (k<=0 || k > maximaCapacidadeDoCesto);
                
                valorInvalido = false;
                
            } catch(NumberFormatException e){
                JOptionPane.showMessageDialog(this, "Valor inválido! Não é um número! Tente novamente.", "Valor inválido", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.logMessage("Capacidade do Cesto = " + k + " bolas\n");
        return k;
    } //Fim lerCapacidadeDoCesto();
    
    
    public void adicionarBolaNoCesto(){
        this.quantidadeBolasCesto++;
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(this.quantidadeBolasCesto == this.capacidadeDoCesto)
            this.jLabelCesto.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "cesto_cheio.png")));
        else
            this.jLabelCesto.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "cesto_com_bola.png")));
        
        if(this.quantidadeBolasCesto==1)
            this.jLabelQuantidade_atual_cesto.setText("Qtd. Atual = " + this.quantidadeBolasCesto + " bola");
        else
            this.jLabelQuantidade_atual_cesto.setText("Qtd. Atual = " + this.quantidadeBolasCesto + " bolas");
    } //Fim adicionarBolaNoCesto();
    
    public void removerBolaDoCesto(){
        this.quantidadeBolasCesto--;
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(this.quantidadeBolasCesto == 0)
            this.jLabelCesto.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "cesto_sem_bola.png")));
        else
            this.jLabelCesto.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "cesto_com_bola.png")));
        
        if(this.quantidadeBolasCesto == 1)
            this.jLabelQuantidade_atual_cesto.setText("Qtd. Atual = " + this.quantidadeBolasCesto + " bola");
        else
            this.jLabelQuantidade_atual_cesto.setText("Qtd. Atual = " + this.quantidadeBolasCesto + " bolas");
    } //Fim removerBolaDoCesto();
    
    /**
     * Verifica se todos os campos do formulário estão preenchidos, antes de ser criada uma nova criança.
    */
    private boolean validaPreenchimentoTextField(){ //seta borda vermelha em campos vazios e mostra mensagem de campos vazios.
        int aux = 0;
        
        if(this.TextField_ID.getText().equals("")){
            this.TextField_ID.setBorder(BorderFactory.createLineBorder(RED, 1));
            aux++;
        }
        
        if(this.TextField_Nome.getText().equals("")){
            this.TextField_Nome.setBorder(BorderFactory.createLineBorder(RED, 1));
            aux++;
        }
        if(!this.RadioButton_SIM.isSelected() && !this.RadioButton_NAO.isSelected()){
            this.Label_Possui_bola.setBorder(BorderFactory.createLineBorder(RED, 1));
            aux++;
        }
        if(this.TextField_Tempo_brincando.getText().equals("")){
            this.TextField_Tempo_brincando.setBorder(BorderFactory.createLineBorder(RED, 1));
            aux++;
        }
        if(this.TextField_Tempo_quieta.getText().equals("")){
            this.TextField_Tempo_quieta.setBorder(BorderFactory.createLineBorder(RED, 1));
            aux++;
        }
        
        if(aux>0){
            this.Label_Campo_obrigatorio.setForeground(new java.awt.Color(255, 0, 0));
            this.Label_Campo_obrigatorio.setText(aux + " campos obrigatórios vazios");
            return false; //Falha
        }
        this.Label_Campo_obrigatorio.setText("0 campos obrigatórios vazios");
        return true; //Sucesso;
    } //Fim validaPreenchimentoTextField();
    
    
    private void esconderCriancas(){
        this.jLabelCrianca1.setVisible(false);
        this.jLabelCrianca2.setVisible(false);
        this.jLabelCrianca3.setVisible(false);
        this.jLabelCrianca4.setVisible(false);
        this.jLabelCrianca5.setVisible(false);
        this.jLabelCrianca6.setVisible(false);
        this.jLabelCrianca7.setVisible(false);
        this.jLabelCrianca8.setVisible(false);
        this.jLabelCrianca9.setVisible(false);
        this.jLabelCrianca10.setVisible(false);
    } //Fim esconderCriancas();
    
    /**
     * FUNÇÃO DE LAYOUT: seta as bordas dos campos de texto para cinza claro.
     */
    private void setBordaCinza(){
        
        java.awt.Color corCinza = new java.awt.Color(153,153,153);
        
        this.TextField_ID.setBorder(BorderFactory.createLineBorder(corCinza, 1));
        this.TextField_Nome.setBorder(BorderFactory.createLineBorder(corCinza, 1));
     
        //Exceção: seta a borda para amarelo-claro
        this.Label_Possui_bola.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255,255,204), 1));
     
        this.TextField_Tempo_brincando.setBorder(BorderFactory.createLineBorder(corCinza, 1));
        this.TextField_Tempo_quieta.setBorder(BorderFactory.createLineBorder(corCinza, 1));
        this.Label_Campo_obrigatorio.setForeground(new java.awt.Color(255, 255, 204));
    } //Fim setBordaCinza();
    
    private void bloquearAdicaoDeCrianca(){
        this.TextField_Nome.setEnabled(false);
        this.RadioButton_SIM.setEnabled(false);
        this.RadioButton_NAO.setEnabled(false);
        this.TextField_Tempo_brincando.setEnabled(false);
        this.TextField_Tempo_quieta.setEnabled(false);
        this.Button_Criar.setEnabled(false);
    } //Fim bloquearAdicaoDeCrianca();
    
    /**
     * Reseta o formulário de criação de uma nova criança, colocando um texto vazio nos campos.
    */
    private void resetForm(){
        this.id = this.id + 1;
        
        if(id > this.maximoNumeroDeCriancas){
            this.bloquearAdicaoDeCrianca();
            this.TextField_ID.setText("-");
            this.Label_Campo_obrigatorio.setForeground(new java.awt.Color(0, 0, 255));
            this.Label_Campo_obrigatorio.setText("Nº máximo de crianças atingido");
        } else {
            this.TextField_ID.setText(Long.toString(this.id));
        }
        
        this.TextField_Nome.setText("");
        this.buttonGroup_possuiBola.clearSelection();
        this.TextField_Tempo_brincando.setText("");
        this.TextField_Tempo_quieta.setText("");
    } //Fim resetForm();

    private void adicionarCrianca(long id, String nome, boolean possuiBola, long tempoBrincando, long tempoQuieta){
        
        Thread crianca = new Crianca(this, id, nome, possuiBola, tempoBrincando, tempoQuieta, cesto_full, cesto_empty, mutex);
        crianca.start();
        
        this.logMessage("\n***Criança " + nome + " criada***\n");
        this.tabelaLog.adicionarLinha((Crianca)crianca);
    } //Fim adicionarCrianca();
    
    public void logMessage(String message) {
        this.TextArea_Log.append(message + "\n");
        this.TextArea_Log.setCaretPosition(this.TextArea_Log.getText().length());
    } //Fim logMessage();
    
    public void mostrarCriancaBrincando_estado1(long id, String nome) {
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(id == 1L){
            this.jLabelCrianca1.setVisible(true);
            this.jLabelCrianca1.setText(nome);
            this.jLabelCrianca1.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 2L){
            this.jLabelCrianca2.setVisible(true);
            this.jLabelCrianca2.setText(nome);
            this.jLabelCrianca2.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 3L){
            this.jLabelCrianca3.setVisible(true);
            this.jLabelCrianca3.setText(nome);
            this.jLabelCrianca3.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 4L){
            this.jLabelCrianca4.setVisible(true);
            this.jLabelCrianca4.setText(nome);
            this.jLabelCrianca4.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 5L){
            this.jLabelCrianca5.setVisible(true);
            this.jLabelCrianca5.setText(nome);
            this.jLabelCrianca5.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 6L){
            this.jLabelCrianca6.setVisible(true);
            this.jLabelCrianca6.setText(nome);
            this.jLabelCrianca6.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 7L){
            this.jLabelCrianca7.setVisible(true);
            this.jLabelCrianca7.setText(nome);
            this.jLabelCrianca7.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 8L){
            this.jLabelCrianca8.setVisible(true);
            this.jLabelCrianca8.setText(nome);
            this.jLabelCrianca8.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 9L){
            this.jLabelCrianca9.setVisible(true);
            this.jLabelCrianca9.setText(nome);
            this.jLabelCrianca9.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        } else if(id == 10L){
            this.jLabelCrianca10.setVisible(true);
            this.jLabelCrianca10.setText(nome);
            this.jLabelCrianca10.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado1.png")));
        }
    } //Fim mostraCriancaBrincando_estado1();
    
    
    public void mostrarCriancaBrincando_estado2(long id, String nome) {
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(id == 1L){
            this.jLabelCrianca1.setVisible(true);
            this.jLabelCrianca1.setText(nome);
            this.jLabelCrianca1.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 2L){
            this.jLabelCrianca2.setVisible(true);
            this.jLabelCrianca2.setText(nome);
            this.jLabelCrianca2.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 3L){
            this.jLabelCrianca3.setVisible(true);
            this.jLabelCrianca3.setText(nome);
            this.jLabelCrianca3.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 4L){
            this.jLabelCrianca4.setVisible(true);
            this.jLabelCrianca4.setText(nome);
            this.jLabelCrianca4.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 5L){
            this.jLabelCrianca5.setVisible(true);
            this.jLabelCrianca5.setText(nome);
            this.jLabelCrianca5.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 6L){
            this.jLabelCrianca6.setVisible(true);
            this.jLabelCrianca6.setText(nome);
            this.jLabelCrianca6.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 7L){
            this.jLabelCrianca7.setVisible(true);
            this.jLabelCrianca7.setText(nome);
            this.jLabelCrianca7.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 8L){
            this.jLabelCrianca8.setVisible(true);
            this.jLabelCrianca8.setText(nome);
            this.jLabelCrianca8.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 9L){
            this.jLabelCrianca9.setVisible(true);
            this.jLabelCrianca9.setText(nome);
            this.jLabelCrianca9.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        } else if(id == 10L){
            this.jLabelCrianca10.setVisible(true);
            this.jLabelCrianca10.setText(nome);
            this.jLabelCrianca10.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_estado2.png")));
        }
    } //Fim mostraCriancaBrincando_estado2();
    
    
    public void mostrarCriancaQuieta_estado1(long id, String nome) {
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(id == 1L){
            this.jLabelCrianca1.setVisible(true);
            this.jLabelCrianca1.setText(nome);
            this.jLabelCrianca1.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 2L){
            this.jLabelCrianca2.setVisible(true);
            this.jLabelCrianca2.setText(nome);
            this.jLabelCrianca2.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 3L){
            this.jLabelCrianca3.setVisible(true);
            this.jLabelCrianca3.setText(nome);
            this.jLabelCrianca3.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 4L){
            this.jLabelCrianca4.setVisible(true);
            this.jLabelCrianca4.setText(nome);
            this.jLabelCrianca4.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 5L){
            this.jLabelCrianca5.setVisible(true);
            this.jLabelCrianca5.setText(nome);
            this.jLabelCrianca5.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 6L){
            this.jLabelCrianca6.setVisible(true);
            this.jLabelCrianca6.setText(nome);
            this.jLabelCrianca6.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 7L){
            this.jLabelCrianca7.setVisible(true);
            this.jLabelCrianca7.setText(nome);
            this.jLabelCrianca7.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 8L){
            this.jLabelCrianca8.setVisible(true);
            this.jLabelCrianca8.setText(nome);
            this.jLabelCrianca8.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 9L){
            this.jLabelCrianca9.setVisible(true);
            this.jLabelCrianca9.setText(nome);
            this.jLabelCrianca9.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        } else if(id == 10L){
            this.jLabelCrianca10.setVisible(true);
            this.jLabelCrianca10.setText(nome);
            this.jLabelCrianca10.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado1.png")));
        }
    } //Fim mostrarCriancaQuieta_estado1();
    
        
    public void mostrarCriancaQuieta_estado2(long id, String nome) {
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(id == 1L){
            this.jLabelCrianca1.setVisible(true);
            this.jLabelCrianca1.setText(nome);
            this.jLabelCrianca1.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 2L){
            this.jLabelCrianca2.setVisible(true);
            this.jLabelCrianca2.setText(nome);
            this.jLabelCrianca2.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 3L){
            this.jLabelCrianca3.setVisible(true);
            this.jLabelCrianca3.setText(nome);
            this.jLabelCrianca3.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 4L){
            this.jLabelCrianca4.setVisible(true);
            this.jLabelCrianca4.setText(nome);
            this.jLabelCrianca4.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 5L){
            this.jLabelCrianca5.setVisible(true);
            this.jLabelCrianca5.setText(nome);
            this.jLabelCrianca5.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 6L){
            this.jLabelCrianca6.setVisible(true);
            this.jLabelCrianca6.setText(nome);
            this.jLabelCrianca6.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 7L){
            this.jLabelCrianca7.setVisible(true);
            this.jLabelCrianca7.setText(nome);
            this.jLabelCrianca7.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 8L){
            this.jLabelCrianca8.setVisible(true);
            this.jLabelCrianca8.setText(nome);
            this.jLabelCrianca8.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 9L){
            this.jLabelCrianca9.setVisible(true);
            this.jLabelCrianca9.setText(nome);
            this.jLabelCrianca9.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        } else if(id == 10L){
            this.jLabelCrianca10.setVisible(true);
            this.jLabelCrianca10.setText(nome);
            this.jLabelCrianca10.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_quieta_estado2.png")));
        }
    } //Fim mostrarCriancaQuieta_estado2();
        
    
    public void mostrarCriancaAguardandoVagaNoCesto(long id) {
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(id == 1L){
            this.jLabelCrianca1.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 2L){
            this.jLabelCrianca2.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 3L){
            this.jLabelCrianca3.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 4L){
            this.jLabelCrianca4.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 5L){
            this.jLabelCrianca5.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 6L){
            this.jLabelCrianca6.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 7L){
            this.jLabelCrianca7.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 8L){
            this.jLabelCrianca8.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 9L){
            this.jLabelCrianca9.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        } else if(id == 10L){
            this.jLabelCrianca10.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_vaga.png")));
        }
    } //Fim mostrarCriancaAguardandoVagaNoCesto();
    
    
    public void mostrarCriancaAguardandoBola(long id, String nome) {
        String diretorio = "/br/edu/ifce/view/imagens/";
        
        if(id == 1L){
            this.jLabelCrianca1.setVisible(true);
            this.jLabelCrianca1.setText(nome);
            this.jLabelCrianca1.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 2L){
            this.jLabelCrianca2.setVisible(true);
            this.jLabelCrianca2.setText(nome);
            this.jLabelCrianca2.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 3L){
            this.jLabelCrianca3.setVisible(true);
            this.jLabelCrianca3.setText(nome);
            this.jLabelCrianca3.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 4L){
            this.jLabelCrianca4.setVisible(true);
            this.jLabelCrianca4.setText(nome);
            this.jLabelCrianca4.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 5L){
            this.jLabelCrianca5.setVisible(true);
            this.jLabelCrianca5.setText(nome);
            this.jLabelCrianca5.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 6L){
            this.jLabelCrianca6.setVisible(true);
            this.jLabelCrianca6.setText(nome);
            this.jLabelCrianca6.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 7L){
            this.jLabelCrianca7.setVisible(true);
            this.jLabelCrianca7.setText(nome);
            this.jLabelCrianca7.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 8L){
            this.jLabelCrianca8.setVisible(true);
            this.jLabelCrianca8.setText(nome);
            this.jLabelCrianca8.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 9L){
            this.jLabelCrianca9.setVisible(true);
            this.jLabelCrianca9.setText(nome);
            this.jLabelCrianca9.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        } else if(id == 10L){
            this.jLabelCrianca10.setVisible(true);
            this.jLabelCrianca10.setText(nome);
            this.jLabelCrianca10.setIcon(new javax.swing.ImageIcon(getClass().getResource(diretorio + "crianca_aguardando_bola.png")));
        }
    } //Fim mostrarCriancaAguardandoBola();
    
    private long getTempoBrincando(){
        long tempoBrincando;
        try{
            tempoBrincando = Long.parseLong(this.TextField_Tempo_brincando.getText());
            return tempoBrincando;
        } catch(NumberFormatException e){
            System.out.println("NumberFormatException: falha na conversão de valores do campo tempoBrincando.");
            JOptionPane.showMessageDialog(this, "Tempo Brincando só aceita números. Verifique se há espaços ou outro caractere que não seja um número", "ERRO", JOptionPane.ERROR_MESSAGE);
            return -1L;
        }
    } //Fim getTempoBrincando();
    
    private long getTempoQuieta(){
        long tempoQuieta;
        try{
            tempoQuieta = Long.parseLong(this.TextField_Tempo_quieta.getText());
            return tempoQuieta;
        } catch(NumberFormatException e){
            System.out.println("NumberFormatException: falha na conversão de valores do campo tempoQuieta.");
            JOptionPane.showMessageDialog(this, "Tempo Quieta só aceita números. Verifique se há espaços ou outro caractere que não seja um número", "ERRO", JOptionPane.ERROR_MESSAGE);
            return -1L;
        }
    } //Fim getTempoQuieta();
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup_possuiBola = new javax.swing.ButtonGroup();
        Label_titulo = new javax.swing.JLabel();
        Button_Sair = new javax.swing.JButton();
        jScrollPane_TelaPrincipal = new javax.swing.JScrollPane();
        jPanel_TelaPrincipal = new javax.swing.JPanel();
        Panel_ControleDeCriancas = new javax.swing.JPanel();
        Label_NovaCrianca = new javax.swing.JLabel();
        RadioButton_SIM = new javax.swing.JRadioButton();
        RadioButton_NAO = new javax.swing.JRadioButton();
        Label_Possui_bola = new javax.swing.JLabel();
        Label_Id = new javax.swing.JLabel();
        Label_Nome = new javax.swing.JLabel();
        TextField_ID = new javax.swing.JTextField();
        TextField_Nome = new javax.swing.JTextField();
        Label_Tempo_brincando = new javax.swing.JLabel();
        Separador = new javax.swing.JSeparator();
        Label_Tempo_quieto = new javax.swing.JLabel();
        TextField_Tempo_brincando = new javax.swing.JTextField();
        TextField_Tempo_quieta = new javax.swing.JTextField();
        Button_Criar = new javax.swing.JButton();
        Label_Campo_obrigatorio = new javax.swing.JLabel();
        painelImagemQuadra1 = new br.edu.ifce.view.PainelImagemQuadra();
        jLabelCrianca1 = new javax.swing.JLabel();
        jLabelCrianca2 = new javax.swing.JLabel();
        jLabelCrianca3 = new javax.swing.JLabel();
        jLabelCrianca4 = new javax.swing.JLabel();
        jLabelCrianca5 = new javax.swing.JLabel();
        jLabelCrianca6 = new javax.swing.JLabel();
        jLabelCrianca7 = new javax.swing.JLabel();
        jLabelCrianca8 = new javax.swing.JLabel();
        jLabelCrianca9 = new javax.swing.JLabel();
        jLabelCrianca10 = new javax.swing.JLabel();
        jPanel_Log = new javax.swing.JPanel();
        jScrollPane_Tabela_Log = new javax.swing.JScrollPane();
        table_log = new javax.swing.JTable();
        jScrollPane_TextArea_Log = new javax.swing.JScrollPane();
        TextArea_Log = new javax.swing.JTextArea();
        jPanel_Cesto = new javax.swing.JPanel();
        jLabelCapacidade_cesto = new javax.swing.JLabel();
        jLabelQuantidade_atual_cesto = new javax.swing.JLabel();
        jLabelCesto = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        menu_Descricao = new javax.swing.JMenu();
        menuItem_Descricao = new javax.swing.JMenuItem();
        menu_Sobre = new javax.swing.JMenu();
        menuItem_Sobre = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Brincadeira de Crianças");
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1272, 914));
        setMinimumSize(new java.awt.Dimension(1200, 500));
        setPreferredSize(new java.awt.Dimension(1272, 914));
        setSize(new java.awt.Dimension(1272, 914));

        Label_titulo.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        Label_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_titulo.setText("Brincadeira de Crianças");

        Button_Sair.setBackground(new java.awt.Color(255, 153, 153));
        Button_Sair.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Button_Sair.setText("Sair");
        Button_Sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_SairActionPerformed(evt);
            }
        });

        jPanel_TelaPrincipal.setBackground(new java.awt.Color(255, 255, 204));
        jPanel_TelaPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel_TelaPrincipal.setPreferredSize(new java.awt.Dimension(1246, 800));

        Panel_ControleDeCriancas.setBackground(new java.awt.Color(255, 255, 204));
        Panel_ControleDeCriancas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Panel_ControleDeCriancas.setToolTipText("Cria nova thread Criança");
        Panel_ControleDeCriancas.setName("teste"); // NOI18N

        Label_NovaCrianca.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        Label_NovaCrianca.setText("Nova Criança");

        RadioButton_SIM.setBackground(new java.awt.Color(255, 255, 204));
        buttonGroup_possuiBola.add(RadioButton_SIM);
        RadioButton_SIM.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        RadioButton_SIM.setText("Sim");

        RadioButton_NAO.setBackground(new java.awt.Color(255, 255, 204));
        buttonGroup_possuiBola.add(RadioButton_NAO);
        RadioButton_NAO.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        RadioButton_NAO.setText("Não");

        Label_Possui_bola.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Label_Possui_bola.setText("Possui bola?");
        Label_Possui_bola.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 204)));

        Label_Id.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Label_Id.setLabelFor(TextField_ID);
        Label_Id.setText("ID");

        Label_Nome.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Label_Nome.setLabelFor(TextField_Nome);
        Label_Nome.setText("Nome");

        TextField_ID.setEditable(false);
        TextField_ID.setBackground(new java.awt.Color(240, 255, 240));
        TextField_ID.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        TextField_ID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextField_ID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        TextField_Nome.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        TextField_Nome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        Label_Tempo_brincando.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Label_Tempo_brincando.setLabelFor(TextField_Tempo_brincando);
        Label_Tempo_brincando.setText("Tempo Brincando");

        Label_Tempo_quieto.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Label_Tempo_quieto.setLabelFor(TextField_Tempo_quieta);
        Label_Tempo_quieto.setText("Tempo Quieta");

        TextField_Tempo_brincando.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        TextField_Tempo_brincando.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextField_Tempo_brincando.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        TextField_Tempo_quieta.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        TextField_Tempo_quieta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextField_Tempo_quieta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        Button_Criar.setBackground(new java.awt.Color(153, 255, 204));
        Button_Criar.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        Button_Criar.setText("Criar");
        Button_Criar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_CriarActionPerformed(evt);
            }
        });

        Label_Campo_obrigatorio.setBackground(new java.awt.Color(255, 255, 204));
        Label_Campo_obrigatorio.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        Label_Campo_obrigatorio.setForeground(new java.awt.Color(255, 255, 204));
        Label_Campo_obrigatorio.setText("Campos obrigatórios vazios");

        javax.swing.GroupLayout Panel_ControleDeCriancasLayout = new javax.swing.GroupLayout(Panel_ControleDeCriancas);
        Panel_ControleDeCriancas.setLayout(Panel_ControleDeCriancasLayout);
        Panel_ControleDeCriancasLayout.setHorizontalGroup(
            Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ControleDeCriancasLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ControleDeCriancasLayout.createSequentialGroup()
                        .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel_ControleDeCriancasLayout.createSequentialGroup()
                                .addComponent(Label_Nome)
                                .addGap(23, 23, 23)
                                .addComponent(TextField_Nome, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Separador)
                                .addComponent(Label_NovaCrianca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Label_Tempo_quieto)
                                .addComponent(Label_Tempo_brincando)
                                .addGroup(Panel_ControleDeCriancasLayout.createSequentialGroup()
                                    .addComponent(Label_Id)
                                    .addGap(42, 42, 42)
                                    .addComponent(TextField_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(Panel_ControleDeCriancasLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(Label_Campo_obrigatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(TextField_Tempo_brincando, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(Panel_ControleDeCriancasLayout.createSequentialGroup()
                                    .addComponent(Label_Possui_bola)
                                    .addGap(18, 18, 18)
                                    .addComponent(RadioButton_SIM)
                                    .addGap(18, 18, 18)
                                    .addComponent(RadioButton_NAO))
                                .addComponent(TextField_Tempo_quieta, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ControleDeCriancasLayout.createSequentialGroup()
                        .addComponent(Button_Criar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95))))
        );
        Panel_ControleDeCriancasLayout.setVerticalGroup(
            Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ControleDeCriancasLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(Label_NovaCrianca, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Separador, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Id)
                    .addComponent(TextField_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Nome)
                    .addComponent(TextField_Nome, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton_SIM)
                    .addComponent(RadioButton_NAO)
                    .addComponent(Label_Possui_bola))
                .addGap(18, 18, 18)
                .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Tempo_brincando)
                    .addComponent(TextField_Tempo_brincando, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Panel_ControleDeCriancasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_Tempo_quieto)
                    .addComponent(TextField_Tempo_quieta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Label_Campo_obrigatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_Criar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        painelImagemQuadra1.setBackground(new java.awt.Color(255, 255, 204));
        painelImagemQuadra1.setMaximumSize(new java.awt.Dimension(900, 570));
        painelImagemQuadra1.setMinimumSize(new java.awt.Dimension(900, 570));

        jLabelCrianca1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca1.setText("Crianca1");

        jLabelCrianca2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca2.setText("Crianca2");

        jLabelCrianca3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca3.setText("Crianca3");

        jLabelCrianca4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca4.setText("Crianca4");

        jLabelCrianca5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca5.setText("Crianca5");

        jLabelCrianca6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca6.setText("Crianca6");

        jLabelCrianca7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca7.setText("Crianca7");

        jLabelCrianca8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca8.setText("Crianca8");

        jLabelCrianca9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca9.setText("Crianca9");

        jLabelCrianca10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/crianca_estado1.png"))); // NOI18N
        jLabelCrianca10.setText("Crianca10");

        javax.swing.GroupLayout painelImagemQuadra1Layout = new javax.swing.GroupLayout(painelImagemQuadra1);
        painelImagemQuadra1.setLayout(painelImagemQuadra1Layout);
        painelImagemQuadra1Layout.setHorizontalGroup(
            painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelImagemQuadra1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelImagemQuadra1Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(jLabelCrianca2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelCrianca6)
                        .addGap(129, 129, 129))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelImagemQuadra1Layout.createSequentialGroup()
                        .addGroup(painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(painelImagemQuadra1Layout.createSequentialGroup()
                                .addComponent(jLabelCrianca1)
                                .addGap(192, 192, 192)
                                .addComponent(jLabelCrianca3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelCrianca8))
                            .addGroup(painelImagemQuadra1Layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jLabelCrianca10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelCrianca9)
                                .addGap(157, 157, 157)
                                .addComponent(jLabelCrianca7)))
                        .addGap(7, 7, 7)))
                .addGroup(painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelImagemQuadra1Layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(jLabelCrianca4)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelImagemQuadra1Layout.createSequentialGroup()
                        .addComponent(jLabelCrianca5)
                        .addGap(146, 146, 146))))
        );
        painelImagemQuadra1Layout.setVerticalGroup(
            painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelImagemQuadra1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCrianca7)
                    .addComponent(jLabelCrianca9)
                    .addComponent(jLabelCrianca10))
                .addGap(35, 35, 35)
                .addGroup(painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelImagemQuadra1Layout.createSequentialGroup()
                        .addComponent(jLabelCrianca5)
                        .addGap(65, 65, 65)
                        .addGroup(painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCrianca4)
                            .addComponent(jLabelCrianca8)
                            .addComponent(jLabelCrianca3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelImagemQuadra1Layout.createSequentialGroup()
                        .addGroup(painelImagemQuadra1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCrianca2)
                            .addComponent(jLabelCrianca6))
                        .addGap(57, 57, 57)
                        .addComponent(jLabelCrianca1)))
                .addGap(56, 56, 56))
        );

        jPanel_Log.setBackground(new java.awt.Color(255, 255, 204));

        table_log.setBackground(new java.awt.Color(240, 255, 240));
        table_log.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table_log.setToolTipText("Lista de Crianças");
        jScrollPane_Tabela_Log.setViewportView(table_log);

        TextArea_Log.setEditable(false);
        TextArea_Log.setColumns(20);
        TextArea_Log.setRows(10);
        TextArea_Log.setToolTipText("Log");
        jScrollPane_TextArea_Log.setViewportView(TextArea_Log);

        javax.swing.GroupLayout jPanel_LogLayout = new javax.swing.GroupLayout(jPanel_Log);
        jPanel_Log.setLayout(jPanel_LogLayout);
        jPanel_LogLayout.setHorizontalGroup(
            jPanel_LogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_LogLayout.createSequentialGroup()
                .addComponent(jScrollPane_Tabela_Log, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane_TextArea_Log, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanel_LogLayout.setVerticalGroup(
            jPanel_LogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_Tabela_Log, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane_TextArea_Log, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
        );

        jPanel_Cesto.setBackground(new java.awt.Color(255, 255, 204));
        jPanel_Cesto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel_Cesto.setToolTipText("Cesto de Bolas");

        jLabelCapacidade_cesto.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabelCapacidade_cesto.setText("Capacidade do Cesto");

        jLabelQuantidade_atual_cesto.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jLabelQuantidade_atual_cesto.setText("Qtd. Atual = 0 bolas");

        jLabelCesto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/ifce/view/imagens/cesto_sem_bola.png"))); // NOI18N

        javax.swing.GroupLayout jPanel_CestoLayout = new javax.swing.GroupLayout(jPanel_Cesto);
        jPanel_Cesto.setLayout(jPanel_CestoLayout);
        jPanel_CestoLayout.setHorizontalGroup(
            jPanel_CestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_CestoLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel_CestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCapacidade_cesto)
                    .addComponent(jLabelQuantidade_atual_cesto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_CestoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelCesto)
                .addGap(51, 51, 51))
        );
        jPanel_CestoLayout.setVerticalGroup(
            jPanel_CestoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_CestoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelCapacidade_cesto)
                .addGap(18, 18, 18)
                .addComponent(jLabelQuantidade_atual_cesto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jLabelCesto)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jPanel_TelaPrincipalLayout = new javax.swing.GroupLayout(jPanel_TelaPrincipal);
        jPanel_TelaPrincipal.setLayout(jPanel_TelaPrincipalLayout);
        jPanel_TelaPrincipalLayout.setHorizontalGroup(
            jPanel_TelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TelaPrincipalLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel_TelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel_Log, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painelImagemQuadra1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel_TelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel_ControleDeCriancas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_Cesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel_TelaPrincipalLayout.setVerticalGroup(
            jPanel_TelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TelaPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_TelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_TelaPrincipalLayout.createSequentialGroup()
                        .addComponent(Panel_ControleDeCriancas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel_Cesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel_TelaPrincipalLayout.createSequentialGroup()
                        .addComponent(painelImagemQuadra1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel_Log, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );

        jScrollPane_TelaPrincipal.setViewportView(jPanel_TelaPrincipal);

        MenuBar.setBackground(new java.awt.Color(255, 255, 255));
        MenuBar.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N

        menu_Descricao.setText("Descrição");
        menu_Descricao.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N

        menuItem_Descricao.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        menuItem_Descricao.setText("Descição do Sistema");
        menuItem_Descricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_DescricaoActionPerformed(evt);
            }
        });
        menu_Descricao.add(menuItem_Descricao);

        MenuBar.add(menu_Descricao);

        menu_Sobre.setText("Sobre");
        menu_Sobre.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N

        menuItem_Sobre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        menuItem_Sobre.setText("Sobre este Software");
        menuItem_Sobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem_SobreActionPerformed(evt);
            }
        });
        menu_Sobre.add(menuItem_Sobre);

        MenuBar.add(menu_Sobre);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 1126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Button_Sair, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane_TelaPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label_titulo)
                    .addComponent(Button_Sair))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane_TelaPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Button_CriarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_CriarActionPerformed
        // TODO add your handling code here:
        this.setBordaCinza();
        
        if(this.validaPreenchimentoTextField()){ //Sucesso: nenhum campo vazio
 
            //Recebe os valores dos inputs da Tela
            long id             = Long.parseLong(this.TextField_ID.getText());
            String nome         = this.TextField_Nome.getText();

            long tempoBrincando = this.getTempoBrincando();
            long tempoQuieta    = this.getTempoQuieta();
            
            if((tempoBrincando == -1L) || (tempoQuieta == -1L)) return; //Falha na leitura do tempoBrincando e tempoQuieta
            
            boolean possuiBola  = false;
            if(this.RadioButton_SIM.isSelected())       possuiBola = true;
            else if(this.RadioButton_NAO.isSelected())  possuiBola = false;
            
            this.adicionarCrianca(id, nome, possuiBola, tempoBrincando, tempoQuieta);
            
            System.out.println(id);
            System.out.println(nome);
            System.out.println(tempoBrincando);
            System.out.println(tempoQuieta);
            System.out.println(possuiBola);
            
            
            //System.out.println("Thread criada");
            this.setBordaCinza();
            this.resetForm(); //Reseta os valores dos campos do formulário
        }
    }//GEN-LAST:event_Button_CriarActionPerformed

    private void Button_SairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_SairActionPerformed
        // TODO add your handling code here:
        int opcao = JOptionPane.showConfirmDialog(this, "Desejas realmente sair?", "Confirmação", JOptionPane.WARNING_MESSAGE);
        if(opcao==0) System.exit(0);
    }//GEN-LAST:event_Button_SairActionPerformed

    private void menuItem_DescricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_DescricaoActionPerformed
        // TODO add your handling code here:
         JOptionPane.showMessageDialog(
                this,
                "Projeto I - Problema Brincadeira de Crianças\n\n"
                + "Imagine N crianças que estão, a princípio, quietas. M (M<N) crianças inicialmente\n"
                + "possuem uma bola e as outras, não. De repente, sentem vontade de brincar com uma bola.\n"
                + "Com esse desejo incontrolável, as que já estão com a bola simplesmente brincam. As que\n"
                + "não têm bola correm ao cesto de bolas, que está inicialmente vazio e que suporta até K\n"
                + "bolas. Se o cesto possuir bolas, uma criança pega a bola e vai brincar feliz. Se o cesto\n"
                + "estiver vazio, ela fica esperando até que outra criança coloque uma bola no cesto. Quando\n"
                + "uma criança termina de brincar, ela tem que colocar a bola no cesto, mas se o cesto já\n"
                + "estiver cheio, ela segura a bola até que outra criança retire uma bola que já está no cesto, e\n"
                + "então solta sua bola no cesto e volta a ficar quieta. Admita que as crianças continuem\n"
                + "brincando e descansando(quieta) eternamente. Utilizando semáforos, modele esse\n"
                + "problema resolvendo os conflitos entre os N threads 'criança'.",
                "Descrição do Sistema",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_menuItem_DescricaoActionPerformed

    private void menuItem_SobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem_SobreActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(
                this,
                "Brincadeira de Crianças\n\n"
                        + "Equipe:\n * Eduardo Maia Santos\n * Carlos Augusto Benevides\n * Rômulo Alberto\n * Rodrigo Viana Castelo Branco\n\n"
                        + "Instituto Federal do Ceará - Campus Fortaleza\n"
                        + "Disciplina: Sistemas Operacionais 2019.2\nProfessor: Fernando Parente Garcia",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_menuItem_SobreActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_Criar;
    private javax.swing.JButton Button_Sair;
    private javax.swing.JLabel Label_Campo_obrigatorio;
    private javax.swing.JLabel Label_Id;
    private javax.swing.JLabel Label_Nome;
    private javax.swing.JLabel Label_NovaCrianca;
    private javax.swing.JLabel Label_Possui_bola;
    private javax.swing.JLabel Label_Tempo_brincando;
    private javax.swing.JLabel Label_Tempo_quieto;
    private javax.swing.JLabel Label_titulo;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JPanel Panel_ControleDeCriancas;
    private javax.swing.JRadioButton RadioButton_NAO;
    private javax.swing.JRadioButton RadioButton_SIM;
    private javax.swing.JSeparator Separador;
    private javax.swing.JTextArea TextArea_Log;
    private javax.swing.JTextField TextField_ID;
    private javax.swing.JTextField TextField_Nome;
    private javax.swing.JTextField TextField_Tempo_brincando;
    private javax.swing.JTextField TextField_Tempo_quieta;
    private javax.swing.ButtonGroup buttonGroup_possuiBola;
    private javax.swing.JLabel jLabelCapacidade_cesto;
    private javax.swing.JLabel jLabelCesto;
    private javax.swing.JLabel jLabelCrianca1;
    private javax.swing.JLabel jLabelCrianca10;
    private javax.swing.JLabel jLabelCrianca2;
    private javax.swing.JLabel jLabelCrianca3;
    private javax.swing.JLabel jLabelCrianca4;
    private javax.swing.JLabel jLabelCrianca5;
    private javax.swing.JLabel jLabelCrianca6;
    private javax.swing.JLabel jLabelCrianca7;
    private javax.swing.JLabel jLabelCrianca8;
    private javax.swing.JLabel jLabelCrianca9;
    private javax.swing.JLabel jLabelQuantidade_atual_cesto;
    private javax.swing.JPanel jPanel_Cesto;
    private javax.swing.JPanel jPanel_Log;
    private javax.swing.JPanel jPanel_TelaPrincipal;
    private javax.swing.JScrollPane jScrollPane_Tabela_Log;
    private javax.swing.JScrollPane jScrollPane_TelaPrincipal;
    private javax.swing.JScrollPane jScrollPane_TextArea_Log;
    private javax.swing.JMenuItem menuItem_Descricao;
    private javax.swing.JMenuItem menuItem_Sobre;
    private javax.swing.JMenu menu_Descricao;
    private javax.swing.JMenu menu_Sobre;
    private br.edu.ifce.view.PainelImagemQuadra painelImagemQuadra1;
    private javax.swing.JTable table_log;
    // End of variables declaration//GEN-END:variables
}
