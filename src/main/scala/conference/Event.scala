package conference

import scala.annotation.switch
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

/**
  * Created by huodon on 16/1/4.
  */


/**
  * Talk and Lunch and Networking Event holder
  */
sealed trait Event {
  val title: String
  val duration: Int
}

case class Talk(val title: String, val duration: Int) extends Event

case class Rest(title: String = "Rest", duration: Int)

object Lunch extends Event {
  val title = "Lunch"
  val duration = 60
}

object NetworkingEvent extends Event {
  val title = "Networking Event"
  val duration = 60
}

object Event {
  def fromString(input: String): Option[Talk] = {
    input.split(" ").last match {
      case _@a if a.equalsIgnoreCase("lightning") => Some(Talk(input, 5))
      case _@a => """(\d+)(\w+)""".r.findFirstMatchIn(a).map(m =>Talk(input, m.group(1).toInt))
    }
  }
}
