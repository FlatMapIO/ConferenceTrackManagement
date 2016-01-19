package conference

import scala.util.Try


object App {
  val input =
    """Writing Fast Tests Against Enterprise Rails 60min
      |Overdoing it in Python 45min
      |Lua for the Masses 30min
      |Ruby Errors from Mismatched Gem Versions 45min
      |Common Ruby Errors 45min
      |Rails for Python Developers lightning
      |Communicating Over Distance 60min
      |Accounting-Driven Development 45min
      |Woah 30min
      |Sit Down and Write 30min
      |Pair Programming vs Noise 45min
      |Rails Magic 60min
      |Ruby on Rails: Why We Should Move On 60min
      |Clojure Ate Scala (on my project) 45min
      |Programming in the Boondocks of Seattle 30min
      |Ruby vs. Clojure for Back-End Development 30min
      |Ruby on Rails Legacy App Maintenance 60min
      |A World Without HackerNews 30min
      |User Interface CSS in Rails Apps 30min""".stripMargin

  val input2 =

    """Rest 10min per talk
      |Writing Fast Tests Against Enterprise Rails 60min
      |Overdoing it in Python 45min
      |Lua for the Masses 30min
      |Ruby Errors from Mismatched Gem Versions 45min
      |Common Ruby Errors 45min
      |Rails for Python Developers lightning
      |Communicating Over Distance 60min
      |Accounting-Driven Development 45min
      |Woah 30min
      |Sit Down and Write 30min
      |Pair Programming vs Noise 45min
      |Rails Magic 60min
      |Ruby on Rails: Why We Should Move On 60min
      |Clojure Ate Scala (on my project) 45min
      |Programming in the Boondocks of Seattle 30min
      |Ruby vs. Clojure for Back-End Development 30min
      |Ruby on Rails Legacy App Maintenance 60min
      |A World Without HackerNews 30min
      |User Interface CSS in Rails Apps 30min""".stripMargin


  def main(args: Array[String]) {
    println("input:\n" + input)
    println("---------------")
    println("output: \n")
    val c = Conference.readFromLines(input.lines.toSeq)
    val tracks = c.tracks()
    tracks foreach println
  }
}