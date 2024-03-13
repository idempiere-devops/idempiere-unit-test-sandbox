package org.idempiere.sandbox.callout;

import static org.idempiere.sandbox.test.util.AnnotationsTestUtil.assertClassAnnotation;
import static org.idempiere.sandbox.test.util.AnnotationsTestUtil.assertClassAnnotationParameter;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import org.adempiere.base.annotation.Callout;
import org.adempiere.exceptions.AdempiereException;
import org.idempiere.sandbox.test.util.RandomTestUtil;
import org.idempiere.sandbox.test.util.ReflectionTestUtil;
import org.junit.jupiter.api.Test;

public class ValidateTaxIDTest {

	// CONFIGURATION TESTS

	@Test
	public void containsCalloutAnnotation() {
		assertClassAnnotation(ValidateTaxID.class, Callout.class);
		assertClassAnnotationParameter(ValidateTaxID.class, Callout.class, "tableName", "C_BPartner");
		assertClassAnnotationParameter(ValidateTaxID.class, Callout.class, "columnName", "TaxID");
	}

	// STATE TESTS

	@Test
	public void validateContainsOnlyNumbers() {
		ValidateTaxID callout = new ValidateTaxID();

		callout.start(null, 0, null, null, RandomTestUtil.getRandomString(), null);

		assertThat(callout.start()).isEqualTo("Invalid Tax ID format, It has to be a number");
	}

	// BEHAVIOR TESTS

	@Test
	public void invalidFromExternalService() {
		FakeExternalService externalService = mock(FakeExternalService.class);
		when(externalService.validateTaxID(anyString())).thenReturn(false);
		ValidateTaxID callout = new ValidateTaxID(externalService);

		String result = callout.start(null, 0, null, null, String.valueOf(RandomTestUtil.getRandomLong()), null);

		assertThat(result).isEqualTo("Invalid Tax ID");
	}

	@Test
	public void invalidFromExternalServiceWithReflection() throws ReflectiveOperationException {
		FakeExternalService externalService = mock(FakeExternalService.class);
		when(externalService.validateTaxID(anyString())).thenReturn(false);
		ValidateTaxID callout = new ValidateTaxID();

		// If you cannot use the constructor you can use reflection
		ReflectionTestUtil.setFieldValue(callout, "service", externalService);
		ReflectionTestUtil.setFieldValue(callout, "value", String.valueOf(RandomTestUtil.getRandomLong()));

		assertThat(callout.start()).isEqualTo("Invalid Tax ID");
	}

	@Test
	public void validFromExternalService() {
		FakeExternalService externalService = mock(FakeExternalService.class);
		when(externalService.validateTaxID(anyString())).thenReturn(true);
		ValidateTaxID callout = new ValidateTaxID(externalService);

		String result = callout.start(null, 0, null, null, String.valueOf(RandomTestUtil.getRandomLong()), null);

		assertThat(result).isNull();
	}

	@Test
	public void validateExternalServiceException() {
		String expectedMessage = "Error connectiong to external service";

		FakeExternalService externalService = mock(FakeExternalService.class);
		when(externalService.validateTaxID(anyString())).thenThrow(new RuntimeException(expectedMessage));

		ValidateTaxID callout = new ValidateTaxID(externalService);

		AdempiereException exception = assertThrows(AdempiereException.class,
				() -> callout.start(null, 0, null, null, String.valueOf(RandomTestUtil.getRandomLong()), null));

		assertThat(exception.getMessage()).isEqualTo(expectedMessage);
	}

}
