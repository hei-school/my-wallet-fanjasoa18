#include <stdio.h>
#include <stdlib.h>
#include "utils.h"

void createAccount() {
    char accountName[100];
    printf("Entrez le nom du compte: ");
    scanf("%s", accountName);

    AccountInfo accountInfo;
    strcpy(accountInfo.accountName, accountName);
    accountInfo.Argent = 0;
    accountInfo.CarteBancaire = 0;
    accountInfo.Permis = 0;
    accountInfo.PhotosIdentite = 0;
    accountInfo.CartesVisite = 0;

    // Demander le nombre d'objets de chaque catégorie
    const char* categories[] = {"Argent", "CarteBancaire", "Permis", "PhotosIdentite", "CartesVisite"};

    for (int i = 0; i < sizeof(categories) / sizeof(categories[0]); i++) {
        accountInfo.values[i] = readLimitedNumber(categories[i], getCategoryLimit(categories[i]));
    }

    // Enregistrer la transaction complète dans le fichier de transactions
    char transaction[200];
    snprintf(transaction, sizeof(transaction), "Date=%s;Type=Création;Compte=%s;Argent=%d;CarteBancaire=%d;Permis=%d;PhotosIdentite=%d;CartesVisite=%d\n",
             getCurrentDate(), accountInfo.accountName, accountInfo.Argent, accountInfo.CarteBancaire, accountInfo.Permis, accountInfo.PhotosIdentite, accountInfo.CartesVisite);
    appendToFile(getTransactionFilePath(), transaction);

    printf("Compte créé avec succès!\n");
}

int getCategoryLimit(const char* category) {
    if (strcmp(category, "Argent") == 0) {
        return 50000;
    } else if (strcmp(category, "CarteBancaire") == 0) {
        return 10;
    } else if (strcmp(category, "Permis") == 0) {
        return 5;
    } else if (strcmp(category, "PhotosIdentite") == 0) {
        return 3;
    } else if (strcmp(category, "CartesVisite") == 0) {
        return 10;
    } else {
        return 0;
    }
}

int readLimitedNumber(const char* category, int limit) {
    int value;

    do {
        printf("Entrez l'unité initiale de %s (limite: %d) : ", category, limit);
        scanf("%d", &value);

        if (value > limit) {
            printf("La valeur ne doit pas dépasser la limite de %d. Veuillez réessayer.\n", limit);
        }
    } while (value > limit);

    return value;
}