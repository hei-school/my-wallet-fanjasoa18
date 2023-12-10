#include <stdio.h>
#include <stdlib.h>
#include "account.c"
#include "balance.c"
#include "transaction.c"
#include "conversion.c"

void mainMenu();

int main() {
    char choice;

    do {
        mainMenu();

        printf("Choisissez une option : ");
        scanf(" %c", &choice);

        switch (choice) {
            case '1':
                createAccount();
                break;
            case '2':
                checkBalance();
                break;
            case '3':
                depositMoney();
                break;
            case '4':
                withdrawMoney();
                break;
            case '5':
                showTransactionHistory();
                break;
            case '6':
                convertCurrency();
                break;
            case '7':
                printf("Merci d'avoir utilisé le programme. Au revoir!\n");
                break;
            default:
                printf("Option invalide. Veuillez réessayer.\n");
                break;
        }
    } while (choice != '7');

    return 0;
}

void mainMenu() {
    printf("\nMenu :\n");
    printf("1. Creation de compte\n");
    printf("2. Consultation de solde\n");
    printf("3. Depot\n");
    printf("4. Retrait\n");
    printf("5. Historique des transactions\n");
    printf("6. Conversion de devises\n");
    printf("7. Quitter\n");
}
