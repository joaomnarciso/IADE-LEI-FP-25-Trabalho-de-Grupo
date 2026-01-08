import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        Menu.MainMenu();
        
        while(!Menu.exitProgram){
            switch(Menu.menuOption.name().toString()){
                case "ROOM":
                    System.out.println("Quarto");
                    break;
                case "GUEST":
                    System.out.println("Hospede");
                    break;
                case "RESERVATION":
                    System.out.println("Reserva");
                    break;
                default:
                    System.out.println("Numero invalido");
            }
        }
    }
}
