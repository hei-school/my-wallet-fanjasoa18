const readlineSync = require('readline-sync');
const fs = require('fs');
const utils = require('./utils');

function createAccount() {
    const accountName = readlineSync.question('Entrez le nom du compte: ');
    //const initialBalance = parseFloat(readlineSync.question('Entrez le solde initial: '));

    const accountInfo = {
        accountName,
        Argent: 0,
        CarteBancaire: 0,
        Permis: 0,
        PhotosIdentite: 0,
        CartesVisite: 0,
    };

    // Demander le nombre d'objets de chaque catégorie
    const categories = ['Argent','CarteBancaire', 'Permis', 'PhotosIdentite', 'CartesVisite'];

    for (const category of categories) {
        accountInfo[category] = readLimitedNumber(category, getCategoryLimit(category));
    }

    // Enregistrer la transaction complète dans le fichier de transactions
    const transaction = `Date=${utils.getCurrentDate()};Type=Création;Compte=${accountName};Argent=${accountInfo['Argent']};CarteBancaire=${accountInfo['CarteBancaire']};Permis=${accountInfo['Permis']};PhotosIdentite=${accountInfo['PhotosIdentite']};CartesVisite=${accountInfo['CartesVisite']}\n`;
    fs.appendFileSync(utils.getTransactionFilePath(), transaction);

    console.log('Compte créé avec succès!');
}

// Fonction utilitaire pour obtenir la limite pour chaque catégorie
function getCategoryLimit(category) {
    switch (category) {
        case 'Argent':
            return 50000;
        case 'CarteBancaire':
            return 10;
        case 'Permis':
            return 5;
        case 'PhotosIdentite':
            return 3;
        case 'CartesVisite':
            return 10;
        default:
            return 0;
    }
}

function readLimitedNumber(category) {
    const limit = getCategoryLimit(category);
    let value;

    do {
        value = parseInt(readlineSync.question(`Entrez l'unité initiale de ${category} (limite: ${limit}) : `), 10) || 0;

        if (value > limit) {
            console.log(`La valeur ne doit pas dépasser la limite de ${limit}. Veuillez réessayer.`);
        }
    } while (value > limit);

    return value;
}


module.exports = {
    getCategoryLimit,
    readLimitedNumber,
    createAccount
};
