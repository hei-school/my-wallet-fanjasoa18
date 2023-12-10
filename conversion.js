const readlineSync = require('readline-sync');
const fs = require('fs');
const utils = require('./utils');

function convertCurrency() {
    const amount = parseFloat(readlineSync.question('Entrez le montant à convertir: '));
    const currentCurrency = readlineSync.question('Entrez la devise actuelle (ar/frc): ');
    const targetCurrency = readlineSync.question('Entrez la devise cible (ar/frc): ');

    // Logique pour convertir la devise
    const conversionRate = utils.conversionRate(currentCurrency, targetCurrency);
    const convertedAmount = amount * conversionRate;

    // Enregistrer la transaction de conversion
    const conversionTransaction = `Date=${utils.getCurrentDate()};Montant=${amount} ${currentCurrency} -> ${convertedAmount} ${targetCurrency}\n`;
    fs.appendFileSync(utils.getTransactionFilePath(), conversionTransaction);

    console.log(`Montant converti: ${convertedAmount} ${targetCurrency}`);
}
module.exports = {
    convertCurrency
};