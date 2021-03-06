package bio.gcat.geneticcode.dich.scan

import bio.gcat.geneticcode.dich.conc.ConcScan
import bio.gcat.geneticcode.dich.ct.ClassTable
import bio.gcat.geneticcode.dich.{ClassTableScan, Scan, BinaryDichotomicAlgorithm}

import scala.collection.mutable
import java.util.Date

trait NumberOfClassesConstraints extends IncreaseConstraints[ClassTable] {

  val classSize = 64 // Dummy

  // val noc = nocSingleton.noc
  val noc = new mutable.ArrayBuffer[Int]() ++ (2 to 64)

  override def isSolution(t: ClassTable) = {
    if (noc contains t.classes.size) {
      noc -= t.classes.size // TODO side effect
      // An actor could send a message that this size was found!
      true
    } else false
  }
}

class ScanNumberOfClassesScan(bdas: List[BinaryDichotomicAlgorithm] = List(), size: Int = 6)
  extends Scan[ClassTable](bdas, size) with NumberOfClassesConstraints
  with ClassTableScan {

  override def startMessage = "Scan for all number of classes..."

  override def output(t: ClassTable) {
    log fine "\n--------------------------------------------------------"
    log fine new Date().toString
    log fine t.bdaMkString
    log fine "class size = " + t.classes.size
    log fine "# configurations = " + configs
    log fine "Left class sizes = " + noc.mkString(",")
  }
}

/**
 * Sequential version only.
 */
object ScanNumberOfClassesScan {
  def main(args: Array[String]) {
    new ScanNumberOfClassesScan().run()
  }
}

/**
 * Not working yet! noc is created for every instance.
 * @param bdas
 * @param size
 * @param noActors
 */
class ConcNumberOfClassesScan(bdas: List[BinaryDichotomicAlgorithm] = List(),
                              size: Int = 6,
                              noActors: Int = 3)
  extends ConcScan[ClassTable](bdas, size, noActors)
  with NumberOfClassesConstraints with ClassTableScan {

  override def startMessage = "Scan for all number of classes concurrently..."

  /*
   Design error, output must not be overwritten.
   */
  override def output(t: ClassTable) {
    log fine "\n--------------------------------------------------------"
    log fine new Date().toString
    log fine t.bdaMkString
    log fine "class size = " + t.classes.size
    log fine "# configurations = " + configs
    log fine "Left class sizes = " + noc.mkString(",")
  }

  def cloneScan() = new ConcNumberOfClassesScan(bdas, size, noActors)
}

object ConcNumberOfClassesScan {
  def main(args: Array[String]) {
    new ConcNumberOfClassesScan().run()
  }
}