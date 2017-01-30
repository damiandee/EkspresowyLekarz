package praca_inzynierska.damian_deska.ekspresowylekarz.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.OpinionModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.PatientModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.SpecializationModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentDateModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.VisitModel;

/**
 * Created by Damian Deska on 2017-01-08.
 */

public class DatabaseConnectionController {

    /*String ip = "192.168.0.104";
    String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    String db = "EkspresowyLekarz";
    String un = "sa";
    String password = "sql2014";*/

    String ip = "ekspresowylekarz.database.windows.net";
    String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    String db = "EkspresowyLekarz";
    String un = "ekspresowylekarz";
    String password = "!sql2014";

    String doctorInfoQuery = "select l.ID_lekarza, l.Imie, l.Nazwisko, l.ID_specjalizacji, l.Opis, l.Ulica, l.Miasto, l.Telefon, " +
            "l.Strona_internetowa, l.Nfz, l.Platnosc_karta, l.Parking, l.Niepelnosprawni, l.Avatar_URL, " +
            "(select round(avg(cast(Ocena as float)), 2) from Opinie where ID_lekarza=l.ID_lekarza group by ID_lekarza) " +
            "as [Srednia] from Lekarze l where";

    private static DatabaseConnectionController instance = null;
    Connection databaseConnection;// = this.newConnection();
    MD5Hasher md5Hasher = new MD5Hasher();

    private DatabaseConnectionController() {
        this.databaseConnection = newConnection();
    }

    public static DatabaseConnectionController getInstance() {
        if (instance == null) {
             instance = new DatabaseConnectionController();
        }
        return instance;
    }

    @SuppressLint("NewApi")
    protected Connection newConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection newConnection = null;
        String connectionURL = null;
        try {

            Class.forName(driverClass);
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + db + ";user=" + un + ";password=" + password;
            newConnection = DriverManager.getConnection(connectionURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return newConnection;
    }

    public boolean signIn(Context context, String email, String password){
        String serverStatement = "";
        boolean isSucceed = false;
        try {
            if (databaseConnection == null) {
                serverStatement = "Błąd połączenia z serwerem, spróbuj ponownie";
            } else {
                String query = "select * from Pacjenci where email='" + email + "' and haslo='" + password + "'";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if(rs.next())
                {
                    UserSession.getSession().signIn(rs.getInt("ID_pacjenta"));
                    UserSession.getSession().setLoggedIn(true);
                    serverStatement = "Zalogowano pomyślnie";
                    isSucceed = true;
                }
                else
                {
                    serverStatement = "Nieprawidłowe dane logowania";
                    UserSession.getSession().setLoggedIn(false);
                }
            }
            Toast.makeText(context, serverStatement, Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {

        }
        return isSucceed;
    }


    public boolean registerUser(Context context, PatientModel newPatient){
        boolean isSucceed = false;
        try {
            if (databaseConnection == null) {
                Toast.makeText(context, "Błąd połączenia z serwerem, spróbuj ponownie", Toast.LENGTH_LONG).show();
            } else {
                isSucceed = true;
                String query = "insert into Pacjenci(Imie, Nazwisko, Email, Haslo, Telefon) values ('" + newPatient.getPatientName() +
                        "', '" + newPatient.getPatientSurname() + "', '" + newPatient.getPatientEmail() + "', '" +
                        md5Hasher.hashToMD5(newPatient.getPatientPassword()) + "', '" + newPatient.getPatientPhoneNumber() + "');";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                return isSucceed;
            }
        }
        catch (Exception ex)
        {

        }
        return isSucceed;
    }

    public ArrayList<SpecializationModel> getAllSpecializationsList() {
        ArrayList<SpecializationModel> specializationsList = new ArrayList<SpecializationModel>();
        try {
            //Connection con = this.newConnection();
            if (databaseConnection == null) {

            } else {
                String query = "select * from Specjalizacje";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    SpecializationModel tmpSpecializationModel = new SpecializationModel();
                    tmpSpecializationModel.setSpecializationID(rs.getInt("ID_specjalizacji"));
                    tmpSpecializationModel.setSpecializationName(rs.getString("Nazwa"));
                    specializationsList.add(tmpSpecializationModel);
                    tmpSpecializationModel = null;
                }

            }
        }
        catch (Exception ex)
        {

        }
        return specializationsList;
    }

    public ArrayList<DoctorModel> getDoctorsFromSpecialization(int specializationID) {
        ArrayList<DoctorModel> doctorsFromSpecializationsList = new ArrayList<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = doctorInfoQuery + " l.ID_specjalizacji=" + specializationID + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    doctorsFromSpecializationsList.add(fillDoctorModel(rs));
                }

            }
        }
        catch (Exception ex)
        {

        }
        return doctorsFromSpecializationsList;
    }

    public ArrayList<DoctorModel> searchForDoctors(String searchinput) {
        ArrayList<DoctorModel> foundedDoctors = new ArrayList<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Lekarze where ((Imie like N'" + searchinput + "%') or (Nazwisko like N'" + searchinput +
                        "%') or (Ulica like N'" + searchinput + "%') or (Miasto like N'" + searchinput + "%'));";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    foundedDoctors.add(getDoctorInfo(rs.getInt("ID_lekarza")));
                }

            }
        }
        catch (Exception ex)
        {

        }
        return foundedDoctors;
    }

    public ArrayList<DoctorModel> getDoctorsAvailableInDate(String date) {
        ArrayList<DoctorModel> foundedDoctors = new ArrayList<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select distinct ID_lekarza from Terminy where Data='" + date + "' and Czy_dostepny=1;";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    foundedDoctors.add(getDoctorInfo(rs.getInt("ID_lekarza")));
                }

            }
        }
        catch (Exception ex)
        {

        }
        return foundedDoctors;
    }

    public DoctorModel getDoctorInfo(int doctorID) {
        DoctorModel doctor = new DoctorModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = doctorInfoQuery + " ID_lekarza='" + doctorID + "'";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    doctor = fillDoctorModel(rs);
                }

            }
        }
        catch (Exception ex)
        {

        }
        return doctor;
    }

    public DoctorModel fillDoctorModel(ResultSet rs) {
        DoctorModel doctor = new DoctorModel();
        try {
            doctor.setDoctorID(rs.getInt("ID_lekarza"));
            doctor.setDoctorName(rs.getString("Imie"));
            doctor.setDoctorSurname(rs.getString("Nazwisko"));
            doctor.setSpecjalizationID(rs.getInt("ID_specjalizacji"));
            doctor.setDoctorDescription(rs.getString("Opis"));
            doctor.setDoctorStreet(rs.getString("Ulica"));
            doctor.setDoctorCity(rs.getString("Miasto"));
            doctor.setDoctorPhoneNumber(rs.getString("Telefon"));
            doctor.setDoctorWebPage(rs.getString("Strona_internetowa"));
            doctor.setIsNfz(rs.getInt("Nfz"));
            doctor.setIsCardPayment(rs.getInt("Platnosc_karta"));
            doctor.setIsParking(rs.getInt("Parking"));
            doctor.setIsForDisabled(rs.getInt("Niepelnosprawni"));
            doctor.setDoctorAvatarURL(rs.getString("Avatar_URL"));
            doctor.setDoctorRating(rs.getFloat("Srednia"));
        } catch (Exception e) {

        }

        return doctor;
    }

    public ArrayList<TreatmentModel> getDoctorTreatmentsList(int doctorID) {
        ArrayList<TreatmentModel> treatmentsList = new ArrayList<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Zabiegi where ID_lekarza=" + doctorID;
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    TreatmentModel tmpTreatment = new TreatmentModel();
                    tmpTreatment.setTreatmentID(rs.getInt("ID_zabiegu"));
                    tmpTreatment.setDoctorID(rs.getInt("ID_lekarza"));
                    tmpTreatment.setTreatmentName(rs.getString("Nazwa"));
                    tmpTreatment.setTreatmentTime(rs.getInt("Czas_trwania"));
                    tmpTreatment.setTreatmentCost(rs.getInt("Cena"));
                    treatmentsList.add(tmpTreatment);
                    tmpTreatment = null;
                }

            }
        }
        catch (Exception ex)
        {

        }
        return treatmentsList;
    }

    public ArrayList<OpinionModel> getDoctorOpinionsList(int doctorID) {
        ArrayList<OpinionModel> opinionsList = new ArrayList<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Opinie where ID_lekarza=" + doctorID + " order by Data_dodania desc;";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    OpinionModel tmpOpinionModel = new OpinionModel();
                    tmpOpinionModel.setOpinionID(rs.getInt("ID_opinii"));
                    tmpOpinionModel.setDoctorID(rs.getInt("ID_lekarza"));
                    tmpOpinionModel.setPatientID(rs.getInt("ID_pacjenta"));
                    tmpOpinionModel.setPatientName(rs.getString("Imie_pacjenta"));
                    tmpOpinionModel.setRating(rs.getInt("Ocena"));
                    tmpOpinionModel.setOpinionContent(rs.getString("Opinia"));
                    tmpOpinionModel.setOpinionAddDate(rs.getString("Data_dodania"));
                    opinionsList.add(tmpOpinionModel);
                    tmpOpinionModel = null;
                }

            }
        }
        catch (Exception ex)
        {

        }
        return opinionsList;
    }

    public void changePassword(String newPassword) {
        try {
            if (databaseConnection == null) {

            } else {
                String query = "update Pacjenci set Haslo = '" + newPassword + "' where ID_pacjenta=" +
                        UserSession.getSession().getUserID() + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
            }
        }
        catch (Exception ex)
        {

        }
    }

    public PatientModel getPatientInfo(int patientID) {
        PatientModel patientModel = new PatientModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Pacjenci where ID_pacjenta=" + patientID;
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    patientModel.setPatientID(rs.getInt("ID_pacjenta"));
                    patientModel.setPatientName(rs.getString("Imie"));
                    patientModel.setPatientSurname(rs.getString("Nazwisko"));
                    patientModel.setPatientEmail(rs.getString("Email"));
                    patientModel.setPatientPassword(rs.getString("Haslo"));
                    patientModel.setPatientPhoneNumber(rs.getString("Telefon"));
                }

            }
        }
        catch (Exception ex)
        {

        }
        return patientModel;
    }

    public HashMap<String, String> getDoctorGalleryUrlsList(int doctorID) {
        HashMap<String, String> doctorUrlsList = new HashMap<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Zdjecia where ID_lekarza=" + doctorID;
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    doctorUrlsList.put(rs.getString("Url"), rs.getString("Opis"));
                }

            }
        }
        catch (Exception ex)
        {

        }
        return doctorUrlsList;
    }

    public TreatmentModel getTreatmentInfo(int treatmentID) {
        TreatmentModel treatmentModel = new TreatmentModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Zabiegi where ID_zabiegu=" + treatmentID + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    treatmentModel.setTreatmentID(rs.getInt("ID_zabiegu"));
                    treatmentModel.setDoctorID(rs.getInt("ID_lekarza"));
                    treatmentModel.setTreatmentName(rs.getString("Nazwa"));
                    treatmentModel.setTreatmentCost(rs.getInt("Cena"));
                }

            }
        }
        catch (Exception ex)
        {

        }
        return treatmentModel;
    }

    public TreatmentDateModel getTreatmentDateInfo(int treatmentDateID) {
        TreatmentDateModel treatmentDateModel = new TreatmentDateModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Terminy where ID_terminu=" + treatmentDateID + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    treatmentDateModel.setTreatmentDateID(rs.getInt("ID_terminu"));
                    treatmentDateModel.setTreatmentID(rs.getInt("ID_zabiegu"));
                    treatmentDateModel.setDoctorID(rs.getInt("ID_lekarza"));
                    treatmentDateModel.setTreatmentDate(rs.getString("Data"));
                    treatmentDateModel.setTreatmentTime(rs.getString("Godzina"));
                    treatmentDateModel.setIsAvailable(rs.getInt("Czy_dostepny"));
                }

            }
        }
        catch (Exception ex)
        {

        }
        return treatmentDateModel;
    }

    public ArrayList<TreatmentDateModel> getTodayTreatment(int treatmentID, String todayDate) {
        ArrayList<TreatmentDateModel> todayTreatment = new ArrayList<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Terminy where ID_zabiegu=" + treatmentID + " and Data='" + todayDate +
                        "' and Czy_dostepny=1" + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    TreatmentDateModel treatmentDateModel = new TreatmentDateModel();
                    treatmentDateModel.setTreatmentDateID(rs.getInt("ID_terminu"));
                    treatmentDateModel.setTreatmentID(rs.getInt("ID_zabiegu"));
                    treatmentDateModel.setDoctorID(rs.getInt("ID_lekarza"));
                    treatmentDateModel.setTreatmentDate(rs.getString("Data"));
                    treatmentDateModel.setTreatmentTime(rs.getString("Godzina"));
                    treatmentDateModel.setIsAvailable(rs.getInt("Czy_dostepny"));
                    todayTreatment.add(treatmentDateModel);
                    treatmentDateModel = null;
                }

            }
        }
        catch (Exception ex)
        {

        }
        return todayTreatment;
    }

    public boolean bookTreatment(int treatmentDateID, int patientID) {
        boolean isSucceed = false;
        try {
            if (databaseConnection == null) {

            } else {
                String query = "insert into Rezerwacje(ID_terminu, ID_pacjenta, Czy_zakonczona, Czy_aktualna, Czy_oceniona, Czy_odwolana) values (" +
                        treatmentDateID +
                        ", " + patientID + ", 0, 1, 0, 0);";
                updateTreatmentAvailability(treatmentDateID);
                Statement stmt = databaseConnection.createStatement();
               // stmt.executeQuery(query);
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {

                }

                isSucceed = true;
            }
        }
        catch (Exception ex)
        {

        }
        return isSucceed;
    }

    public void updateTreatmentAvailability(int treatmentDateID) {
        try {
            if (databaseConnection == null) {

            } else {
                String query = "update Terminy set Czy_dostepny=0 where ID_terminu=" + treatmentDateID + ";";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
            }
        }
        catch (Exception ex)
        {

        }
    }



    public ArrayList<VisitModel> getPatientVisitsList(int patientID) {
        ArrayList<VisitModel> patientVisitsList = new ArrayList<>();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select r.ID_rezerwacji, r.ID_terminu, r.ID_pacjenta, r.Czy_zakonczona,  r.Czy_aktualna, r.Czy_oceniona, Czy_odwolana, Notatka, " +
                        "(select Data from Terminy where ID_terminu = r.ID_terminu) as [Data], " +
                        "(select Godzina from Terminy where ID_terminu = r.ID_terminu) as [Czas] from Rezerwacje r " +
                        "where ID_pacjenta=" + patientID + " order by Data desc";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next())
                {
                    VisitModel tmpVisit = new VisitModel();
                    tmpVisit.setVisitID(rs.getInt("ID_rezerwacji"));
                    tmpVisit.setTreatmentDateID(rs.getInt("ID_terminu"));
                    tmpVisit.setPatientID(rs.getInt("ID_pacjenta"));
                    tmpVisit.setIsEnded(rs.getInt("Czy_zakonczona"));
                    tmpVisit.setIsActual(rs.getInt("Czy_aktualna"));
                    tmpVisit.setIsReviewed(rs.getInt("Czy_oceniona"));
                    tmpVisit.setIsCancelled(rs.getInt("Czy_odwolana"));
                    tmpVisit.setVisitNote(rs.getString("Notatka"));
                    tmpVisit.setVisitDate(rs.getString("Data"));
                    tmpVisit.setVisitTime(rs.getString("Czas"));
                    patientVisitsList.add(tmpVisit);
                    tmpVisit = null;
                }

            }
        }
        catch (Exception ex)
        {

        }
        return patientVisitsList;
    }

/*    public VisitModel fillVisitModel(int visitID) {
        VisitModel tmpVisitModel = new VisitModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "update Terminy set Czy_dostepny=0 where ID_terminu=" + treatmentDateID + ";";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
            }
        }
        catch (Exception ex)
        {

        }
        return tmp
    }*/

    public void removeReservation(int visitID, int treatmentDateID) {
        try {
            if (databaseConnection == null) {

            } else {
                String query = "update Rezerwacje set Czy_odwolana=1 where ID_rezerwacji=" + visitID + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

            }
        }
        catch (Exception ex)
        {

        }
        setTreatmentDateAvailable(treatmentDateID);
    }

    public void setTreatmentDateAvailable(int treatmentDateID) {
        try {
            if (databaseConnection == null) {

            } else {
                String query = "update Terminy set Czy_dostepny=1 where ID_terminu=" + treatmentDateID + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
            }
        }
        catch (Exception ex)
        {

        }
    }

    public VisitModel getVisitInfo(int treatmentDateID) {
        VisitModel tmpVisit = new VisitModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Rezerwacje where ID_terminu=" + treatmentDateID + ";";
                Statement stmt = databaseConnection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while(rs.next()) {
                    tmpVisit.setVisitID(rs.getInt("ID_rezerwacji"));
                    tmpVisit.setTreatmentDateID(rs.getInt("ID_terminu"));
                    tmpVisit.setPatientID(rs.getInt("ID_pacjenta"));
                    tmpVisit.setIsEnded(rs.getInt("Czy_zakonczona"));
                    tmpVisit.setIsActual(rs.getInt("Czy_aktualna"));
                    tmpVisit.setIsReviewed(rs.getInt("Czy_oceniona"));
                    tmpVisit.setIsCancelled(rs.getInt("Czy_odwolana"));
                    tmpVisit.setVisitNote(rs.getString("Notatka"));
                }
            }
        }
        catch (Exception ex)
        {

        }
        return tmpVisit;
    }

    public void addOpinion(OpinionModel opinion, int visitID) {
        try {
            if (databaseConnection == null) {

            } else {
                String query = "insert into Opinie(ID_lekarza, ID_pacjenta, Imie_pacjenta, Ocena, Opinia, Data_dodania) values (" +
                        opinion.getDoctorID() + ", " + opinion.getPatientID() + ", N'" + opinion.getPatientName() + "', " +
                        opinion.getRating() + ", N'" + opinion.getOpinionContent() + "', '" + opinion.getOpinionAddDate() + "');";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
                /*ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {

                }*/
            }
        }
        catch (Exception ex)
        {

        }
        setVisitReviewed(visitID);
    }

    public void setVisitReviewed(int visitID) {
        try {
            if (databaseConnection == null) {

            } else {
                String query = "update Rezerwacje set Czy_oceniona=1 where ID_rezerwacji=" + visitID + ";";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
                /*ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {

                }*/
            }
        }
        catch (Exception ex)
        {

        }
    }

    public SpecializationModel getSpecializationInfo(int specializationID) {
        SpecializationModel specializationModel = new SpecializationModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Specjalizacje where ID_specjalizacji=" + specializationID + ";";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                    specializationModel.setSpecializationID(rs.getInt("ID_specjalizacji"));
                    specializationModel.setSpecializationName(rs.getString("Nazwa"));
                }
            }
        }
        catch (Exception ex)
        {

        }
        return specializationModel;
    }

    public SpecializationModel getSpecializationInfoFromName(String specializationName) {
        SpecializationModel specializationModel = new SpecializationModel();
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Specjalizacje where Nazwa='" + specializationName + "';";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                    specializationModel.setSpecializationID(rs.getInt("ID_specjalizacji"));
                    specializationModel.setSpecializationName(rs.getString("Nazwa"));
                }
            }
        }
        catch (Exception ex)
        {

        }
        return specializationModel;
    }

    public boolean isPatientBlocked(int doctorID, int patientiD) {
        boolean isBlocked = false;
        try {
            if (databaseConnection == null) {

            } else {
                String query = "select * from Zablokowani_pacjenci where ID_lekarza=" + doctorID + " and ID_pacjenta=" + patientiD + ";";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
                ResultSet rs = stmt.executeQuery(query);
                if(rs.next()) {
                    isBlocked = true;
                }
            }
        }
        catch (Exception ex)
        {

        }
        return isBlocked;
    }


    public void updatePatientInfo(PatientModel patient){
        try {
            if (databaseConnection == null) {

            } else {
                String query = "update Pacjenci set Imie = N'" + patient.getPatientName() + "', Nazwisko = N'" + patient.getPatientSurname() +
                        "', Email = '" + patient.getPatientEmail() + "', Telefon = '" + patient.getPatientPhoneNumber() +
                        "' where ID_pacjenta = " + patient.getPatientID() + ";";
                Statement stmt = databaseConnection.createStatement();
                stmt.executeQuery(query);
                ResultSet rs = stmt.executeQuery(query);
            }
        }
        catch (Exception ex)
        {

        }
    }



}
