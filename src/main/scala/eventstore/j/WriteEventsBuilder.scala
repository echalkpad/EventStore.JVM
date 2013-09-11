package eventstore
package j

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._

/**
 * @author Yaroslav Klymko
 */
class WriteEventsBuilder(val streamId: String) extends Builder[WriteEvents] {
  protected val _streamId: EventStream.Id = EventStream(streamId)
  protected var _events: ListBuffer[EventData] = new ListBuffer()
  protected var _expectedVersion: ExpectedVersion = ExpectedVersion.Any
  protected var _requireMaster: Boolean = true

  def addEvent(x: EventData) = set {
    _events += x
  }

  def addEvents(xs: java.lang.Iterable[EventData]) = set {
    _events ++= xs.asScala
  }

  def event(x: EventData) = set {
    _events = new ListBuffer()
    addEvent(x)
  }

  def events(xs: java.lang.Iterable[EventData]) = set {
    _events = new ListBuffer()
    addEvents(xs)
  }

  def expectNoStream = set {
    _expectedVersion = ExpectedVersion.NoStream
  }

  def expectAnyVersion = set {
    _expectedVersion = ExpectedVersion.Any
  }

  def expectVersion(x: Int) = set {
    _expectedVersion = ExpectedVersion.Exact(x)
  }

  def requireMaster(x: Boolean) = set {
    _requireMaster = x
  }

  def build(): WriteEvents = WriteEvents(
    streamId = _streamId,
    events = Seq(_events: _*),
    expectedVersion = _expectedVersion,
    requireMaster = _requireMaster)
}
