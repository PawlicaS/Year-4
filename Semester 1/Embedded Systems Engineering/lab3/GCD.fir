;buildInfoPackage: chisel3, version: 3.4.3, scalaVersion: 2.12.12, sbtVersion: 1.3.10
circuit GCD : 
  module GCD : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip a : UInt<32>, flip b : UInt<32>, flip e : UInt<1>, z : UInt<32>, v : UInt<1>}
    
    reg x : UInt<32>, clock @[GCD.scala 14:16]
    reg y : UInt<32>, clock @[GCD.scala 15:16]
    node _T = gt(x, y) @[GCD.scala 16:13]
    when _T : @[GCD.scala 16:18]
      node _x_T = sub(x, y) @[GCD.scala 17:14]
      node _x_T_1 = tail(_x_T, 1) @[GCD.scala 17:14]
      x <= _x_T_1 @[GCD.scala 17:9]
      skip @[GCD.scala 16:18]
    else : @[GCD.scala 18:17]
      node _y_T = sub(y, x) @[GCD.scala 19:14]
      node _y_T_1 = tail(_y_T, 1) @[GCD.scala 19:14]
      y <= _y_T_1 @[GCD.scala 19:9]
      skip @[GCD.scala 18:17]
    when io.e : @[GCD.scala 21:17]
      x <= io.a @[GCD.scala 22:9]
      y <= io.b @[GCD.scala 23:9]
      skip @[GCD.scala 21:17]
    io.z <= x @[GCD.scala 25:10]
    node _io_v_T = eq(y, UInt<1>("h00")) @[GCD.scala 26:15]
    io.v <= _io_v_T @[GCD.scala 26:10]
    
