CREATE DATABASE IF NOT EXISTS assurance;
USE assurance;

DROP TABLE IF EXISTS demande_remboursement;
DROP TABLE IF EXISTS couverture;
DROP TABLE IF EXISTS assurance_sante;

CREATE TABLE assurance_sante (
    id_assurance INT AUTO_INCREMENT PRIMARY KEY,
    numero_contrat VARCHAR(50) NOT NULL UNIQUE,
    type_assurance ENUM('Publique','Privee','Complementaire') NOT NULL,
    nom_assureur VARCHAR(100) NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE,
    plafond_annuel DECIMAL(10,2),
    taux_remboursement DOUBLE,
    statut ENUM('Active','Expiree','Suspendue') DEFAULT 'Active',
    id_user INT NOT NULL
);

CREATE TABLE couverture (
    id_couverture INT AUTO_INCREMENT PRIMARY KEY,
    id_assurance INT NOT NULL,
    type_service ENUM('Consultation','Medicament','Analyse','Radiologie','Hospitalisation') NOT NULL,
    pourcentage_couverture DECIMAL(5,2) NOT NULL,
    montant_max DECIMAL(10,2),
    condition_speciale VARCHAR(255)
);

CREATE TABLE demande_remboursement (
    id_remboursement INT AUTO_INCREMENT PRIMARY KEY,
    id_assurance INT NOT NULL,
    type_depense ENUM('Consultation','Medicament','Analyse','Radiologie','Hospitalisation') NOT NULL,
    montant_depense DECIMAL(10,2) NOT NULL,
    montant_estime DECIMAL(10,2),
    montant_valide DECIMAL(10,2),
    date_demande DATE NOT NULL,
    date_validation DATE NULL,
    statut ENUM('En attente','Valide','Refuse') DEFAULT 'En attente',
    commentaire VARCHAR(255)
);

INSERT INTO assurance_sante (numero_contrat,type_assurance,nom_assureur,date_debut,date_fin,plafond_annuel,taux_remboursement,statut,id_user) VALUES
('CTR-2026-001','Privee','STAR Assurance','2026-01-01','2026-12-31',5000,80,'Active',1),
('CTR-2025-OLD','Complementaire','AMI Assurance','2025-01-01','2025-12-31',3000,70,'Expiree',1);

INSERT INTO couverture (id_assurance,type_service,pourcentage_couverture,montant_max,condition_speciale) VALUES
(1,'Consultation',80,100,'2 consultations par mois'),
(1,'Medicament',70,300,'Sur ordonnance'),
(1,'Analyse',90,500,'Laboratoire partenaire');

INSERT INTO demande_remboursement (id_assurance,type_depense,montant_depense,montant_estime,montant_valide,date_demande,date_validation,statut,commentaire) VALUES
(1,'Consultation',120,96,90,'2026-04-10','2026-04-15','Valide','Consultation cardiologue'),
(1,'Analyse',200,180,NULL,'2026-04-18',NULL,'En attente','Bilan sanguin');
