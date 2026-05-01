package missao.tiopatinhas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id; // PK
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String senhaHash;
    private LocalDateTime dataCriacao;
    private List<Carteira> carteiras; // One-to-Many: Um usuário pode ter várias carteiras
    private List<Favorito> favoritos; // Many-to-Many: Um usuário pode favoritar várias criptomoedas

    public Usuario() {
        this.carteiras = new ArrayList<>();
        this.favoritos = new ArrayList<>();
    }

    public Usuario(int id, String nome, String cpf, String telefone, String email, String senhaHash, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCriacao = dataCriacao;
        this.carteiras = new ArrayList<>();
        this.favoritos = new ArrayList<>();
    }

    public void adicionarCarteira(Carteira carteira) {
        this.carteiras.add(carteira);
        carteira.setUsuario(this);
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