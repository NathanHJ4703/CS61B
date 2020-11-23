package byow.Core;

import java.util.ArrayList;
import java.util.List;

public class PathGraph implements AStarGraph<Position> {

    @Override
    public List<WeightedEdge<Position>> neighbors(Position p) {
        ArrayList<WeightedEdge<Position>> neighbors = new ArrayList<>();
        neighbors.add(new WeightedEdge<>(p, new Position(p.getX()+1, p.getY()), 1));
        neighbors.add(new WeightedEdge<>(p, new Position(p.getX(), p.getY()+1), 1));
        neighbors.add(new WeightedEdge<>(p, new Position(p.getX()-1, p.getY()), 1));
        neighbors.add(new WeightedEdge<>(p, new Position(p.getX(), p.getY()-1), 1));
        return neighbors;
    }

    @Override
    public double estimatedDistanceToGoal(Position s, Position goal) {
        return 0;
    }
}
