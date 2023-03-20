package CPU

import Chisel.fromtIntToLiteral
import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}
import utils.{CPUConfig, Disassembler}
import chisel3.util.experimental.loadMemoryFromFile
import firrtl.stage.FirrtlSourceAnnotation
import firrtl.{ExecutionOptionsManager, HasFirrtlOptions}

import java.io.{File, PrintWriter, RandomAccessFile}
import scala.collection.SortedMap
import net.fornwall.jelf.ElfFile
import treadle.{HasTreadleOptions, TreadleOptionsManager, TreadleTester};




case  class SimulatorOptions(
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






object Testing1 {
  def build(optionsManager: SimulatorOptionsManager, conf: CPUConfig): String = {
    optionsManager.firrtlOptions = optionsManager.firrtlOptions.copy(compilerName = "low")

    chisel3.Driver.execute(optionsManager, () => new Tile(conf)) match {
      case ChiselExecutionSuccess(Some(_), _, Some(firrtlExecutionResult)) =>
        firrtlExecutionResult match {
          case firrtl.FirrtlExecutionSuccess(_, compiledFirrtl) =>
            compiledFirrtl
          case firrtl.FirrtlExecutionFailure(message) =>
            throw new Exception(s"FirrtlBackend: Compile failed. Message: $message")
        }
      case _ =>
        throw new Exception("Problem with compilation")
    }
  }
  def elfToHex(filename: String, outfile: String) = {
    val elf = ElfFile.fromFile(new java.io.File(filename))
    val sections = Seq(".text", ".data") // These are the sections we want to pull out
    // address to put the data -> offset into the binary, size of section
    var info = SortedMap[Long, (Long, Long)]()
    // Search for these section names
    for (i <- 1 until elf.num_sh) {
      val section =  elf.getSection(i)
      if (sections.contains(section.getName)) {
        //println("Found "+section.address + " " + section.section_offset + " " + section.size)
        info += section.address -> (section.section_offset, section.size)
      }
    }

    // Now, we want to create a new file to load into our memory
    val output = new PrintWriter(new File(outfile))
    val f = new RandomAccessFile(filename, "r")
    // println("Length: "+ f.length)
    var location = 0
    for ((address, (offset, size)) <- info) {
      //println(s"Skipping until $address")
      while (location < address) {
        require(location + 3 < address, "Assuming addresses aligned to 4 bytes")
        output.write("00000000\n")
        location += 4
      }
      //println(s"Writing $size bytes")
      val data = new Array[Byte](size.toInt)
      f.seek(offset)
      f.read(data)
      var s = List[String]()
      for (byte <- data) {
        s = s :+ ("%02X" format byte)
        location += 1
        if (location % 4 == 0) {
          // Once we've read 4 bytes, swap endianness
          output.write(s(3)+s(2)+s(1)+s(0)+"\n")
          s = List[String]()
        }
      }
      //println(s"Wrote until $location")
    }
    output.close()
    // Return the final PC value we're looking for
    var symbol = elf.getELFSymbol("_last")
    val sh = elf.getSymbolTableSection();
    if (sh != null) {
      val numSymbols = sh.getNumberOfSymbols();
     // println(s"numSymbols $numSymbols ")
      var found = false
      var i = 0
      while (i < numSymbols && found == false) {
        symbol = sh.getELFSymbol(i);
        val name = symbol.getName();
        if (name != null) {
         // println(s"strings  " + name)
          if (name.equals("_last")) {
            found = true
          }
          val addr = symbol.value
         // println(s"address $addr " + name)
        }
        i = i + 1
      }
    }


    if (symbol != null) {
     // println(s"getelfsymbol ${symbol.value} ")
      symbol.value
    }
    else 0x200L
  }

  def main(args: Array[String]): Unit = {
    val optionsManager = new SimulatorOptionsManager()
    val conf = new CPUConfig()
    // Convert the binary to a hex file that can be loaded by treadle
    // (Do this after compiling the firrtl so the directory is created)
    //var endPC = elfToHex("src/test/resources/risc-v/add1", "src/test/resources/risc-v/add1.hex")
    val filename = "src/test/resources/risc-v/bge"
    var endPC = elfToHex(filename, filename+".hex")

    //endPC = 0x50
    conf.cpuType     = "single-cycle"
    //conf.memFile = "src/test/resources/risc-v/add1.hex";
    conf.memFile = filename+".hex";
    conf.memType     = "combinational"
    conf.memPortType = "combinational-port"
    conf.memLatency  = 0
    // This compiles the chisel to firrtl
    val compiledFirrtl = build(optionsManager, conf)
   // if (!Driver(() => new Tile(conf),"treadle")(c => new DebugTests1(c))) System.exit(1)
    // println("compiledFirrtl: ",compiledFirrtl)
    // Instantiate the simulator
    val sourceAnnotation = FirrtlSourceAnnotation(compiledFirrtl)
    val simulator = TreadleTester(sourceAnnotation +: optionsManager.toAnnotationSeq)
    val cycles = 3000000
    var cycle =0
    println(s"endpc=${endPC.toString().padTo(8, ' ')}")
    println(filename+".risv")
    simulator.poke(s"cpu.registers.regs_5", 20)
    simulator.poke(s"cpu.registers.regs_6", 10)
    simulator.poke(s"cpu.registers.regs_7", 15)
    simulator.poke(s"cpu.registers.regs_28", 14)
    while (cycle < cycles && simulator.peek("cpu.pc") != endPC) {

        //val v = simulator.peek("cpu.pc")
      val pc = simulator.peek("cpu.pc").toInt
      val v = simulator.peekMemory("mem.memory", pc/4)
      // Note: the memory is a 32-bit memory
      val inst = Disassembler.disassemble(v.longValue())
      val hex = v.toInt.toHexString.reverse.padTo(8, '0').reverse
      println(s"${pc.toString().padTo(8, ' ')}: ${inst.padTo(20, ' ')} (0x${hex})")
        //println(s"PC: ${v}")
     // println(s"${cycle} cycles simulated.")
      simulator.step(1)
      cycle += 1
    }
    for( a <- 0 to 20) {
      val v = simulator.peek(s"cpu.registers.regs_$a")
      println(s"reg $a: ${v}")
    }
  }
}
