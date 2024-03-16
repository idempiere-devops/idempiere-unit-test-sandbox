package org.idempiere.sandbox.callout;

import org.adempiere.base.annotation.Callout;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_BPartner;
import org.idempiere.sandbox.base.CustomCallout;

@Callout(tableName = I_C_BPartner.Table_Name, columnName = I_C_BPartner.COLUMNNAME_TaxID)
public class ValidateTaxID extends CustomCallout {

	private TaxIDVerifier verifier;

	public ValidateTaxID() {
		this.verifier = new RealTaxIDVerifier();
	}

	public ValidateTaxID(TaxIDVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	protected String start() {
		if (getValue() == null)
			return null;

		String value = getValue().toString();

		if (!value.matches("^[0-9]*$")) {
			return "Invalid format: '%s'".formatted(value);
		}

		try {
			return verifier.isValid(Integer.parseInt(value)) ? null : "Invalid ID";
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}
	}
}
