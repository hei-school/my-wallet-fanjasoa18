#include <stdio.h>
#include <stdlib.h>
#include "utils.h"

void checkBalance() {
    char accountName[100];
    printf("Entrez le nom du compte : ");
    scanf("%s", accountName);

    char transactions[1000];  // Assurez-vous d'ajuster la taille selon vos besoins
    readFromFile(getTransactionFilePath(), transactions);

    WalletInfo walletInfo = calculateBalance(transactions, accountName);

    if (walletInfo.exists) {
        printf("Solde actuel du compte \"%s\":\n", accountName);
        printf("- Argent: %d\n", walletInfo.argent);
        printf("- Cartes Bancaires: %d\n", walletInfo.cartesBancaires);
        printf("- Permis de Conduire: %d\n", walletInfo.permisDeConduire);
        printf("- Photos d'Identité: %d\n", walletInfo.photosIdentite);
        printf("- Cartes de Visite: %d\n", walletInfo.cartesVisite);
    } else {
        printf("Le compte \"%s\" n'existe pas.\n", accountName);
    }
}