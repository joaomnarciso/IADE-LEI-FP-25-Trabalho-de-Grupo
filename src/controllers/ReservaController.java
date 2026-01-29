package controllers;

import models.Quarto;
import models.Hospede;
import models.Reserva;
import validators.ReservaValidator;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ReservaController {
    private Reserva[] reservas;
    private int totalReservas;
    private int proximoId;
    private QuartoController quartoController;
    private HospedeController hospedeController;
    private static final int MAX_RESERVAS = 1000;
    private static final String FICHEIRO_RESERVAS = "src/media/reservas.csv";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReservaController() {
        this.reservas = new Reserva[MAX_RESERVAS];
        this.totalReservas = 0;
        this.proximoId = 1;
    }

    public void setControllers(QuartoController quartoController, HospedeController hospedeController) {
        this.quartoController = quartoController;
        this.hospedeController = hospedeController;
    }

    public void criarReserva(Scanner scanner) {
        System.out.println("\n=== CRIAR RESERVA ===");

        if (totalReservas >= MAX_RESERVAS) {
            System.out.println("Erro: Limite de reservas atingido.");
            return;
        }

        System.out.print("ID do hóspede: ");
        int idHospede = lerInteiro(scanner);

        Hospede hospede = hospedeController.encontrarHospedePorId(idHospede);
        if (hospede == null) {
            System.out.println("Erro: Hóspede não encontrado.");
            return;
        }

        System.out.print("Número de hóspedes: ");
        int numeroHospedes = lerInteiro(scanner);

        if (numeroHospedes < 1) {
            System.out.println("Erro: Número de hóspedes deve ser pelo menos 1.");
            return;
        }

        System.out.print("Data de início (YYYY-MM-DD): ");
        String dataInicio = scanner.nextLine().trim();

        System.out.print("Data de fim (YYYY-MM-DD): ");
        String dataFim = scanner.nextLine().trim();

        if (!ReservaValidator.validarFormatoData(dataInicio)) {
            System.out.println(ReservaValidator.obterMensagemErroFormatoData());
            return;
        }

        if (!ReservaValidator.validarFormatoData(dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroFormatoData());
            return;
        }

        if (!ReservaValidator.validarOrdemDatas(dataInicio, dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroOrdemDatas());
            return;
        }

        Quarto quarto = quartoController.encontrarQuartoLivreAdequado(numeroHospedes);

        if (quarto == null) {
            System.out.println("Erro: Não há quartos disponíveis com capacidade adequada.");
            return;
        }

        if (!ReservaValidator.validarNumeroHospedes(numeroHospedes, quarto.getCapacidade())) {
            System.out.println(ReservaValidator.obterMensagemErroNumeroHospedes(quarto.getCapacidade()));
            return;
        }

        if (verificarSobreposicao(quarto.getId(), dataInicio, dataFim, -1)) {
            System.out.println(ReservaValidator.obterMensagemErroSobreposicao());
            return;
        }

        Reserva novaReserva = new Reserva(proximoId++, quarto.getId(), idHospede,
                numeroHospedes, dataInicio, dataFim);
        reservas[totalReservas++] = novaReserva;

        atualizarOcupacaoQuartos();

        System.out.println("Reserva criada com sucesso!");
        System.out.printf("ID da Reserva: %d | Quarto: %d | Hóspede: %s\n",
                novaReserva.getId(), quarto.getNumero(), hospede.getNome());
    }

    public void listarTodasReservas() {
        System.out.println("\n=== TODAS AS RESERVAS ===");
        if (totalReservas == 0) {
            System.out.println("Não existem reservas registadas.");
            return;
        }

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];
            Quarto q = quartoController.encontrarQuartoPorId(r.getIdQuarto());
            Hospede h = hospedeController.encontrarHospedePorId(r.getIdHospede());

            System.out.printf("ID: %d | Quarto: %d | Hóspede: %s | Pessoas: %d | %s a %s | Ativa: %s\n",
                    r.getId(), q != null ? q.getNumero() : 0,
                    h != null ? h.getNome() : "Desconhecido",
                    r.getNumeroHospedes(), r.getDataInicio(), r.getDataFim(),
                    r.isAtiva() ? "Sim" : "Não");
        }
    }

    public void listarReservasPorQuarto(int idQuarto) {
        System.out.println("\n=== RESERVAS DO QUARTO ===");
        boolean encontrou = false;
        LocalDate hoje = LocalDate.now();

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];
            if (r.getIdQuarto() == idQuarto) {
                LocalDate dataFim = LocalDate.parse(r.getDataFim(), FORMATO_DATA);

                if (!dataFim.isBefore(hoje)) {
                    Hospede h = hospedeController.encontrarHospedePorId(r.getIdHospede());
                    System.out.printf("ID: %d | Hóspede: %s | Pessoas: %d | %s a %s | Ativa: %s\n",
                            r.getId(), h != null ? h.getNome() : "Desconhecido",
                            r.getNumeroHospedes(), r.getDataInicio(), r.getDataFim(),
                            r.isAtiva() ? "Sim" : "Não");
                    encontrou = true;
                }
            }
        }

        if (!encontrou) {
            System.out.println("Não existem reservas presentes ou futuras para este quarto.");
        }
    }

    public void listarReservasPorHospede(int idHospede) {
        System.out.println("\n=== RESERVAS DO HÓSPEDE ===");
        boolean encontrou = false;
        LocalDate hoje = LocalDate.now();

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];
            if (r.getIdHospede() == idHospede) {
                LocalDate dataFim = LocalDate.parse(r.getDataFim(), FORMATO_DATA);

                if (!dataFim.isBefore(hoje)) {
                    Quarto q = quartoController.encontrarQuartoPorId(r.getIdQuarto());
                    System.out.printf("ID: %d | Quarto: %d | Pessoas: %d | %s a %s | Ativa: %s\n",
                            r.getId(), q != null ? q.getNumero() : 0,
                            r.getNumeroHospedes(), r.getDataInicio(), r.getDataFim(),
                            r.isAtiva() ? "Sim" : "Não");
                    encontrou = true;
                }
            }
        }

        if (!encontrou) {
            System.out.println("Não existem reservas presentes ou futuras para este hóspede.");
        }
    }

    public void editarReserva(int id, Scanner scanner) {
        Reserva reserva = encontrarReservaPorId(id);

        if (reserva == null) {
            System.out.println("Reserva não encontrada.");
            return;
        }

        if (!reserva.isAtiva()) {
            System.out.println("Erro: Não é possível editar uma reserva que já passou (inativa).");
            return;
        }

        System.out.println("\n=== EDITAR RESERVA ===");
        System.out.println("Número de hóspedes atual: " + reserva.getNumeroHospedes());
        System.out.print("Novo número de hóspedes (0 para manter): ");
        int novoNumero = lerInteiro(scanner);

        System.out.println("Data início atual: " + reserva.getDataInicio());
        System.out.print("Nova data início (vazio para manter): ");
        String novaDataInicio = scanner.nextLine().trim();

        System.out.println("Data fim atual: " + reserva.getDataFim());
        System.out.print("Nova data fim (vazio para manter): ");
        String novaDataFim = scanner.nextLine().trim();

        String dataInicio = novaDataInicio.isEmpty() ? reserva.getDataInicio() : novaDataInicio;
        String dataFim = novaDataFim.isEmpty() ? reserva.getDataFim() : novaDataFim;
        int numeroHospedes = novoNumero == 0 ? reserva.getNumeroHospedes() : novoNumero;

        if (!ReservaValidator.validarFormatoData(dataInicio)) {
            System.out.println(ReservaValidator.obterMensagemErroFormatoData());
            return;
        }

        if (!ReservaValidator.validarFormatoData(dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroFormatoData());
            return;
        }

        if (!ReservaValidator.validarOrdemDatas(dataInicio, dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroOrdemDatas());
            return;
        }

        Quarto quarto = quartoController.encontrarQuartoPorId(reserva.getIdQuarto());

        if (!ReservaValidator.validarNumeroHospedes(numeroHospedes, quarto.getCapacidade())) {
            System.out.println(ReservaValidator.obterMensagemErroNumeroHospedes(quarto.getCapacidade()));
            return;
        }

        if (verificarSobreposicao(reserva.getIdQuarto(), dataInicio, dataFim, id)) {
            System.out.println(ReservaValidator.obterMensagemErroSobreposicao());
            return;
        }

        reserva.setNumeroHospedes(numeroHospedes);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFim(dataFim);

        atualizarOcupacaoQuartos();

        System.out.println("Reserva editada com sucesso!");
    }

    public void cancelarReserva(int id) {
        Reserva reserva = encontrarReservaPorId(id);

        if (reserva == null) {
            System.out.println("Reserva não encontrada.");
            return;
        }

        if (!reserva.isAtiva()) {
            System.out.println("Esta reserva já não está ativa (data já passou).");
            return;
        }

        for (int i = 0; i < totalReservas; i++) {
            if (reservas[i].getId() == id) {
                for (int j = i; j < totalReservas - 1; j++) {
                    reservas[j] = reservas[j + 1];
                }
                totalReservas--;
                break;
            }
        }

        atualizarOcupacaoQuartos();

        System.out.println("Reserva cancelada com sucesso!");
    }

    private Reserva encontrarReservaPorId(int id) {
        for (int i = 0; i < totalReservas; i++) {
            if (reservas[i].getId() == id) {
                return reservas[i];
            }
        }
        return null;
    }

    private boolean verificarSobreposicao(int idQuarto, String dataInicio, String dataFim, int idReservaExcluir) {
        LocalDate inicio = LocalDate.parse(dataInicio, FORMATO_DATA);
        LocalDate fim = LocalDate.parse(dataFim, FORMATO_DATA);

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];

            if (r.getIdQuarto() == idQuarto && r.getId() != idReservaExcluir) {
                LocalDate rInicio = LocalDate.parse(r.getDataInicio(), FORMATO_DATA);
                LocalDate rFim = LocalDate.parse(r.getDataFim(), FORMATO_DATA);

                if (!(fim.isBefore(rInicio) || inicio.isAfter(rFim))) {
                    return true;
                }
            }
        }

        return false;
    }

    private void atualizarOcupacaoQuartos() {
        for (int i = 0; i < quartoController.getTotalQuartos(); i++) {
            Quarto quarto = quartoController.getQuartos()[i];
            quarto.setOcupado(false);
        }

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];

            if (r.isAtiva()) {
                Quarto quarto = quartoController.encontrarQuartoPorId(r.getIdQuarto());
                if (quarto != null) {
                    quarto.setOcupado(true);
                }
            }
        }
    }

    private int lerInteiro(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void carregarDados() {
        try {
            File ficheiro = new File(FICHEIRO_RESERVAS);
            if (!ficheiro.exists()) {
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(ficheiro));
            String linha;
            totalReservas = 0;
            int maiorId = 0;

            linha = reader.readLine();

            while ((linha = reader.readLine()) != null && totalReservas < MAX_RESERVAS) {
                String[] partes = linha.split(";");
                if (partes.length >= 6) {
                    int id = Integer.parseInt(partes[0].trim());
                    int idQuarto = Integer.parseInt(partes[1].trim());
                    int idHospede = Integer.parseInt(partes[2].trim());
                    int numeroHospedes = Integer.parseInt(partes[3].trim());
                    String dataInicio = partes[4].trim();
                    String dataFim = partes[5].trim();

                    reservas[totalReservas++] = new Reserva(id, idQuarto, idHospede,
                            numeroHospedes, dataInicio, dataFim);

                    if (id > maiorId) {
                        maiorId = id;
                    }
                }
            }

            proximoId = maiorId + 1;
            reader.close();

            atualizarOcupacaoQuartos();
        } catch (IOException e) {
            System.out.println("Erro ao carregar reservas: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro no formato dos dados das reservas: " + e.getMessage());
        }
    }

    public void guardarDados() {
        try {
            File diretorio = new File("src/media");
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FICHEIRO_RESERVAS));

            writer.write("id;idQuarto;idHospede;numeroHospedes;dataInicio;dataFim\n");

            for (int i = 0; i < totalReservas; i++) {
                Reserva r = reservas[i];
                writer.write(String.format("%d;%d;%d;%d;%s;%s\n",
                        r.getId(), r.getIdQuarto(), r.getIdHospede(), r.getNumeroHospedes(),
                        r.getDataInicio(), r.getDataFim()));
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao guardar reservas: " + e.getMessage());
        }
    }

    public void debugReservas() {
        System.out.println("\n=== DEBUG RESERVAS ===");
        System.out.println("Total de reservas: " + totalReservas);
        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];
            System.out.printf("Reserva %d: Quarto=%d, Início=%s, Fim=%s, Ativa=%s\n",
                    r.getId(), r.getIdQuarto(), r.getDataInicio(), r.getDataFim(), r.isAtiva());
        }
    }
}