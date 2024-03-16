package org.idempiere.sandbox.callout;

import java.util.Random;

public class RealTaxIDVerifier implements TaxIDVerifier {

	@Override
	public boolean isValid(int id) throws Exception {
		Random random = new Random();
		return random.nextBoolean();
	}

}
