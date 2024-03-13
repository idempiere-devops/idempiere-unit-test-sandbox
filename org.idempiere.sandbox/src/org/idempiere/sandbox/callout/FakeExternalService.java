package org.idempiere.sandbox.callout;

import java.util.Random;

public class FakeExternalService {

	public boolean validateTaxID(String value) {
		Random random = new Random();
		return random.nextBoolean();
	}

}
