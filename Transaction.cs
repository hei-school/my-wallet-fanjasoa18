using System;
using System.IO;

class Transaction
{
    private readonly Utils _utils;

    public Transaction(Utils utils)
    {
        _utils = utils;
    }

    public void DepositMoney()
    {
        Console.Write("Entrez le nom du compte : ");
        string accountName = Console.ReadLine();

        Console.WriteLine("Sélectionnez la catégorie pour le dépôt : ");
        for (int i = 0; i < categories.Length; i++)
        {
            Console.WriteLine($"{i + 1}. {categories[i]}");
        }

        int depositCategory;
        while (!int.TryParse(Console.ReadLine(), out depositCategory) || depositCategory < 1 || depositCategory > categories.Length)
        {
            Console.WriteLine("Veuillez entrer une option valide.");
        }

        if (depositCategory == 0)
        {
            Console.WriteLine("Opération annulée.");
            return;
        }

        double depositAmount = 0;
        var currentBalance = _utils.GetCurrentBalance(accountName);

        switch (depositCategory - 1)
        {
            case 0: // Argent
                double currentArgent = currentBalance.Argent ?? 0;
                double limit = GetCategoryLimit("Argent");
                Console.Write($"Entrez le montant de Argent à déposer (limite: {limit - currentArgent}) : ");
                while (!double.TryParse(Console.ReadLine(), out depositAmount) || currentArgent + depositAmount > limit)
                {
                    Console.WriteLine($"Le montant total de Argent dépasse la limite de {limit}. Entrez une nouvelle valeur.");
                }
                break;
            // Ajoutez des cas pour les autres catégories ici
            default:
                break;
        }

        // Enregistrer la transaction de dépôt
        string transaction = $"Date={_utils.GetCurrentDate()};Type=Dépôt;Compte={accountName};Categorie={categories[depositCategory - 1]};Montant={depositAmount}\n";
        File.AppendAllText(_utils.GetTransactionFilePath(), transaction);

        Console.WriteLine("Dépôt effectué avec succès !");
    }

    public void WithdrawMoney()
        {
            Console.Write("Entrez le nom du compte : ");
            string accountName = Console.ReadLine();

            Console.WriteLine("Sélectionnez la catégorie pour le retrait : ");
            for (int i = 0; i < categories.Length; i++)
            {
                Console.WriteLine($"{i + 1}. {categories[i]}");
            }

            int withdrawalCategory;
            while (!int.TryParse(Console.ReadLine(), out withdrawalCategory) || withdrawalCategory < 1 || withdrawalCategory > categories.Length)
            {
                Console.WriteLine("Veuillez entrer une option valide.");
            }

            if (withdrawalCategory == 0)
            {
                Console.WriteLine("Opération annulée.");
                return;
            }

            double withdrawalAmount;

            var currentBalance = _utils.GetCurrentBalance(accountName);
            double currentAmount = currentBalance[categories[withdrawalCategory - 1]] ?? 0;
            double limit = GetCategoryLimit(categories[withdrawalCategory - 1]);

            do
            {
                // Demander le montant à retirer
                Console.Write($"Entrez le montant de {categories[withdrawalCategory - 1]} à retirer : ");
                while (!double.TryParse(Console.ReadLine(), out withdrawalAmount) || withdrawalAmount > limit)
                {
                    Console.WriteLine($"Le montant à retirer dépasse la limite de {limit}. Entrez une nouvelle valeur.");
                }
            } while (withdrawalAmount > currentAmount || withdrawalAmount > limit);

            // Enregistrer la transaction de retrait
            string transaction = $"Date={_utils.GetCurrentDate()};Type=Retrait;Compte={accountName};Categorie={categories[withdrawalCategory - 1]};Montant={withdrawalAmount}\n";
            File.AppendAllText(_utils.GetTransactionFilePath(), transaction);

            Console.WriteLine("Retrait effectué avec succès !");
        }

        public void ShowTransactionHistory()
        {
            Console.Write("Entrez le nom du compte : ");
            string accountName = Console.ReadLine();

            string transactions = File.ReadAllText(_utils.GetTransactionFilePath());

            // Vérifier si le compte existe avant d'afficher l'historique
            if (_utils.AccountExists(transactions, accountName))
            {
                // Les trois dernières transactions
                string[] lastThreeTransactions = transactions.Trim().Split('\n').Reverse().Take(3).Reverse().ToArray();

                if (lastThreeTransactions.Length > 0)
                {
                    Console.WriteLine("Les trois dernières transactions :");

                    foreach (string transaction in lastThreeTransactions)
                    {
                        var transactionDetails = _utils.ParseTransaction(transaction);

                        // Afficher les détails de la transaction en fonction du type
                        switch (transactionDetails.Type)
                        {
                            case "Création":
                                Console.WriteLine($"- Type: {transactionDetails.Type}, Montant initial: {transactionDetails.Argent}");
                                break;
                            case "Dépôt":
                            case "Retrait":
                                Console.WriteLine($"- Type: {transactionDetails.Type}, Categorie: {transactionDetails.Categorie}, Montant: {transactionDetails.Montant}");
                                break;
                            default:
                                break;
                        }
                    }
                }
                else
                {
                    Console.WriteLine("Aucune transaction effectuée jusqu'à présent.");
                }
            }
            else
            {
                Console.WriteLine($"Le compte \"{accountName}\" n'existe pas.");
            }
        }
}