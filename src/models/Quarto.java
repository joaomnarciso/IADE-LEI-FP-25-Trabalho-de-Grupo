package models;

public class Quarto {
    public int id;
    public int numero;
    public int capacidade;
    public boolean estaOcupado;

    // Construtor da Classe
    public Quarto(int _id, int _numero, int _capacidade, boolean _estaOcupado) {
        this.id = _id;
        this.numero = _numero;
        this.capacidade = _capacidade;
        this.estaOcupado = _estaOcupado;
    }
}
