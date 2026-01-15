import java.util.stream.Stream;
import models.Hospede;
import models.Quarto;
import models.Reserva;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File {

    // Recebe o nome do ficheiro, lê-o e devolve o conteúdo presente no ficheiro
    public static String[] ReadFile(String fileName) {

        // Procura o ficheiro e lê-o de acordo com os caracteres portugueses
        try (Stream<String> fileStream = Files
                .lines(Paths.get("src/media/" + fileName + ".csv"), StandardCharsets.UTF_8)) {

            // Lê cada linha e guarda-a num array
            String[] result = fileStream.toArray(size -> new String[size]);

            // Devolve o resultado
            return result;
        }

        // Escreve uma mensagem de erro e devolve null
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Guarda os dados relativos ao ficheiro recebido
    public static boolean SaveFile(String fileName) {
        // Cria um array de strings com o tamanho do ficheiro recebido
        String[] fileToSave = new String[GetFileSize(fileName) + 1];
        int lineNumber = 0;

        // Transpõe o array de objetos para um array de strings de forma dinâmica
        switch (fileName) {
            case "quartos":
                // Coloca a primeira linha com os atributos da entidade
                fileToSave[0] = "id;numero;capacidade;estaOcupado";

                // Traduz cada quarto para uma string de acordo com o ficheiro csv original
                for (Quarto quarto : App.quartos) {
                    if (quarto != null) {
                        String quartoId = String.valueOf(quarto.id);
                        String quartoNumero = String.valueOf(quarto.numero);
                        String quartoCapacidade = String.valueOf(quarto.capacidade);
                        String quartoOcupado = quarto.estaOcupado ? "1" : "0";

                        fileToSave[lineNumber + 1] = String.format("%s;%s;%s;%s",
                                quartoId,
                                quartoNumero,
                                quartoCapacidade,
                                quartoOcupado);

                        lineNumber++;
                    } else {
                        break;
                    }
                }
                break;
            case "hospedes":
                // Coloca a primeira linha com os atributos da entidade
                fileToSave[0] = "id;nome;documento";

                // Traduz cada hospede para uma string de acordo com o ficheiro csv original
                for (Hospede hospede : App.hospedes) {
                    if (hospede != null) {
                        String hospedeId = String.valueOf(hospede.id);
                        String hospedeNome = hospede.nome;
                        String hospedeDoc = hospede.documento;

                        fileToSave[lineNumber + 1] = String.format("%s;%s;%s",
                                hospedeId,
                                hospedeNome,
                                hospedeDoc);

                        lineNumber++;
                    } else {
                        break;
                    }
                }
                break;
            case "reservas":
                // Coloca a primeira linha com os atributos da entidade
                fileToSave[0] = "id;idQuarto;idHospede;numeroHospedes;dataInicio;dataFim;ativa";

                // Traduz cada reserva para uma string de acordo com o ficheiro csv original
                for (Reserva reserva : App.reservas) {
                    if (reserva != null) {
                        String reservaId = String.valueOf(reserva.id);
                        String quarto = String.valueOf(reserva.idQuarto);
                        String hospede = String.valueOf(reserva.idHospede);
                        String numeroHospedes = String.valueOf(reserva.numeroHospedes);
                        String dataIncio = String.valueOf(reserva.dataInicio);
                        String dataFim = String.valueOf(reserva.dataFim);
                        String ativa = reserva.ativa ? "1" : "0";

                        fileToSave[lineNumber + 1] = String.format("%s;%s;%s;%s;%s;%s;%s",
                                reservaId,
                                quarto,
                                hospede,
                                numeroHospedes,
                                dataIncio,
                                dataFim,
                                ativa);

                        lineNumber++;
                    } else {
                        break;
                    }
                }
                break;
            default:
                break;
        }

        try {
            // Cria um objeto do tipo FileWriter, definindo onde vai guardar a informação
            FileWriter writer = new FileWriter("src/media/" + fileName + ".csv");
            // Escreve linha a linha
            for (String line : fileToSave) {
                if (line != null) {
                    writer.write(line + "\n");
                } else {
                    break;
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Devolve o número de linhas que o array correspondente ao ficheiro
    private static int GetFileSize(String fileName) {
        switch (fileName) {
            case "quartos":
                return App.GetRoomCount();
            case "hospedes":
                return App.GetGuestCount();
            case "reservas":
                return App.GetReservationCount();
            default:
                return 0;
        }
    }
}