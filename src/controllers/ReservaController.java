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

        if (!ReservaValidator.validarDataFimAposDataInicio(dataInicio, dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroDatasIguais());
            return;
        }

        if (!ReservaValidator.validarDataNaoPassado(dataInicio)) {
            System.out.println(ReservaValidator.obterMensagemErroDataPassado());
            return;
        }

        if (!ReservaValidator.validarDataFimFuturo(dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroDataFimPassado());
            return;
        }

        Quarto quarto = encontrarMelhorQuartoDisponivel(numeroHospedes, dataInicio, dataFim, -1);

        if (quarto == null) {
            System.out.printf("Erro: Não há quartos disponíveis com capacidade para %d hóspedes no período selecionado.\n", numeroHospedes);
            return;
        }

        Reserva novaReserva = new Reserva(proximoId++, quarto.getId(), idHospede,
                numeroHospedes, dataInicio, dataFim);
        reservas[totalReservas++] = novaReserva;

        atualizarOcupacaoQuartos();

        System.out.println("Reserva criada com sucesso!");
        System.out.printf("ID da Reserva: %d | Quarto: %d (Capacidade: %d) | Hóspede: %s\n",
                novaReserva.getId(), quarto.getNumero(), quarto.getCapacidade(), hospede.getNome());
    }

    public void listarTodasReservas() {
        System.out.println("\n=== TODAS AS RESERVAS ===");
        if (totalReservas == 0) {
            System.out.println("Não existem reservas.");
            return;
        }

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];
            Quarto q = quartoController.encontrarQuartoPorId(r.getIdQuarto());
            Hospede h = hospedeController.encontrarHospedePorId(r.getIdHospede());

            System.out.printf("Nº Reserva: %d | Quarto: %d | Hóspede: %s | Pessoas: %d | %s a %s | Ativa: %s\n",
                    r.getId(), q != null ? q.getNumero() : 0,
                    h != null ? h.getNome() : "Desconhecido",
                    r.getNumeroHospedes(), r.getDataInicio(), r.getDataFim(),
                    r.getAtiva() ? "Sim" : "Não");
        }
    }

    public void listarReservasPorQuarto(int idQuarto) {
        System.out.println("\n=== RESERVAS DO QUARTO ===");

        int contadorReservas = 0;

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];
            if (r.getIdQuarto() == idQuarto) {
                Hospede h = hospedeController.encontrarHospedePorId(r.getIdHospede());
                System.out.printf("Nº Reserva: %d | Hóspede: %s | Pessoas: %d | %s a %s | Ativa: %s\n",
                        r.getId(), h != null ? h.getNome() : "Desconhecido",
                        r.getNumeroHospedes(), r.getDataInicio(), r.getDataFim(),
                        r.getAtiva() ? "Sim" : "Não");
                contadorReservas++;
            }
        }

        if (contadorReservas == 0) {
            System.out.println("Não existem reservas presentes ou futuras para este quarto.");
        }
    }

    public void listarReservasPorHospede(int idHospede) {
        System.out.println("\n=== RESERVAS DO HÓSPEDE ===");
        Hospede h = hospedeController.encontrarHospedePorId(idHospede);

        if (h == null) {
            System.out.println("Hóspede não encontrado.");
            return;
        }

        System.out.printf("Nº Hóspede: %d | Nome: %s | Documento: %s\n", h.getId(), h.getNome(), h.getDocumento());

        int numeroDeReservas = 0;

        for (int i = 0; i < totalReservas; i++) {
            Reserva r = reservas[i];
            if (r.getIdHospede() == idHospede) {
                Quarto q = quartoController.encontrarQuartoPorId(r.getIdQuarto());
                System.out.printf("Nº Reserva: %d | Quarto: %d | Pessoas: %d | %s a %s | Ativa: %s\n",
                        r.getId(), q != null ? q.getNumero() : 0,
                        r.getNumeroHospedes(), r.getDataInicio(), r.getDataFim(),
                        r.getAtiva() ? "Sim" : "Não");
                numeroDeReservas++;
            }
        }

        if (numeroDeReservas == 0) {
            System.out.println("Não existem reservas presentes ou futuras para este hóspede.");
        }
    }

    public void editarReserva(int id, Scanner scanner) {
        int indiceReserva = -1;
        for (int i = 0; i < totalReservas; i++) {
            if (reservas[i].getId() == id) {
                indiceReserva = i;
                break;
            }
        }

        if (indiceReserva == -1) {
            System.out.println("Reserva não encontrada.");
            return;
        }

        Reserva reserva = reservas[indiceReserva];

        if (!ReservaValidator.validarReservaPodeSerEditada(reserva.getDataFim())) {
            System.out.println(ReservaValidator.obterMensagemErroReservaJaTerminou());
            return;
        }

        System.out.println("\n=== EDITAR RESERVA ===");
        System.out.println("ID da Reserva: " + reserva.getId());

        Quarto quartoAtual = quartoController.encontrarQuartoPorId(reserva.getIdQuarto());
        if (quartoAtual != null) {
            System.out.println("Quarto atual: " + quartoAtual.getNumero() + " (Capacidade: " + quartoAtual.getCapacidade() + ")");
        }

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

        if (!novaDataInicio.isEmpty() && !ReservaValidator.validarFormatoData(dataInicio)) {
            System.out.println(ReservaValidator.obterMensagemErroFormatoData());
            return;
        }

        if (!novaDataFim.isEmpty() && !ReservaValidator.validarFormatoData(dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroFormatoData());
            return;
        }

        if (!ReservaValidator.validarOrdemDatas(dataInicio, dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroOrdemDatas());
            return;
        }

        if (!ReservaValidator.validarDataFimAposDataInicio(dataInicio, dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroDatasIguais());
            return;
        }

        if (!ReservaValidator.validarDataNaoPassado(dataInicio)) {
            System.out.println(ReservaValidator.obterMensagemErroDataPassado());
            return;
        }

        if (!ReservaValidator.validarDataFimFuturo(dataFim)) {
            System.out.println(ReservaValidator.obterMensagemErroDataFimPassado());
            return;
        }

        Quarto quartoFinal = encontrarMelhorQuartoDisponivel(numeroHospedes, dataInicio, dataFim, id);

        if (quartoFinal == null) {
            System.out.printf("Erro: Não há quartos disponíveis com capacidade para %d hóspedes no período selecionado.\n", numeroHospedes);
            return;
        }

        if (quartoAtual != null && quartoFinal.getId() != quartoAtual.getId()) {
            System.out.printf("Quarto alterado de %d para %d (Capacidade: %d)\n",
                    quartoAtual.getNumero(), quartoFinal.getNumero(), quartoFinal.getCapacidade());
        }

        reservas[indiceReserva].setIdQuarto(quartoFinal.getId());
        reservas[indiceReserva].setNumeroHospedes(numeroHospedes);
        reservas[indiceReserva].setDataInicio(dataInicio);
        reservas[indiceReserva].setDataFim(dataFim);

        atualizarOcupacaoQuartos();

        System.out.println("Reserva editada com sucesso!");
        System.out.printf("Novos valores - Quarto: %d (Capacidade: %d) | Pessoas: %d | %s a %s\n",
                quartoFinal.getNumero(),
                quartoFinal.getCapacidade(),
                reservas[indiceReserva].getNumeroHospedes(),
                reservas[indiceReserva].getDataInicio(),
                reservas[indiceReserva].getDataFim());
    }

    private Quarto encontrarMelhorQuartoDisponivel(int numeroHospedes, String dataInicio, String dataFim, int idReservaExcluir) {
        Quarto[] quartos = quartoController.getQuartos();
        int totalQuartos = quartoController.getTotalQuartos();

        Quarto melhorQuarto = null;
        int menorCapacidade = Integer.MAX_VALUE;

        for (int i = 0; i < totalQuartos; i++) {
            Quarto quarto = quartos[i];

            if (quarto.getCapacidade() >= numeroHospedes) {
                if (!verificarSobreposicao(quarto.getId(), dataInicio, dataFim, idReservaExcluir)) {
                    if (quarto.getCapacidade() < menorCapacidade) {
                        melhorQuarto = quarto;
                        menorCapacidade = quarto.getCapacidade();
                    }
                }
            }
        }

        return melhorQuarto;
    }

    public void cancelarReserva(int id) {
        Reserva reserva = encontrarReservaPorId(id);

        if (reserva == null) {
            System.out.println("Reserva não encontrada.");
            return;
        }

        if (!ReservaValidator.validarReservaPodeSerEditada(reserva.getDataFim())) {
            System.out.println(ReservaValidator.obterMensagemErroReservaJaTerminou());
            return;
        }

        for (int i = 0; i < totalReservas; i++) {
            if (reservas[i].getId() == id) {
                for (int j = i; j < totalReservas - 1; j++) {
                    reservas[j] = reservas[j + 1];
                }
                reservas[totalReservas - 1] = null;
                totalReservas--;
                break;
            }
        }

        atualizarOcupacaoQuartos();

        System.out.println("Reserva cancelada com sucesso!");
    }

    public Reserva encontrarReservaPorId(int id) {
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

            if (r.getAtiva()) {
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

    public int getTotalReservas() {
        return totalReservas;
    }

    public Reserva[] getReservas() {
        return reservas;
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
}