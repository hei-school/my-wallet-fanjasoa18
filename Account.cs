using System;
using System.IO;

class Account
{
    static void Main()
    {
        CreateAccount();
    }

    static void CreateAccount()
    {
        Console.Write("Entrez le nom du compte: ");
        string accountName = Console.ReadLine();

        var accountInfo = new
        {
            AccountName = accountName,
            Argent = 0,
            CarteBancaire = 0,
            Permis = 0,
            PhotosIdentite = 0,
            CartesVisite = 0
        };

        string[] categories = { "Argent", "CarteBancaire", "Permis", "PhotosIdentite", "CartesVisite" };

        foreach (var category in categories)
        {
            accountInfo.GetType().GetProperty(category).SetValue(accountInfo, ReadLimitedNumber(category, GetCategoryLimit(category)));
        }

        string transaction = $"Date={GetCurrentDate()};Type=Création;Compte={accountInfo.AccountName};Argent={accountInfo.Argent};CarteBancaire={accountInfo.CarteBancaire};Permis={accountInfo.Permis};PhotosIdentite={accountInfo.PhotosIdentite};CartesVisite={accountInfo.CartesVisite}\n";
        File.AppendAllText(GetTransactionFilePath(), transaction);

        Console.WriteLine("Compte créé avec succès!");
    }

    static int GetCategoryLimit(string category)
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

    static int ReadLimitedNumber(string category, int limit)
    {
        int value;

        do
        {
            Console.Write($"Entrez l'unité initiale de {category} (limite: {limit}) : ");
            value = int.TryParse(Console.ReadLine(), out int result) ? result : 0;

            if (value > limit)
            {
                Console.WriteLine($"La valeur ne doit pas dépasser la limite de {limit}. Veuillez réessayer.");
            }
        } while (value > limit);

        return value;
    }

    static string GetCurrentDate()
    {
        DateTime now = DateTime.Now;
        return $"{now.Year}-{now.Month}-{now.Day}";
    }

    static string GetTransactionFilePath()
    {
        return "transactions.txt";
    }
}
a