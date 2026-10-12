package missao.tiopatinhas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String senhaHash;
    private LocalDateTime dataCriacao;

    private List<Carteira> carteiras;
    private List<Favorito> favoritos;

    public Usuario() {
        this.carteiras = new ArrayList<>();
        this.favoritos = new ArrayList<>();
    }

    public Usuario(
            int id,
            String nome,
            String cpf,
            String telefone,
            String email,
            String senhaHash,
            LocalDateTime dataCriacao
    ) {
        this();

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCriacao = dataCriacao;
    }

    public void adicionarCarteira(Carteira carteira) {
        carteiras.add(carteira);
        carteira.setUsuario(this);
    }

    public void exibirDados() {
        System.out.println("Usuário: " + nome);
    }

    public void exibirDados(boolean detalhado) {

        if (detalhado) {
            System.out.println("ID: " + id);
            System.out.println("Nome: " + nome);
            System.out.println("CPF: " + cpf);
            System.out.println("Telefone: " + telefone);
            System.out.println("Email: " + email);
        } else {
            exibirDados();
        }
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

    public List<Carteira> getCarteiras() {
        return carteiras;
    }

    public void setCarteiras(List<Carteira> carteiras) {
        this.carteiras = carteiras;
    }

    public List<Favorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Favorito> favoritos) {
        this.favoritos = favoritos;
    }
}