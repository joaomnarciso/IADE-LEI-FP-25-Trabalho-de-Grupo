package models;

import java.time.LocalDate;

public class Reserva {
    public int id;
    public int idQuarto;
    public int idHospede;
    public int numeroHospedes;
    public LocalDate dataInicio;
    public LocalDate dataFim;
    public boolean ativa;

    // Construtor da Classe
    public Reserva(int _id, int _idQuarto, int _idHospede, int _numeroHospedes, LocalDate _dataInicio,
            LocalDate _dataFim, boolean _ativa) {
        this.id = _id;
        this.idQuarto = _idQuarto;
        this.idHospede = _idHospede;
        this.numeroHospedes = _numeroHospedes;
        this.dataInicio = _dataInicio;
        this.dataFim = _dataFim;
        this.ativa = _ativa;
    }

}
