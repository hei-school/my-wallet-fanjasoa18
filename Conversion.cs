using System;

class Conversion
{
    private readonly Utils _utils;

    public Conversion(Utils utils)
    {
        _utils = utils;
    }

    public void ConvertCurrency()
    {
        Console.Write("Entrez le montant à convertir : ");
        double amount;
        while (!double.TryParse(Console.ReadLine(), out amount))
        {
            Console.WriteLine("Veuillez entrer un montant valide.");
        }

        Console.Write("Entrez la devise actuelle (ar/frc) : ");
        string currentCurrency = Console.ReadLine();

        Console.Write("Entrez la devise cible (ar/frc) : ");
        string targetCurrency = Console.ReadLine();

        // Logique pour convertir la devise
        double conversionRate = _utils.ConversionRate(currentCurrency, targetCurrency);
        double convertedAmount = amount * conversionRate;

        // Enregistrer la transaction de conversion
        string conversionTransaction = $"Date={_utils.GetCurrentDate()};Montant={amount} {currentCurrency} -> {convertedAmount} {targetCurrency}\n";
        System.IO.File.AppendAllText(_utils.GetTransactionFilePath(), conversionTransaction);

        Console.WriteLine($"Montant converti : {convertedAmount} {targetCurrency}");
    }
}
