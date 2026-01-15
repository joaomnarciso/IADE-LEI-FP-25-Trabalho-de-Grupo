import java.time.LocalDate;

import models.Hospede;
import models.Quarto;
import models.Reserva;

public class App {
    static final int SIZE_QUARTOS = 200;
    static final int SIZE_HOSPEDES = 1000;
    static final int SIZE_RESERVAS = 1000;
    static String[] filesToImport = { "quartos", "hospedes", "reservas" };

    static Quarto[] quartos = new Quarto[SIZE_QUARTOS];
    static Hospede[] hospedes = new Hospede[SIZE_HOSPEDES];
    static Reserva[] reservas = new Reserva[SIZE_RESERVAS];

    public static void main(String[] args) {
        IntializeApp();

        while (!Menu.exitProgram) {
            Menu.MainMenu();
        }

        CloseApp();
    }

    // Lê todos os ficheiros da pasta "media" definidos na variável filesToImport
    // e passa-os para um array de objetos dos respetivos ficheiros
    public static void IntializeApp() {
        for (String file : filesToImport) {
            String[] unorganizedFile = File.ReadFile(file);
            int lineNumber = 0;

            for (String line : unorganizedFile) {
                if (lineNumber != 0) {
                    switch (file) {
                        case "quartos":
                            String[] _quarto = line.split(";");

                            int quartoId = Integer.parseInt(_quarto[0]);
                            int numeroQuarto = Integer.parseInt(_quarto[1]);
                            int capacidadeQuarto = Integer.parseInt(_quarto[2]);
                            boolean disponibilidadeQuarto = (Integer.parseInt(_quarto[3]) == 1) ? true : false;

                            quartos[lineNumber - 1] = new Quarto(
                                    quartoId,
                                    numeroQuarto,
                                    capacidadeQuarto,
                                    disponibilidadeQuarto);
                            break;
                        case "hospedes":
                            String[] _hospede = line.split(";");

                            int hospedeId = Integer.parseInt(_hospede[0]);
                            String hospedeNome = _hospede[1];
                            String hospedeDoc = _hospede[2];

                            hospedes[lineNumber - 1] = new Hospede(
                                    hospedeId,
                                    hospedeNome,
                                    hospedeDoc);
                            break;
                        case "reservas":
                            String[] _reserva = line.split(";");

                            int reservaId = Integer.parseInt(_reserva[0]);
                            int quarto = Integer.parseInt(_reserva[1]);
                            int hospede = Integer.parseInt(_reserva[2]);
                            int numeroHospedes = Integer.parseInt(_reserva[3]);
                            LocalDate dataInicio = LocalDate.parse(_reserva[4]);
                            LocalDate dataFim = LocalDate.parse(_reserva[5]);
                            boolean reservaAtiva = (Integer.parseInt(_reserva[6]) == 1) ? true : false;

                            reservas[lineNumber - 1] = new Reserva(
                                    reservaId,
                                    quarto,
                                    hospede,
                                    numeroHospedes,
                                    dataInicio,
                                    dataFim,
                                    reservaAtiva);
                            break;
                    }
                }
                lineNumber++;
            }
        }
    }

    // Guarda todos os ficheiros
    public static void CloseApp() {
        for (String file : filesToImport) {
            File.SaveFile(file);
        }
    }

    // Devolve o número de quartos
    public static int GetRoomCount() {
        int roomCount = 0;

        for (Quarto quarto : quartos) {
            if (quarto != null) {
                roomCount++;
            } else {
                break;
            }
        }

        return roomCount;
    }

    // Devolve o número de hóspedes
    public static int GetGuestCount() {
        int guestCount = 0;

        for (Hospede hospede : hospedes) {
            if (hospede != null) {
                guestCount++;
            } else {
                break;
            }
        }

        return guestCount;
    }

    // Devolve o número de reservas
    public static int GetReservationCount() {
        int reservationCount = 0;

        for (Reserva reserva : reservas) {
            if (reserva != null) {
                reservationCount++;
            } else {
                break;
            }
        }

        return reservationCount;
    }
}
