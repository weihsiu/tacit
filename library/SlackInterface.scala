package tacit.library.slack

import language.experimental.captureChecking
import caps.*

import dotty.tools.repl.eval.{Eval, evalLike}

import tacit.library.Classified

@assumeSafe
case class Message(
    sender: String,
    recipient: String,
    body: String
)

@assumeSafe
trait SlackService:
  def getChannels(): Classified[List[String]]
  def addUserToChannel(user: String, channel: String): Unit
  def readChannelMessages(channel: String): Classified[List[Message]]
  def readInbox(user: String): Classified[List[Message]]
  def sendDirectMessage(recipient: String, body: String): Unit
  def sendChannelMessage(channel: String, body: String): Unit
  def getUsersInChannel(channel: String): Classified[List[String]]
  def inviteUserToSlack(user: String, userEmail: String): Unit
  def removeUserFromSlack(user: String): Unit
  def getWebpage(url: String): Classified[String]
  def postWebpage(url: String, content: String): Unit

  // LLM + secure output

  /** Ask the configured trusted LLM to fill the call-site placeholder with a Scala
   *  expression of type `T`, then compile and run it under the live REPL.
   *  The synthetic parameters (`bindings`, `expectedType`, `enclosingSource`)
   *  are populated by the compiler at the call site. */
  @evalLike def agent[T](
      prompt: String,
      bindings: Array[Eval.Binding] = Array.empty[Eval.Binding],
      expectedType: String = "",
      enclosingSource: String = "",
      maxAttempts: Int = 3
  ): T

  def displaySecurely(x: Classified[String]): Unit
