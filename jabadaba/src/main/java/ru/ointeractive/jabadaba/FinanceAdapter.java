	package ru.ointeractive.jabadaba;
	
	import java.text.DecimalFormat;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	
	public class FinanceAdapter {
		
		public static DecimalFormat df = new DecimalFormat ("#.##");
		protected String mCurrency;
		
		public float incomeMonth, outcomeMonth, invest, inflation;
		
		private List<float[]> mCreditPayments = new ArrayList<> ();
		public float creditOverpayment = 0;
		
		protected Map<String, String> mCurrencies = new HashMap<> ();
		
		public FinanceAdapter () {
			
			addCurrency ("USD", "$");
			addCurrency ("RUR", "p.");
			
		}
		
		public FinanceAdapter addCurrency (String value, String symbol) {
			
			mCurrencies.put (value, symbol);
			
			return this;
			
		}
		
		public void setCurrency (String currency) {
			mCurrency = mCurrencies.get (currency);
		}
		
		public float stockMonth () { // Выручка
			return (incomeMonth - outcomeMonth);
		}
		
		public String format (float value) {
			
			if (mCurrency == null) setCurrency ("USD");
			return Int.format (df.format (value)) + " " + mCurrency;
			
		}
		
		public float getAccrual () {
			return (invest - inflation);
		}
		
		public float getStockYear () {
			return stockMonth () * 12;
		}
		
		public float getInvestMonth () {
			return (stockMonth () * getAccrual ()) / 100;
		}
		
		public float getInvestYear () {
			return getInvestMonth () * 12;
		}
		
		public float getSummMonth () {
			return stockMonth () + getInvestMonth ();
		}
		
		public float getSummYear () {
			return getStockYear () + getInvestYear ();
		}
		
		public float getInterest (float rate) {
			return (rate / 100 / 12);
		}
		
		protected float getCreditPayment (float amount, float interest, int months) {
			return (amount * (interest / (1 - (float) Math.pow (1 + interest, -months))));
		}
		
		public List<float[]> getCreditPayments (float amount, float rate, int months) {
			
			creditOverpayment = 0;
			mCreditPayments = new ArrayList<> ();
			
			float interest = getInterest (rate), newAmount = amount;
			
			for (int i = 0; i < months; i++) {
				
				float[] output2 = new float[5];
				
				output2[0] = newAmount;
				output2[1] = getCreditPayment (amount, interest, months);
				output2[2] = (output2[0] * interest);
				output2[3] = (output2[1] - output2[2]);
				output2[4] = (output2[0] - output2[3]);
				
				newAmount = output2[4];
				
				mCreditPayments.add (output2);
				creditOverpayment += output2[2];
				
			}
			
			return mCreditPayments;
			
		}
		
	}