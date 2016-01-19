package conference.test

import org.scalatest._

import conference._
import scala.concurrent.duration._
import scala.util.Try
import scala.util.matching.Regex.Match

/**
  * Created by huodon on 16/1/3.
  */

/**
  * Rules
  * - The conference has multiple tracks each of which has a morning and afternoon session.
  * - Each session contains multiple talks.
  * - Morning sessions begin at 9am and must finish before 12 noon, for lunch.
  * - Afternoon sessions begin at 1pm and must finish in time for the networking event.
  * - The networking event can start no earlier than 4:00 and no later than 5:00.
  * - No talk title has numbers in it.
  * - All talk lengths are either in minutes (not hours) or lightning (5 minutes).
  * - Presenters will be very punctual; there needs to be no gap between sessions.
  */
class ConferenceTest extends FunSpec {

  val testInput =
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


  val talksCount = testInput.lines.length
  val conference: Conference = Conference.readFromLines(testInput.lines.toSeq)

  describe("Conference Track Management Test Case") {

    describe("When input parsed") {
      it(s"Talks count should be $talksCount") {
        assert(talksCount == conference.events.size)
      }

      it("Should be 2 days and tow tracks") {
        assert(conference.possibleDays == 2)
      }
    }

    describe("When tracks scheduled") {
      val tracks: Seq[Track] = conference.tracks()

      it("Tracks count should be equal to days") {
        assert(tracks.length == conference.possibleDays)
      }



      it("Start at 9:00") {
        val mornings = tracks.map(x => x.toString.lines.toSeq(1))
        val r = """(\d+):(\d+).*""".r
        mornings.foreach(x => {
          val res = r.findFirstMatchIn(x)
          assert(res.isDefined)
          assert(res.get.group(1).toInt == 9)
          assert(res.get.group(2).toInt == 0)
        })
      }
      it("Lunch at 12:00") {
        val r = """(\d+):(\d+).*Lunch""".r
        tracks.foreach(t => {
          val s = t.toString
          val res = r.findFirstMatchIn(s)
          assert(res.nonEmpty)
          if(res.nonEmpty) {
            val m = res.get
            assert(m.groupCount == 2)
            assert(m.group(1).toInt == 12)
            assert(m.group(2).toInt == 0)
          }
        })
      }

      it("End with Networking event") {
        val r = """(\d+):(\d+).*(Networking\sEvent)""".r
        for (t <- tracks) {

          val res = r.findFirstMatchIn(t.toString)
          assert(res.isDefined)
          if(res.isDefined) {
            val m = res.get
            assert(m.groupCount == 3)
            assert(m.group(1).toInt >= 4 || m.group(1).toInt <= 5)
            assert(m.group(2).toInt >= 4 || m.group(2).toInt <= 5)
          }
        }
      }
    }
  }

}
