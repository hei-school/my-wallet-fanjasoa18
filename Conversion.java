import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Conversion {
    private static final Scanner scanner = new Scanner(System.in);

    public static void convertCurrency() {
        System.out.print("Entrez le montant à convertir : ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne restante après la saisie du montant

        System.out.print("Entrez la devise actuelle (ar/frc) : ");
        String currentCurrency = scanner.nextLine();

        System.out.print("Entrez la devise cible (ar/frc) : ");
        String targetCurrency = scanner.nextLine();

        // Logique pour convertir la devise
        double conversionRate = Utils.conversionRate(currentCurrency, targetCurrency);
        double convertedAmount = amount * conversionRate;

        // Enregistrer la transaction de conversion
        String conversionTransaction = "Date=" + Utils.getCurrentDate() +
                ";Montant=" + amount + " " + currentCurrency + " -> " + convertedAmount + " " + targetCurrency + "\n";

        try {
            FileWriter writer = new FileWriter(Utils.getTransactionFilePath(), true);
            writer.write(conversionTransaction);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Montant converti : " + convertedAmount + " " + targetCurrency);
    }
}
