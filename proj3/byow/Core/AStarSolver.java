package byow.Core;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

/** Uses the modified A* algorithm to find shortest path between two vertices.
 * @author nathanpak
 */

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    /** Priority Queue for vertices. */
    private ArrayHeapMinPQ<Vertex> pq;
    /** Map keeping distance for each vertex. */
    private Map<Vertex, Double> disTo;
    /** Map keeping track of which vertices are connected. */
    private Map<Vertex, Vertex> edgeTo;
    /** Outcome of A*. */
    private SolverOutcome result;
    /** The path from start to end vertex. */
    private List<Vertex> solution;
    /** The total distance from start to end. */
    private double solutionWeight;
    /** The number of states visited. */
    private int numStates = 0;
    /** How long it took A* to complete. */
    private double timeSpent;
    /** All nodes that have been visited. */
    private List<Vertex> visitedNodes;

    /** Runs the A* Algorithm.
     * @param input The graph
     * @param start The start vertex
     * @param end The vertex to reach
     * @param timeout Time limit for how long A* runs */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end,
                       double timeout) {
        Stopwatch sw = new Stopwatch();
        visitedNodes = new ArrayList<>();
        pq = new ArrayHeapMinPQ<>();
        edgeTo = new HashMap<>();
        disTo = new HashMap<>();
        disTo.put(start, 0.0);
        pq.add(start, input.estimatedDistanceToGoal(start, end));
        Vertex best = pq.removeSmallest();
        visitedNodes.add(best);
        while (!best.equals(end)) {
            List<WeightedEdge<Vertex>> adjacentEdges = input.neighbors(best);
            for (int i = 0; i < adjacentEdges.size(); i++) {
                WeightedEdge<Vertex> edgeTarget = adjacentEdges.get(i);
                relax(input, end, edgeTarget);
            }
            if (pq.size() == 0) {
                result = SolverOutcome.UNSOLVABLE;
                break;
            }
            best = pq.removeSmallest();
            visitedNodes.add(best);
            numStates++;

            if (sw.elapsedTime() > timeout) {
                result = SolverOutcome.TIMEOUT;
                break;
            }
        }
        if (result != SolverOutcome.UNSOLVABLE
                && result != SolverOutcome.TIMEOUT) {
            result = SolverOutcome.SOLVED;
        }
        Vertex b = end;
        solution = new ArrayList<>();
        if (result != SolverOutcome.TIMEOUT
                && result != SolverOutcome.UNSOLVABLE) {
            solution.add(b);
            while (!b.equals(start)) {
                b = edgeTo.get(b);
                solution.add(b);
            }
            Collections.reverse(solution);
            solutionWeight = disTo.get(end);
        }
        timeSpent = sw.elapsedTime();
    }

    /** Relaxes the edges if distance from the vertex is
     * shorter than its current one.
     * @param input The graph
     * @param end The goal vertex
     * @param targetEdge The edge to check
     * */
    private void relax(AStarGraph<Vertex> input, Vertex end,
                       WeightedEdge<Vertex> targetEdge) {
        Vertex ogVertex = targetEdge.from();
        Vertex adVertex = targetEdge.to();
        double disFromSource = targetEdge.weight() + disTo.get(ogVertex);
        if (pq.contains(adVertex)) {
            if (disFromSource < disTo.get(adVertex)) {
                disTo.put(adVertex, disFromSource);
                edgeTo.put(adVertex, ogVertex);
                pq.changePriority(adVertex,
                        input.estimatedDistanceToGoal(adVertex, end)
                                + disFromSource);
            }
        } else if (!visitedNodes.contains(adVertex)) {
            pq.add(adVertex,
                    input.estimatedDistanceToGoal(adVertex, end)
                            + disFromSource);
            disTo.put(adVertex, disFromSource);
            edgeTo.put(adVertex, ogVertex);
        }
    }

    /** Tells the outcome of A* algorithm.
     * @return Returns the outcome
     */
    public SolverOutcome outcome() {
        return result;
    }
    /** Gives the path in the order of start to end.
     * @return Returns the path */
    public List<Vertex> solution() {
        return solution;
    }

    /** Gives the total cost from start to end vertex.
     * @return Returns the total cost
     */
    public double solutionWeight() {
        if (outcome() == SolverOutcome.TIMEOUT
                || outcome() == SolverOutcome.UNSOLVABLE) {
            return 0;
        }
        return solutionWeight;
    }

    /** Number of states explored.
     *
     * @return Returns the number of times priority queue was dequeued.
     */
    public int numStatesExplored() {
        return numStates;
    }

    /** Time spent for A* to complete.
     *
      * @return The amount of time the algorithm spent to finish
     */
    public double explorationTime() {
        return timeSpent;
    }
}
