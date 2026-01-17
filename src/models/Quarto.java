package models;

public class Quarto {
    private int id;
    private int numero;
    private int capacidade;
    private boolean estaOcupado;

    // Construtor
    public Quarto(int id, int numero, int capacidade, boolean estaOcupado) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.estaOcupado = estaOcupado;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public boolean isEstaOcupado() {
        return estaOcupado;
    }
}
