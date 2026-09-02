package missao.tiopatinhas.model;

import java.time.LocalDateTime;

/**
 * Entidade Associativa que representa a relação Many-to-Many entre Usuario e Criptomoeda.
 * Um usuário pode ter várias criptomoedas favoritas, e uma criptomoeda pode ser favorita de vários usuários.
 */
public class Favorito {
    private int id; // PK
    private Usuario usuario; // FK
    private Criptomoeda criptomoeda; // FK
    private LocalDateTime dataAdicionado;

    public Favorito() {
    }

    public Favorito(int id, Usuario usuario, Criptomoeda criptomoeda, LocalDateTime dataAdicionado) {
        this.id = id;
        this.usuario = usuario;
        this.criptomoeda = criptomoeda;
        this.dataAdicionado = dataAdicionado;
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

    public Criptomoeda getCriptomoeda() {
        return criptomoeda;
    }

    public void setCriptomoeda(Criptomoeda criptomoeda) {
        this.criptomoeda = criptomoeda;
    }

    public LocalDateTime getDataAdicionado() {
        return dataAdicionado;
    }

    public void setDataAdicionado(LocalDateTime dataAdicionado) {
        this.dataAdicionado = dataAdicionado;
    }
}
