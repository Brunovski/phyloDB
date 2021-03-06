package algorithm.visualization.implementation;

import algorithm.visualization.model.Coordinate;
import algorithm.visualization.model.Tree;
import algorithm.visualization.model.Vertex;
import algorithm.visualization.model.Visualization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Radial is an VisualizationAlgorithm which implements the radial algorithm
 */
public class Radial extends VisualizationAlgorithm {

	public static final String NAME = "radial";
	private static final int DEFAULT_DISTANCE_MULTIPLIER = 100;
	private static final int DEFAULT_ZERO_DISTANCE = 10;

	private static int leafs(Vertex node) {
		return node.getChildren().length == 0 ? 1 : Arrays.stream(node.getChildren()).reduce(0, (a, c) -> a + leafs(c), Integer::sum);
	}

	@Override
	public void init(Object... params) {
		this.projectId = (String) params[0];
		this.datasetId = (String) params[1];
		this.inferenceId = (String) params[2];
		this.id = (String) params[3];
	}

	@Override
	public Visualization compute(Tree tree) {
		Vertex[] roots = tree.getRoots();
		Map<String, RadialCoordinate> coordinates = new HashMap<>();
		for (int i = 0; i < roots.length; i++) {
			Vertex root = roots[i];
			Stack<Vertex> nodes = new Stack<>();
			int leaftotal = leafs(root);
			nodes.push(root);
			coordinates.put(root.getId(), new RadialCoordinate(root.getId(), i + 1, 0, 0, 0));
			while (nodes.size() > 0) {
				Vertex parent = nodes.pop();
				RadialCoordinate parentCoordinate = coordinates.get(parent.getId());
				double border = parentCoordinate.getRightBorder();
				for (Vertex child : parent.getChildren()) {
					double wedge = 2 * Math.PI * leafs(child) / leaftotal;
					double alpha = border + wedge / 2;
					double distance = child.getDistance() * DEFAULT_DISTANCE_MULTIPLIER + DEFAULT_ZERO_DISTANCE;
					nodes.push(child);
					double x = parentCoordinate.getX() + Math.cos(alpha) * distance;
					double y = parentCoordinate.getY() + Math.sin(alpha) * distance;
					coordinates.put(child.getId(), new RadialCoordinate(child.getId(), i + 1, x, y, border));
					border += wedge;
				}
			}
		}
		return new Visualization(projectId, datasetId, inferenceId, id, NAME, coordinates.values()
				.stream()
				.map(r -> new Coordinate(r.getProfileId(), r.getComponent(), r.getX(), r.getY()))
				.toArray(Coordinate[]::new));
	}

	/**
	 * Helper class used to calculate coordinates during radial algorithm
	 */
	public static class RadialCoordinate extends Coordinate {

		private final double rightBorder;

		public RadialCoordinate(String profileId, int component, double x, double y, double rigthBorder) {
			super(profileId, component, x, y);
			this.rightBorder = rigthBorder;
		}

		public double getRightBorder() {
			return rightBorder;
		}

	}

}
