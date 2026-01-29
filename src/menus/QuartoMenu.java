package menus;

import controllers.QuartoController;
import java.util.Scanner;

public class QuartoMenu {
    private Scanner scanner;
    private QuartoController quartoController;

    public QuartoMenu(Scanner scanner, QuartoController quartoController) {
        this.scanner = scanner;
        this.quartoController = quartoController;
    }

    public void RoomMenu() {
        int subMenuNumber;
        do {
            System.out.println("\n=== MENU QUARTOS ===");
            System.out.println("1 - Listar todos os quartos");
            System.out.println("2 - Listar quartos livres");
            System.out.println("3 - Listar quartos ocupados");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            subMenuNumber = ReadOption();

            switch (subMenuNumber) {
                case 1:
                    quartoController.listarTodosQuartos();
                    break;
                case 2:
                    quartoController.listarQuartosLivres();
                    break;
                case 3:
                    quartoController.listarQuartosOcupados();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opcao invalida");
            }
        } while (subMenuNumber != 4);
    }

    private int ReadOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}