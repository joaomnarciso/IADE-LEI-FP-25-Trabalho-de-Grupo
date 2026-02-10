package validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReservaValidator {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean validarNumeroHospedes(int numeroHospedes, int capacidadeQuarto) {
        return numeroHospedes >= 1 && numeroHospedes <= capacidadeQuarto;
    }

    public static boolean validarFormatoData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }

        try {
            LocalDate.parse(data, FORMATO_DATA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validarOrdemDatas(String dataInicio, String dataFim) {
        try {
            LocalDate inicio = LocalDate.parse(dataInicio, FORMATO_DATA);
            LocalDate fim = LocalDate.parse(dataFim, FORMATO_DATA);
            return !inicio.isAfter(fim);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida se a data de fim é estritamente posterior à data de início (não podem ser iguais)
     */
    public static boolean validarDataFimAposDataInicio(String dataInicio, String dataFim) {
        try {
            LocalDate inicio = LocalDate.parse(dataInicio, FORMATO_DATA);
            LocalDate fim = LocalDate.parse(dataFim, FORMATO_DATA);
            return fim.isAfter(inicio);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validarDataNaoPassado(String data) {
        try {
            LocalDate dataReserva = LocalDate.parse(data, FORMATO_DATA);
            LocalDate hoje = LocalDate.now();
            return !dataReserva.isBefore(hoje);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validarDataFimFuturo(String dataFim) {
        try {
            LocalDate fim = LocalDate.parse(dataFim, FORMATO_DATA);
            LocalDate hoje = LocalDate.now();
            return fim.isAfter(hoje);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validarReservaPodeSerEditada(String dataFim) {
        try {
            LocalDate fim = LocalDate.parse(dataFim, FORMATO_DATA);
            LocalDate hoje = LocalDate.now();
            return !fim.isBefore(hoje);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean verificarSobreposicaoDatas(String dataInicio1, String dataFim1,
                                                     String dataInicio2, String dataFim2) {
        try {
            LocalDate inicio1 = LocalDate.parse(dataInicio1, FORMATO_DATA);
            LocalDate fim1 = LocalDate.parse(dataFim1, FORMATO_DATA);
            LocalDate inicio2 = LocalDate.parse(dataInicio2, FORMATO_DATA);
            LocalDate fim2 = LocalDate.parse(dataFim2, FORMATO_DATA);

            return !(fim1.isBefore(inicio2) || inicio1.isAfter(fim2));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean verificarReservaVigente(String dataInicio, String dataFim) {
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate inicio = LocalDate.parse(dataInicio, FORMATO_DATA);
            LocalDate fim = LocalDate.parse(dataFim, FORMATO_DATA);

            return !hoje.isBefore(inicio) && !hoje.isAfter(fim);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String obterMensagemErroNumeroHospedes(int capacidade) {
        return String.format("Número de hóspedes deve estar entre 1 e %d (capacidade do quarto).", capacidade);
    }

    public static String obterMensagemErroFormatoData() {
        return "Formato de data inválido. Use AAAA-MM-DD (exemplo: 2026-01-15).";
    }

    public static String obterMensagemErroOrdemDatas() {
        return "Data de início deve ser anterior ou igual à data de fim.";
    }

    public static String obterMensagemErroDatasIguais() {
        return "A data de fim deve ser posterior à data de início. As datas não podem ser iguais.";
    }

    public static String obterMensagemErroSobreposicao() {
        return "Já existe uma reserva ativa para este quarto no período especificado.";
    }

    public static String obterMensagemErroCapacidade() {
        return "Capacidade do quarto insuficiente para o número de hóspedes.";
    }

    public static String obterMensagemErroDataPassado() {
        return "A data de início não pode ser no passado. Deve ser hoje ou uma data futura.";
    }

    public static String obterMensagemErroDataFimPassado() {
        return "A data de fim não pode ser no passado. Deve ser uma data futura.";
    }

    public static String obterMensagemErroReservaJaTerminou() {
        return "Erro: Não é possível editar ou cancelar uma reserva que já terminou.";
    }
}