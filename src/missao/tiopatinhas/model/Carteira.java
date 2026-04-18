package missao.tiopatinhas.model;

import java.util.ArrayList;
import java.util.List;

public class Carteira {
    private int id;
    private Usuario usuario;
    private double valorTotalInvestido;
    private double valorAtual;
    private double lucroPrejuizo;
    private List<Ativo> ativos;
    private List<Transacao> transacoes;
    private List<Aporte> aportes;

    public Carteira() {
        this.ativos = new ArrayList<>();
        this.transacoes = new ArrayList<>();
        this.aportes = new ArrayList<>();
    }

    public Carteira(int id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
        this.ativos = new ArrayList<>();
        this.transacoes = new ArrayList<>();
        this.aportes = new ArrayList<>();
    }

    public void adicionarAtivo(Ativo ativo) {
        ativos.add(ativo);
    }

    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public void adicionarAporte(Aporte aporte) {
        aportes.add(aporte);
    }

    public double calcularValorAtual() {
        double soma = 0;
        for (Ativo ativo : ativos) {
            soma += ativo.getValorAtual();
        }
        this.valorAtual = soma;
        return this.valorAtual;
    }

    public double calcularValorTotalInvestido() {
        double soma = 0;
        for (Ativo ativo : ativos) {
            soma += ativo.getValorInvestido();
        }
        this.valorTotalInvestido = soma;
        return this.valorTotalInvestido;
    }

    public double calcularLucroPrejuizo() {
        this.lucroPrejuizo = calcularValorAtual() - calcularValorTotalInvestido();
        return this.lucroPrejuizo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public double getValorTotalInvestido() {
        return valorTotalInvestido;
    }

    public void setValorTotalInvestido(double valorTotalInvestido) {
        this.valorTotalInvestido = valorTotalInvestido;
    }


    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }


    public double getLucroPrejuizo() {
        return lucroPrejuizo;
    }

    public void setLucroPrejuizo(double lucroPrejuizo) {
        this.lucroPrejuizo = lucroPrejuizo;
    }


    public List<Ativo> getAtivos() {
        return ativos;
    }

    public void setAtivos(List<Ativo> ativos) {
        this.ativos = ativos;
    }


    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }


    public List<Aporte> getAportes() {
        return aportes;
    }

    public void setAportes(List<Aporte> aportes) {
        this.aportes = aportes;
    }
}
