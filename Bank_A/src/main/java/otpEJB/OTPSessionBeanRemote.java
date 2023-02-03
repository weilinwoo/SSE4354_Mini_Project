package otpEJB;

import javax.ejb.Remote;

@Remote
public interface OTPSessionBeanRemote {
	
	public String getOTPFromCloud();
	public void sendEmail(String id,int otp);//send OTP thru email
	public void sendConsole(int otp);//send OTP thru console
	public void storeOTP(int otp,String id);
	public boolean verifyOTP(String id, int enteredOTP);
}
