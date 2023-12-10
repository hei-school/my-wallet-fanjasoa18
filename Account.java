import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Account {
    private static final Scanner scanner = new Scanner(System.in);


    public static void createAccount() {
        System.out.print("Entrez le nom du compte: ");
        String accountName = scanner.nextLine();

        Map<String, Integer> accountInfo = new HashMap<>();
        accountInfo.put("Argent", 0);
        accountInfo.put("CarteBancaire", 0);
        accountInfo.put("Permis", 0);
        accountInfo.put("PhotosIdentite", 0);
        accountInfo.put("CartesVisite", 0);

        // Demander le nombre d'objets de chaque catégorie
        String[] categories = {"Argent", "CarteBancaire", "Permis", "PhotosIdentite", "CartesVisite"};

        for (String category : categories) {
            accountInfo.put(category, readLimitedNumber(category, getCategoryLimit(category)));
        }

        // Enregistrer la transaction complète dans le fichier de transactions
        String transaction = "Date=" + Utils.getCurrentDate() +
                ";Type=Création;Compte=" + accountName +
                ";Argent=" + accountInfo.get("Argent") +
                ";CarteBancaire=" + accountInfo.get("CarteBancaire") +
                ";Permis=" + accountInfo.get("Permis") +
                ";PhotosIdentite=" + accountInfo.get("PhotosIdentite") +
                ";CartesVisite=" + accountInfo.get("CartesVisite") + "\n";

        try {
            FileWriter writer = new FileWriter(Utils.getTransactionFilePath(), true);
            writer.write(transaction);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Compte créé avec succès!");
    }

    public static int getCategoryLimit(String category) {
        switch (category) {
            case "Argent":
                return 50000;
            case "CarteBancaire":
                return 10;
            case "Permis":
                return 5;
            case "PhotosIdentite":
                return 3;
            case "CartesVisite":
                return 10;
            default:
                return 0;
        }
    }

    public static int readLimitedNumber(String category, int limit) {
        int value;

        do {
            System.out.print("Entrez l'unité initiale de " + category + " (limite: " + limit + "): ");
            value = scanner.nextInt();

            if (value > limit) {
                System.out.println("La valeur ne doit pas dépasser la limite de " + limit + ". Veuillez réessayer.");
            }
        } while (value > limit);

        return value;
    }
}
