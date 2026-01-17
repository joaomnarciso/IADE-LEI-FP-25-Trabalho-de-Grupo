package controllers;

import models.Quarto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controller para Quartos
 *
 */

public class QuartoController {

    private static final String FICHEIRO_CSV = "src/media/quartos.csv";
    private static final int MAX_QUARTOS = 200;

    private Quarto[] quartos;
    private int totalQuartos;

    public QuartoController() {
        this.quartos = new Quarto[MAX_QUARTOS];
        this.totalQuartos = 0;
    }

    /**
     * Carrega todos os quartos guardados no respectivo ficheiro CSV para o array quartos
     *
     */
    public void carregarQuartos() {
        totalQuartos = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_CSV))) {
            String linha;

            br.readLine();

            while ((linha = br.readLine()) != null && totalQuartos < MAX_QUARTOS) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");

                int id = Integer.parseInt(dados[0].trim());
                int numero = Integer.parseInt(dados[1].trim());
                int capacidade = Integer.parseInt(dados[2].trim());

                boolean estaOcupado = dados[3].trim().equals("1");

                Quarto quarto = new Quarto(id, numero, capacidade, estaOcupado);
                quartos[totalQuartos] = quarto;
                totalQuartos++;
            }

        } catch (IOException e) {
            System.out.println("Não foi possivel ler o ficheiro com os quartos: " + e.getMessage());
        }
    }

    public void todosQuartos() {
        if(quartos.length == 0){
            this.carregarQuartos();
        }
        if (totalQuartos == 0) {
            System.out.println("Não foram encontrados quartos. Por favor insira.");
            return;
        }

        System.out.println("Quartos:");
        for (int i = 0; i < totalQuartos; i++) {
            Quarto q = quartos[i];
            System.out.println("ID: " + q.getId() + " | Numero: " + q.getNumero()+" | Capacidade: " + q.getCapacidade() +" | Ocupado: " + q.isEstaOcupado());
        }
    }

    public void mostraQuartosDisponiveis() {
        if(quartos.length == 0){
            this.carregarQuartos();
        }
        if (totalQuartos == 0) {
            System.out.println("Não foram encontrados disponiveis.");
            return;
        }

        System.out.println("Quartos:");
        for (int i = 0; i < totalQuartos; i++) {
            Quarto q = quartos[i];

            if(!q.isEstaOcupado()) {
                System.out.println("ID: " + q.getId() + " | Numero: " + q.getNumero()+" | Capacidade: " + q.getCapacidade() +" | Ocupado: " + q.isEstaOcupado());
            }

        }
    }

    public void mostraQuartosOcupados() {
        if(quartos.length == 0){
            this.carregarQuartos();
        }
        if (totalQuartos == 0) {
            System.out.println("Não foram encontrados quartos ocupados.");
            return;
        }

        System.out.println("Quartos:");
        for (int i = 0; i < totalQuartos; i++) {
            Quarto q = quartos[i];

            if(q.isEstaOcupado()) {
                System.out.println("ID: " + q.getId() + " | Numero: " + q.getNumero()+" | Capacidade: " + q.getCapacidade() +" | Ocupado: " + q.isEstaOcupado());
            }

        }
    }

    public void mostrarQuartoPorNumero(int numero) {
        if(quartos[numero] == null){
            System.out.println("Não foi encontrado quarto com o número '"+numero+"'.");
            return;
        }
    }

    public int getTotalQuartos() {
        return totalQuartos;
    }

    public Quarto[] getQuartos() {
        return quartos;
    }

    public Quarto getQuartoPorIndice(int indice) {
        if (indice < 0 || indice >= totalQuartos) {
            return null;
        }
        return quartos[indice];
    }

    public Quarto getQuartoPorNumero(int numero) {
        for (int i = 0; i < totalQuartos; i++) {
            if (quartos[i].getNumero() == numero) {
                return quartos[i];
            }
        }
        return null;
    }
}
