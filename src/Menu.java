import java.util.*;

public class Menu {
    enum MenuOptions {
        MAIN,
        ROOM,
        GUEST,
        RESERVATION
    }

    public static MenuOptions menuOption;
    public static int menuNumber;
    public static int subMenuNumber;
    public static boolean exitProgram = false;

    // Mostra o Menu Principal e lê a opção do utilizador
    public static void MainMenu() {
        menuOption = MenuOptions.MAIN;

        System.out.println("1 - Quartos");
        System.out.println("2 - Hospedes");
        System.out.println("3 - Reservas");
        System.out.println("4 - Sair");
        ReadOption();
    }

    // Mostra o Menu dos Quartos e lê a opção do utilizador
    public static void RoomMenu() {
        System.out.println("1 - Listar todos os quartos");
        System.out.println("2 - Listar quartos livres");
        System.out.println("3 - Listar quartos ocupados");
        System.out.println("4 - Sair");

        menuOption = MenuOptions.ROOM;
        ReadOption();
    }

    // Mostra o Menu dos Hóspedes e lê a opção do utilizador
    public static void GuestMenu() {
        System.out.println("1 - Listar hospedes");
        System.out.println("2 - Procurar hospede por documento");
        System.out.println("3 - Editar hospede");
        System.out.println("4 - Sair");

        menuOption = MenuOptions.GUEST;
        ReadOption();
    }

    // Mostra o Menu das Reservas e lê a opção do utilizador
    public static void ReservationMenu() {
        System.out.println("1 - Criar Reserva");
        System.out.println("2 - Listar todas as reservas");
        System.out.println("3 - Listar reservas por quarto");
        System.out.println("4 - Listar reservas por hospede");
        System.out.println("5 - Editar reserva");
        System.out.println("6 - Cancelar reserva");
        System.out.println("7 - Sair");

        menuOption = MenuOptions.RESERVATION;
        ReadOption();
    }

    // Lê a opção do utilizador e redireciona para o respetivo Menu ou função
    private static void ReadOption() {
        Scanner input = new Scanner(System.in);

        // O utilizador está no Menu Principal
        if (menuOption == MenuOptions.MAIN) {
            menuNumber = input.nextInt();
            switch (menuNumber) {
                case 1:
                    menuOption = MenuOptions.ROOM;
                    RoomMenu();
                    break;
                case 2:
                    menuOption = MenuOptions.GUEST;
                    GuestMenu();
                    break;
                case 3:
                    menuOption = MenuOptions.RESERVATION;
                    ReservationMenu();
                    break;
                case 4:
                    menuNumber = 4;
                    exitProgram = true;
                    System.out.println("Adeus");
                    break;
                default:
                    System.out.println("Opcao invalida");
                    break;
            }
        }

        // O utilizador está dentro de um Submenu
        else {
            subMenuNumber = input.nextInt();
            switch (menuOption.name().toString()) {
                case "ROOM":

                case "GUEST":
                    if (subMenuNumber == 4) {
                        menuOption = MenuOptions.MAIN;
                        MainMenu();
                    }
                    break;
                case "RESERVATION":
                    if (subMenuNumber == 7) {
                        menuOption = MenuOptions.MAIN;
                        MainMenu();
                    }
                    break;

                default:
                    break;
            }
        }

        input.close();
    }
}
