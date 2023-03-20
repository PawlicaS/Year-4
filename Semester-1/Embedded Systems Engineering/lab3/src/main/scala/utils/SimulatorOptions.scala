package utils


import firrtl.{ExecutionOptionsManager, HasFirrtlOptions}
import treadle.{HasTreadleOptions, TreadleOptionsManager, TreadleTester}
import java.io.{File, PrintWriter, RandomAccessFile}

import chisel3.{ChiselExecutionFailure,ChiselExecutionSuccess,HasChiselExecutionOptions}
//import net.fornwall.jelf.ElfFile

import scala.collection.SortedMap
import scala.util.control.NonFatal

case class SimulatorOptions(
                             maxCycles           : Int              = 0
                           )
  extends firrtl.ComposableOptions {
}

trait HasSimulatorOptions {
  self: ExecutionOptionsManager =>

  val simulatorOptions = SimulatorOptions()

  parser.note("simulator-options")

  parser.opt[Int]("max-cycles")
    .abbr("mx")
    .valueName("<long-value>")
    .foreach {x =>
      simulatorOptions.copy(maxCycles = x)
    }
    .text("Max number of cycles to simulate. Default is 0, to continue simulating")
}

class SimulatorOptionsManager extends TreadleOptionsManager with HasSimulatorSuite

trait HasSimulatorSuite extends TreadleOptionsManager with HasChiselExecutionOptions with HasFirrtlOptions with HasTreadleOptions with HasSimulatorOptions {
  self : ExecutionOptionsManager =>
}
