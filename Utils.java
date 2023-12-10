import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utils {
    private static final Scanner scanner = new Scanner(System.in);
    public static String getCurrentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        return sqlDate.toString();
    }

    public static Balance.WalletInfo calculateBalance(String transactions, String accountName) {
        String[] lines = transactions.split("\n");
        double argent = 0;
        int cartesBancaires = 0;
        int permisDeConduire = 0;
        int photosIdentite = 0;
        int cartesVisite = 0;

        for (String line : lines) {
            TransactionDetails transactionDetails = parseTransaction(line);

            if (transactionDetails != null && transactionDetails.getCompte().equals(accountName)) {
                // Mettre à jour les objets lors de la création du compte
                if ("Création".equals(transactionDetails.getType())) {
                    argent = Double.parseDouble(transactionDetails.getArgent());
                    cartesBancaires = Integer.parseInt(transactionDetails.getCarteBancaire());
                    permisDeConduire = Integer.parseInt(transactionDetails.getPermis());
                    photosIdentite = Integer.parseInt(transactionDetails.getPhotosIdentite());
                    cartesVisite = Integer.parseInt(transactionDetails.getCartesVisite());
                } else {
                    // Mettre à jour les objets en fonction du type de transaction
                    switch (transactionDetails.getType()) {
                        case "Dépôt":
                            Balance.WalletInfo updatedBalanceAfterDeposit = updateObjectsOnDeposit(transactionDetails, argent, cartesBancaires, permisDeConduire, photosIdentite, cartesVisite);
                            argent = updatedBalanceAfterDeposit.getArgent();
                            cartesBancaires = updatedBalanceAfterDeposit.getCartesBancaires();
                            permisDeConduire = updatedBalanceAfterDeposit.getPermisDeConduire();
                            photosIdentite = updatedBalanceAfterDeposit.getPhotosIdentite();
                            cartesVisite = updatedBalanceAfterDeposit.getCartesVisite();
                            break;
                        case "Retrait":
                            Balance.WalletInfo updatedBalanceAfterWithdrawal = updateObjectsOnWithdrawal(transactionDetails, argent, cartesBancaires, permisDeConduire, photosIdentite, cartesVisite);
                            argent = updatedBalanceAfterWithdrawal.getArgent();
                            cartesBancaires = updatedBalanceAfterWithdrawal.getCartesBancaires();
                            permisDeConduire = updatedBalanceAfterWithdrawal.getPermisDeConduire();
                            photosIdentite = updatedBalanceAfterWithdrawal.getPhotosIdentite();
                            cartesVisite = updatedBalanceAfterWithdrawal.getCartesVisite();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return new Balance.WalletInfo(argent, cartesBancaires, permisDeConduire, photosIdentite, cartesVisite);
    }

    public static Balance.WalletInfo getCurrentBalance(String accountName) {
        String transactions = readTransactionsFromFile();
        return calculateBalance(transactions, accountName);
    }


    public static TransactionDetails parseTransaction(String line) {
        String[] keyValuePairs = line.split(";");

        TransactionDetails transactionDetails = new TransactionDetails();

        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";

            switch (key) {
                case "Date":
                    transactionDetails.setDate(value);
                    break;
                case "Type":
                    transactionDetails.setType(value);
                    break;
                case "Compte":
                    transactionDetails.setCompte(value);
                    break;
                case "Categorie":
                    transactionDetails.setCategorie(value);
                    break;
                case "Montant":
                    transactionDetails.setMontant(value);
                    break;
                case "Argent":
                    transactionDetails.setArgent(value);
                    break;
                case "CarteBancaire":
                    transactionDetails.setCarteBancaire(value);
                    break;
                case "Permis":
                    transactionDetails.setPermis(value);
                    break;
                case "PhotosIdentite":
                    transactionDetails.setPhotosIdentite(value);
                    break;
                case "CartesVisite":
                    transactionDetails.setCartesVisite(value);
                    break;
                default:
                    break;
            }
        }

        return transactionDetails;
    }

    public static String getTransactionFilePath() {
        return "transactions.txt";
    }

    public static boolean accountExists(String transactions, String accountName) {
        String[] lines = transactions.split("\n");

        for (String line : lines) {
            TransactionDetails transactionDetails = parseTransaction(line);

            if (transactionDetails != null && transactionDetails.getCompte().equals(accountName)) {
                return true;
            }
        }

        return false;
    }

    public static double conversionRate(String currentCurrency, String targetCurrency) {
        double tauxDeConversion = 1;

        if ("ar".equals(currentCurrency) && "frc".equals(targetCurrency)) {
            tauxDeConversion = 5;
        } else if ("frc".equals(currentCurrency) && "ar".equals(targetCurrency)) {
            tauxDeConversion = 1 / 5.0;
        }

        return tauxDeConversion;
    }

    public static String readTransactionsFromFile() {
        try {
            Path path = Paths.get(getTransactionFilePath());
            List<String> lines = Files.readAllLines(path);
            return String.join("\n", lines);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Balance.WalletInfo updateObjectsOnDeposit(TransactionDetails transactionDetails, double argent, int cartesBancaires, int permisDeConduire, int photosIdentite, int cartesVisite) {
        switch (transactionDetails.getCategorie()) {
            case "Argent":
                argent += Double.parseDouble(transactionDetails.getMontant());
                break;
            case "CarteBancaire":
                cartesBancaires += Integer.parseInt(transactionDetails.getMontant());
                break;
            case "Permis":
                permisDeConduire += Integer.parseInt(transactionDetails.getMontant());
                break;
            case "PhotosIdentite":
                photosIdentite += Integer.parseInt(transactionDetails.getMontant());
                break;
            case "CartesVisite":
                cartesVisite += Integer.parseInt(transactionDetails.getMontant());
                break;
            default:
                break;
        }

        return new Balance.WalletInfo(argent, cartesBancaires, permisDeConduire, photosIdentite, cartesVisite);
    }

    public static Balance.WalletInfo updateObjectsOnWithdrawal(TransactionDetails transactionDetails, double argent, int cartesBancaires, int permisDeConduire, int photosIdentite, int cartesVisite) {
        switch (transactionDetails.getCategorie()) {
            case "Argent":
                argent = Math.max(0, argent - Double.parseDouble(transactionDetails.getMontant()));
                break;
            case "CarteBancaire":
                int withdrawalAmount = Integer.parseInt(transactionDetails.getMontant());
                cartesBancaires = Math.max(0, cartesBancaires - withdrawalAmount);
                break;
            case "Permis":
                int withdrawalPermis = Integer.parseInt(transactionDetails.getMontant());
                permisDeConduire = Math.max(0, permisDeConduire - withdrawalPermis);
                break;
            case "PhotosIdentite":
                int withdrawalPhotos = Integer.parseInt(transactionDetails.getMontant());
                photosIdentite = Math.max(0, photosIdentite - withdrawalPhotos);
                break;
            case "CartesVisite":
                int withdrawalCartes = Integer.parseInt(transactionDetails.getMontant());
                cartesVisite = Math.max(0, cartesVisite - withdrawalCartes);
                break;
            default:
                break;
        }

        // Retourner une instance mise à jour de WalletInfo
        return new Balance.WalletInfo(argent, cartesBancaires, permisDeConduire, photosIdentite, cartesVisite);
    }


    public static int displayMenu(String prompt, String[] options) {
        System.out.println(prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.print("Choisissez une option : ");
        int choice = scanner.nextInt();

        // Vérifier si le choix est valide
        if (choice < 1 || choice > options.length) {
            System.out.println("Option invalide. Veuillez réessayer.");
            return -1; // Indique que le choix n'est pas valide
        }

        return choice - 1; // Soustraire 1 pour obtenir l'index de l'option choisie
    }
}
