package org.idempiere.sandbox.callout;

public class FakeTaxIDVerifier implements TaxIDVerifier {

	public boolean isValid(int id) throws Exception {
		return true;
	}

}
