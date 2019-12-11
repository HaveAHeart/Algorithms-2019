package lesson5;

import javafx.util.Pair;
import kotlin.NotImplementedError;

import java.lang.reflect.Array;
import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {

    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */

    //all the estimated memory/complexity is for the worst cases, v is the amount of vertices, e - of edges
    //dfs realisation complexity: O(v+e),
    //memory: O(v+e) for result(v) and connections(e)
    private static List<Pair<Graph.Vertex, Graph.Edge>> dfs(Graph.Vertex prev, Graph.Vertex vertex,
        Graph.Vertex search, Set<Graph.Vertex> visited, Set<Graph.Edge> ignored, Graph graph) {
        List<Pair<Graph.Vertex, Graph.Edge>> result = new ArrayList<>();
        Map<Graph.Vertex, Graph.Edge> connections = graph.getConnections(vertex);
        visited.add(vertex);

        for (Graph.Vertex next : graph.getNeighbors(vertex)) {
            if (!next.equals(prev) && !ignored.contains(connections.get(next))
                    && (!visited.contains(next) || next.equals(search))) {
                if (next.equals(search)) {
                    result.add(new Pair(search, connections.get(search)));
                    return result;
                }
                //recursion - anyway, the worst case is travelling
                //through all the edges and through all the vertices(v+e)
                List<Pair<Graph.Vertex, Graph.Edge>> nextRes = dfs(vertex, next, search, visited, ignored, graph);
                if (nextRes != null) {
                    result.addAll(nextRes);
                    result.add(new Pair(next, connections.get(next)));
                    return result;
                }
            }
        }
        return null;
    }

    //complexity: O(v+e) due to dfs,
    //memory: total O(v+e), O(v) for visited vertices,but still better then O(v+e) from the dfs
    private static List<Pair<Graph.Vertex, Graph.Edge>> findCycle(
            Graph.Vertex vertex, Graph graph, Set<Graph.Edge> ignored) {

        Set<Graph.Vertex> visited = new HashSet<>();
        return dfs(null, vertex, vertex, visited, ignored, graph);
    }

    //Complexity: still O(v+e), if we'll have more cimple cycles - they'll be smaller
    //memory: O(2v+e) = O(v+e)
    private static List<Graph.Edge> groupCycles(Graph.Vertex vertex, Graph graph, Set<Graph.Edge> ignored) {
        List<Pair<Graph.Vertex, Graph.Edge>> cycle;
        ArrayList<Graph.Edge> result = new ArrayList<>(); //the result takes additional O(v) memory

        do {
            cycle = findCycle(vertex, graph, ignored);
            if (cycle != null) {
                for (Pair pair : cycle) {
                    Graph.Edge edge = (Graph.Edge) pair.getValue();
                    ignored.add(edge);
                }
                result.addAll(groupCycles(cycle.get(0).getKey(), graph, ignored));
                for (int i = 1; i < cycle.size(); i++) {
                    result.add(cycle.get(i - 1).getValue());
                    result.addAll(groupCycles(cycle.get(i).getKey(), graph, ignored));
                }
                result.add(cycle.get(cycle.size() - 1).getValue());

            }

        } while (cycle != null);
        //for (Graph.Edge edge : result) System.out.println(edge.getBegin() + " - " + edge.getEnd());
        //System.out.println();
        return result;
    }

    //complexity: O(v+e)
    //memory: O(v+e)
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        //empty graph should return empty result
        //complexity: O(1)
        if(graph.getVertices().isEmpty()) return new ArrayList<>();
        //there are Euler loops in the graph if every vertex have an odd amount of neighbours
        //complexity: O(v)
        for (Graph.Vertex vertex : graph.getVertices()) {
            if (graph.getConnections(vertex).size() % 2 != 0) return new ArrayList<>();
        }
        //checking for the separated vertices: wfs with checking every vertex
        //complexity: O(v+e) for wfs, O(v) for comparing the graph.getVertices() and checkedVertices
        //memory: O(v) - there are two collections(checkedVertices, checkingQueue) which can be up to O(v) memory,
        //but the bigger the first is, the smaller is another
        Set<Graph.Vertex> checkedVertices = new HashSet<>();
        Deque<Graph.Vertex> checkingQueue = new ArrayDeque<>();
        Graph.Vertex initVertex = graph.getVertices().iterator().next();
        checkingQueue.addLast(initVertex);
        while (!checkingQueue.isEmpty()) {
            Graph.Vertex nextVertex = checkingQueue.pollLast();
            checkedVertices.add(nextVertex);
            for (Graph.Vertex neighbour : graph.getNeighbors(nextVertex))
                if (!checkedVertices.contains(neighbour)) checkingQueue.addLast(neighbour);

        }
        for (Graph.Vertex vertex : graph.getVertices())
            if (!checkedVertices.contains(vertex)) return new ArrayList<>();

        //the main stuff finally goes on: in summary, getting every edge only once by adding it to the ignored Set
        //after their first usage
        //and, the groupCycles method, complexity: O(v+e)
        //memory: O(v+e)
        Set<Graph.Edge> ignored = new HashSet<>();
        return groupCycles(initVertex, graph, ignored);

    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */


    private static Path simpleLongestPathForVertex(Graph.Vertex vertex, Graph graph) {

        //dfs from every vertex with the return of the longest path
        //Complexity: O(v + e) due to checking every vertex and every edge
        //Memory: O(v) in the worst cases:
        // A -- B
        // | -- C
        // | -- D
        // .......
        //all the vertices except one will be in the queue
        //or, the existance of both currRoute and longestPath(its current state copy)
        //in the worst case it will take O(2*vlp) = O(vlp) memory,
        //where vlp is the amount of vertices it the longest simple path

        //wanna try here a stack-based realisation of dfs instead of recursion-based, which was in findEulerLoop

        Graph.Vertex currVertex;
        Integer currDepth;
        Deque<Pair<Graph.Vertex, Integer>> queue = new ArrayDeque<>(); //neighbour and its search depth
        queue.addLast(new Pair<>(vertex, 1));
        List<Graph.Vertex> currRoute = new ArrayList<>();
        List<Graph.Vertex> longestPath = new ArrayList<>();

        while (!queue.isEmpty()) {
            Pair<Graph.Vertex, Integer> pair = queue.pollLast();
            currVertex = pair.getKey();
            currDepth = pair.getValue();

            while (currRoute.size() >= currDepth) currRoute.remove(currRoute.size() - 1);
            currRoute.add(currVertex);
            if (currRoute.size() > longestPath.size()) longestPath = (List) ((ArrayList) currRoute).clone();

            for (Graph.Vertex neighbour : graph.getNeighbors(currVertex)) {
                if (!currRoute.contains(neighbour)) queue.addLast(new Pair<>(neighbour, currDepth + 1));
            }
        }

        Path result = new Path();
        for (Graph.Vertex v : longestPath) {
            if (result.getVertices().isEmpty()) result = new Path(v);
            else result = new Path(result, graph, v);
        }
        return result;

    }

    //complexity: O((e+v)*v) ~ O(v^2) due to usage of simpleLongestPathForVertex on every vertex
    //memory: O(v) - same
    public static Path longestSimplePath(Graph graph) {
        Path longestSimplePath = new Path();
        for (Graph.Vertex vertex : graph.getVertices()) {
            Path currPath = simpleLongestPathForVertex(vertex, graph);
            if (currPath.getLength() > longestSimplePath.getLength()) longestSimplePath = currPath;
        }
        return longestSimplePath;
    }
}
