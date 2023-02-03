<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>System 3 Merchant</title>
</head>

<body>
<h1>Mobile Top Up</h1>
<form action="PaymentServlet" method = "POST">

      Account: <input type="text" id="account" name="account" /><br>
      PIN: <input type="text" id="pin" name="pin" /><br>
      Top Up(RM): <input type="text" id="topup" name="topup"/><br>
      <input type="submit" name="pay" value="Pay" />
</form>

</body>
</html>