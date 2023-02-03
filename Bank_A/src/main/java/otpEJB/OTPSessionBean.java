package otpEJB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Session Bean implementation class OTPSessionBean
 */
@Stateless
@LocalBean
public class OTPSessionBean implements OTPSessionBeanRemote, OTPSessionBeanLocal {

    /**
     * Default constructor. 
     */
    public OTPSessionBean() {
        // TODO Auto-generated constructor stub
    }
    //get OTP from cloud netlify
	@Override
	public String getOTPFromCloud() {
		String otp = "";
		try {
			// Create a URL object for the web service endpoint
	        URL url = new URL("https://fastidious-pothos-4bd0f8.netlify.app/.netlify/functions/generateOTP");

	        // Open a connection to the web service
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();

	        // Set the request method to GET
	        con.setRequestMethod("GET");

	        // Read the response
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer content = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	            content.append(inputLine);
	        }
	        in.close();
	        otp = content.toString();
	        // Close the connection
	        con.disconnect();
			}catch(Exception e) {
				System.out.println("Something went wrong!");
			}
		return otp;
	}
	//send OTP thru console
	@Override
	public void sendConsole(int otp) {
		System.out.println("Dear Customer!");
		System.out.println("This is an Email from Bank A");
		System.out.println("Your OTP: " + otp);
	}

	//send the OTP thru email stored in db
	@Override
	public void sendEmail(String id,int otp) {
		String email = "";
		try {
			DBConnection db = new DBConnection();
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = db.createConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT email FROM accounts where id=?");
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				email =  rs.getString("email");
					
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
		 Properties properties = new Properties();
		  properties = System.getProperties();
		  properties.put("mail.smtp.host", "smtp.gmail.com");
		  properties.put("mail.smtp.port", "465");
		  properties.put("mail.smtp.auth", "true");
		  properties.put("mail.smtp.ssl.enable", "true");
	
	      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	        	   protected PasswordAuthentication getPasswordAuthentication() {
	        		    return new PasswordAuthentication("weilinwoo1123@gmail.com", "password");
	        	}
	      });
	      session.setDebug(true);
	        
	      Message message = new MimeMessage(session);
	      message.setFrom(new InternetAddress("weilinwoo1123@gmail.com"));
	      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
	      message.setSubject("Top Up Payment");
	      message.setText("Dear customer!This is an verification email from Bank A. Your OTP: " + otp);

	      Transport.send(message);
	      System.out.println("Email sent successfully.");
	    
		} catch (MessagingException e) {
	            throw new RuntimeException(e);
	    }
	}
	//update new otp to database
	@Override
	public void storeOTP(int otp,String id) {
		// TODO Auto-generated method stub
		try {
			DBConnection db = new DBConnection();
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = db.createConnection();
			String insertSQL = "UPDATE accounts SET otp = ? WHERE id = ?";
	        PreparedStatement insertStatement = conn.prepareStatement(insertSQL);

	        // Set the values for the statement's parameters
	        insertStatement.setInt(1, otp);
	        insertStatement.setString(2, id);

	        // Execute the statement
	        insertStatement.executeUpdate();

	        // Close the connection
	        conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//verify the otp entered by comparing value stored in db
	@Override
	public boolean verifyOTP(String id,int enteredOTP) {
		int otp_db = 0;
		
		try {
	     // get OTP from the database
		DBConnection db = new DBConnection();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = db.createConnection();
		PreparedStatement stmt = conn.prepareStatement("SELECT otp FROM accounts where id=?");
		stmt.setString(1, id);
		ResultSet rs = stmt.executeQuery();
		 while (rs.next()) {			 
			 String temp =  rs.getString("otp");
			 otp_db = Integer.parseInt(temp);
		 }
		
		 // set the expiry time for OTP
		 LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
		 // this is the current time to submit OTP
		 LocalDateTime now = LocalDateTime.now();
		 // if valid return true flag and update flag in database
	        if (enteredOTP == otp_db && now.isBefore(expiryTime)) {
	        	//stmt = conn.prepareStatement("UPDATE accounts SET opt_expire = ? WHERE id = ?");
	            return true;
	        }else {
	        	return false;
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
}
