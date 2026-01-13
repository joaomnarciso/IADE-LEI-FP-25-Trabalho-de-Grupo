public class App {
    public static void main(String[] args) throws Exception {

        File.ReadFile("quartos");
        while (!Menu.exitProgram) {
            Menu.MainMenu();
        }
    }
}
