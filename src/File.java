import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File {

    // Recebe o nome do ficheiro, lê-o e devolve o conteúdo presente no ficheiro
    public static String[] ReadFile(String fileName) {
        // Procura o ficheiro
        /// Scanner scanner = new Scanner(Paths.get("src/media/" + fileName + ".csv")
        try (Stream<String> fileStream = Files.lines(Paths.get("src/media/" + fileName + ".csv"))) {

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
}