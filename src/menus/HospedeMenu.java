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
            System.out.println("1 - Listar hospedes");
            System.out.println("2 - Procurar hospede por documento");
            System.out.println("3 - Editar hospede");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            subMenuNumber = ReadOption();

            switch (subMenuNumber) {
                case 1:
                    hospedeController.listarHospedes();
                    break;
                case 2:
                    ProcurarPorDocumento();
                    break;
                case 3:
                    EditarHospede();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opcao invalida");
            }
        } while (subMenuNumber != 4);
    }

    private void ProcurarPorDocumento() {
        System.out.print("Documento do hóspede: ");
        String documento = scanner.nextLine();
        hospedeController.procurarPorDocumento(documento);
    }

    private void EditarHospede() {
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