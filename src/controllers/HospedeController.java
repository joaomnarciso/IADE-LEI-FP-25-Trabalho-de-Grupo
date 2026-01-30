package controllers;

import models.Hospede;

import java.io.*;
import java.util.Scanner;

import validators.HospedeValidator;

public class HospedeController {
    private Hospede[] hospedes;
    private int totalHospedes;
    private int proximoId;
    private static final int MAX_HOSPEDES = 1000;
    private static final String FICHEIRO_HOSPEDES = "src/media/hospedes.csv";

    public HospedeController() {
        this.hospedes = new Hospede[MAX_HOSPEDES];
        this.totalHospedes = 0;
        this.proximoId = 1;
    }

    public void listarHospedes() {
        System.out.println("\n=== LISTA DE HÓSPEDES ===");
        if (totalHospedes == 0) {
            System.out.println("Não existem hóspedes registados.");
            return;
        }

        for (int i = 0; i < totalHospedes; i++) {
            Hospede h = hospedes[i];
            System.out.printf("ID: %d | Nome: %s | Documento: %s\n", h.getId(), h.getNome(), h.getDocumento());
        }
    }

    public void procurarPorDocumento(String documento) {
        System.out.println("\n=== PROCURAR HÓSPEDE ===");

        if (!HospedeValidator.validarDocumento(documento)) {
            System.out.println(HospedeValidator.obterMensagemErroDocumento());
            return;
        }

        Hospede hospede = encontrarHospedePorDocumento(documento.trim());

        if (hospede == null) {
            System.out.println("Hóspede não encontrado.");
        } else {
            System.out.printf("ID: %d | Nome: %s | Documento: %s\n",
                    hospede.getId(), hospede.getNome(), hospede.getDocumento());
        }
    }

    public void editarHospede(int id, Scanner scanner) {
        Hospede hospede = encontrarHospedePorId(id);

        if (hospede == null) {
            System.out.println("Hóspede não encontrado.");
            return;
        }

        System.out.println("\n=== EDITAR HÓSPEDE ===");
        System.out.println("Nome atual: " + hospede.getNome());
        System.out.print("Novo nome (deixe vazio para manter): ");
        String novoNome = scanner.nextLine();

        System.out.println("Documento atual: " + hospede.getDocumento());
        System.out.print("Novo documento (deixe vazio para manter): ");
        String novoDocumento = scanner.nextLine();

        if (!novoNome.trim().isEmpty()) {
            if (!HospedeValidator.validarNome(novoNome)) {
                System.out.println(HospedeValidator.obterMensagemErroNome());
                return;
            }
            hospede.setNome(novoNome.trim());
        }

        if (!novoDocumento.trim().isEmpty()) {
            if (!HospedeValidator.validarDocumento(novoDocumento)) {
                System.out.println(HospedeValidator.obterMensagemErroDocumento());
                return;
            }
            if (documentoDuplicado(novoDocumento.trim(), id)) {
                System.out.println(HospedeValidator.obterMensagemErroDocumentoDuplicado());
                return;
            }
            hospede.setDocumento(novoDocumento.trim());
        }

        System.out.println("Hóspede editado com sucesso!");
    }

    public Hospede criarHospede(String nome, String documento) {
        if (totalHospedes >= MAX_HOSPEDES) {
            System.out.println("Erro: Limite de hóspedes atingido.");
            return null;
        }

        if (!HospedeValidator.validarNome(nome)) {
            System.out.println(HospedeValidator.obterMensagemErroNome());
            return null;
        }

        if (!HospedeValidator.validarDocumento(documento)) {
            System.out.println(HospedeValidator.obterMensagemErroDocumento());
            return null;
        }

        if (documentoDuplicado(documento.trim(), -1)) {
            System.out.println(HospedeValidator.obterMensagemErroDocumentoDuplicado());
            return null;
        }

        Hospede novoHospede = new Hospede(proximoId++, nome.trim(), documento.trim());
        hospedes[totalHospedes++] = novoHospede;
        System.out.println("Hóspede criado com sucesso! ID: " + novoHospede.getId());

        return novoHospede;
    }

    public Hospede encontrarHospedePorId(int id) {
        for (int i = 0; i < totalHospedes; i++) {
            if (hospedes[i].getId() == id) {
                return hospedes[i];
            }
        }
        return null;
    }

    public Hospede encontrarHospedePorDocumento(String documento) {
        for (int i = 0; i < totalHospedes; i++) {
            if (hospedes[i].getDocumento().equals(documento)) {
                return hospedes[i];
            }
        }
        return null;
    }

    private boolean documentoDuplicado(String documento, int idExcluir) {
        for (int i = 0; i < totalHospedes; i++) {
            if (hospedes[i].getDocumento().equals(documento) && hospedes[i].getId() != idExcluir) {
                return true;
            }
        }
        return false;
    }

    public void carregarDados() {
        try {
            File ficheiro = new File(FICHEIRO_HOSPEDES);
            if (!ficheiro.exists()) {
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(ficheiro));
            String linha;
            totalHospedes = 0;
            int maiorId = 0;

            linha = reader.readLine();

            while ((linha = reader.readLine()) != null && totalHospedes < MAX_HOSPEDES) {
                String[] partes = linha.split(";", 3);
                if (partes.length == 3) {
                    int id = Integer.parseInt(partes[0].trim());
                    String nome = partes[1].trim();
                    String documento = partes[2].trim();

                    hospedes[totalHospedes++] = new Hospede(id, nome, documento);

                    if (id > maiorId) {
                        maiorId = id;
                    }
                }
            }

            proximoId = maiorId + 1;
            reader.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar hóspedes: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro no formato dos dados dos hóspedes: " + e.getMessage());
        }
    }

    public void guardarDados() {
        try {
            File diretorio = new File("src/media");
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FICHEIRO_HOSPEDES));

            writer.write("id;nome;documento\n");

            for (int i = 0; i < totalHospedes; i++) {
                Hospede h = hospedes[i];
                writer.write(String.format("%d;%s;%s\n",
                        h.getId(), h.getNome(), h.getDocumento()));
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao guardar hóspedes: " + e.getMessage());
        }
    }

}