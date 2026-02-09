package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private int id;
    private int idQuarto;
    private int idHospede;
    private int numeroHospedes;
    private String dataInicio;
    private String dataFim;
    private Boolean ativa;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Reserva(int id, int idQuarto, int idHospede, int numeroHospedes, String dataInicio, String dataFim) {
        this.id = id;
        this.idQuarto = idQuarto;
        this.idHospede = idHospede;
        this.numeroHospedes = numeroHospedes;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(int idQuarto) {
        this.idQuarto = idQuarto;
    }

    public int getIdHospede() {
        return idHospede;
    }

    public void setIdHospede(int idHospede) {
        this.idHospede = idHospede;
    }

    public int getNumeroHospedes() {
        return numeroHospedes;
    }

    public void setNumeroHospedes(int numeroHospedes) {
        this.numeroHospedes = numeroHospedes;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public boolean getAtiva() {
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate inicio = LocalDate.parse(dataInicio, FORMATO_DATA);
            LocalDate fim = LocalDate.parse(dataFim, FORMATO_DATA);

            return !hoje.isBefore(inicio) && !hoje.isAfter(fim);
        } catch (Exception e) {
            return false;
        }
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}