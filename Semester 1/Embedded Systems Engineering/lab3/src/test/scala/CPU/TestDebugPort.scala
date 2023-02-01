package CPU

import Chisel.fromtIntToLiteral
import chisel3.{Hexadecimal, PrintableHelper}
import chisel3.iotesters.{Driver, PeekPokeTester}
import utils.CPUConfig

import chisel3.util.experimental.loadMemoryFromFile


class DebugTests(c: Tile) extends PeekPokeTester(c) {
  var myList = Array(1, 5, 3,88,4,66,6)
  var cnt=0
  var b = false
  /*
  TODO
  Transmit the array
   */
  do {
    /*
    1. place the next element of the list into the input port c.io.rx_data of the data Input module.
       Use the function poke to do this.
    2. Turn on the c.io.rx_rstrb using the function poke to indicate to the data Input module that data is
       available.
    3. step the simulation using the step function.
    4. check the c.io.rx_data_done port to see if the I/O module has read the current integer of the array
    4a. if the Input module has completed the last character then we move to the next integer in the array
    ==> if everything works then you should see the debug message
    write to address 20000008 and the current integer value
    */

    poke(c.io.rx_rstrb, 1)
    poke(c.io.rx_data, myList(cnt))


    step(1)
    if (peek(c.io.rx_data_done) == 1) {
      cnt=cnt+1
    }

  }while (cnt<myList.length)
  // finished with the read strobe c.io.rx_rstrb set it to 0
  poke(c.io.rx_rstrb, 0)

  do {
    /*TODO
    read data from the debug I/O device
    1. call step(1) to step the simulation.
    2. use peek(c.io.tx_wstrb) to read the transmit strobe if this strobe is one then there is data available from
    the debug device.
    3. use peek(c.io.tx_data) to read the data from the debug device
    4. print out the data to the user.
    5. the port c.io.tx_data returns an integer an integer such as:
    val x = 88
    6. leave the while(peek(c.io.test_passed)!= 1) code
    alone as this will terminate the simulation
     */
    step(1)
    if (peek(c.io.tx_wstrb) == 1) {
      printf("Data Value: %d\n", peek(c.io.tx_data))
    }



    // println("in top test_passed %d" + peek(c.io.test_passed))

  } while(peek(c.io.test_passed)!= 1)
}


object Testing {
  def main(args: Array[String]): Unit = {

    val conf = new CPUConfig()
    // Convert the binary to a hex file that can be loaded by treadle
    // (Do this after compiling the firrtl so the directory is created)

    conf.cpuType     = "single-cycle"
    conf.memFile     = "firmware/firmware.hex"
    conf.memType     = "combinational"
    conf.memPortType = "combinational-port"
    conf.memLatency  = 0
    if (!Driver(() => new Tile(conf),"treadle")(c => new DebugTests(c))) System.exit(1)
  }
}
