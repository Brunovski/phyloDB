package pt.ist.meic.phylodb.analysis.visualization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pt.ist.meic.phylodb.ServiceTestsContext;
import pt.ist.meic.phylodb.analysis.visualization.model.Visualization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

public class VisualizationServiceTests extends ServiceTestsContext {

	private static final int LIMIT = 2;
	private static final Visualization[] STATE = new Visualization[]{VISUALIZATION1, VISUALIZATION2};

	private static Stream<Arguments> getInferences_params() {
		List<Visualization> expected1 = new ArrayList<Visualization>() {{
			add(STATE[0]);
		}};
		List<Visualization> expected2 = new ArrayList<Visualization>() {{
			add(STATE[0]);
			add(STATE[1]);
		}};
		return Stream.of(Arguments.of(0, Collections.emptyList()),
				Arguments.of(0, expected1),
				Arguments.of(0, expected2),
				Arguments.of(-1, null));
	}

	private static Stream<Arguments> getInference_params() {
		return Stream.of(Arguments.of(VISUALIZATION1.getPrimaryKey(), VISUALIZATION1),
				Arguments.of(VISUALIZATION1.getPrimaryKey(), null));
	}

	private static Stream<Arguments> deleteInference_params() {
		return Stream.of(Arguments.of(STATE[0].getPrimaryKey(), true),
				Arguments.of(STATE[0].getPrimaryKey(), false));
	}

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@ParameterizedTest
	@MethodSource("getInferences_params")
	public void getVisualizations(int page, List<Visualization> expected) {
		Mockito.when(visualizationRepository.findAll(anyInt(), anyInt(), any(), any(), any())).thenReturn(Optional.ofNullable(expected));
		Optional<List<Visualization>> result = visualizationService.getVisualizations(PROJECT1.getPrimaryKey(), DATASET1.getPrimaryKey().getId(), INFERENCE1.getPrimaryKey().getId(), page, LIMIT);
		if (expected == null && !result.isPresent()) {
			assertTrue(true);
			return;
		}
		assertNotNull(expected);
		assertTrue(result.isPresent());
		List<Visualization> schemas = result.get();
		assertEquals(expected.size(), schemas.size());
		assertEquals(expected, schemas);
	}

	@ParameterizedTest
	@MethodSource("getInference_params")
	public void getVisualization(Visualization.PrimaryKey key, Visualization expected) {
		Mockito.when(visualizationRepository.find(any())).thenReturn(Optional.ofNullable(expected));
		Optional<Visualization> result = visualizationService.getVisualization(key.getProjectId(), key.getDatasetId(),key.getAnalysisId(), key.getId());
		assertTrue((expected == null && !result.isPresent()) || (expected != null && result.isPresent()));
		if (expected != null)
			assertEquals(expected, result.get());
	}

	@ParameterizedTest
	@MethodSource("deleteInference_params")
	public void deleteVisualization(Visualization.PrimaryKey key, boolean expected) {
		Mockito.when(visualizationRepository.remove(any())).thenReturn(expected);
		boolean result = visualizationService.deleteVisualization(key.getProjectId(), key.getDatasetId(), key.getAnalysisId(), key.getId());
		assertEquals(expected, result);
	}

}