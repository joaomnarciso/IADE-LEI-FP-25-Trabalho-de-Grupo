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
            System.out.println("1 - Mostrar todos os quartos");
            System.out.println("2 - Mostrar todos quartos livres");
            System.out.println("3 - Mostrar todos quartos ocupados");
            System.out.println("4 - Mostrar quarto por número");
            System.out.println("5 - Sair");
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
                    mostrarQuartoEspecifico();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opção '"+subMenuNumber+"' inválida.");
            }
        } while (subMenuNumber != 5);
    }

    private void mostrarQuartoEspecifico() {
        System.out.print("ID do quarto: ");
        int idQuarto = ReadOption();
        quartoController.listarQuartoEspecifico(idQuarto);
    }

    private int ReadOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}