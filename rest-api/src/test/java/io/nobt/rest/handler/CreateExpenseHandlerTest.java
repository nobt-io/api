package io.nobt.rest.handler;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;
import spark.utils.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CreateExpenseHandlerTest {

	private NobtDao nobtDaoMock;
	private Request requestMock;
	private Response responseMock;

	private CreateExpenseHandler sut;

	@Before
	public void setUp() throws Exception {
		nobtDaoMock = mock(NobtDao.class);
		requestMock = mock(Request.class);
		responseMock = mock(Response.class);
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();

		sut = new CreateExpenseHandler(nobtDaoMock, gson, jsonParser);
	}

	@Test
	public void testShouldParseExampleRequest() throws Exception {

		final InputStream jsonResourceStream = getClass().getResourceAsStream("/create-expense.json");
		final String body = new String(IOUtils.toByteArray(jsonResourceStream), Charset.forName("UTF-8"));
		final UUID uuid = UUID.randomUUID();

		when(requestMock.body()).thenReturn(body);
		when(requestMock.params(":nobtId")).thenReturn(uuid.toString());

		sut.handle(requestMock, responseMock);

		verify(nobtDaoMock).createExpense(
				eq(uuid),
				eq("Billa"),
				eq(Amount.fromDouble(30).getRoundedValue()),
				eq(Person.forName("Thomas")),
				eq(persons("Matthias", "Thomas B.", "Thomas")));
	}

	private static Set<Person> persons(String... names) {
		return Arrays.stream(names).map(Person::forName).collect(toSet());
	}
}