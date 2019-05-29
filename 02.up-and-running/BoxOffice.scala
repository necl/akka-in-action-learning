import akka.actor.{Actor, ActorRef, Props}

object BoxOffice {

  case class Event(name: String, tickets: Int)

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
      def getEvents = context.children.map{ child =>
        self.ask(GetEvent(child.path.name)).mapTo[Option[Event]]
      }

    case _ =>
  }
  def createTicketSeller(name: String): ActorRef = 
    context.actorOf(TicketSeller.props(name), name)
}

