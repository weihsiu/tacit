package tacit
package executor

import core.Context

import java.util.UUID

/** Executes Scala code snippets */
object ScalaExecutor:

  /** Execute a Scala code snippet stateless and return the result */
  def execute(code: String)(using Context): ExecutionResult =
    ManagedRepl().init().run(code)

/** A REPL session that maintains state across executions */
class ReplSession(val id: String)(using Context):
  private val repl = ManagedRepl().init()

  /** Execute code in this session and return the result */
  def execute(code: String): ExecutionResult = repl.run(code)

object ReplSession:
  def create(using Context): ReplSession = ReplSession(UUID.randomUUID().toString)
