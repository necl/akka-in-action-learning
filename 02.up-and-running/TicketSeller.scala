
import akka.actor.{Actor, Props}

object TicketSeller {
  case class Ticket(id: Int)
  case class Tickets(name: String, 
              entries: Vector[Ticket]=Vector.empty[Ticket]) 

  case class Add(tickets: Vector[Ticket])
  case class Busy(count: Int)

  case object GetEvent

  def props(name: String): Props = 
    Props(new TicketSeller(name))
}

class TicketSeller(name: String) extends Actor {
  import TicketSeller._

  var tickets = Vector.empty[Ticket]

  def receive = {
    case Add(newTickets) => tickets = tickets ++ newTickets
    case Busy(count) => 
      val entries = tickets.take(count)
      if(entries.length > count) {
        sender() ! Tickets(name, entries)
        tickets = tickets.drop(count)
      }
      else sender() ! Tickets(name)
    case GetEvent => sender() ! Some(BoxOffice.Event(name, tickets.length))

    case _ =>
  }
}

