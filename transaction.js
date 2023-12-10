const readlineSync = require('readline-sync');
const fs = require('fs');
const utils = require('./utils');
const { getCategoryLimit } = require('./account');

const categories = ['Argent','CarteBancaire', 'Permis', 'PhotosIdentité', 'CartesVisite'];


function depositMoney() {
    const accountName = readlineSync.question('Entrez le nom du compte : ');

    // Demander la catégorie d'objet pour le dépôt
    const depositCategory = readlineSync.keyInSelect(['Argent','CarteBancaire', 'Permis', 'PhotosIdentite', 'CartesVisite'], 'Sélectionnez la catégorie pour le dépôt : ');

    if (depositCategory === -1) {
        console.log('Opération annulée.');
        return;
    }

    let depositAmount = 0;
    const currentBalance = utils.getCurrentBalance(accountName);

    switch (depositCategory) {
        case 0: // Argent
            const currentArgent = currentBalance.argent || 0;
            const limit = getCategoryLimit('Argent');
            depositAmount = parseFloat(readlineSync.question(`Entrez le montant de Argent à déposer (limite: ${limit - currentArgent}) : `), 10);
            
            if (currentArgent + depositAmount > limit) {
                console.log(`Le montant total de Argent dépasse la limite de ${limit}. Opération annulée.`);
                return;
            }
        //depositAmount = parseFloat(readlineSync.question(`Entrez le montant de ${categories[depositCategory]} à déposer : `), 50000);
        break;
        case 1: // CarteBancaire
            const currentCarte = currentBalance.cartesBancaires || 0;
            const limitCarte = getCategoryLimit('CarteBancaire');
            depositAmount = parseFloat(readlineSync.question(`Entrez le nombre de carte bancaire à déposer (limite: ${limitCarte - currentBalance}) : `), 10);
            
            if (currentCarte + depositAmount > limit) {
                console.log(`Le nombre dépasse la limite de ${limitCarte}. Opération annulée.`);
                return;
            }
            depositAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[depositCategory]} à déposer : `), 10);
            break;
        case 2: // Permis
            const currentPermis = currentBalance.permisDeConduire || 0;
            const limitPermis = getCategoryLimit('Permis');
            depositAmount = parseFloat(readlineSync.question(`Entrez le nombre de permis à déposer (limite: ${limitPermis - currentBalance}) : `), 10);
            
            if (currentPermis + depositAmount > limit) {
                console.log(`Le nombre dépasse la limite de ${limitPermis}. Opération annulée.`);
                return;
            }
            depositAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[depositCategory]} à déposer : `), 5);
            break;
        case 3: // PhotosIdentite
            const currentPhoto = currentBalance.photosIdentite || 0;
            const limitPhoto = getCategoryLimit('PhotosIdentite');
            depositAmount = parseFloat(readlineSync.question(`Entrez le nombre de photo à déposer (limite: ${limitPhoto - currentBalance}) : `), 10);
        
            if (currentPhoto + depositAmount > limit) {
                console.log(`Le nombre dépasse la limite de ${limitPhoto}. Opération annulée.`);
                return;
            }
            depositAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[depositCategory]} à déposer : `), 3);
            break;
        case 4: // CartesVisite
            const currentVisite = currentBalance.cartesVisite || 0;
            const limitVisite = getCategoryLimit('CartesVisite');
            depositAmount = parseFloat(readlineSync.question(`Entrez le nombre de carte de visite à déposer (limite: ${limitVisite - currentBalance}) : `), 10);
        
            if (currentVisite + depositAmount > limit) {
                console.log(`Le nombre dépasse la limite de ${limitVisite}. Opération annulée.`);
                return;
            }
            depositAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[depositCategory]} à déposer : `), 10);
            break;
        default:
            break;
    }

    // Enregistrer la transaction de dépôt
    const transaction = `Date=${utils.getCurrentDate()};Type=Dépôt;Compte=${accountName};Categorie=${categories[depositCategory]};Montant=${depositAmount}\n`;
    fs.appendFileSync(utils.getTransactionFilePath(), transaction);

    console.log('Dépôt effectué avec succès !');
}


function withdrawMoney() {
    const accountName = readlineSync.question('Entrez le nom du compte : ');

    // Demander la catégorie d'objet pour le retrait
    const withdrawalCategory = readlineSync.keyInSelect(['Argent', 'CarteBancaire', 'Permis', 'PhotosIdentite', 'CartesVisite'], 'Sélectionnez la catégorie pour le retrait : ');

    if (withdrawalCategory === -1) {
        console.log('Opération annulée.');
        return;
    }

    let withdrawalAmount;

    const currentBalance = utils.getCurrentBalance(accountName);
    let currentAmount = currentBalance[categories[withdrawalCategory]] || 0;
    const limit = getCategoryLimit(categories[withdrawalCategory]);

    do {
        // Demander le montant à retirer
        switch (withdrawalCategory) {
            case 0: // Argent
                withdrawalAmount = parseFloat(readlineSync.question(`Entrez le montant de ${categories[withdrawalCategory]} à retirer : `), 10);
                break;
            case 1: // CarteBancaire
                withdrawalAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[withdrawalCategory]} à retirer : `), 10);
                break;
            case 2: // Permis
                withdrawalAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[withdrawalCategory]} à retirer : `), 5);
                break;
            case 3: // PhotosIdentite
                withdrawalAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[withdrawalCategory]} à retirer : `), 3);
                break;
            case 4: // CartesVisite
                withdrawalAmount = parseInt(readlineSync.question(`Entrez le nombre de ${categories[withdrawalCategory]} à retirer : `), 10);
                break;
            default:
                break;
        }

        // Vérifier si le montant à retirer dépasse la limite
        if (withdrawalAmount > limit) {
            console.log(`Le montant à retirer dépasse la limite de ${limit}. Entrez une nouvelle valeur.`);
        }
    } while (withdrawalAmount > currentAmount || withdrawalAmount > limit);

    // Enregistrer la transaction de retrait
    const transaction = `Date=${utils.getCurrentDate()};Type=Retrait;Compte=${accountName};Categorie=${categories[withdrawalCategory]};Montant=${withdrawalAmount}\n`;
    fs.appendFileSync(utils.getTransactionFilePath(), transaction);

    console.log('Retrait effectué avec succès !');
}


function showTransactionHistory() {
    const accountName = readlineSync.question('Entrez le nom du compte : ');

    const transactions = fs.readFileSync(utils.getTransactionFilePath(), 'utf8');

    // Vérifier si le compte existe avant d'afficher l'historique
    if (utils.accountExists(transactions, accountName)) {
        // Les trois dernières transactions
        const lastThreeTransactions = transactions.trim().split('\n').slice(-3);

        if (lastThreeTransactions.length > 0) {
            console.log('Les trois dernières transactions :');

            for (const transaction of lastThreeTransactions) {
                const transactionDetails = utils.parseTransaction(transaction);

                // Afficher les détails de la transaction en fonction du type
                switch (transactionDetails.Type) {
                    case 'Création':
                        console.log(`- Type: ${transactionDetails.Type}, Montant initial: ${transactionDetails.Argent}`);
                        break;
                    case 'Dépôt':
                    case 'Retrait':                                        
                        console.log(`- Type: ${transactionDetails.Type}, Categorie: ${transactionDetails.Categorie}, Montant: ${transactionDetails.Montant}`);
                        break;
                    default:
                        break;
                }
            }
        } else {
            console.log('Aucune transaction effectuée jusqu\'à présent.');
        }
    } else {
        console.log(`Le compte "${accountName}" n'existe pas.`);
    }
}


module.exports = {
    depositMoney,
    withdrawMoney,
    showTransactionHistory
};
