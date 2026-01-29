package models;

public class Quarto {
    private int id;
    private int numero;
    private int capacidade;
    private boolean estaOcupado;

    public Quarto(int id, int numero, int capacidade) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.estaOcupado = false;
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

    //estaOcupado... nao é muito intuitivo... faria mais sentido "ocupado" bool
    public boolean getOcupado() {
        return estaOcupado;
    }

    public void setOcupado(boolean ocupado) {
        this.estaOcupado = ocupado;
    }

}