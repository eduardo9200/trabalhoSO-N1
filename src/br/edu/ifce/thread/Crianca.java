/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifce.thread;

import br.edu.ifce.view.TelaPrincipal;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo
 */
public class Crianca extends Thread {
    private TelaPrincipal telaPrincipal;
    private final long idCrianca;
    private final String nome;
    private final long tempoDeBrincar;
    private final long tempoQuieta;
    
    private final boolean iniciouComBola; //Apenas para mostrar na tabela de Crianças sem alterar seu valor
    private boolean tenho_bola;
    
    private Semaphore cesto_full;
    private Semaphore cesto_empty;
    private Semaphore mutex;
    
    /* CONSTRUTOR */
    public Crianca(
            TelaPrincipal tela,
            long id,
            String nome,
            boolean tenho_bola,
            long tb,
            long tq,
            Semaphore cesto_full,
            Semaphore cesto_empty,
            Semaphore mutex
    ) {
        this.telaPrincipal  = tela;
        this.idCrianca      = id;
        this.nome           = nome;
        this.tenho_bola     = tenho_bola;
        this.iniciouComBola = tenho_bola;
        this.tempoDeBrincar = tb;
        this.tempoQuieta    = tq;
        
        this.cesto_full     = cesto_full;
        this.cesto_empty    = cesto_empty;
        this.mutex          = mutex;
      } //Fim Construtor de crianças;
    
    @Override
    public void run() {
        while(true) {
            if(this.tenho_bola){
                try {
                    this.brincar(this.tempoDeBrincar);
                    
                    /* dorme se o cesto estiver cheio */
                    this.telaPrincipal.mostrarCriancaAguardandoVagaNoCesto(this.idCrianca);
                    this.telaPrincipal.logMessage(this.nome + " aguardando para colocar bola no cesto");
                    cesto_empty.acquire();
                    mutex.acquire();
                    this.devolverBolaParaCesto();
                    cesto_full.release();
                    mutex.release();
                    this.quieta(this.tempoQuieta);
                    
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException: Falha no método acquire");
                    Logger.getLogger(Crianca.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else { //se a criança não tem bola
                try {
                    /* dorme se não tiver bola no cesto */
                    this.telaPrincipal.mostrarCriancaAguardandoBola(this.idCrianca, this.nome);
                    this.telaPrincipal.logMessage(this.nome + " aguardando ter bola no cesto");
                    cesto_full.acquire();
                    mutex.acquire();
                    this.pegarBolaDoCesto();
                    cesto_empty.release();
                    mutex.release();
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException: Falha no método acquire");
                    Logger.getLogger(Crianca.class.getName()).log(Level.SEVERE, null, ex);
                }
            }    
        } //Fim while(true);
    }
    
    /**
     * Métodos da Thread Criança
     */
    
    /*
    */
    private long converteParaMillis(long unidadeDeTempo){
        return unidadeDeTempo * 1000;
    } //Fim converteParaMillis();
    
    /*
    */
    public void brincar(long unidadeDeTempo) {
        long start, finish, tempo;
        
        this.telaPrincipal.logMessage(this.nome + " está brincando");
        tempo = this.converteParaMillis(unidadeDeTempo);
        
        start = System.currentTimeMillis();
        do {
            this.telaPrincipal.mostrarCriancaBrincando_estado1(this.idCrianca, this.nome);
            
            finish = System.currentTimeMillis();
            
            this.telaPrincipal.mostrarCriancaBrincando_estado2(this.idCrianca, this.nome);
        } while((finish-start) <= tempo);
        
        this.telaPrincipal.logMessage(this.nome + " terminou de brincar");
    } //Fim brincar();
    
    
    /*
    */
    private void quieta(long unidadeDeTempo){
        long start, finish, tempo;
        
        this.telaPrincipal.logMessage("A criança " + this.nome + " está quieta");
        tempo = this.converteParaMillis(unidadeDeTempo);
        
        start = System.currentTimeMillis();
        do {
            this.telaPrincipal.mostrarCriancaQuieta_estado1(this.idCrianca, this.nome);
            
            finish = System.currentTimeMillis();
            
            this.telaPrincipal.mostrarCriancaQuieta_estado2(this.idCrianca, this.nome);
        
        } while((finish-start) <= tempo);
        
        this.telaPrincipal.logMessage("A criança " + this.nome + " cansou de ficar quieta");
    } //Fim quieta();
    
    
    /*
    */
    private void pegarBolaDoCesto(){
        this.tenho_bola = true;
        this.telaPrincipal.removerBolaDoCesto();
        this.telaPrincipal.logMessage(this.nome + " pegou uma bola do cesto");
    } //Fim pegarBolaDoCesto();
    
    
    /*
    */
    private void devolverBolaParaCesto(){
        this.tenho_bola = false;
        this.telaPrincipal.adicionarBolaNoCesto();
        this.telaPrincipal.logMessage(this.nome + " devolveu sua bola para o cesto");
    } //Fim devolverBolaParaCesto();
    
    
    /* Getters para a tabela de Crianças */
    public long getIdCrianca() {
        return idCrianca;
    }

    public String getNome() {
        return nome;
    }

    public long getTempoDeBrincar() {
        return tempoDeBrincar;
    }

    public long getTempoQuieta() {
        return tempoQuieta;
    }

    public String getIniciouComBola(){
        if(this.iniciouComBola) return "SIM";
        else return "NÃO";
    }
    
    public boolean getTenho_bola() {
        return tenho_bola;
    }
    
}
