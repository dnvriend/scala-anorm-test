package com.github.dnvriend

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.Source
import akka.stream.testkit.TestSubscriber
import akka.stream.testkit.scaladsl.TestSink
import akka.testkit.TestProbe
import akka.util.Timeout
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.Try

abstract class TestSpec extends FlatSpec with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit var system: ActorSystem = _
  implicit var ec: ExecutionContext = _
  implicit var mat: Materializer = _
  implicit val pc: PatienceConfig = PatienceConfig(timeout = 5.seconds)
  implicit val timeout = Timeout(5.seconds)

  def randomId = UUID.randomUUID.toString

  implicit class PimpedByteArray(self: Array[Byte]) {
    def getString: String = new String(self)
  }

  implicit class PimpedFuture[T](self: Future[T]) {
    def toTry: Try[T] = Try(self.futureValue)
  }

  implicit class SourceOps[A](src: Source[A, _]) {
    def testProbe(f: TestSubscriber.Probe[A] => Unit): Unit =
      f(src.runWith(TestSink.probe(system)))
  }

  def killActors(actors: ActorRef*): Unit = {
    val tp = TestProbe()
    actors.foreach { (actor: ActorRef) =>
      tp watch actor
      actor ! PoisonPill
      tp.expectTerminated(actor)
    }
  }

  override protected def beforeAll(): Unit = {
    system = ActorSystem()
    ec = system.dispatcher
    mat = ActorMaterializer()
  }

  override protected def afterAll(): Unit = {
    system.terminate().toTry should be a 'success
  }
}
