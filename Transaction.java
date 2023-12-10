import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Transaction {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] categories = {"Argent", "CarteBancaire", "Permis", "PhotosIdentite", "CartesVisite"};

    public static void depositMoney() {
        System.out.print("Entrez le nom du compte : ");
        String accountName = scanner.nextLine();

        // Demander la catégorie d'objet pour le dépôt
        int depositCategory = Utils.displayMenu("Sélectionnez la catégorie pour le dépôt :", categories);

        if (depositCategory == -1) {
            System.out.println("Opération annulée.");
            return;
        }

        double depositAmount = 0;
        Balance.WalletInfo currentBalance = Utils.getCurrentBalance(accountName);

        switch (depositCategory) {
            case 0: // Argent
                double currentArgent = currentBalance.getArgent();
                double limitArgent = Account.getCategoryLimit("Argent");

                depositAmount = Account.readLimitedNumber("Entrez le montant de Argent à déposer (limite: " + (limitArgent - currentArgent) + ") : ", (int) (limitArgent - currentArgent));

                if (currentArgent + depositAmount > limitArgent) {
                    System.out.println("Le montant total de Argent dépasse la limite de " + limitArgent + ". Opération annulée.");
                    return;
                }
                break;
            case 1: // CarteBancaire
                int currentCarte = currentBalance.getCartesBancaires();
                int limitCarte = Account.getCategoryLimit("CarteBancaire");

                depositAmount = Account.readLimitedNumber("Entrez le nombre de carte bancaire à déposer (limite: " + (limitCarte - currentCarte) + ") : ", limitCarte - currentCarte);

                if (currentCarte + depositAmount > limitCarte) {
                    System.out.println("Le nombre dépasse la limite de " + limitCarte + ". Opération annulée.");
                    return;
                }
                break;
            case 2: // Permis
                int currentPermis = currentBalance.getPermisDeConduire();
                int limitPermis = Account.getCategoryLimit("Permis");

                depositAmount = Account.readLimitedNumber("Entrez le nombre de permis à déposer (limite: " + (limitPermis - currentPermis) + ") : ", limitPermis - currentPermis);

                if (currentPermis + depositAmount > limitPermis) {
                    System.out.println("Le nombre dépasse la limite de " + limitPermis + ". Opération annulée.");
                    return;
                }
                break;
            case 3: // PhotosIdentite
                int currentPhoto = currentBalance.getPhotosIdentite();
                int limitPhoto = Account.getCategoryLimit("PhotosIdentite");

                depositAmount = Account.readLimitedNumber("Entrez le nombre de photo à déposer (limite: " + (limitPhoto - currentPhoto) + ") : ", limitPhoto - currentPhoto);

                if (currentPhoto + depositAmount > limitPhoto) {
                    System.out.println("Le nombre dépasse la limite de " + limitPhoto + ". Opération annulée.");
                    return;
                }
                break;
            case 4: // CartesVisite
                int currentVisite = currentBalance.getCartesVisite();
                int limitVisite = Account.getCategoryLimit("CartesVisite");

                depositAmount = Account.readLimitedNumber("Entrez le nombre de carte de visite à déposer (limite: " + (limitVisite - currentVisite) + ") : ", limitVisite - currentVisite);

                if (currentVisite + depositAmount > limitVisite) {
                    System.out.println("Le nombre dépasse la limite de " + limitVisite + ". Opération annulée.");
                    return;
                }
                break;
            default:
                break;
        }

        // Enregistrer la transaction de dépôt
        String transaction = "Date=" + Utils.getCurrentDate() + ";Type=Dépôt;Compte=" + accountName +
                ";Categorie=" + categories[depositCategory] + ";Montant=" + depositAmount + "\n";

        try {
            FileWriter writer = new FileWriter(Utils.getTransactionFilePath(), true);
            writer.write(transaction);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Dépôt effectué avec succès !");
    }

    public static void withdrawMoney() {
        System.out.print("Entrez le nom du compte : ");
        String accountName = scanner.nextLine();

        // Demander la catégorie d'objet pour le retrait
        int withdrawalCategory = Utils.displayMenu("Sélectionnez la catégorie pour le retrait :", categories);

        if (withdrawalCategory == -1) {
            System.out.println("Opération annulée.");
            return;
        }

        Balance.WalletInfo currentBalance = Utils.getCurrentBalance(accountName);
        int currentAmount = (int) currentBalance.getCategoryBalance(categories[withdrawalCategory]);
        int limit = Account.getCategoryLimit(categories[withdrawalCategory]);

        int withdrawalAmount;

        do {
            // Demander le montant à retirer
            switch (withdrawalCategory) {
                case 0: // Argent
                    withdrawalAmount = Account.readLimitedNumber("Entrez le montant de " + categories[withdrawalCategory] + " à retirer : ", limit);
                    break;
                case 1: // CarteBancaire
                    withdrawalAmount = Account.readLimitedNumber("Entrez le nombre de " + categories[withdrawalCategory] + " à retirer : ", limit);
                    break;
                case 2: // Permis
                    withdrawalAmount = Account.readLimitedNumber("Entrez le nombre de " + categories[withdrawalCategory] + " à retirer : ", limit);
                    break;
                case 3: // PhotosIdentite
                    withdrawalAmount = Account.readLimitedNumber("Entrez le nombre de " + categories[withdrawalCategory] + " à retirer : ", limit);
                    break;
                case 4: // CartesVisite
                    withdrawalAmount = Account.readLimitedNumber("Entrez le nombre de " + categories[withdrawalCategory] + " à retirer : ", limit);
                    break;
                default:
                    System.out.println("Option de catégorie non valide. Veuillez choisir une catégorie valide.");
                    withdrawalAmount = Account.readLimitedNumber("Entrez le montant à retirer : ", limit);
                    break;
            }

            // Vérifier si le montant à retirer dépasse la limite
            if (withdrawalAmount > limit) {
                System.out.println("Le montant à retirer dépasse la limite de " + limit + ". Entrez une nouvelle valeur.");
            }

            // Vérifier si le montant à retirer dépasse le solde actuel
            if (withdrawalAmount > currentAmount) {
                System.out.println("Le montant à retirer dépasse le solde actuel de " + currentAmount + ". Entrez une nouvelle valeur.");
            }

            // Mettre à jour le solde actuel
            currentAmount = (int) currentBalance.getCategoryBalance(categories[withdrawalCategory]);

        } while (withdrawalAmount > limit || withdrawalAmount > currentAmount);

        // Enregistrer la transaction de retrait
        String transaction = "Date=" + Utils.getCurrentDate() + ";Type=Retrait;Compte=" + accountName +
                ";Categorie=" + categories[withdrawalCategory] + ";Montant=" + withdrawalAmount + "\n";

        try {
            FileWriter writer = new FileWriter(Utils.getTransactionFilePath(), true);
            writer.write(transaction);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Retrait effectué avec succès !");
    }

    public static void showTransactionHistory() {
        System.out.print("Entrez le nom du compte : ");
        String accountName = scanner.nextLine();

        String transactions = Utils.readTransactionsFromFile();

        // Vérifier si le compte existe avant d'afficher l'historique
        if (Utils.accountExists(transactions, accountName)) {
            // Les trois dernières transactions
            String[] lastThreeTransactions = transactions.trim().split("\n");
            int numTransactions = lastThreeTransactions.length;

            if (numTransactions > 0) {
                System.out.println("Les trois dernières transactions :");

                for (int i = Math.max(0, numTransactions - 3); i < numTransactions; i++) {
                    String transaction = lastThreeTransactions[i];
                    TransactionDetails transactionDetails = Utils.parseTransaction(transaction);

                    // Afficher les détails de la transaction en fonction du type
                    switch (transactionDetails.getType()) {
                        case "Création":
                            System.out.println("- Type: " + transactionDetails.getType() + ", Montant initial: " + transactionDetails.getArgent());
                            break;
                        case "Dépôt":
                        case "Retrait":
                            System.out.println("- Type: " + transactionDetails.getType() + ", Categorie: " +
                                    transactionDetails.getCategorie() + ", Montant: " + transactionDetails.getMontant());
                            break;
                        default:
                            break;
                    }
                }
            } else {
                System.out.println("Aucune transaction effectuée jusqu'à présent.");
            }
        } else {
            System.out.println("Le compte \"" + accountName + "\" n'existe pas.");
        }
    }

}
