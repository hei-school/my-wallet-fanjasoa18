using System;

class Program
{
    static void Main()
    {
        string choice;

        do
        {
            DisplayMenu();

            Console.Write("Choisissez une option: ");
            choice = Console.ReadLine();

            switch (choice)
            {
                case "1":
                    AccountModule.CreateAccount();
                    break;
                case "2":
                    BalanceModule.CheckBalance();
                    break;
                case "3":
                    TransactionModule.DepositMoney();
                    break;
                case "4":
                    TransactionModule.WithdrawMoney();
                    break;
                case "5":
                    TransactionModule.ShowTransactionHistory();
                    break;
                case "6":
                    ConversionModule.ConvertCurrency();
                    break;
                case "7":
                    Console.WriteLine("Merci d'avoir utilisé le programme. Au revoir!");
                    break;
                default:
                    Console.WriteLine("Option invalide. Veuillez réessayer.");
                    break;
            }

        } while (choice != "7");
    }

    static void DisplayMenu()
    {
        Console.WriteLine("\nMenu:");
        Console.WriteLine("1. Creation de compte");
        Console.WriteLine("2. Consultation de solde");
        Console.WriteLine("3. Depot");
        Console.WriteLine("4. Retrait");
        Console.WriteLine("5. Historique des transactions");
        Console.WriteLine("6. Conversion de devises");
        Console.WriteLine("7. Quitter");
    }
}
