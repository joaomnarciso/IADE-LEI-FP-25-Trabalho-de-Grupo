package models;

public class Hospede {
    private int id;
    private String nome;
    private String documento;

    public Hospede(int id, String nome, String documento) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}