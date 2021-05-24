package sql.database;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.table.DefaultTableModel;


//klasa obslugujaca polaczenie z baza danych MySQL
public class SQLConnection {
    Connection con;
            
    SQLConnection(String username, String password){
        try{
        con = getConnection(username, password);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
        public static Connection getConnection(String username, String password) throws Exception{
    try{
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/school_database"; 
        Class.forName(driver);

        Connection connection = DriverManager.getConnection(url,username,password);
        System.out.println("Connected");
        return connection;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
        
        public String[] getColumnNames(){
            try{
                PreparedStatement stat = con.prepareStatement("SELECT * FROM student");
                ResultSet rs = stat.executeQuery();
                ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
                String[] columnNames = new String[rsmd.getColumnCount()-1];
                for(int i = 1; i <= rsmd.getColumnCount(); i++){
                    columnNames[i-1] = rsmd.getColumnName(i);
                }
                return columnNames;
            }catch(Exception e){
                System.out.println(e);
            }
            return null;
        }
        
        public int count(){
            try{
                PreparedStatement stat = con.prepareStatement("SELECT COUNT(*) AS count FROM student");
                ResultSet rs = stat.executeQuery();
                rs.next();
                int count = rs.getInt("count");
                return count;
            }catch(Exception e){
                System.out.println(e);
            }
            return 0;
        }
        
        public void addToDatabase(String name, String surname, String clas, String bday){
            try{
            PreparedStatement stat = con.prepareStatement("INSERT INTO student(class, name, surname, birthdate) VALUES('"+clas+"','"+name+"','"+surname+"','"+bday+"')");
            stat.execute();
            }catch(Exception e){
                System.out.println(e);   
            }
        }
        
        public void deleteFromDatabase(String name, String surname, String clas, String bday){
            try{
                PreparedStatement stat = con.prepareStatement("DELETE FROM student WHERE class = '"+clas+"' AND name = '"+name+"' AND surname = '"+surname+"' AND birthdate = '"+bday+"'");
                stat.execute();
            }catch(Exception e){
                System.out.println(e);
            }
            
        }
        
        public void searchData(String column, String searched, DefaultTableModel tableModel){
            try{   
                int i;
                
                if(column.equals("Imie"))
                    column = "name";
                if(column.equals("nazwisko"))
                    column = "surname";
                if(column.equals("klasa"))
                    column = "class";
                if(column.equals("Data ur."))
                    column = "birthdate";
                        
                PreparedStatement stat = con.prepareStatement("SELECT class, name, surname, birthdate FROM student WHERE "+column+" LIKE '%"+searched+"%'");
                ResultSet rs = stat.executeQuery();
                String[] data = new String[4];
                while(rs.next()){
                   i=0;
                   data[i++] = rs.getString("name");
                   data[i++] = rs.getString("surname");
                   data[i++] = rs.getString("class");
                   data[i++] = rs.getString("birthdate");
                   tableModel.addRow(data);
                }
            }catch(Exception e){
                System.out.println(e);
            }
        }
        
        public String[][] getData(int nOfRows){
            try{
                int i;
                PreparedStatement stat = con.prepareStatement("SELECT class, name, surname, birthdate FROM student ");
                ResultSet rs = stat.executeQuery();
                String[][] data = new String[nOfRows][4];
                while(rs.next()){
                   i=0;
                   data[rs.getRow()-1][i++] = rs.getString("name");
                   data[rs.getRow()-1][i++] = rs.getString("surname");
                   data[rs.getRow()-1][i++] = rs.getString("class");
                   data[rs.getRow()-1][i++] = rs.getString("birthdate");
                }
                return data;
            }catch(Exception e){
                System.out.println(e);   
            }
            return null;
        }
}
