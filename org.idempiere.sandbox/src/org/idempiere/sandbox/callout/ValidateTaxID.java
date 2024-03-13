/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Copyright (C) 2015 INGEINT <http://www.ingeint.com> and contributors (see README.md file).
 */

package org.idempiere.sandbox.callout;

import org.adempiere.base.annotation.Callout;
import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang3.math.NumberUtils;
import org.compiere.model.I_C_BPartner;
import org.compiere.util.CLogger;
import org.idempiere.sandbox.base.CustomCallout;

@Callout(tableName = I_C_BPartner.Table_Name, columnName = I_C_BPartner.COLUMNNAME_TaxID)
public class ValidateTaxID extends CustomCallout {

	private final static CLogger log = CLogger.getCLogger(ValidateTaxID.class);
	private FakeExternalService service;

	public ValidateTaxID() {
		this.service = new FakeExternalService();
	}

	public ValidateTaxID(FakeExternalService service) {
		this.service = service;
	}

	@Override
	protected String start() {
		String value = (String) getValue();

		if (value == null) {
			return null;
		}

		// Check it's a number

		if (!NumberUtils.isCreatable(value)) {
			return "Invalid Tax ID format, It has to be a number";
		}

		// Validate with external service

		try {
			if (!service.validateTaxID(value)) {
				return "Invalid Tax ID";
			}
		} catch (Exception e) {
			throw new AdempiereException(e.getMessage());
		}

		log.info("TaxId %s valid".formatted(value));

		return null;
	}
}
