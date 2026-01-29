package menus;

import controllers.ReservaController;
import java.util.Scanner;

public class ReservaMenu {
    private Scanner scanner;
    private ReservaController reservaController;

    public ReservaMenu(Scanner scanner, ReservaController reservaController) {
        this.scanner = scanner;
        this.reservaController = reservaController;
    }

    public void ReservationMenu() {
        int subMenuNumber;
        do {
            System.out.println("\n=== MENU RESERVAS ===");
            System.out.println("1 - Criar Reserva");
            System.out.println("2 - Listar todas as reservas");
            System.out.println("3 - Listar reservas por quarto");
            System.out.println("4 - Listar reservas por hospede");
            System.out.println("5 - Editar reserva");
            System.out.println("6 - Cancelar reserva");
            System.out.println("7 - Sair");
            System.out.print("Escolha uma opção: ");

            subMenuNumber = ReadOption();

            switch (subMenuNumber) {
                case 1:
                    reservaController.criarReserva(scanner);
                    break;
                case 2:
                    reservaController.listarTodasReservas();
                    break;
                case 3:
                    ListarReservasPorQuarto();
                    break;
                case 4:
                    ListarReservasPorHospede();
                    break;
                case 5:
                    EditarReserva();
                    break;
                case 6:
                    CancelarReserva();
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Opcao invalida");
            }
        } while (subMenuNumber != 7);
    }

    private void ListarReservasPorQuarto() {
        System.out.print("ID do quarto: ");
        int idQuarto = ReadOption();
        reservaController.listarReservasPorQuarto(idQuarto);
    }

    private void ListarReservasPorHospede() {
        System.out.print("ID do hóspede: ");
        int idHospede = ReadOption();
        reservaController.listarReservasPorHospede(idHospede);
    }

    private void EditarReserva() {
        System.out.print("ID da reserva: ");
        int idReserva = ReadOption();
        reservaController.editarReserva(idReserva, scanner);
    }

    private void CancelarReserva() {
        System.out.print("ID da reserva: ");
        int idReserva = ReadOption();
        reservaController.cancelarReserva(idReserva);
    }

    private int ReadOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}