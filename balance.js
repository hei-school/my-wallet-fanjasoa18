const readlineSync = require('readline-sync');
const fs = require('fs');
const utils = require('./utils');

function checkBalance() {
    const accountName = readlineSync.question('Entrez le nom du compte: ');

    const transactions = fs.readFileSync(utils.getTransactionFilePath(), 'utf8');
    const walletInfo = utils.calculateBalance(transactions, accountName);

    if (walletInfo !== undefined) {
        console.log(`Solde actuel du compte "${accountName}":`);
        console.log(`- Argent: ${walletInfo.argent}`);
        console.log(`- Cartes Bancaires: ${walletInfo.cartesBancaires}`);
        console.log(`- Permis de Conduire: ${walletInfo.permisDeConduire}`);
        console.log(`- Photos d'Identité: ${walletInfo.photosIdentite}`);
        console.log(`- Cartes de Visite: ${walletInfo.cartesVisite}`);
    } else {
        console.log(`Le compte "${accountName}" n'existe pas.`);
    }
}

module.exports = {
    checkBalance
};
