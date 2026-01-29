package controllers;

import models.Quarto;

import java.io.*;

public class QuartoController {
    private Quarto[] quartos;
    private int totalQuartos;
    private static final int MAX_QUARTOS = 200;
    private static final String FICHEIRO = "src/media/quartos.csv";

    public QuartoController() {
        this.quartos = new Quarto[MAX_QUARTOS];
        this.totalQuartos = 0;
    }

    public void listarTodosQuartos() {
        System.out.println("\n=== TODOS OS QUARTOS ===");
        if (totalQuartos == 0) {
            System.out.printf("Não foram encontrados quartos no ficheiro '%s'.\n", FICHEIRO);
            return;
        }

        for (int i = 0; i < totalQuartos; i++) {
            Quarto q = quartos[i];
            System.out.printf("ID: %d | Número: %d | Capacidade: %d | Ocupado: %s\n",
                    q.getId(), q.getNumero(), q.getCapacidade(),
                    q.getOcupado() ? "Sim" : "Não");
        }
    }

    public void listarQuartosLivres() {
        System.out.println("\n=== QUARTOS LIVRES ===");
        int quartosLivres = 0;

        for (int i = 0; i < totalQuartos; i++) {
            Quarto q = quartos[i];
            if (!q.getOcupado()) {
                System.out.printf("ID: %d | Número: %d | Capacidade: %d\n",
                        q.getId(), q.getNumero(), q.getCapacidade());
                quartosLivres++;
            }
        }

        if (quartosLivres == 0) {
            System.out.println("Não existem quartos livres.");
        }
    }

    public void listarQuartosOcupados() {
        System.out.println("\n=== QUARTOS OCUPADOS ===");
        int quartosOcupados = 0;

        for (int i = 0; i < totalQuartos; i++) {
            Quarto q = quartos[i];
            if (q.getOcupado()) {
                System.out.printf("ID: %d | Número: %d | Capacidade: %d\n",
                        q.getId(), q.getNumero(), q.getCapacidade());
                quartosOcupados++;
            }
        }

        if (quartosOcupados == 0) {
            System.out.println("Não existem quartos ocupados.");
        }
    }

    public void listarQuartoEspecifico(int id) {
        Quarto quarto = encontrarQuartoPorId(id);

        if (quarto == null) {
            System.out.println("Quarto não encontrado.");
            return;
        }

        System.out.println("\n=== DETALHES DO QUARTO ===");
        System.out.printf("ID: %d | Número: %d | Capacidade: %d | Ocupado: %s\n",
                quarto.getId(), quarto.getNumero(), quarto.getCapacidade(),
                quarto.getOcupado() ? "Sim" : "Não");
    }

    public Quarto encontrarQuartoPorId(int id) {
        for (int i = 0; i < totalQuartos; i++) {
            if (quartos[i].getId() == id) {
                return quartos[i];
            }
        }
        return null;
    }

    public Quarto encontrarQuartoLivreAdequado(int numeroHospedes) {
        Quarto melhorQuarto = null;
        int menorDiferenca = Integer.MAX_VALUE;

        for (int i = 0; i < totalQuartos; i++) {
            Quarto q = quartos[i];
            if (!q.getOcupado() && q.getCapacidade() >= numeroHospedes) {
                int diferenca = q.getCapacidade() - numeroHospedes;
                if (diferenca < menorDiferenca) {
                    menorDiferenca = diferenca;
                    melhorQuarto = q;
                }
            }
        }

        return melhorQuarto;
    }

    public void atualizarOcupacao(int idQuarto, boolean ocupado) {
        Quarto quarto = encontrarQuartoPorId(idQuarto);
        if (quarto != null) {
            quarto.setOcupado(ocupado);
        }
    }

    public void carregarDados() {
        try {
            File ficheiro = new File(FICHEIRO);
            if (!ficheiro.exists()) {
                System.out.printf("O sistema não encontrou o ficheiro '%s'.\n", FICHEIRO);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(ficheiro));
            String linha;
            totalQuartos = 0;

            linha = reader.readLine();

            while ((linha = reader.readLine()) != null && totalQuartos < MAX_QUARTOS) {
                String[] partes = linha.split(";");
                if (partes.length >= 3) {
                    int id = Integer.parseInt(partes[0].trim());
                    int numero = Integer.parseInt(partes[1].trim());
                    int capacidade = Integer.parseInt(partes[2].trim());

                    quartos[totalQuartos++] = new Quarto(id, numero, capacidade);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar quartos no array: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro no formato dos dados dos quartos: " + e.getMessage());
        }
    }

    public Quarto[] getQuartos() {
        return quartos;
    }

    public int getTotalQuartos() {
        return totalQuartos;
    }
}