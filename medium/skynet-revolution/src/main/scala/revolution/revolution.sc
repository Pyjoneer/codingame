val ein = List((1, 3), (2, 3), (0, 1), (0, 2))

val edges = ein

val graph = edges.groupBy(_._1)

val si = 0
val ei = 3

val path = shortest(si, graph, ei)

def shortest(s: Int, graph: Map[Int, List[(Int, Int)]], End: Int): List[Int] = {
    val root = -1

    def findPath(vertices: List[Int], path: List[Int], visited: Map[Int, Int]): List[Int] = vertices match {
        case Nil => path.filterNot(_ == root).reverse
        case x :: xs => findPath(List(visited.get(vertices.head)).flatten ::: xs, vertices.head :: path, visited)
    }

    def bfs(vertices: List[(Int, Int)], visited: Map[Int, Int]): List[Int] = vertices match {
        case (End, x2) :: xs => findPath(List(x2), List(End), visited)
        case x :: xs if (visited contains x._1) => bfs(xs, visited)
        case x :: xs =>
            bfs(xs ::: graph.get(x._1).map(_.map(_._2)).toList.flatten.map {(_, x._1)}, visited + (x._1 -> x._2))
    }

    bfs(List(s -> root), Map())
}