#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils.h"
#include "account.h"

void depositMoney() {
    char accountName[50];
    printf("Entrez le nom du compte : ");
    scanf("%s", accountName);

    // Demander la catégorie d'objet pour le dépôt
    char* depositCategories[] = {"Argent", "CarteBancaire", "Permis", "PhotosIdentite", "CartesVisite"};
    int depositCategory;
    printf("Sélectionnez la catégorie pour le dépôt : \n");
    for (int i = 0; i < sizeof(depositCategories) / sizeof(depositCategories[0]); i++) {
        printf("%d. %s\n", i + 1, depositCategories[i]);
    }
    scanf("%d", &depositCategory);
    depositCategory--;

    if (depositCategory == -1) {
        printf("Opération annulée.\n");
        return;
    }

    double depositAmount = 0;
    Balance currentBalance = getCurrentBalance(accountName);

    switch (depositCategory) {
        case 0: // Argent
            double currentArgent = currentBalance.argent;
            double limitArgent = getCategoryLimit("Argent");
            printf("Entrez le montant de Argent à déposer (limite : %.2lf) : ", limitArgent - currentArgent);
            scanf("%lf", &depositAmount);

            if (currentArgent + depositAmount > limitArgent) {
                printf("Le montant total de Argent dépasse la limite de %.2lf. Opération annulée.\n", limitArgent);
                return;
            }
            break;
        case 1: // CarteBancaire
            int currentCarte = currentBalance.cartesBancaires;
            int limitCarte = getCategoryLimit("CarteBancaire");
            printf("Entrez le nombre de carte bancaire à déposer (limite : %d) : ", limitCarte - currentCarte);
            scanf("%d", &depositAmount);

            if (currentCarte + depositAmount > limitCarte) {
                printf("Le nombre dépasse la limite de %d. Opération annulée.\n", limitCarte);
                return;
            }
            break;
        case 2: // Permis
            int currentPermis = currentBalance.permisDeConduire;
            int limitPermis = getCategoryLimit("Permis");
            printf("Entrez le nombre de permis à déposer (limite : %d) : ", limitPermis - currentPermis);
            scanf("%d", &depositAmount);

            if (currentPermis + depositAmount > limitPermis) {
                printf("Le nombre dépasse la limite de %d. Opération annulée.\n", limitPermis);
                return;
            }
            break;
        case 3: // PhotosIdentite
            int currentPhoto = currentBalance.photosIdentite;
            int limitPhoto = getCategoryLimit("PhotosIdentite");
            printf("Entrez le nombre de photo à déposer (limite : %d) : ", limitPhoto - currentPhoto);
            scanf("%d", &depositAmount);

            if (currentPhoto + depositAmount > limitPhoto) {
                printf("Le nombre dépasse la limite de %d. Opération annulée.\n", limitPhoto);
                return;
            }
            break;
        case 4: // CartesVisite
            int currentVisite = currentBalance.cartesVisite;
            int limitVisite = getCategoryLimit("CartesVisite");
            printf("Entrez le nombre de carte de visite à déposer (limite : %d) : ", limitVisite - currentVisite);
            scanf("%d", &depositAmount);

            if (currentVisite + depositAmount > limitVisite) {
                printf("Le nombre dépasse la limite de %d. Opération annulée.\n", limitVisite);
                return;
            }
            break;
        default:
            break;
    }

    // Enregistrer la transaction de dépôt
    char transaction[200];
    snprintf(transaction, sizeof(transaction), "Date=%s;Type=Dépôt;Compte=%s;Categorie=%s;Montant=%.2lf\n",
             getCurrentDate(), accountName, depositCategories[depositCategory], depositAmount);
    appendToFile(getTransactionFilePath(), transaction);

    printf("Dépôt effectué avec succès !\n");
}

void withdrawMoney() {
    char accountName[50];
    printf("Entrez le nom du compte : ");
    scanf("%s", accountName);

    // Demander la catégorie d'objet pour le retrait
    char* withdrawalCategories[] = {"Argent", "CarteBancaire", "Permis", "PhotosIdentite", "CartesVisite"};
    int withdrawalCategory;
    printf("Sélectionnez la catégorie pour le retrait : \n");
    for (int i = 0; i < sizeof(withdrawalCategories) / sizeof(withdrawalCategories[0]); i++) {
        printf("%d. %s\n", i + 1, withdrawalCategories[i]);
    }
    scanf("%d", &withdrawalCategory);
    withdrawalCategory--;

    if (withdrawalCategory == -1) {
        printf("Opération annulée.\n");
        return;
    }

    double withdrawalAmount;

    Balance currentBalance = getCurrentBalance(accountName);
    double currentAmount = 0;

    switch (withdrawalCategory) {
        case 0: // Argent
            currentAmount = currentBalance.argent;
            break;
        case 1: // CarteBancaire
            currentAmount = currentBalance.cartesBancaires;
            break;
        case 2: // Permis
            currentAmount = currentBalance.permisDeConduire;
            break;
        case 3: // PhotosIdentite
            currentAmount = currentBalance.photosIdentite;
            break;
        case 4: // CartesVisite
            currentAmount = currentBalance.cartesVisite;
            break;
        default:
            break;
    }

    double limit = getCategoryLimit(withdrawalCategories[withdrawalCategory]);

    do {
        // Demander le montant à retirer
        switch (withdrawalCategory) {
            case 0: // Argent
                printf("Entrez le montant de Argent à retirer : ");
                scanf("%lf", &withdrawalAmount);
                break;
            case 1: // CarteBancaire
                printf("Entrez le nombre de CarteBancaire à retirer : ");
                scanf("%lf", &withdrawalAmount);
                break;
            case 2: // Permis
                printf("Entrez le nombre de Permis à retirer : ");
                scanf("%lf", &withdrawalAmount);
                break;
            case 3: // PhotosIdentite
                printf("Entrez le nombre de PhotosIdentite à retirer : ");
                scanf("%lf", &withdrawalAmount);
                break;
            case 4: // CartesVisite
                printf("Entrez le nombre de CartesVisite à retirer : ");
                scanf("%lf", &withdrawalAmount);
                break;
            default:
                break;
        }

        // Vérifier si le montant à retirer dépasse la limite
        if (withdrawalAmount > limit) {
            printf("Le montant à retirer dépasse la limite de %.2lf. Entrez une nouvelle valeur.\n", limit);
        }
    } while (withdrawalAmount > currentAmount || withdrawalAmount > limit);

    // Enregistrer la transaction de retrait
    char transaction[200];
    snprintf(transaction, sizeof(transaction), "Date=%s;Type=Retrait;Compte=%s;Categorie=%s;Montant=%.2lf\n",
             getCurrentDate(), accountName, withdrawalCategories[withdrawalCategory], withdrawalAmount);
    appendToFile(getTransactionFilePath(), transaction);

    printf("Retrait effectué avec succès !\n");
}

void showTransactionHistory() {
    char accountName[50];
    printf("Entrez le nom du compte : ");
    scanf("%s", accountName);

    char transactions[1000];
    readFromFile(getTransactionFilePath(), transactions);

    // Vérifier si le compte existe avant d'afficher l'historique
    if (accountExists(transactions, accountName)) {
        // Les trois dernières transactions
        char* lastThreeTransactions[3];
        char* token = strtok(transactions, "\n");
        int transactionCount = 0;

        while (token != NULL) {
            if (transactionCount >= 3) {
                break;
            }
            lastThreeTransactions[transactionCount++] = token;
            token = strtok(NULL, "\n");
        }

        if (transactionCount > 0) {
            printf("Les trois dernières transactions :\n");

            for (int i = 0; i < transactionCount; i++) {
                char* transactionDetails = parseTransaction(lastThreeTransactions[i]);

                // Afficher les détails de la transaction en fonction du type
                char transactionType[MAX_CATEGORY_LENGTH];
                sscanf(transactionDetails, "Type=%s", transactionType);

                if (strcmp(transactionType, "Création") == 0) {
                    char argentDetails[MAX_CATEGORY_LENGTH];
                    sscanf(transactionDetails, "Argent=%s", argentDetails);
                    printf("- Type: %s, Montant initial: %s\n", transactionType, argentDetails);
                } else if (strcmp(transactionType, "Dépôt") == 0 || strcmp(transactionType, "Retrait") == 0) {
                    char categoryDetails[MAX_CATEGORY_LENGTH];
                    sscanf(transactionDetails, "Categorie=%s", categoryDetails);
                    char amountDetails[MAX_CATEGORY_LENGTH];
                    sscanf(transactionDetails, "Montant=%s", amountDetails);
                    printf("- Type: %s, Categorie: %s, Montant: %s\n", transactionType, categoryDetails, amountDetails);
                }
            }
        } else {
            printf("Aucune transaction effectuée jusqu'à présent.\n");
        }
    } else {
        printf("Le compte \"%s\" n'existe pas.\n", accountName);
    }
}
