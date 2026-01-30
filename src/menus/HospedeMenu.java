package menus;

import controllers.HospedeController;
import java.util.Scanner;

public class HospedeMenu {
    private Scanner scanner;
    private HospedeController hospedeController;

    public HospedeMenu(Scanner scanner, HospedeController hospedeController) {
        this.scanner = scanner;
        this.hospedeController = hospedeController;
    }

    public void GuestMenu() {
        int subMenuNumber;
        do {
            System.out.println("\n=== MENU HÓSPEDES ===");
            System.out.println("1 - Mostral todos os hóspedes");
            System.out.println("2 - Procurar hóspede por documento");
            System.out.println("3 - Alterar hóspede");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            subMenuNumber = ReadOption();

            switch (subMenuNumber) {
                case 1:
                    hospedeController.listarHospedes();
                    break;
                case 2:
                    procurarHospedePorDocumento();
                    break;
                case 3:
                    alterarHospede();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opção '"+subMenuNumber+"' inválida.");
            }
        } while (subMenuNumber != 4);
    }

    private void procurarHospedePorDocumento() {
        System.out.print("Documento do hóspede: ");
        String documento = scanner.nextLine();
        hospedeController.procurarPorDocumento(documento);
    }

    private void alterarHospede() {
        System.out.print("ID do hóspede: ");
        int idHospede = ReadOption();
        hospedeController.editarHospede(idHospede, scanner);
    }

    private int ReadOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}