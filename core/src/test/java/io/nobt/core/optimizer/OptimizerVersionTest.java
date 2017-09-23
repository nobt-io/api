package io.nobt.core.optimizer;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class OptimizerVersionTest {

	@Test
	public void version1ShouldReturnSelfSortingOptimizer() throws Exception {

		final OptimizerStrategy version1Strategy = OptimizerVersion.V1.getStrategy();

		assertThat(version1Strategy.getClass(), equalTo(SelfSortingOptimizerStrategy.class));
	}
}