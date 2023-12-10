const fs = require('fs');


function getCurrentDate() {
    const now = new Date();
    const day = now.getDate();
    const month = now.getMonth() + 1;
    const year = now.getFullYear();

    return `${year}-${month}-${day}`;
}

function calculateBalance(transactions, accountName) {
    const lines = transactions.split('\n');
    let argent = 0;
    let cartesBancaires = 0;
    let permisDeConduire = 0;
    let photosIdentite = 0;
    let cartesVisite = 0;

    for (const line of lines) {
        const transactionDetails = parseTransaction(line);

        if (transactionDetails && transactionDetails.Compte === accountName) {
            // Mettre à jour les objets lors de la création du compte
            if (transactionDetails.Type === 'Création') {
                argent = parseFloat(transactionDetails.Argent || '0', 50000);
                cartesBancaires = parseInt(transactionDetails.CarteBancaire || '0', 10);
                permisDeConduire = parseInt(transactionDetails.Permis || '0', 5);
                photosIdentite = parseInt(transactionDetails.PhotosIdentite || '0', 3);
                cartesVisite = parseInt(transactionDetails.CartesVisite || '0', 10);
            } else {
                // Mettre à jour les objets en fonction du type de transaction
                switch (transactionDetails.Type) {
                    case 'Dépôt':
                        
                        if (transactionDetails.Categorie === 'Argent') {
                            argent += parseFloat(transactionDetails.Montant);
                        }
                        if (transactionDetails.Categorie === 'CarteBancaire') {
                            cartesBancaires += parseInt(transactionDetails.Montant, 10);
                        } else if (transactionDetails.Categorie === 'Permis') {
                            permisDeConduire += parseInt(transactionDetails.Montant, 5);
                        } else if (transactionDetails.Categorie === 'PhotosIdentite') {
                            photosIdentite += parseInt(transactionDetails.Montant, 5);
                        } else if (transactionDetails.Categorie === 'CartesVisite') {
                            cartesVisite += parseInt(transactionDetails.Montant, 10);
                        }
                        // Ajoutez d'autres catégories ici au besoin
                        break;
                    case 'Retrait':
                        // Logique pour traiter les retraits d'argent et d'autres catégories
                        if (transactionDetails.Categorie === 'Argent') {
                            argent = Math.max(0, argent - parseFloat(transactionDetails.Montant) || '0');
                        }
                        if (transactionDetails.Categorie === 'CarteBancaire') {
                            cartesBancaires = Math.max(0, cartesBancaires - parseInt(transactionDetails.Montant, 10));
                        } else if (transactionDetails.Categorie === 'Permis') {
                            permisDeConduire = Math.max(0, permisDeConduire - parseInt(transactionDetails.Montant, 5));
                        } else if (transactionDetails.Categorie === 'PhotosIdentite') {
                            photosIdentite = Math.max(0, photosIdentite - parseInt(transactionDetails.Montant, 5));
                        } else if (transactionDetails.Categorie === 'CartesVisite') {
                            cartesVisite = Math.max(0, cartesVisite - parseInt(transactionDetails.Montant, 10));
                        }
                        // Ajoutez d'autres catégories ici au besoin
                        break;
                    default:
                        break;
                }
            }
        }
    }

    return { argent, cartesBancaires, permisDeConduire, photosIdentite, cartesVisite };
}

function getCurrentBalance(accountName) {
    const transactions = fs.readFileSync(getTransactionFilePath(), 'utf8');
    const walletInfo = calculateBalance(transactions, accountName);

    return walletInfo;
}

function parseTransaction(line) {
    const transactionDetails = {};
    const keyValuePairs = line.split(';');

    for (const pair of keyValuePairs) {
        const [key, value] = pair.split('=');
        transactionDetails[key] = value;
    }

    return transactionDetails;
}

function getTransactionFilePath() {
    return 'transactions.txt';
}

function accountExists(transactions, accountName) {
    const lines = transactions.split('\n');

    for (const line of lines) {
        const transactionDetails = parseTransaction(line);

        if (transactionDetails && transactionDetails.Compte === accountName) {
            return true;
        }
    }

    return false;
}

function conversionRate(currentCurrency, targetCurrency) {
    // Taux de conversion par défaut (1:1)
    let tauxDeConversion = 1;

    // Définir les taux de conversion en fonction de la relation donnée
    if (currentCurrency === 'ar' && targetCurrency === 'frc') {
        tauxDeConversion = 5;
    } else if (currentCurrency === 'frc' && targetCurrency === 'ar') {
        tauxDeConversion = 1 / 5;
    }

    return tauxDeConversion;
}

module.exports = {
    getCurrentDate,
    calculateBalance,
    getCurrentBalance,
    parseTransaction,
    getTransactionFilePath,
    accountExists,
    conversionRate
};