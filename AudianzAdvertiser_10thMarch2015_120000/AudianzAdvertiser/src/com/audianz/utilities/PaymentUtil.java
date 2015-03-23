package com.audianz.utilities;

public class PaymentUtil {

	private final String CARD_NUMBER = "4111111111111111";
	private final String EXPDATE     = "0118";
	private final String CVC         = "222";
	
	public boolean isPaymentSuccess(String cardno,String expdate,String cvv)
	{
		if(cardno.equals(CARD_NUMBER) && expdate.equals(EXPDATE) && cvv.equals(CVC))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
