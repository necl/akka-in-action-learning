import scala.concurrent.Future
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{ask, pipe}

import akka.util.Timeout
import java.util.concurrent.TimeUnit

object BoxOffice {

  case class Event(name: String, tickets: Int)
  case class Events(events: Vector[Event])

  case class CreateEvent(name: String, tickets: Int) 
  case class  GetEvent(name: String)
  case object GetEvents


  //response
  case class EventCreated(event: Event)
  case object EventExists


  def props(): Props = Props(new BoxOffice())
}

class BoxOffice extends Actor {
  import BoxOffice._
  import context._  //for implicit ExecutionContext for Future.sequence


  def receive= {
    case CreateEvent(name, tickets) =>
      def create() = {
        val seller = createTicketSeller(name)
        val newTickets = (1 to tickets).map{ ticketId =>
            TicketSeller.Ticket(ticketId)
        }.toVector
        seller ! TicketSeller.Add(newTickets)
        sender() ! EventCreated(Event(name, tickets))
      }
      context.child(name).fold(create())(_ => sender() ! EventExists)

    case GetEvent(name) =>
      def notFound = sender() ! None
      def getEvent(child: ActorRef) = child forward TicketSeller.GetEvent
      context.child(name).fold(notFound)(getEvent)

    case GetEvents =>
      implicit def timeout = Timeout(2, TimeUnit.SECONDS)
      def getEvents(): Iterable[Future[Option[Event]]] = 
        context.children.map { child =>
          self.ask(GetEvent(child.path.name)).mapTo[Option[Event]]
      }
      def convertToEvents(f: Future[Iterable[Option[Event]]]) =
        f.map(_.flatten).map(l=> Events(l.toVector))

      val evsFutrue: Future[Iterable[Option[Event]]] =
                                  Future.sequence(getEvents)

      pipe(convertToEvents(evsFutrue)) to sender()

    case _ =>
  }
  def createTicketSeller(name: String): ActorRef = 
    context.actorOf(TicketSeller.props(name), name)
}

