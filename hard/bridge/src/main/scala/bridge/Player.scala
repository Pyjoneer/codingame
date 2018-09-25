package bridge

import scala.annotation.tailrec

/**
  * Auto-generated code below aims at helping you parse
  * the standard input according to the problem statement.
  **/
object Player extends App {
    val m = readInt // the amount of motorbikes to control
    val v = readInt // the minimum amount of motorbikes that must survive
    val l0 = readLine // L0 to L3 are lanes of the road. A dot character . represents a safe space, a zero 0 represents a hole in the road.
    val l1 = readLine
    val l2 = readLine
    val l3 = readLine

    // game loop
    while(true) {
        val s = readInt // the motorbikes' speed

        var bikes: List[Bike] = List(Bike(0, 1, true), Bike(0, 2, true))
        for (i <- 0 until m) {
            // x: x coordinate of the motorbike
            // y: y coordinate of the motorbike
            // a: indicates whether the motorbike is activated "1" or detroyed "0"
            val Array(x, y, a) = for (i <- readLine split " ") yield i.toInt
            bikes = Bike(x, y, a == 1) :: bikes
        }

        implicit val ctx = Context(v, l0, l1, l2, l3)

        implicit val actions: List[State => Option[State]] = {
            import Action._
            List(speed, jump, up, down, nop, slow)
        }

        // Write an action using println
        // To debug: Console.err.println("Debug messages...")

        // A single line containing one of 6 keywords: SPEED, SLOW, JUMP, WAIT, UP, DOWN.
        val initialState = State(s, bikes, "INIT")

        val sim = simulate(List(List(initialState)))
        sim.reverse.map(_.cmd).filter(_ != "INIT").foreach(println(_))
    }

    @tailrec
    def simulate(steps: List[List[State]])(implicit actions: List[(State) => Option[State]], ctx: Context): List[State] = steps match {
        case Nil :: tl => simulate(tl.head.tail :: steps.tail.tail)   // remove Nil and state that leads to Nil
        case s :: tl if (s.head.isWin) => steps.map(_.head)
        case _ => simulate(actions.map(_(steps.head.head)).flatten :: steps)    // continue simulation with expanded states
    }
}

object Action {
    import scala.math.min

    def speed(state: State)(implicit ctx: Context): Option[State] = changeSpeed(state, (speed) => speed + 1, "SPEED")
    def slow(state: State)(implicit ctx: Context): Option[State] = changeSpeed(state, (speed) => speed - 1, "SLOW")
    def nop(state: State)(implicit ctx: Context): Option[State] = {
        val bikes = state
                .bikes
                .filter(_.alive)
                .map(bike => Bike(bike.x + state.speed, bike.y, canMove(bike.x, bike.y, bike.x + state.speed, bike.y)))

        val nextState = State(state.speed, bikes, "WAIT")
        if (nextState.isValid) Some(nextState) else None
    }

    def jump(state: State)(implicit ctx: Context): Option[State] = {
        val bikes = state
                .bikes
                .filter(_.alive)
                .map(bike => Bike(bike.x + state.speed, bike.y, canJump(bike.x, bike.y, bike.x + state.speed, bike.y)))

        val nextState = State(state.speed, bikes, "JUMP")

        if (nextState.isValid) Some(nextState) else None
    }
    def up(state: State)(implicit ctx: Context): Option[State] = {
        if (state.bikes.filter(_.alive).exists((bike: Bike) => bike.y == 0)) None
        else changeLane(state, -1, "UP")
    }

    def down(state: State)(implicit ctx: Context): Option[State] = {
        if (state.bikes.filter(_.alive).exists((bike: Bike) => bike.y == ctx.lastIndex)) None
        else changeLane(state,1, "DOWN")
    }

    private def changeLane(state: State, laneDiff: Int, cmd: String)(implicit ctx: Context): Option[State] = {
        val bikes = state
                .bikes
                .filter(_.alive)
                .map(bike => {
                    if ((bike.y + laneDiff < 0) || (bike.y + laneDiff >= ctx.roads.length)) // cannot switch lane if bike would hit the bridge rail
                        None
                    else
                        Some(Bike(bike.x + state.speed, bike.y + laneDiff, canMove(bike.x, bike.y, bike.x + state.speed, bike.y + laneDiff)))
                })
                .flatten

        val nextState = State(state.speed, bikes, cmd)

        if (bikes.length != state.bikes.length) None
        else if (nextState.isValid) Some(nextState)
        else None
    }

    private def changeSpeed(state: State, op: (Int) => Int, cmd: String)(implicit ctx: Context): Option[State] = {
        val speed = op(state.speed)
        val bikes = state
                .bikes
                .filter(_.alive)
                .map(bike => Bike(bike.x + speed, bike.y, canMove(bike.x, bike.y, bike.x + speed, bike.y)))

        val nextState = State(speed, bikes, cmd)

        if (nextState.isValid) Some(nextState) else None
    }

    private def canMove(fromX: Int, fromY: Int, toX: Int, toY: Int)(implicit ctx: Context): Boolean = {
        val fdx = fromX + 1                         // the first X where can be a hole

        ((fdx to min(toX, ctx.lastIndex)) forall(ctx.isRoad(_, toY))) &&                // check tiles in destination lane
                ((fdx to min (toX - 1, ctx.lastIndex)) forall(ctx.isRoad(_, fromY)))    // check tiles in origin lane
    }

    private def canJump(fromX: Int, fromY: Int, toX: Int, toY: Int)(implicit ctx: Context): Boolean = ctx.isRoad(toX, toY)
}

case class Bike(x: Int, y: Int, alive: Boolean)

object Bike {
    def apply(position: (Int, Int), alive: Boolean) = new Bike(position._1, position._2, alive)
}

case class Context(roads: Array[Array[Boolean]], mustSurvive: Int) {
    def isRoad(x: Int, y: Int): Boolean = x < length && roads(y)(x)
    def isHole(x: Int, y: Int): Boolean = x < length && !isRoad(x, y)
    val length = roads(0).length
    val lastIndex = length - 1
}

object Context {
    def apply(mustSurvive: Int, roads: String*): Context = Context(roads.map(road => road.map(_ == '.').toArray).toArray, mustSurvive)
}

trait SimStep {
    val cmd: String
}

case class State(speed: Int, bikes: List[Bike], cmd: String) extends SimStep {
    def isValid(implicit ctx: Context): Boolean = bikes.count(_.alive) >= ctx.mustSurvive && speed > 0
    def isWin(implicit ctx: Context): Boolean = isValid && bikes.filter(_.alive).forall(_.x >= ctx.lastIndex)   // let the bikes drive through bridge's roads to win
}