package org.idempiere.sandbox.callout;

import static org.idempiere.sandbox.test.util.AnnotationTestUtil.assertClassAnnotation;
import static org.idempiere.sandbox.test.util.AnnotationTestUtil.assertClassAnnotationParameter;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
	void returnNullWhenNullInput() {
		ValidateTaxID callout = new ValidateTaxID(new FakeTaxIDVerifier()); // ARRANGE

		String result = callout.start(null, 0, null, null, null, null); // ACT

		assertThat(result).isNull(); // ASSERT
	}

	@Test
	void returnNullWhenTaxIDIsANumber() {
		ValidateTaxID callout = new ValidateTaxID(new FakeTaxIDVerifier());

		String result = callout.start(null, 0, null, null, RandomTestUtil.getRandomInt(), null);

		assertThat(result).isNull();
	}

	@Test
	void returnAMessageWhenTaxIDIsAString() {
		Object input = RandomTestUtil.getRandomString();
		ValidateTaxID callout = new ValidateTaxID(new FakeTaxIDVerifier());

		String result = callout.start(null, 0, null, null, input, null);

		assertThat(result).isEqualTo("Invalid format: '%s'".formatted(input));
	}

	@Test
	void returnNullIfStringIsvalid() {
		Object input = String.valueOf(RandomTestUtil.getRandomInt());
		ValidateTaxID callout = new ValidateTaxID(new FakeTaxIDVerifier());

		String result = callout.start(null, 0, null, null, input, null);

		assertThat(result).isNull();
	}

	@Test
	void returnErrorOnInvalidValueType() {
		Object input = getRandomObject();
		ValidateTaxID callout = new ValidateTaxID(new FakeTaxIDVerifier());

		String result = callout.start(null, 0, null, null, input, null);

		assertThat(result).isEqualTo("Invalid format: '%s'".formatted(input));
	}

	private Object getRandomObject() {
		return RandomTestUtil.getRandomBoolean() ? RandomTestUtil.getRandomFloat() : RandomTestUtil.getRandomBoolean();
	}

	// BEHAVIOR TESTS

	@Test
	void returnMessageIfVerifierServiceSaysItIsAnInvalidNumber() throws Exception {
		int input = RandomTestUtil.getRandomInt();

		TaxIDVerifier verifier = mock(TaxIDVerifier.class);
		when(verifier.isValid(anyInt())).thenReturn(false);

		ValidateTaxID callout = new ValidateTaxID(verifier);
		String result = callout.start(null, 0, null, null, input, null); // ACT

		assertThat(result).isEqualTo("Invalid ID");
		verify(verifier).isValid(input);
	}

	@Test
	void returnMessageIfVerifierServiceSaysItIsAnInvalidNumberWhenString() throws Exception {
		int input = RandomTestUtil.getRandomInt();

		TaxIDVerifier verifier = mock(TaxIDVerifier.class);
		when(verifier.isValid(anyInt())).thenReturn(false);

		ValidateTaxID callout = new ValidateTaxID(verifier);

		String result = callout.start(null, 0, null, null, String.valueOf(input), null); // ACT

		assertThat(result).isEqualTo("Invalid ID");
		verify(verifier).isValid(input);
	}

	@Test
	void returnNullIfNumberIsValid() throws Exception {
		int input = RandomTestUtil.getRandomInt();

		TaxIDVerifier verifier = mock(TaxIDVerifier.class);
		when(verifier.isValid(anyInt())).thenReturn(true);

		ValidateTaxID callout = new ValidateTaxID(verifier);

		String result = callout.start(null, 0, null, null, input, null); // ACT

		assertThat(result).isNull();
		verify(verifier).isValid(input);
	}

	@Test
	void returnNullIfStringIsValid() throws Exception {
		int input = RandomTestUtil.getRandomInt();

		TaxIDVerifier verifier = mock(TaxIDVerifier.class);
		when(verifier.isValid(anyInt())).thenReturn(true);

		ValidateTaxID callout = new ValidateTaxID(verifier);

		String result = callout.start(null, 0, null, null, String.valueOf(input), null); // ACT

		assertThat(result).isNull();
		verify(verifier).isValid(input);
	}

	// USING REFLECTION

	@Test
	public void validFromExternalServiceWithReflection() throws ReflectiveOperationException {
		ValidateTaxID callout = new ValidateTaxID(new FakeTaxIDVerifier());

		// If you cannot use the constructor you can use reflection
		ReflectionTestUtil.setFieldValue(callout, "value", String.valueOf(RandomTestUtil.getRandomLong()));

		assertThat(callout.start()).isNull();
	}

	// TEST EXCEPTION

	@Test
	public void validateExternalServiceException() throws Exception {
		String expectedMessage = "Error connectiong to external service";

		TaxIDVerifier externalService = mock(TaxIDVerifier.class);
		when(externalService.isValid(anyInt())).thenThrow(new RuntimeException(expectedMessage));

		ValidateTaxID callout = new ValidateTaxID(externalService);

		AdempiereException exception = assertThrows(AdempiereException.class,
				() -> callout.start(null, 0, null, null, String.valueOf(RandomTestUtil.getRandomLong()), null));

		assertThat(exception.getMessage()).isEqualTo(expectedMessage);
	}

}
