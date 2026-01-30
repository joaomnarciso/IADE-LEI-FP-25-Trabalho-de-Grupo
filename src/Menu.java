import controllers.QuartoController;
import controllers.HospedeController;
import controllers.ReservaController;
import menus.QuartoMenu;
import menus.HospedeMenu;
import menus.ReservaMenu;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private QuartoController quartoController;
    private HospedeController hospedeController;
    private ReservaController reservaController;
    private QuartoMenu quartoMenu;
    private HospedeMenu hospedeMenu;
    private ReservaMenu reservaMenu;
    private boolean exitProgram;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.quartoController = new QuartoController();
        this.hospedeController = new HospedeController();
        this.reservaController = new ReservaController();
        this.reservaController.setControllers(quartoController, hospedeController);

        this.quartoMenu = new QuartoMenu(scanner, quartoController);
        this.hospedeMenu = new HospedeMenu(scanner, hospedeController);
        this.reservaMenu = new ReservaMenu(scanner, reservaController);

        this.exitProgram = false;
    }

    public void iniciar() {
        carregarDados();

        while (!exitProgram) {
            MainMenu();
        }

        guardarDados();
        scanner.close();
        System.out.println("Adeus");
    }

    private void MainMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1 - Quartos");
        System.out.println("2 - Hospedes");
        System.out.println("3 - Reservas");
        System.out.println("4 - Sair e guardar");
        System.out.print("Escolha uma opção: ");

        int menuNumber = lerOpcao();

        switch (menuNumber) {
            case 1:
                quartoMenu.RoomMenu();
                break;
            case 2:
                hospedeMenu.GuestMenu();
                break;
            case 3:
                reservaMenu.ReservationMenu();
                break;
            case 4:
                exitProgram = true;
                break;
            default:
                System.out.println("Opção '"+menuNumber+"' inválida.");
                break;
        }
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void carregarDados() {
        quartoController.carregarDados();
        hospedeController.carregarDados();
        reservaController.carregarDados();
        //System.out.println("Dados carregados com sucesso!");
    }

    private void guardarDados() {
        hospedeController.guardarDados();
        reservaController.guardarDados();
    }
}