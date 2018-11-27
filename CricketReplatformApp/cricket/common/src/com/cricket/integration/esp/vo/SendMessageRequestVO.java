package com.cricket.integration.esp.vo;

public class SendMessageRequestVO extends ESPRequestVO{

	private String mFirstName;
	
	private String mToEmail;
	
	private String mFromEmail;
	
	private String mZipCode;
	
	private String mExistingCustomer;
	
	private String mCustomerType;
	
	private String mCurrentCarrier;
	
	private String mServiceProvider;
	
	private String mTemplateIdentifier;
	
	private String mTemplateType;
	
	private String mSubjectLine;
	
	private String mTemplateLanguage;
	
	
	private NameValuePairVO[] mSubstitutionVariables;

	/**
	 * @return the firstName
	 * 
	 */
	public String getFirstName() {
		return mFirstName;
	}

	/**
	 * @param pFirstName the firstName to set
	 */
	public void setFirstName(String pFirstName) {
		mFirstName = pFirstName;
	}

	 

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return mZipCode;
	}

	/**
	 * @param pZipCode the zipCode to set
	 */
	public void setZipCode(String pZipCode) {
		mZipCode = pZipCode;
	}

	/**
	 * @return the existingCustomer
	 */
	public String getExistingCustomer() {
		return mExistingCustomer;
	}

	/**
	 * @param pExistingCustomer the existingCustomer to set
	 */
	public void setExistingCustomer(String pExistingCustomer) {
		mExistingCustomer = pExistingCustomer;
	}

	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return mCustomerType;
	}

	/**
	 * @param pCustomerType the customerType to set
	 */
	public void setCustomerType(String pCustomerType) {
		mCustomerType = pCustomerType;
	}

	/**
	 * @return the currentCarrier
	 */
	public String getCurrentCarrier() {
		return mCurrentCarrier;
	}

	/**
	 * @param pCurrentCarrier the currentCarrier to set
	 */
	public void setCurrentCarrier(String pCurrentCarrier) {
		mCurrentCarrier = pCurrentCarrier;
	}

	/**
	 * @return the serviceProvider
	 */
	public String getServiceProvider() {
		return mServiceProvider;
	}

	/**
	 * @param pServiceProvider the serviceProvider to set
	 */
	public void setServiceProvider(String pServiceProvider) {
		mServiceProvider = pServiceProvider;
	}

	/**
	 * @return the templateIdentifier
	 */
	public String getTemplateIdentifier() {
		return mTemplateIdentifier;
	}

	/**
	 * @param pTemplateIdentifier the templateIdentifier to set
	 */
	public void setTemplateIdentifier(String pTemplateIdentifier) {
		mTemplateIdentifier = pTemplateIdentifier;
	}

	/**
	 * @return the substitutionVariables
	 */
	public NameValuePairVO[] getSubstitutionVariables() {
		return mSubstitutionVariables;
	}

	/**
	 * @param pSubstitutionVariables the substitutionVariables to set
	 */
	public void setSubstitutionVariables(NameValuePairVO[] pSubstitutionVariables) {
		mSubstitutionVariables = pSubstitutionVariables;
	}
	
	  public  NameValuePairVO getSubstitutionVariables(int i) {
	        return this.mSubstitutionVariables[i];
	    }

	    public void setSubstitutionVariables(int i, NameValuePairVO value) {
	        this.mSubstitutionVariables[i] = value;
	    }

		/**
		 * @return the toEmail
		 */
		public String getToEmail() {
			return mToEmail;
		}

		/**
		 * @param pToEmail the toEmail to set
		 */
		public void setToEmail(String pToEmail) {
			mToEmail = pToEmail;
		}

		/**
		 * @return the fromEmail
		 */
		public String getFromEmail() {
			return mFromEmail;
		}

		/**
		 * @param pFromEmail the fromEmail to set
		 */
		public void setFromEmail(String pFromEmail) {
			mFromEmail = pFromEmail;
		}

		/**
		 * @return the templateType
		 */
		public String getTemplateType() {
			return mTemplateType;
		}

		/**
		 * @param pTemplateType the templateType to set
		 */
		public void setTemplateType(String pTemplateType) {
			mTemplateType = pTemplateType;
		}

		/**
		 * @return the subjectLine
		 */
		public String getSubjectLine() {
			return mSubjectLine;
		}

		/**
		 * @param pSubjectLine the subjectLine to set
		 */
		public void setSubjectLine(String pSubjectLine) {
			mSubjectLine = pSubjectLine;
		}

		/**
		 * @return the templateLanguage
		 */
		public String getTemplateLanguage() {
			return mTemplateLanguage;
		}

		/**
		 * @param pTemplateLanguage the templateLanguage to set
		 */
		public void setTemplateLanguage(String pTemplateLanguage) {
			mTemplateLanguage = pTemplateLanguage;
		}
}
