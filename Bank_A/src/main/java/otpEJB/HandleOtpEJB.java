package otpEJB;

import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
public class HandleOtpEJB {

	@EJB
	private OTPSessionBean otpSession; //create OTPSessionBean object 
	
	//web service for merchant to request bank to send OTP using EJB:OTPSessionBean.java
	@POST
	@Path("/sendOTP")
	public void processOTP(@QueryParam("id")String id) {
		// Request EJB to get the OTP from Netlify
		String sotp = otpSession.getOTPFromCloud();
		String motp = sotp.substring(1, sotp.length()-1);
		int otp = Integer.valueOf(motp);
		//request EJB to send OTP thru console
		otpSession.sendConsole(otp);
		//request EJB to send OTP thru console
		//otpSession.sendEmail(id, otp);
		// store the OTP to Database
		otpSession.storeOTP(otp,id);
	}
	//web service to access ejb for OTP verification on Bank site 
	@POST
	@Path("/verifyOTP")
	@Produces("text/plain")
	public boolean verifyOTP(@QueryParam("id")String id, @QueryParam("otp")int otp) {
		//Request EJB to verify OTP that entered by user
		boolean is_ValidOTP = otpSession.verifyOTP(id,otp);
		return is_ValidOTP;
		
	}
}
