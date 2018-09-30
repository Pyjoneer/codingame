package revolution

object Player extends App {
/*
    // n: the total number of nodes in the level, including the gateways
    // l: the number of links
    // e: the number of exit gateways
    val Array(n, l, e) = for(i <- readLine split " ") yield i.toInt

    for(i <- 0 until l) {
        // n1: N1 and N2 defines a link between these nodes
        val Array(n1, n2) = for(i <- readLine split " ") yield i.toInt
    }
    for(i <- 0 until e) {
        val ei = readInt // the index of a gateway node
    }
*/

    val (n, l, e) = (4, 4, 1)
    val edges = List((1, 3), (2, 3), (0, 1), (0, 2))     // n1: N1 and N2 defines a link between these nodes
    val ei = 3  // the index of a gateway node
    val si = 0  // The index of the node on which the Skynet agent is positioned this turn


    e

/*
    // game loop
    while(true) {
        val si = readInt // The index of the node on which the Skynet agent is positioned this turn


        // Write an action using println
        // To debug: Console.err.println("Debug messages...")


        // Example: 0 1 are the indices of the nodes you wish to sever the link between
        println("0 1")
    }
*/
}