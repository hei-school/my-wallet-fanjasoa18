using System;
using System.IO;

class Balance
{
    private readonly Utils _utils;

    public Balance(Utils utils)
    {
        _utils = utils;
    }

    public void CheckBalance()
    {
        Console.Write("Entrez le nom du compte : ");
        string accountName = Console.ReadLine();

        string transactions = File.ReadAllText(_utils.GetTransactionFilePath());
        WalletInfo walletInfo = _utils.CalculateBalance(transactions, accountName);

        if (walletInfo != null)
        {
            Console.WriteLine($"Solde actuel du compte \"{accountName}\":");
            Console.WriteLine($"- Argent: {walletInfo.Argent}");
            Console.WriteLine($"- Cartes Bancaires: {walletInfo.CartesBancaires}");
            Console.WriteLine($"- Permis de Conduire: {walletInfo.PermisDeConduire}");
            Console.WriteLine($"- Photos d'Identité: {walletInfo.PhotosIdentite}");
            Console.WriteLine($"- Cartes de Visite: {walletInfo.CartesVisite}");
        }
        else
        {
            Console.WriteLine($"Le compte \"{accountName}\" n'existe pas.");
        }
    }
}
