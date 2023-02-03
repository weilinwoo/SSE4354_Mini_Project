package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PayServlet
 */
@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//doGet(request, response);
		String id = request.getParameter("account");
		String pin = request.getParameter("pin");
		String payment = request.getParameter("topup");
		//store info in a request obj
		//first string is name of attribute
		request.setAttribute("id", id);
		//second is value of attribute
		request.setAttribute("payment", payment);
		//Create HTTP session object and store value as session attribute
		HttpSession session = request.getSession();
		session.setAttribute("id", id);
		session.setAttribute("payment", payment);
		//check is valid user or not
		if(isValidUser(id,pin)) {
	        	//check whether balance is enough or not
		        if (isApproved(id,payment)) {
		        	processOTP(id);//process related to OTP
		        	request.getRequestDispatcher("otp.jsp").forward(request, response);
		        }else {
		        	request.setAttribute("errorMessage", "Error: Not enough balance!");
		        	request.getRequestDispatcher("mobileTopUp.jsp").forward(request, response);
		        	return;
		        }
		}else {
			request.setAttribute("errorMessage", "Error: Wrong Pin!");
        	request.getRequestDispatcher("mobileTopUp.jsp").forward(request, response);
        	return;
		}
	}
	
	//method to call web services from Bank A to authenticate valid user (invoke AuthenticateUser method in Bank A's PaymentRequest.java)
	public boolean isValidUser(String id,String pin) {
		boolean isValid = false;
		try {
		// Create a URL object for the web service endpoint
        URL url = new URL("http://localhost:8080/Bank_A/checkPin?id="+id+"&pin="+pin);

        // Open a connection to the web service
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Set the request method to POST
        con.setRequestMethod("POST");

        // Read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
       
        String c = content.toString();
        isValid = Boolean.parseBoolean(c);
        
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isValid;
        
	}
	
	//method to call web services from Bank A to approve payment (invoke checkBalance method in Bank A's PaymentRequest.java)
	public boolean isApproved(String id,String payment) {
		boolean paymentApproved = false;
		try {
		// Create a URL object for the web service endpoint
        URL url = new URL("http://localhost:8080/Bank_A/checkBalance?acc="+id+"&payment="+payment);

        // Open a connection to the web service
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Set the request method to POST
        con.setRequestMethod("POST");

        // Read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
       
        String c = content.toString();
        paymentApproved = Boolean.parseBoolean(c);
        
		}catch(Exception e) {
			e.printStackTrace();
		}
		return paymentApproved;
        
	}

	//method to call web services from bank to send OTP (invoke sendOTP method in Bank A's HandleOtpEJB.java)
	public void processOTP(String id) {
		try {
			// Create a URL object for the web service endpoint
	        URL url = new URL("http://localhost:8080/Bank_A/sendOTP?id="+id);

	        // Open a connection to the web service
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();

	        // Set the request method to POST
	        con.setRequestMethod("POST");

	        // Read the response
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer content = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	            content.append(inputLine);
	        }
	        in.close();
	       
	       
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
}
