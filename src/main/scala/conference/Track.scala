package conference

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.{TemporalUnit, ChronoUnit}

import scala.annotation.tailrec

/**
  * Created by huodon on 16/1/4.
  */


/**
  * Representation talks track for one day
  *
  * @param no     track number
  * @param events Seq[Event]
  */
case class Track(no: Int, events: Seq[Event]) {
  override def toString: String = {
    def foldString(time: LocalTime, ex: Seq[Event], agg: String): String = {
      if (ex.isEmpty) agg else foldString(time.plus(ex.head.duration, ChronoUnit.MINUTES), ex.tail, agg + s"${time.format(Track.dataTimeFormatter)} ${ex.head.title} \n")
    }
    foldString(Rule.morningSessionStartTime, events, "Track " + no + ":\n")
  }
}

object Track {
  // format reference https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
  private val dataTimeFormatter = DateTimeFormatter.ofPattern("hh:mma")
}
