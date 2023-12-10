#include <stdio.h>
#include <stdlib.h>
#include "utils.h"

void convertCurrency() {
    double amount;
    printf("Entrez le montant à convertir: ");
    scanf("%lf", &amount);

    char currentCurrency[4];
    printf("Entrez la devise actuelle (ar/frc): ");
    scanf("%s", currentCurrency);

    char targetCurrency[4];
    printf("Entrez la devise cible (ar/frc): ");
    scanf("%s", targetCurrency);

    // Logique pour convertir la devise
    double conversionRate = conversionRate(currentCurrency, targetCurrency);
    double convertedAmount = amount * conversionRate;

    // Enregistrer la transaction de conversion
    char conversionTransaction[200];
    snprintf(conversionTransaction, sizeof(conversionTransaction), "Date=%s;Montant=%.2lf %s -> %.2lf %s\n",
             getCurrentDate(), amount, currentCurrency, convertedAmount, targetCurrency);
    appendToFile(getTransactionFilePath(), conversionTransaction);

    printf("Montant converti: %.2lf %s\n", convertedAmount, targetCurrency);
}