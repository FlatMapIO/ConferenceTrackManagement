package conference

import java.time

import scala.annotation.tailrec
import scala.util.matching.Regex.Match

/**
  * Created by huodon on 16/1/4.
  */


trait Scheduler {
  /**
    * Schedule algorithm with immutable and recursive
    *
    * @param events         Schedule Events source
    * @param days           Session count
    * @param duration       How long duration time of per session
    * @param durationFactor Duration time tolerance (must be between duration and duration+durationFactor)
    * @return Scheduled events group
    */
  def schedule(events: Seq[Event], days: Int, duration: Int, durationFactor: Int) = {
    @tailrec
    def select(rem: Seq[Event], stack: Seq[Event], matched: Seq[Seq[Event]]): Seq[Seq[Event]] = {
      if (matched.size == days) return matched
      if (rem.isEmpty) return Seq.empty[Seq[Event]]
      val sum = stack.foldLeft(0)((z, x) => z + x.duration)
      val valid = if (durationFactor == 0) sum == duration else sum >= duration && sum <= duration + durationFactor
      val (nSel: Seq[Event], nRem: Seq[Event], nMatched) =
        if (valid) (Seq.empty[Event], rem.tail, matched :+ stack)
        else if (sum > duration + durationFactor) (stack.dropRight(1), rem.tail, matched)
        else (stack :+ rem.head, rem.tail, matched)

      select(nRem, nSel, nMatched)
    }

    select(events, Seq.empty[Event], Seq.empty[Seq[Event]])
  }
}

/**
  * Represents a simple implemented <code>Scheduler</code> trait of <code>Conference</code> management
  *
  * @param events input talks list for conference, maybe parse from string with <code>Conference.fromString</code>.
  * TODO implement when rest as Some(Int) scheduler will genrate Seq[Talk] for onec
  */
class Conference private(val events: Seq[Event], restTime: Option[Int]) extends Scheduler {
  lazy val totalDuration = events.foldLeft(0)((a, e) => a + e.duration) /* +
    (events.size * restTime.getOrElse(0)) */

  lazy val possibleDays = if (totalDuration < Rule.perDayMinDuration) 1
  else totalDuration / Rule.perDayMinDuration

  /**
    * Group events as <code>Tracks</code>
    *
    * @return
    */
  def tracks(): Seq[Track] = {
    val morningSession = schedule(events, possibleDays, Rule.morningDuration, 0)
    val rem = events.toSet &~ morningSession.flatten.toSet toSeq
    val afternoonSession = schedule(rem, possibleDays, Rule.afternoonMinDuration, Rule.afternoonDurationFactor)

    for (i <- 0 until possibleDays) yield Track(i + 1, (morningSession(i) :+ Lunch) ++ (afternoonSession(i) :+ NetworkingEvent))
  }
}

object Conference {
  def readFromFile(filePath: String): Conference = {
    import java.nio.file

    import scala.collection.convert.decorateAsScala._
    val content = file.Files.readAllLines(file.Paths.get(filePath))
    readFromLines(content.asScala)
  }

  def readFromLines(input: Seq[String]): Conference = {
    val talks = input.map(Event.fromString).filter(_.nonEmpty).map(_.get)

    /*
     * TODO Maybe this problem has addition condition, see <code>App::input2</code>
     */
    def hasRestTime(ln: String): Option[Int] = {
      val re = """(rest)(\d+)(min)""".r
      val res: Option[Match] = re.findFirstMatchIn(ln)
      res.filter(_.groupCount == 3).map(_.group(2).toInt)
    }
    val rest: Option[Int] = hasRestTime(input.head).orElse(hasRestTime(input.last))

    new Conference(talks.sortWith((a, b) => a.duration > b.duration), rest)
  }

  def readFromString(talks: String): Conference = readFromLines(talks.lines.toSeq)
}


object Rule {
  val morningSessionStartTime = time.LocalTime.of(9, 0)
  val morningSessionEndTime = time.LocalTime.of(12, 0)
  val afternoonSessionStartTime = time.LocalTime.of(13, 0)
  val afternoonSessionEndTimeEarlie = time.LocalTime.of(16, 0)
  val afternoonSessionEndTimeLater = time.LocalTime.of(17, 0)
  val morningDuration = time.Duration.between(morningSessionStartTime, morningSessionEndTime).toMinutes.toInt
  val afternoonMinDuration = time.Duration.between(afternoonSessionStartTime, afternoonSessionEndTimeEarlie).toMinutes.toInt
  val afternoonMaxDuration = time.Duration.between(afternoonSessionStartTime, afternoonSessionEndTimeLater).toMinutes.toInt
  val afternoonDurationFactor = 60

  def perDayMinDuration = time.Duration.between(morningSessionStartTime, afternoonSessionEndTimeEarlie).toMinutes.toInt - 60

}

