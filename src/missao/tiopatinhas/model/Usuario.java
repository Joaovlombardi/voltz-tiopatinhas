package missao.tiopatinhas.model;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String senhaHash;
    private LocalDateTime dataCriacao;
    private Carteira carteira;

    public Usuario() {
    }

    public Usuario(int id, String nome, String cpf, String telefone, String email, String senhaHash, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCriacao = dataCriacao;
    }

    public void vincularCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }


    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }


    public Carteira getCarteira() {
        return carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }
}