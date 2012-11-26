package controllers

import play.api._
import play.api.mvc._
import play.api.libs._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import java.util.concurrent.TimeUnit
import Execution.Implicits.defaultContext

object Application extends Controller {
  
  val timeEnum = Streams.getmtime
  val broadcaster = Concurrent.broadcast[String](timeEnum)    //The Play 2.1 way

  val timestep = 2000  // 2 seconds
  
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
 
  //Streams.getmtime is an Enumarator
  //EventSource() return an Enumaratee
  //Streams.getmtime &> EventSource() means: 
  //  passe Streams.getmtime _through_ EventSource and get a modified Enumarator
  def servereventsindividual = Action {
    val mtime = Enumerator.generateM{
      Promise.timeout( {
        val currentMillis = java.lang.System.currentTimeMillis() / 100
        Logger.debug(currentMillis toString)
        Some(currentMillis +" dsec")},
      timestep, TimeUnit.MILLISECONDS )
    }
    Ok.stream(mtime &> EventSource())
  }
  
  // broadcaster._1
  def servereventshub = Action {
    implicit val encoder = Comet.CometMessage.stringMessages
    Ok.stream(broadcaster._1 &> EventSource())
  }
   
  
  //gives the same result as servereventsindividual (not intuitive for me, why isn't timeEnum shared between the client?)
  def servereventsshared = Action {
    Ok.stream(timeEnum &> EventSource())
  }
  

  object Streams {
    val getmtime = Enumerator.generateM{ 
      Promise.timeout( {
        val currentMillis = java.lang.System.currentTimeMillis() / 100
        Logger.debug(currentMillis toString)
        Some(currentMillis +" dsec")},
      timestep, TimeUnit.MILLISECONDS )
    }
    
  }
}