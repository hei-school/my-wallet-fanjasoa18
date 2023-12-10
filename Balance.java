import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Balance {

    public static void checkBalance() {
        System.out.print("Entrez le nom du compte : ");
        String accountName = Main.scanner.nextLine();

        try {
            String transactions = new String(Files.readAllBytes(Paths.get(Utils.getTransactionFilePath())));
            WalletInfo walletInfo = Utils.calculateBalance(transactions, accountName);

            if (walletInfo != null) {
                System.out.println("Solde actuel du compte \"" + accountName + "\":");
                System.out.println("- Argent: " + walletInfo.getArgent());
                System.out.println("- Cartes Bancaires: " + walletInfo.getCartesBancaires());
                System.out.println("- Permis de Conduire: " + walletInfo.getPermisDeConduire());
                System.out.println("- Photos d'Identité: " + walletInfo.getPhotosIdentite());
                System.out.println("- Cartes de Visite: " + walletInfo.getCartesVisite());
            } else {
                System.out.println("Le compte \"" + accountName + "\" n'existe pas.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class WalletInfo {
        private double argent;
        private int cartesBancaires;
        private int permisDeConduire;
        private int photosIdentite;
        private int cartesVisite;

        public double getCategoryBalance(String category) {
            // Ici, vous devriez implémenter la logique pour récupérer la balance associée à la catégorie spécifiée
            // Cela peut impliquer de parcourir vos objets de transaction et de filtrer par catégorie

            // Exemple simplifié pour démonstration :
            switch (category) {
                case "Argent":
                    return argent;
                case "CartesBancaires":
                    return cartesBancaires;
                case "PermisDeConduire":
                    return permisDeConduire;
                case "PhotosIdentite":
                    return photosIdentite;
                case "CartesVisite":
                    return cartesVisite;
                default:
                    return 0;  // ou une valeur par défaut appropriée si la catégorie n'est pas trouvée
            }
        }

        // Constructeur
        public WalletInfo(double argent, int cartesBancaires, int permisDeConduire, int photosIdentite, int cartesVisite) {
            this.argent = argent;
            this.cartesBancaires = cartesBancaires;
            this.permisDeConduire = permisDeConduire;
            this.photosIdentite = photosIdentite;
            this.cartesVisite = cartesVisite;
        }

        // Getters et Setters (si nécessaire)
        public double getArgent() {
            return argent;
        }

        public void setArgent(double argent) {
            this.argent = argent;
        }

        public int getCartesBancaires() {
            return cartesBancaires;
        }

        public void setCartesBancaires(int cartesBancaires) {
            this.cartesBancaires = cartesBancaires;
        }

        public int getPermisDeConduire() {
            return permisDeConduire;
        }

        public void setPermisDeConduire(int permisDeConduire) {
            this.permisDeConduire = permisDeConduire;
        }

        public int getPhotosIdentite() {
            return photosIdentite;
        }

        public void setPhotosIdentite(int photosIdentite) {
            this.photosIdentite = photosIdentite;
        }

        public int getCartesVisite() {
            return cartesVisite;
        }

        public void setCartesVisite(int cartesVisite) {
            this.cartesVisite = cartesVisite;
        }

    }

}
