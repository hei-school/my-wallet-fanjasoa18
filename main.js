const readlineSync = require('readline-sync');
const accountModule = require('./account');
const balanceModule = require('./balance');
const transactionModule = require('./transaction');
const conversionModule = require('./conversion')

function main() {
    let choice;

    while (choice !== '7') {
        displayMenu();

        choice = readlineSync.question('Choisissez une option: ');

        switch (choice) {
            case '1':
                accountModule.createAccount();
                break;
            case '2':
                balanceModule.checkBalance();
                break;
            case '3':
                transactionModule.depositMoney();
                break;
            case '4':
                transactionModule.withdrawMoney();
                break;
            case '5':
                transactionModule.showTransactionHistory();
                break;
            case '6':
                conversionModule.convertCurrency();
                break;
            case '7':
                console.log('Merci d\'avoir utilisé le programme. Au revoir!');
                break;
            default:
                console.log('Option invalide. Veuillez réessayer.');
                break;
        }
    }
}

function displayMenu() {
    console.log('\nMenu:');
    console.log('1. Creation de compte');
    console.log('2. Consultation de solde');
    console.log('3. Depot');
    console.log('4. Retrait');
    console.log('5. Historique des transactions');
    console.log('6. Conversion de devises');
    console.log('7. Quitter');
}

// Exécution du programme
main();
