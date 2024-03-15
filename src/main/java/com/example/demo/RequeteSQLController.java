    package com.example.demo;


    import com.example.demo.Entity.Demande;
    import com.example.demo.Tools.ConnexionBDD;

    import java.sql.*;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.*;
    import java.util.Date;

    public class RequeteSQLController {
        private static Connection cnx;
        private static PreparedStatement ps;
        private ResultSet rs;

        public RequeteSQLController() {
            cnx = ConnexionBDD.getCnx();

        }

        // Partie connexion et check dans la base de données
        public boolean verifierIdentifiants(String emailUtilisateur, String mdpUtilisateur) {
            boolean verification = false;
            try {
                ps = cnx.prepareStatement("SELECT user.email, user.password FROM user WHERE user.email = ? AND user.password = ?;");
                ps.setString(1, emailUtilisateur);
                ps.setString(2, mdpUtilisateur);
                rs = ps.executeQuery();
                while (rs.next()) {
                    verification = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return verification;
        }

        public Map<String, String> getNomPrenomUtilisateur(String emailUtilisateur) {
            Map<String, String> utilisateurInfo = new HashMap<>();
            try {
                ps = cnx.prepareStatement("SELECT user.nom, user.prenom FROM user WHERE user.email = ?");
                ps.setString(1, emailUtilisateur);
                rs = ps.executeQuery();
                if (rs.next()) {
                    utilisateurInfo.put("nom", rs.getString("nom"));
                    utilisateurInfo.put("prenom", rs.getString("prenom"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return utilisateurInfo;
        }


        public List<String> getDesignationsMatiere() {
            List<String> designations = new ArrayList<>();
            try {
                ps = cnx.prepareStatement("SELECT matiere.designation FROM matiere");
                rs = ps.executeQuery();
                while (rs.next()) {
                    String designation = rs.getString("designation");
                    designations.add(designation);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return designations;
        }

        public List<String> getSousMatieresPourMatiere(String matiereSelectionnee) {
            List<String> sousMatieres = new ArrayList<>();
            try {
                String query = "SELECT DISTINCT sous_matiere FROM competence WHERE id_matiere = (SELECT id FROM matiere WHERE designation = ?)";
                ps = cnx.prepareStatement(query);
                ps.setString(1, matiereSelectionnee);

                rs = ps.executeQuery();

                while (rs.next()) {
                    String sousMatiere = rs.getString("sous_matiere");
                    sousMatieres.add(sousMatiere);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return sousMatieres;
        }


        public int getIdUtilisateur(String emailUtilisateur, String mdpUtilisateur) {
            int idUtilisateur = -1; // c'est la valeur par défaut si l'utilisateur n'est pas trouvé

            try {
                ps = cnx.prepareStatement("SELECT id FROM user WHERE email = ? AND password = ?");
                ps.setString(1, emailUtilisateur);
                ps.setString(2, mdpUtilisateur);
                rs = ps.executeQuery();

                if (rs.next()) {
                    idUtilisateur = rs.getInt("id");
                    System.out.println("ID de l'utilisateur récupéré : " + idUtilisateur);
                } else {
                    System.out.println("Aucun utilisateur trouvé avec ces identifiants.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return idUtilisateur;
        }

        public List<String> getToutesDemandes() {
            List<String> demandesList = new ArrayList<>();

            try {

                String sqlQuery = "SELECT * FROM demande";

                try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String sousMatiere = resultSet.getString("sous_matiere");
                        Date date = resultSet.getDate("date_updated");
                        Date date2 = resultSet.getDate("date_fin_demande");
                        int status = resultSet.getInt("status");

                        String informationDemande = String.format(" Date Examen: %s, Sous-matière: %s",
                                date2.toString(), sousMatiere);


                        demandesList.add(informationDemande);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return demandesList;
        }

        public void creerDemandeUtilisateurConnecte(Date dateupdated, Date dateFinDemande, String sousMatiereDemandee, int idUtilisateur, int idMatiere, int status) {
            try {
                ps = cnx.prepareStatement("INSERT INTO demande (date_updated, date_fin_demande, sous_matiere, id_user,id_matiere, status) " +
                        "VALUES (NOW(), ?, ?, ?, ?,?)");


                ps.setDate(1, new java.sql.Date(dateFinDemande.getTime()));
                ps.setString(2, sousMatiereDemandee);
                ps.setInt(3, idUtilisateur);
                ps.setInt(4, idMatiere);
                ps.setInt(5, status);

                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();

            }
        }


        public int getIdMatiere(String matiereSelectionnee) {
            int idMatiere = -1;

            try {
                ps = cnx.prepareStatement("SELECT id FROM matiere WHERE designation = ?");
                ps.setString(1, matiereSelectionnee);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    idMatiere = rs.getInt("id");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            return idMatiere;
        }


        public void updateDemandeStatut(int demandeId) {
            try {
                String sqlQuery = "UPDATE demande SET status = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {
                    preparedStatement.setInt(1, 2);
                    preparedStatement.setInt(2, demandeId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected == 0) {
                        System.out.println("Aucune ligne mise à jour. L'ID de la demande peut ne pas exister.");
                    } else {
                        System.out.println("Statut de la demande mis à jour avec succès.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void creerCompetenceConnecte(int idMatiere, int idUtilisateur, String sousMatiereDemandee, int status) {
            try {
                ps = cnx.prepareStatement("INSERT INTO competence (id_matiere,id_user,sous_matiere,statut) " +
                        "VALUES ( ?, ?, ?, ?)");


                ps.setInt(1, idMatiere);
                ps.setInt(2, idUtilisateur);
                ps.setString(3, sousMatiereDemandee);
                ps.setInt(4, status);

                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        public List<String> getDemandesUtilisateurConnecte(int idUtilisateur) {
            List<String> demandesUtilisateur = new ArrayList<>();

            try {
                String sqlQuery = "SELECT * FROM demande WHERE id_user = ? ORDER BY date_fin_demande DESC";

                ps = cnx.prepareStatement(sqlQuery);
                ps.setInt(1, idUtilisateur);

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    String sousMatiere = resultSet.getString("sous_matiere");
                    Date date2 = resultSet.getDate("date_fin_demande");
                    int demandeId = resultSet.getInt("id");


                    String informationDemande = String.format(" Date Examen: %s, Sous-matière: %s, %s",
                            date2.toString(), sousMatiere, demandeId);


                    demandesUtilisateur.add(informationDemande);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return demandesUtilisateur;
        }

        public static List<String> getCompetencesUtilisateurConnecte(int idUtilisateur) {
            List<String> competencesUtilisateur = new ArrayList<>();

            try {
                String sqlQuery = "SELECT * FROM competence WHERE id_user = ?";
                ps = cnx.prepareStatement(sqlQuery);
                ps.setInt(1, idUtilisateur);

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    String competence = resultSet.getString("sous_matiere");
                    competencesUtilisateur.add(competence);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return competencesUtilisateur;
        }


        public HashMap<String, Integer> getNombreSoutiensParUtilisateurConnecte(int idUtilisateurConnecte) {
            HashMap<String, Integer> soutiensParMatiere = new HashMap<>();
            try {
                String sql = "SELECT matiere.designation AS matiere, COUNT(soutien.id) AS nombre_soutiens " +
                        "FROM user " +
                        "JOIN competence ON user.id = competence.id_user " +
                        "JOIN matiere ON competence.id_matiere = matiere.id " +
                        "LEFT JOIN soutien ON competence.id = soutien.id_competence " +
                        "WHERE user.id = ? " +
                        "GROUP BY matiere.id, matiere.designation " +
                        "ORDER BY matiere.designation";
                PreparedStatement ps = cnx.prepareStatement(sql);
                ps.setInt(1, idUtilisateurConnecte);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String matiere = rs.getString("matiere");
                    int nombreSoutiens = rs.getInt("nombre_soutiens");
                    soutiensParMatiere.put(matiere, nombreSoutiens);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return soutiensParMatiere;
        }

        public HashMap<String, Integer> getNombreDemandesParMatiere(int idUtilisateurConnecte) {
            HashMap<String, Integer> nombreDemandesParMatiere = new HashMap<>();

            try {

                String sql = "SELECT matiere.designation AS matiere_designation, COUNT(*) " +
                        "AS nombreDemandes FROM demande JOIN matiere ON demande.id_matiere = matiere.id " +
                        "WHERE demande.id_user = ? GROUP BY demande.id_matiere";
                ps = cnx.prepareStatement(sql);

                ps.setInt(1, idUtilisateurConnecte);

                rs = ps.executeQuery();

                while (rs.next()) {
                    String designation = rs.getString("matiere_designation");
                    int nombreDemandes = rs.getInt("nombreDemandes");
                    nombreDemandesParMatiere.put(designation, nombreDemandes);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return nombreDemandesParMatiere;
        }

        public Demande getDemandeById(int demandeId) {
            Demande demande = new Demande(); // Crée un objet Demande pour stocker les informations récupérées
            try {
                // Prépare la requête SQL pour récupérer les informations de la demande avec l'ID spécifié
                String sqlQuery = "SELECT * FROM demande WHERE id_demande = ?";
                ps = cnx.prepareStatement(sqlQuery);
                ps.setInt(1, demandeId);

                // Exécute la requête SQL
                rs = ps.executeQuery();

                // Vérifie s'il y a des résultats
                if (rs.next()) {
                    // Récupère les informations de la demande depuis le ResultSet
                    demande.setId(rs.getInt("id_demande"));
                    demande.setDateFin(rs.getDate("date_fin_demande"));
                    demande.setSousMatiere(rs.getString("sous_matiere"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return demande;
        }


        public String getNiveauUtilisateur(int idUtilisateur) {
            String niveauUtilisateur = ""; // Valeur par défaut si le niveau n'est pas trouvé

            try {
                PreparedStatement ps = cnx.prepareStatement("SELECT niveau FROM user WHERE id = ?");
                ps.setInt(1, idUtilisateur);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    niveauUtilisateur = rs.getString("niveau");
                    System.out.println("Niveau de l'utilisateur récupéré : " + niveauUtilisateur);
                } else {
                    System.out.println("Aucun utilisateur trouvé avec cet identifiant.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return niveauUtilisateur;
        }


        public List<String> getDemandesMasterUn() {
            List<String> demandesList = new ArrayList<>();

            try {
                String sqlQuery = "SELECT d.sous_matiere, d.date_updated, d.date_fin_demande " +
                        "FROM demande d " +
                        "JOIN user u ON d.id_user = u.id " +
                        "WHERE u.niveau IN ('Terminale', 'BTS 1', 'BTS 2', 'Bachelor')"
                        + "AND d.status=1";

                try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String sousMatiere = resultSet.getString("sous_matiere");
                        Date date2 = resultSet.getDate("date_fin_demande");
                        int demandeId = resultSet.getInt("id");


                        String informationDemande = String.format(" Date Examen: %s, Sous-matière: %s, %s",
                                date2.toString(), sousMatiere, demandeId);


                        demandesList.add(informationDemande);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return demandesList;


        }

        public List<String> getDemandesMasterDeux() {
            List<String> demandesList = new ArrayList<>();

            try {
                String sqlQuery = "SELECT d.sous_matiere, d.date_updated, d.date_fin_demande, d.id " +
                        "FROM demande d " +
                        "JOIN user u ON d.id_user = u.id " +
                        "WHERE u.niveau IN ('Terminale', 'BTS 1', 'BTS 2', 'Bachelor', 'Master 1')"
                        + "AND d.status=1";
                try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String sousMatiere = resultSet.getString("sous_matiere");
                        Date date2 = resultSet.getDate("date_fin_demande");
                        int demandeId = resultSet.getInt("id");


                        String informationDemande = String.format(" Date Examen: %s, Sous-matière: %s, %s",
                                date2.toString(), sousMatiere, demandeId);


                        demandesList.add(informationDemande);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return demandesList;
        }

        public List<String> getDemandesBTS() {
            List<String> demandesList = new ArrayList<>();

            try {
                String sqlQuery = "SELECT d.sous_matiere, d.date_updated, d.date_fin_demande " +
                        "FROM demande d " +
                        "JOIN user u ON d.id_user = u.id " +
                        "WHERE u.niveau IN ('Terminale', 'BTS 1')";
                try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String sousMatiere = resultSet.getString("sous_matiere");
                        Date date2 = resultSet.getDate("date_fin_demande");
                        int demandeId = resultSet.getInt("id");


                        String informationDemande = String.format(" Date Examen: %s, Sous-matière: %s, %s",
                                date2.toString(), sousMatiere, demandeId);


                        demandesList.add(informationDemande);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return demandesList;
        }

        public List<String> getDemandesTerminale() {
            List<String> demandesList = new ArrayList<>();

            try {
                String sqlQuery = "SELECT d.sous_matiere, d.date_updated, d.date_fin_demande " +
                        "FROM demande d " +
                        "JOIN user u ON d.id_user = u.id " +
                        "WHERE u.niveau = 'Terminale'"
                        + "AND d.status=1";

                try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String sousMatiere = resultSet.getString("sous_matiere");
                        Date date2 = resultSet.getDate("date_fin_demande");
                        int demandeId = resultSet.getInt("id");


                        String informationDemande = String.format(" Date Examen: %s, Sous-matière: %s, %s",
                                date2.toString(), sousMatiere, demandeId);


                        demandesList.add(informationDemande);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return demandesList;
        }


        public void aiderDemande(int demandeId, int competenceId) {
            try {
                // Obtenez la date actuelle
                LocalDate dateDuSoutien = LocalDate.now().plusWeeks(2);

                // Requête SQL pour insérer une nouvelle entrée dans la table soutien
                String sqlQuery = "INSERT INTO soutien (id_demande, id_competence, id_salle, date_du_soutien, date_updated, description, status) VALUES (?, ?, ?, ?, CURDATE(), '', 1)";

                try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlQuery)) {
                    // Assignez les valeurs aux paramètres de la requête
                    preparedStatement.setInt(1, demandeId);
                    preparedStatement.setInt(2, competenceId);
                    preparedStatement.setInt(3, 1); // id_salle = 1

                    // Conversion de la date en format SQL Date
                    java.sql.Date dateDuSoutienSQL = java.sql.Date.valueOf(dateDuSoutien);
                    preparedStatement.setDate(4, dateDuSoutienSQL);

                    // Exécutez la requête d'insertion
                    int rowsAffected = preparedStatement.executeUpdate();

                    // Vérifiez si l'insertion a réussi
                    if (rowsAffected > 0) {
                        System.out.println("Nouveau soutien ajouté avec succès.");
                    } else {
                        System.out.println("Échec de l'ajout du soutien.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        public List<Integer> getCompetenceIds(String[] sousMatieres) throws SQLException {
            List<Integer> competenceIds = new ArrayList<>();

            // Créer une requête SQL dynamique avec un nombre variable de paramètres
            StringBuilder sqlBuilder = new StringBuilder("SELECT id FROM competence WHERE 1=1");
            for (int i = 0; i < sousMatieres.length; i++) {
                sqlBuilder.append(" AND sous_matiere LIKE ?");
            }

            try (PreparedStatement preparedStatement = cnx.prepareStatement(sqlBuilder.toString())) {
                for (int i = 0; i < sousMatieres.length; i++) {
                    preparedStatement.setString(i + 1, "%" + sousMatieres[i] + "%");
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    competenceIds.add(resultSet.getInt("id"));
                }
            }

            return competenceIds;
        }

    }

