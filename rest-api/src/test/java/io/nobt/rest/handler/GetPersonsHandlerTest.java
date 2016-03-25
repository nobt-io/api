package io.nobt.rest.handler;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.rest.json.GsonFactory;
import io.nobt.util.Sets;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static io.nobt.core.domain.Person.forName;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Thomas Eizinger, Senacor Technologies AG.
 */
public class GetPersonsHandlerTest {

	private NobtDao nobtDaoMock;
	private Request requestMock;
	private Response responseMock;

	private GetPersonsHandler sut;

	@Before
	public void setUp() throws Exception {
		nobtDaoMock = mock(NobtDao.class);
		requestMock = mock(Request.class);
		responseMock = mock(Response.class);

		sut = new GetPersonsHandler(nobtDaoMock, GsonFactory.createConfiguredGsonInstance());
	}

	@Test
	public void testSerialization() throws Exception {

		final Nobt nobtMock = mock(Nobt.class);

		when(nobtMock.getParticipatingPersons()).thenReturn(Sets.newHashSet(thomas(), matthias(), lukas(), david()));
		when(nobtDaoMock.find(anyObject())).thenReturn(nobtMock);
		when(requestMock.params(":nobtId")).thenReturn(UUID.randomUUID().toString());

		final String result = (String) sut.handle(requestMock, responseMock);

		Assert.assertThat(result, Matchers.is(
						"[\n" +
						"  \"Lukas\",\n" +
						"  \"Thomas\",\n" +
						"  \"Matthias\",\n" +
						"  \"David\"\n" +
						"]")
		);
	}

	private Person david() {
		return forName("David");
	}

	private Person lukas() {
		return forName("Lukas");
	}

	private Person matthias() {
		return forName("Matthias");
	}

	private Person thomas() {
		return forName("Thomas");
	}
}