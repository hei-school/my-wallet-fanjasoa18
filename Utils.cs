using System;
using System.IO;
using System.Linq;

class Utils
{
    public string GetCurrentDate()
    {
        DateTime now = DateTime.Now;
        return $"{now.Year}-{now.Month}-{now.Day}";
    }

    public double GetCategoryLimit(string category)
    {
        switch (category)
        {
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

    public double ConversionRate(string currentCurrency, string targetCurrency)
    {
        // Taux de conversion par défaut (1:1)
        double tauxDeConversion = 1;

        // Définir les taux de conversion en fonction de la relation donnée
        if (currentCurrency == "ar" && targetCurrency == "frc")
        {
            tauxDeConversion = 5;
        }
        else if (currentCurrency == "frc" && targetCurrency == "ar")
        {
            tauxDeConversion = 1 / 5.0;
        }

        return tauxDeConversion;
    }

    public Dictionary<string, double?> CalculateBalance(string transactions, string accountName)
    {
        var lines = transactions.Split('\n');
        double argent = 0;
        double cartesBancaires = 0;
        double permisDeConduire = 0;
        double photosIdentite = 0;
        double cartesVisite = 0;

        foreach (var line in lines)
        {
            var transactionDetails = ParseTransaction(line);

            if (transactionDetails != null && transactionDetails["Compte"] == accountName)
            {
                // Mettre à jour les objets lors de la création du compte
                if (transactionDetails["Type"] == "Création")
                {
                    argent = Convert.ToDouble(transactionDetails["Argent"] ?? "0");
                    cartesBancaires = Convert.ToDouble(transactionDetails["CarteBancaire"] ?? "0");
                    permisDeConduire = Convert.ToDouble(transactionDetails["Permis"] ?? "0");
                    photosIdentite = Convert.ToDouble(transactionDetails["PhotosIdentite"] ?? "0");
                    cartesVisite = Convert.ToDouble(transactionDetails["CartesVisite"] ?? "0");
                }
                else
                {
                    // Mettre à jour les objets en fonction du type de transaction
                    switch (transactionDetails["Type"])
                    {
                        case "Dépôt":
                            if (transactionDetails["Categorie"] == "Argent")
                            {
                                argent += Convert.ToDouble(transactionDetails["Montant"] ?? "0");
                            }
                            else if (transactionDetails["Categorie"] == "CarteBancaire")
                            {
                                cartesBancaires += Convert.ToDouble(transactionDetails["Montant"] ?? "0");
                            }
                            else if (transactionDetails["Categorie"] == "Permis")
                            {
                                permisDeConduire += Convert.ToDouble(transactionDetails["Montant"] ?? "0");
                            }
                            else if (transactionDetails["Categorie"] == "PhotosIdentite")
                            {
                                photosIdentite += Convert.ToDouble(transactionDetails["Montant"] ?? "0");
                            }
                            else if (transactionDetails["Categorie"] == "CartesVisite")
                            {
                                cartesVisite += Convert.ToDouble(transactionDetails["Montant"] ?? "0");
                            }
                            // Ajoutez d'autres catégories ici au besoin
                            break;
                        case "Retrait":
                            if (transactionDetails["Categorie"] == "Argent")
                            {
                                argent = Math.Max(0, argent - Convert.ToDouble(transactionDetails["Montant"] ?? "0"));
                            }
                            else if (transactionDetails["Categorie"] == "CarteBancaire")
                            {
                                cartesBancaires = Math.Max(0, cartesBancaires - Convert.ToDouble(transactionDetails["Montant"] ?? "0"));
                            }
                            else if (transactionDetails["Categorie"] == "Permis")
                            {
                                permisDeConduire = Math.Max(0, permisDeConduire - Convert.ToDouble(transactionDetails["Montant"] ?? "0"));
                            }
                            else if (transactionDetails["Categorie"] == "PhotosIdentite")
                            {
                                photosIdentite = Math.Max(0, photosIdentite - Convert.ToDouble(transactionDetails["Montant"] ?? "0"));
                            }
                            else if (transactionDetails["Categorie"] == "CartesVisite")
                            {
                                cartesVisite = Math.Max(0, cartesVisite - Convert.ToDouble(transactionDetails["Montant"] ?? "0"));
                            }
                            // Ajoutez d'autres catégories ici au besoin
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return new Dictionary<string, double?>
        {
            { "Argent", argent },
            { "CartesBancaires", cartesBancaires },
            { "PermisDeConduire", permisDeConduire },
            { "PhotosIdentite", photosIdentite },
            { "CartesVisite", cartesVisite }
        };
    }

    public Dictionary<string, string> ParseTransaction(string line)
    {
        var transactionDetails = new Dictionary<string, string>();
        var keyValuePairs = line.Split(';');

        foreach (var pair in keyValuePairs)
        {
            var keyValue = pair.Split('=');
            transactionDetails[keyValue[0]] = keyValue[1];
        }

        return transactionDetails;
    }

    public string GetTransactionFilePath()
    {
        return "transactions.txt";
    }

    public bool AccountExists(string transactions, string accountName)
    {
        var lines = transactions.Split('\n');

        return lines.Any(line =>
        {
            var transactionDetails = ParseTransaction(line);
            return transactionDetails != null && transactionDetails["Compte"] == accountName;
        });
    }
}
