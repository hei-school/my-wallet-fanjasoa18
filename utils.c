#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils.h"
#include "account.h"

char* getCurrentDate() {
    time_t t;
    struct tm* tm_info;

    time(&t);
    tm_info = localtime(&t);

    char* date = (char*)malloc(11 * sizeof(char));
    snprintf(date, 11, "%04d-%02d-%02d", tm_info->tm_year + 1900, tm_info->tm_mon + 1, tm_info->tm_mday);

    return date;
}

Balance calculateBalance(char* transactions, char* accountName) {
    char* lines = strtok(transactions, "\n");

    double argent = 0;
    int cartesBancaires = 0;
    int permisDeConduire = 0;
    int photosIdentite = 0;
    int cartesVisite = 0;

    while (lines != NULL) {
        char* transactionDetails = parseTransaction(lines);

        if (transactionDetails != NULL && strcmp(transactionDetails.Compte, accountName) == 0) {
            // Mettre à jour les objets lors de la création du compte
            if (strcmp(transactionDetails.Type, "Création") == 0) {
                sscanf(transactionDetails.Argent, "%lf", &argent);
                sscanf(transactionDetails.CarteBancaire, "%d", &cartesBancaires);
                sscanf(transactionDetails.Permis, "%d", &permisDeConduire);
                sscanf(transactionDetails.PhotosIdentite, "%d", &photosIdentite);
                sscanf(transactionDetails.CartesVisite, "%d", &cartesVisite);
            } else {
                // Mettre à jour les objets en fonction du type de transaction
                if (strcmp(transactionDetails.Type, "Dépôt") == 0) {
                    if (strcmp(transactionDetails.Categorie, "Argent") == 0) {
                        double montant;
                        sscanf(transactionDetails.Montant, "%lf", &montant);
                        argent += montant;
                    } else if (strcmp(transactionDetails.Categorie, "CarteBancaire") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        cartesBancaires += montant;
                    } else if (strcmp(transactionDetails.Categorie, "Permis") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        permisDeConduire += montant;
                    } else if (strcmp(transactionDetails.Categorie, "PhotosIdentite") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        photosIdentite += montant;
                    } else if (strcmp(transactionDetails.Categorie, "CartesVisite") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        cartesVisite += montant;
                    }
                    // Ajoutez d'autres catégories ici au besoin
                } else if (strcmp(transactionDetails.Type, "Retrait") == 0) {
                    // Logique pour traiter les retraits d'argent et d'autres catégories
                    if (strcmp(transactionDetails.Categorie, "Argent") == 0) {
                        double montant;
                        sscanf(transactionDetails.Montant, "%lf", &montant);
                        argent = fmax(0, argent - montant);
                    } else if (strcmp(transactionDetails.Categorie, "CarteBancaire") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        cartesBancaires = fmax(0, cartesBancaires - montant);
                    } else if (strcmp(transactionDetails.Categorie, "Permis") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        permisDeConduire = fmax(0, permisDeConduire - montant);
                    } else if (strcmp(transactionDetails.Categorie, "PhotosIdentite") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        photosIdentite = fmax(0, photosIdentite - montant);
                    } else if (strcmp(transactionDetails.Categorie, "CartesVisite") == 0) {
                        int montant;
                        sscanf(transactionDetails.Montant, "%d", &montant);
                        cartesVisite = fmax(0, cartesVisite - montant);
                    }
                    // Ajoutez d'autres catégories ici au besoin
                }
            }
        }
        lines = strtok(NULL, "\n");
    }

    Balance walletInfo;
    walletInfo.argent = argent;
    walletInfo.cartesBancaires = cartesBancaires;
    walletInfo.permisDeConduire = permisDeConduire;
    walletInfo.photosIdentite = photosIdentite;
    walletInfo.cartesVisite = cartesVisite;

    return walletInfo;
}

Balance getCurrentBalance(char* accountName) {
    char transactions[1000];
    readFromFile(getTransactionFilePath(), transactions);
    Balance walletInfo = calculateBalance(transactions, accountName);

    return walletInfo;
}

Transaction parseTransaction(char* line) {
    Transaction transactionDetails;
    char* keyValuePairs = strtok(line, ";");

    while (keyValuePairs != NULL) {
        char key[50];
        char value[50];
        sscanf(keyValuePairs, "%[^=]=%s", key, value);

        if (strcmp(key, "Date") == 0) {
            strcpy(transactionDetails.Date, value);
        } else if (strcmp(key, "Type") == 0) {
            strcpy(transactionDetails.Type, value);
        } else if (strcmp(key, "Compte") == 0) {
            strcpy(transactionDetails.Compte, value);
        } else if (strcmp(key, "Categorie") == 0) {
            strcpy(transactionDetails.Categorie, value);
        } else if (strcmp(key, "Montant") == 0) {
            strcpy(transactionDetails.Montant, value);
        }

        keyValuePairs = strtok(NULL, ";");
    }

    return transactionDetails;
}

char* getTransactionFilePath() {
    return "transactions.txt";
}

int accountExists(char* transactions, char* accountName) {
    char* lines = strtok(transactions, "\n");

    while (lines != NULL) {
        Transaction transactionDetails = parseTransaction(lines);

        if (strcmp(transactionDetails.Compte, accountName) == 0) {
            return 1;
        }
        lines = strtok(NULL, "\n");
    }

    return 0;
}

double conversionRate(char* currentCurrency, char* targetCurrency) {
    // Taux de conversion par défaut (1:1)
    double tauxDeConversion = 1;

    // Définir les taux de conversion en fonction de la relation donnée
    if (strcmp(currentCurrency, "ar") == 0 && strcmp(targetCurrency, "frc") == 0) {
        tauxDeConversion = 5;
    } else if (strcmp(currentCurrency, "frc") == 0 && strcmp(targetCurrency, "ar") == 0) {
        tauxDeConversion = 1 / 5;
    }

    return tauxDeConversion;
}
