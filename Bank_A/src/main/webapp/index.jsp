<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Online Banking</title>
</head>
<body>
<h2>Check Balance</h2>
<form method="POST">
      Account: <input type="text" id="account" name="account" /><br>
      PIN: <input type="text" id="pin" name="pin" /><br>
      <input type="submit" value="Submit" />
</form>
<% 
String id="", name="", balance="", online_pin="";
String acc = request.getParameter("account");
String pin = request.getParameter("pin");

if (acc!=null && pin!=null){
	try{
	
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm", "nky", "nky");
		PreparedStatement stmt = conn.prepareStatement("SELECT id, name, balance, online_pin FROM accounts where id=? and online_pin=?");
		stmt.setString(1, acc);
		stmt.setString(2, pin);
		ResultSet rs = stmt.executeQuery();
		
	    //display the account details especially the balance
	    while (rs.next()) {
	    	id=rs.getString("id");
	    	name=rs.getString("name");
	    	balance=rs.getString("balance");
	    	online_pin=rs.getString("online_pin");
	    }
        rs.close();
        stmt.close();
        conn.close();
	%>

	<%	
	}catch(Exception e){
		e.printStackTrace();
		out.println("Error connecting to the database: " + e.getMessage());
	}%>
	<h4>Account Number: <%=id%></h4>
	<h4>Name: <%= name %></h4>
	<h4>Balance (RM): <%=balance%></h4>
	<h4>Online Pin: <%=online_pin%></h4>
<% 
}
%>
</body>
</html>