/*--------------
auxiliar 1 |AID|
--------------*/

/*import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._
import scala.io._
sealed trait Message
case object Calculate extends Message
case class Work(f: Int) extends Message
case class Result(valor: Double) extends Message
case class Show(conta: Double, duration: Duration) extends Message

class Worker extends Actor{
    
    def conta(f: Int): Double = {
        var fatorial = 1
        if(f != 0 && f != 1)
        {
            for(i <- 2 until f+1)
                fatorial *= i
        }
        return fatorial
    }
    
    override def receive: Receive = {
        case Work(f) => sender ! Result(conta(f))
    }
}
class Master(workers: Int, msgs: Int, listener: ActorRef) extends Actor{
    var conta: Double = _
    var resultados: Int = _
    val start: Long = System.currentTimeMillis
 
    val worker = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(workers)), "worker")
      
    override def receive: Receive = {
        case Calculate => {
                worker ! Work(msgs)
        }
        
        case Result(res) => {
            conta += res
            listener ! Show(conta, (System.currentTimeMillis - start).millis)
            context.stop(self)
        }
    }
}

class Listener extends Actor{
    def receive: Receive = {
      case Show(res, duration) ⇒
        println("Resultado: \t\t%s\n\tTempo: \t%s".format(res, duration))
        context.system.shutdown()
    }
}

object Teste{
    def main(args: Array[String]): Unit = {
        val system = ActorSystem("MainSystem")
        println("Digite os trabalhadores ")
        val workers = StdIn.readInt()
        println("Digite o fatorial ")
        val f = StdIn.readInt()
        val listener = system.actorOf(Props[Listener], "listener")
        val masterActor = system.actorOf(Props(new Master(workers, f, listener)),"masterActor")
        
        masterActor ! Calculate
    }
}*/

/*--------------
auxiliar 2 |AID|
--------------*/

/*import akka.actor._
sealed trait Message
case class Start(s: String) extends Message
case class mensagem(s: String) extends Message

class DataSource(prox: ActorRef, var start: String) extends Actor{
    override def receive: Receive = {
        case Start(s) => {
            println("DataSource " + s)
            prox ! mensagem(s)
        }
    }
    
}

class LowerCase(prox: ActorRef) extends Actor{
    override def receive: Receive = {
        case mensagem(s) => {
            var lc: String = s.toLowerCase()
            println("LowerCase " + lc)
            prox ! mensagem(lc)
        }
    }
    
}

class UpperCase(prox: ActorRef) extends Actor{
    override def receive: Receive = {
        case mensagem(s) => {
            var uc: String = s.toUpperCase()
            println("UpperCase " + uc)
            prox ! mensagem(uc)
        }
    }
    
}

class FilterVowels(prox: ActorRef) extends Actor{
    override def receive: Receive = {
        case mensagem(s) => {
            var fv: String = ""
            for(i <- 0 until s.length()){
                if(s.charAt(i) != 'a' && s.charAt(i) != 'A'
                && s.charAt(i) != 'e' && s.charAt(i) != 'E'
                && s.charAt(i) != 'i' && s.charAt(i) != 'I'
                && s.charAt(i) != 'o' && s.charAt(i) != 'O'
                && s.charAt(i) != 'u' && s.charAt(i) != 'U'){
                    fv += s.charAt(i)
                }
            }
            println("FilterVowels " + fv)
            prox ! mensagem(fv)
        }
    }
    
}

class Duplicate extends Actor{
    override def receive: Receive = {
        case mensagem(s) => {
            var d: String = s+s
            println("Duplicate " + d)
        }
    }
    
}

object Main{
    def main(args: Array[String]): Unit = {
        val system = ActorSystem("MainSystem")
        val start: String = "String"
        val Duplicate = system.actorOf(Props[Duplicate],"Duplicate")
        val FilterVowels = system.actorOf(Props(new FilterVowels(Duplicate)),"FilterVowels")
        val UpperCase = system.actorOf(Props(new UpperCase(FilterVowels)),"UpperCase")
        val LowerCase = system.actorOf(Props(new LowerCase(UpperCase)),"LowerCase")
        val DataSource = system.actorOf(Props(new DataSource(LowerCase, start)),"DataSource")
        DataSource ! Start(start)
    }
}*/

/*----------------------------------
leitura e controle com charAt |FAIL|
----------------------------------*/

/*import scala.io.Source

object Main{
    def main(args: Array[String]): Unit = {
        val filename = "t1"
        val fileContents = Source.fromFile(filename).getLines.mkString
        for(i <- 0 until fileContents.length()){
            if(fileContents.charAt(i) == '0' || fileContents.charAt(i) == '1'
            || fileContents.charAt(i) == '2' || fileContents.charAt(i) == '3'
            || fileContents.charAt(i) == '4' || fileContents.charAt(i) == '5'
            || fileContents.charAt(i) == '6' || fileContents.charAt(i) == '7'
            || fileContents.charAt(i) == '8' || fileContents.charAt(i) == '9'){
                print(fileContents.charAt(i))
            }
            else if(fileContents.charAt(i) == ' '){
                print("|")
            }
            else if(fileContents.charAt(i) == '\n'){
                print("\n")
            }
        }
    }
}*/

/*--------------------------
leitura com getLines 1 |AID|
--------------------------*/

/*import scala.io.Source

object Main{
    def main(args: Array[String]): Unit = {
        val filename = "t1"
        for (line <- Source.fromFile(filename).getLines) {
            println(line)
        }
    }
}*/

/*--------------------------
leitura com getLines 2 |AID|
--------------------------*/

/*import resource._

object Main{
    def main(args: Array[String]): Unit = {
        var string
        for (source <- managed(scala.io.Source.fromFile("t1"))) {
            for (line <- source.getLines) {
                //println(line)
            }
        }
    }
}*/

/*--------------------------
leitura com getLines 3 |AID|
--------------------------*/

/*import scala.util.{Try, Success, Failure}

object Control {
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

object Main{
    def main(args: Array [String]): Unit = {
        val filename = "t1"
        def readTextFile(filename: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(filename)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        println("--- FOREACH ---")
        val result = readTextFile(filename)
        result foreach {
            strings => strings.foreach(println)
        }
        
        println("\n--- MATCH ---")
        readTextFile(filename) match {
            case Some(lines) => lines.foreach(println)
            case None => println("Não foi possível ler o arquivo.")
        }
    }
}*/

/*--------------------------------------------
leitura e controle com getLines e charAt |AID|
--------------------------------------------*/

/*import scala.util.{Try, Success, Failure}
import scala.io.Source

object Control {
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

object Main{
    def main(args: Array [String]): Unit = {
        val filename = "t1"
        var i = 0
        var count = 0
        
        for (line <- Source.fromFile(filename).getLines)
            count += 1
        
        var as = new Array[String](count)
        
        def readTextFile(filename: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(filename)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        val result = readTextFile(filename)
        
        println("--- FOREACH ---")
        result foreach {
            strings => strings.foreach(println)
        }
        
        println("\n--- MATCH ---")
        readTextFile(filename) match {
            case Some(lines) => lines.foreach(println)
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- FOR ---")
        for (line <- Source.fromFile(filename).getLines){
            as(i) = line
            for(j <- 0 until as(i).length()){
                if(i != 0 && j == 0)
                    print("\n")
                if(as(i).charAt(j) == '0' || as(i).charAt(j) == '1'
                || as(i).charAt(j) == '2' || as(i).charAt(j) == '3'
                || as(i).charAt(j) == '4' || as(i).charAt(j) == '5'
                || as(i).charAt(j) == '6' || as(i).charAt(j) == '7'
                || as(i).charAt(j) == '8' || as(i).charAt(j) == '9'){
                    print(as(i).charAt(j))
                }
                else if(as(i).charAt(j) == ' ')
                    print("|")
            }
            i+=1
        }
        i=0
        print("\n")
        println("\n--- CASE AND FOR ---")
        readTextFile(filename) match {
            case Some(lines) => {
                for (line <- Source.fromFile(filename).getLines){
                    for(j <- 0 until as(i).length()){
                        if(i != 0 && j == 0)
                            print("\n")
                        if(as(i).charAt(j) == '0' || as(i).charAt(j) == '1'
                        || as(i).charAt(j) == '2' || as(i).charAt(j) == '3'
                        || as(i).charAt(j) == '4' || as(i).charAt(j) == '5'
                        || as(i).charAt(j) == '6' || as(i).charAt(j) == '7'
                        || as(i).charAt(j) == '8' || as(i).charAt(j) == '9'){
                            print(as(i).charAt(j))
                        }
                        else if(as(i).charAt(j) == ' ')
                            print("|")
                    }
                    i+=1
                }
                i=0
                print("\n")
            }
            case None => println("Não foi possível ler o arquivo.")
        }
    }
}*/

/*-------------------------------------------
leitura e controle com getLines e split |AID|
-------------------------------------------*/

//import scala.util.{Try, Success, Failure}
/*import scala.io.Source
import Array._

object Control {
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

object Main{
    def main(args: Array [String]): Unit = {
        val filename = "t1"
        var countl = 0
        var countc = 0
        var i = 0
        
        for(line <- Source.fromFile(filename).getLines){
            countl += 1
            if(countc == 0)
                countc = line.split(" ").length
        }
        println(s"Quantidade de linhas: $countl\nQuantidade de colunas: $countc\n")
        
        var ms = ofDim[String](countl,countc)
        
        for(line <- Source.fromFile(filename).getLines){
                ms(i) = line.split(" ")
                i+=1
        }
        i=0
        
        def readTextFile(filename: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(filename)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        val result = readTextFile(filename)
        
        println("--- FOREACH ---")
        result foreach {
            strings => strings.foreach(println)
        }
        
        println("\n--- MATCH ---")
        readTextFile(filename) match {
            case Some(lines) => lines.foreach(println)
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- FOR ---")
        for(i <- 0 until countl){
            for(j <- 0 until countc){
                print(ms(i)(j))
                if(j < countc-1)
                    print("|")
            }
            print("\n")
        }
        
        println("\n--- CASE AND FOR ---")
        readTextFile(filename) match {
            case Some(lines) => {
                for(i <- 0 until countl){
                    for(j <- 0 until countc){
                        print(ms(i)(j))
                        if(j < countc-1)
                        print("|")
                    }
                    print("\n")
                }
            }
            case None => println("Não foi possível ler o arquivo.")
        }
    }
}*/

/*--------------------------------
conversão de string para int |AID|
--------------------------------*/

/*import scala.io.Source
import Array._

object Control {
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

object Main{
    def main(args: Array [String]): Unit = {
        val filename = "t1"
        var countl = 0
        var countc = 0
        var i = 0
        
        for(line <- Source.fromFile(filename).getLines){
            countl += 1
            if(countc == 0)
                countc = line.split(" ").length
        }
        println(s"Quantidade de linhas: $countl\nQuantidade de colunas: $countc\n")
        
        var ms = ofDim[String](countl,countc)
        var mi = ofDim[Int](countl,countc)
        
        for(line <- Source.fromFile(filename).getLines){
                ms(i) = line.split(" ")
                i+=1
        }
        
        def readTextFile(filename: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(filename)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        val result = readTextFile(filename)
        
        println("--- FOREACH ---")
        result foreach {
            strings => strings.foreach(println)
        }
        
        println("\n--- MATCH ---")
        readTextFile(filename) match {
            case Some(lines) => lines.foreach(println)
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- FOR ---")
        for(i <- 0 until countl){
            for(j <- 0 until countc){
                mi(i)(j) = ms(i)(j).toInt
                print(mi(i)(j))
                if(j < countc-1)
                    print("|")
            }
            print("\n")
        }
        
        println("\n--- CASE AND FOR ---")
        readTextFile(filename) match {
            case Some(lines) => {
                for(i <- 0 until countl){
                    for(j <- 0 until countc){
                        print(mi(i)(j))
                        if(j < countc-1)
                            print("|")
                    }
                    print("\n")
                }
            }
            case None => println("Não foi possível ler o arquivo.")
        }
    }
}*/

/*-------------------------------------------
gravação, multiplos arquivos e inteiros |AID|
-------------------------------------------*/

/*import scala.io.Source
import Array._
import java.io._

object Control {
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

object Main{
    def main(args: Array [String]): Unit = {
        val m1 = "t1"
        val m2 = "t2"
        val res = "t3"
        val int = "t4_int"
        val wres = new PrintWriter(res)
        val wint = new PrintWriter(int)
        var countl = 0
        var countc = 0
        var npos = 0
        var nneg = 0
        var zero = 0
        var i = 0
        
        for(line <- Source.fromFile(m1).getLines){
            countl += 1
            if(countc == 0)
                countc = line.split(" ").length
        }
        println(s"Quantidade de linhas: $countl\nQuantidade de colunas: $countc\n")
        
        var m1s = ofDim[String](countl,countc)
        var m1i = ofDim[Int](countl,countc)
        var m2s = ofDim[String](countl,countc)
        var m2i = ofDim[Int](countl,countc)
        
        for(line <- Source.fromFile(m1).getLines){
                m1s(i) = line.split(" ")
                i+=1
        }
        i=0
        for(line <- Source.fromFile(m2).getLines){
                m2s(i) = line.split(" ")
                i+=1
        }
        
        def readTextFile(m1: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(m1)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        val result = readTextFile(m1)
        
        println("--- FOREACH ---")
        result foreach {
            strings => strings.foreach(println)
        }
        
        println("\n--- MATCH ---")
        readTextFile(m1) match {
            case Some(lines) => lines.foreach(println)
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- FOR ---")
        for(i <- 0 until countl){
            for(j <- 0 until countc){
                m1i(i)(j) = m1s(i)(j).toInt
                m2i(i)(j) = m2s(i)(j).toInt
                print(m1i(i)(j))
                wres.write(m1i(i)(j).toString)
                if(j < countc-1){
                    print("|")
                    wres.write("|")
                }
                if(m1i(i)(j) > 0)
                    npos+=1
                else if(m1i(i)(j) < 0)
                    nneg+=1
                else
                    zero+=1
            }
            print("\n")
            if(i != countl-1)
                wres.write("\n")
        }
        wres.close()
        wint.write(s"Quantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero")
        wint.close()
        
        println("\n--- CASE AND FOR ---")
        readTextFile(m2) match {
            case Some(lines) => {
                for(i <- 0 until countl){
                    for(j <- 0 until countc){
                        print(m2i(i)(j))
                        if(j < countc-1)
                            print("|")
                    }
                    print("\n")
                }
            }
            case None => println("Não foi possível ler o arquivo.")
        }
    }
}*/

/*-------------
operações |AID|
-------------*/

/*import scala.io.Source
import Array._
import java.io._

object Control{
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

object Main{
    def main(args: Array [String]): Unit = {
        val m1 = "t1"
        val m2 = "t2"
        val res = "t3"
        val int = "t4_int"
        val wres = new PrintWriter(res)
        val wint = new PrintWriter(int)
        var verif = false
        var countla = 0
        var countca = 0
        var countlb = 0
        var countcb = 0
        var npos = 0
        var nneg = 0
        var zero = 0
        var i = 0
        
        for(line <- Source.fromFile(m1).getLines){
            countla += 1
            if(countca == 0)
                countca = line.split(" ").length
        }
        println(s"Matriz A\n\nQuantidade de linhas: $countla\nQuantidade de colunas: $countca\n")
        
        for(line <- Source.fromFile(m2).getLines){
            countlb += 1
            if(countcb == 0)
                countcb = line.split(" ").length
        }
        println(s"Matriz B\n\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countcb\n")
        
        var m1s = ofDim[String](countla,countca)
        var m1i = ofDim[Int](countla,countca)
        var m2s = ofDim[String](countlb,countcb)
        var m2i = ofDim[Int](countlb,countcb)
        var tran = ofDim[Int](countcb,countlb)
        var soma = ofDim[Int](countla,countca)
        var mult = ofDim[Int](countlb,countca)
        
        for(line <- Source.fromFile(m1).getLines){
                m1s(i) = line.split(" ")
                i+=1
        }
        i=0
        for(line <- Source.fromFile(m2).getLines){
                m2s(i) = line.split(" ")
                i+=1
        }
        
        def readTextFile(m1: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(m1)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        val result = readTextFile(m1)
        
        println("--- FOREACH ---")
        result foreach {
            strings => strings.foreach(println)
        }
        
        println("\n--- MATCH ---")
        readTextFile(m1) match {
            case Some(lines) => lines.foreach(println)
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- FOR ---")
        for(i <- 0 until countla){
            for(j <- 0 until countca){
                m1i(i)(j) = m1s(i)(j).toInt
                print(m1i(i)(j))
                if(j < countca-1)
                    print("|")
            }
            print("\n")
        }
        
        println("\n--- CASE AND FOR ---")
        readTextFile(m2) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m2i(i)(j) = m2s(i)(j).toInt
                        print(m2i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
            }
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- TRANSPOSTA ---")
        readTextFile(m2) match {
            case Some(lines) => {
                for(i <- 0 until countcb){
                    for(j <- 0 until countlb){
                        tran(i)(j) = m2i(j)(i)
                        print(tran(i)(j))
                        if(j < countlb-1)
                            print("|")
                    }
                    print("\n")
                }
            }
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- SOMA ---")
        readTextFile(m2) match {
            case Some(lines) => {
                if(countla == countcb && countlb == countca){
                    verif = true
                    for(i <- 0 until countla){
                        for(j <- 0 until countca){
                            soma(i)(j) = m1i(i)(j) + tran(i)(j)
                            print(soma(i)(j))
                            if(j < countca-1)
                                print("|")
                        }
                        print("\n")
                    }
                }
                else
                    println(s"Soma inválida, pois a ordem da matriz A ($countla" + "x" + s"$countca)\n" +
                    s"é diferente da ordem da transposta de B ($countcb" + "x" + s"$countlb)")
            }
            case None => println("Não foi possível ler o arquivo.")
        }
        
        println("\n--- MULTIPLICAÇÃO ---")
        readTextFile(m2) match {
            case Some(lines) => {
                if(verif == true){
                    for(i <- 0 until countlb){
                        for(j <- 0 until countca){
                            for(k <- 0 until countcb){
                                mult(i)(j) += m2i(i)(k) * soma(k)(j)
                            }
                            print(mult(i)(j))
                            wres.write(mult(i)(j).toString)
                            if(j < countcb-1){
                                print("|")
                                wres.write("|")
                            }
                            if(mult(i)(j) > 0)
                                npos+=1
                            else if(mult(i)(j) < 0)
                                nneg+=1
                            else
                                zero+=1
                        }
                        print("\n")
                        if(i != countlb-1)
                            wres.write("\n")
                    }
                    wres.close()
                    wint.write(s"Quantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero")
                    wint.close()
                }
                else
                    println(s"Multiplicação inválida, pois a soma da matriz A com a transposta de B não ocorreu.")
            }
            case None => println("Não foi possível ler o arquivo.")
        }
    }
}*/

/*---------------
organização |AID|
---------------*/

/*import scala.io.Source
import Array._
import java.io._

object Control{
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

class Matriz(val m1: String, val m2: String, val res: String, val int: String){
    val wres = new PrintWriter(res)
    val wint = new PrintWriter(int)
    var verif = false
    var countla = 0
    var countca = 0
    var countlb = 0
    var countcb = 0
    var npos = 0
    var nneg = 0
    var zero = 0
    var i = 0
    
    for(line <- Source.fromFile(m1).getLines){
        countla += 1
        if(countca == 0)
            countca = line.split(" ").length
    }
    
    for(line <- Source.fromFile(m2).getLines){
        countlb += 1
        if(countcb == 0)
            countcb = line.split(" ").length
    }
    
    var m1s = ofDim[String](countla,countca)
    var m1i = ofDim[Int](countla,countca)
    var m2s = ofDim[String](countlb,countcb)
    var m2i = ofDim[Int](countlb,countcb)
    var tran = ofDim[Int](countcb,countlb)
    var soma = ofDim[Int](countla,countca)
    var mult = ofDim[Int](countlb,countca)
    
    for(line <- Source.fromFile(m1).getLines){
            m1s(i) = line.split(" ")
            i+=1
    }
    i=0
    for(line <- Source.fromFile(m2).getLines){
            m2s(i) = line.split(" ")
            i+=1
    }
    
    def readTextFile(arq: String): Option[List[String]] = {
        try{
            val lines = Control.using(io.Source.fromFile(arq)){
                source => (for (line <- source.getLines) yield line).toList
            }
            Some(lines)
        }catch{
            case e: Exception => None
        }
    }
    
    def mA{
        println("\nMatriz A\n")
        readTextFile(m1) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m1i(i)(j) = m1s(i)(j).toInt
                        print(m1i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countla\nQuantidade de colunas: $countca")
            }
            case None => println("Não foi possível ler o arquivo da Matriz A.")
        }
    }
    
    def mB{
        println("\nMatriz B\n")
        readTextFile(m2) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m2i(i)(j) = m2s(i)(j).toInt
                        print(m2i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countcb")
            }
            case None => println("Não foi possível ler o arquivo da Matriz B.")
        }
    }
    
    def mC{
        if(verif == true)
            verif = false
            
        println("\nMatriz C\n")
        for(i <- 0 until countcb)
            for(j <- 0 until countlb)
                tran(i)(j) = m2i(j)(i)
                
        if(countla == countcb && countlb == countca){
            verif = true
            for(i <- 0 until countla)
                for(j <- 0 until countca)
                    soma(i)(j) = m1i(i)(j) + tran(i)(j)
        }
        else
            println(s"Soma inválida, pois a ordem da matriz A ($countla" + "x" + s"$countca)\n" +
            s"é diferente da ordem da transposta de B ($countcb" + "x" + s"$countlb)")
            
        if(verif == true){
            for(i <- 0 until countlb){
                for(j <- 0 until countca){
                    for(k <- 0 until countcb)
                        mult(i)(j) += m2i(i)(k) * soma(k)(j)
                    print(mult(i)(j))
                    wres.write(mult(i)(j).toString)
                    if(j < countcb-1){
                        print("|")
                        wres.write("|")
                    }
                    if(mult(i)(j) > 0)
                        npos+=1
                    else if(mult(i)(j) < 0)
                        nneg+=1
                    else
                        zero+=1
                }
                print("\n")
                if(i != countlb-1)
                    wres.write("\n")
            }
            println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countca")
            wres.close()
            println(s"\nQuantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero\n")
            wint.write(s"Quantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero")
            wint.close()
        }
        else
            println(s"Multiplicação inválida, pois a soma da matriz A com a transposta de B não ocorreu.")
    }
}

object Main{
    def main(args: Array [String]): Unit = {
        val m1 = "t1"
        val m2 = "t2"
        val res = "t3"
        val int = "t4_int"
        val m = new Matriz(m1, m2, res, int)
        m.mA
        m.mB
        m.mC
    }
}*/

/*-------------------------
implementação do akka |AID|
-------------------------*/

/*import scala.io.Source
import Array._
import java.io._
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._
sealed trait Message
case object Calculate extends Message
case class Work(m1: String, m2: String, res: String, int: String) extends Message
case class Result(matriz: Unit) extends Message
case class Show(duration: Duration) extends Message

object Control{
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

class Worker extends Actor{
    
    def Matriz(m1: String, m2: String, res: String, int: String){
        val wres = new PrintWriter(res)
        val wint = new PrintWriter(int)
        var verif = false
        var countla = 0
        var countca = 0
        var countlb = 0
        var countcb = 0
        var npos = 0
        var nneg = 0
        var zero = 0
        var i = 0
        
        for(line <- Source.fromFile(m1).getLines){
            countla += 1
            if(countca == 0)
                countca = line.split(" ").length
        }
        
        for(line <- Source.fromFile(m2).getLines){
            countlb += 1
            if(countcb == 0)
                countcb = line.split(" ").length
        }
        
        var m1s = ofDim[String](countla,countca)
        var m1i = ofDim[Int](countla,countca)
        var m2s = ofDim[String](countlb,countcb)
        var m2i = ofDim[Int](countlb,countcb)
        var tran = ofDim[Int](countcb,countlb)
        var soma = ofDim[Int](countla,countca)
        var mult = ofDim[Int](countlb,countca)
        
        for(line <- Source.fromFile(m1).getLines){
                m1s(i) = line.split(" ")
                i+=1
        }
        i=0
        for(line <- Source.fromFile(m2).getLines){
                m2s(i) = line.split(" ")
                i+=1
        }
        
        def readTextFile(arq: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(arq)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        println("\nMatriz A\n")
        readTextFile(m1) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m1i(i)(j) = m1s(i)(j).toInt
                        print(m1i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countla\nQuantidade de colunas: $countca")
            }
            case None => println("Não foi possível ler o arquivo da Matriz A.")
        }
        
        println("\nMatriz B\n")
        readTextFile(m2) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m2i(i)(j) = m2s(i)(j).toInt
                        print(m2i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countcb")
            }
            case None => println("Não foi possível ler o arquivo da Matriz B.")
        }
        
        if(verif == true)
            verif = false
            
        println("\nMatriz C\n")
        for(i <- 0 until countcb)
            for(j <- 0 until countlb)
                tran(i)(j) = m2i(j)(i)
                
        if(countla == countcb && countlb == countca){
            verif = true
            for(i <- 0 until countla)
                for(j <- 0 until countca)
                    soma(i)(j) = m1i(i)(j) + tran(i)(j)
        }
        else
            println(s"Soma inválida, pois a ordem da matriz A ($countla" + "x" + s"$countca)\n" +
            s"é diferente da ordem da transposta de B ($countcb" + "x" + s"$countlb)")
            
        if(verif == true){
            for(i <- 0 until countlb){
                for(j <- 0 until countca){
                    for(k <- 0 until countcb)
                        mult(i)(j) += m2i(i)(k) * soma(k)(j)
                    print(mult(i)(j))
                    wres.write(mult(i)(j).toString)
                    if(j < countcb-1){
                        print("|")
                        wres.write("|")
                    }
                    if(mult(i)(j) > 0)
                        npos+=1
                    else if(mult(i)(j) < 0)
                        nneg+=1
                    else
                        zero+=1
                }
                print("\n")
                if(i != countlb-1)
                    wres.write("\n")
            }
            println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countca")
            wres.close()
            println(s"\nQuantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero\n")
            wint.write(s"Quantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero")
            wint.close()
        }
        else
            println(s"Multiplicação inválida, pois a soma da matriz A com a transposta de B não ocorreu.")
    }
    
    override def receive: Receive = {
        case Work(m1, m2, res, int) => sender ! Result(Matriz(m1, m2, res, int))
    }
}
class Master(workers: Int, m1: String, m2: String, res: String, int: String, listener: ActorRef) extends Actor{
    val start: Long = System.currentTimeMillis
 
    val worker = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(workers)), "worker")
      
    override def receive: Receive = {
        case Calculate => {
                worker ! Work(m1, m2, res, int)
        }
        
        case Result(res) => {
            listener ! Show((System.currentTimeMillis - start).millis)
            context.stop(self)
        }
    }
}

class Listener extends Actor{
    def receive: Receive = {
      case Show(duration) ⇒
        println("Tempo de execução: %s\n".format(duration))
        context.system.shutdown()
    }
}

object Main{
    def main(args: Array [String]): Unit = {
        val m1 = "t1"
        val m2 = "t2"
        val res = "t3"
        val int = "t4_int"
        val system = ActorSystem("MainSystem")
        val workers = 4
        val listener = system.actorOf(Props[Listener], "listener")
        val masterActor = system.actorOf(Props(new Master(workers, m1, m2, res, int, listener)),"masterActor")
        
        masterActor ! Calculate
    }
}*/

/*-------------------------------------
leitura dos arquivos oficiais |SUCCESS|
-------------------------------------*/

/*import scala.io.Source
import Array._
import java.io._
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._
sealed trait Message
case object Calculate extends Message
case class Work(m1: String, m2: String, res: String, int: String) extends Message
case class Result(matriz: Unit) extends Message
case class Show(duration: Duration) extends Message

object Control{
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

class Worker extends Actor{
    
    def Matriz(m1: String, m2: String, res: String, int: String){
        val wres = new PrintWriter(res)
        val wint = new PrintWriter(int)
        var verif = false
        var countla = 0
        var countca = 0
        var countlb = 0
        var countcb = 0
        var npos = 0
        var nneg = 0
        var zero = 0
        var i = 0
        
        for(line <- Source.fromFile(m1).getLines){
            countla += 1
            if(countca == 0)
                countca = line.split(" ").length
        }
        
        for(line <- Source.fromFile(m2).getLines){
            countlb += 1
            if(countcb == 0)
                countcb = line.split(" ").length
        }
        
        var m1s = ofDim[String](countla,countca)
        var m1i = ofDim[Int](countla,countca)
        var m2s = ofDim[String](countlb,countcb)
        var m2i = ofDim[Int](countlb,countcb)
        var tran = ofDim[Int](countcb,countlb)
        var soma = ofDim[Int](countla,countca)
        var mult = ofDim[Int](countlb,countca)
        
        for(line <- Source.fromFile(m1).getLines){
                m1s(i) = line.split(" ")
                i+=1
        }
        i=0
        for(line <- Source.fromFile(m2).getLines){
                m2s(i) = line.split(" ")
                i+=1
        }
        
        def readTextFile(arq: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(arq)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        println("\nMatriz A\n")
        readTextFile(m1) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m1i(i)(j) = m1s(i)(j).toInt
                        print(m1i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countla\nQuantidade de colunas: $countca")
            }
            case None => println("Não foi possível ler o arquivo da Matriz A.")
        }
        
        println("\nMatriz B\n")
        readTextFile(m2) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m2i(i)(j) = m2s(i)(j).toInt
                        print(m2i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countcb")
            }
            case None => println("Não foi possível ler o arquivo da Matriz B.")
        }
        
        if(verif == true)
            verif = false
            
        println("\nMatriz C\n")
        for(i <- 0 until countcb)
            for(j <- 0 until countlb)
                tran(i)(j) = m2i(j)(i)
                
        if(countla == countcb && countlb == countca){
            verif = true
            for(i <- 0 until countla)
                for(j <- 0 until countca)
                    soma(i)(j) = m1i(i)(j) + tran(i)(j)
        }
        else
            println(s"Soma inválida, pois a ordem da matriz A ($countla" + "x" + s"$countca)\n" +
            s"é diferente da ordem da transposta de B ($countcb" + "x" + s"$countlb)")
            
        if(verif == true){
            for(i <- 0 until countlb){
                for(j <- 0 until countca){
                    for(k <- 0 until countcb)
                        mult(i)(j) += m2i(i)(k) * soma(k)(j)
                    print(mult(i)(j))
                    wres.write(mult(i)(j).toString)
                    if(j < countcb-1){
                        print("|")
                        wres.write("|")
                    }
                    if(mult(i)(j) > 0)
                        npos+=1
                    else if(mult(i)(j) < 0)
                        nneg+=1
                    else
                        zero+=1
                }
                print("\n")
                if(i != countlb-1)
                    wres.write("\n")
            }
            println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countca")
            wres.close()
            println(s"\nQuantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero\n")
            wint.write(s"Quantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero")
            wint.close()
        }
        else
            println(s"Multiplicação inválida, pois a soma da matriz A com a transposta de B não ocorreu.")
    }
    
    override def receive: Receive = {
        case Work(m1, m2, res, int) => sender ! Result(Matriz(m1, m2, res, int))
    }
}
class Master(workers: Int, m1: String, m2: String, res: String, int: String, listener: ActorRef) extends Actor{
    val start: Long = System.currentTimeMillis
 
    val worker = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(workers)), "worker")
      
    override def receive: Receive = {
        case Calculate => {
                worker ! Work(m1, m2, res, int)
        }
        
        case Result(res) => {
            listener ! Show((System.currentTimeMillis - start).millis)
            context.stop(self)
        }
    }
}

class Listener extends Actor{
    def receive: Receive = {
      case Show(duration) ⇒
        println("Tempo de execução: %s\n".format(duration))
        context.system.shutdown()
    }
}

object Main{
    def main(args: Array [String]): Unit = {
        val m1 = "ppar"
        val m2 = "ppar2"
        val res = "matriz_c"
        val int = "inteiro"
        val system = ActorSystem("MainSystem")
        val workers = 12
        val listener = system.actorOf(Props[Listener], "listener")
        val masterActor = system.actorOf(Props(new Master(workers, m1, m2, res, int, listener)),"masterActor")
        
        masterActor ! Calculate
    }
}*/

/*-------------------------------
teste de número ideal de divisões
-------------------------------*/

import scala.io.Source
import Array._
import java.io._
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._
sealed trait Message
case object Calculate extends Message
case class Work(m1: String, m2: String, res: String, int: String) extends Message
case class Result(matriz: Unit) extends Message
case class Show(duration: Duration) extends Message

object Control{
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
        try{
            f(resource)
        }finally{
            resource.close()
        }
}

class Worker extends Actor{
    
    def Matriz(m1: String, m2: String, res: String, int: String){
        val wres = new PrintWriter(res)
        val wint = new PrintWriter(int)
        var verif = false
        var countla = 0
        var countca = 0
        var countlb = 0
        var countcb = 0
        var npos = 0
        var nneg = 0
        var zero = 0
        var i = 0
        
        for(line <- Source.fromFile(m1).getLines){
            countla += 1
            if(countca == 0)
                countca = line.split(" ").length
        }
        
        for(line <- Source.fromFile(m2).getLines){
            countlb += 1
            if(countcb == 0)
                countcb = line.split(" ").length
        }
        
        var m1s = ofDim[String](countla,countca)
        var m1i = ofDim[Int](countla,countca)
        var m2s = ofDim[String](countlb,countcb)
        var m2i = ofDim[Int](countlb,countcb)
        var tran = ofDim[Int](countcb,countlb)
        var soma = ofDim[Int](countla,countca)
        var mult = ofDim[Int](countlb,countca)
        
        for(line <- Source.fromFile(m1).getLines){
                m1s(i) = line.split(" ")
                i+=1
        }
        i=0
        for(line <- Source.fromFile(m2).getLines){
                m2s(i) = line.split(" ")
                i+=1
        }
        
        def readTextFile(arq: String): Option[List[String]] = {
            try{
                val lines = Control.using(io.Source.fromFile(arq)){
                    source => (for (line <- source.getLines) yield line).toList
                }
                Some(lines)
            }catch{
                case e: Exception => None
            }
        }
        
        println("\nMatriz A\n")
        readTextFile(m1) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m1i(i)(j) = m1s(i)(j).toInt
                        print(m1i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countla\nQuantidade de colunas: $countca")
            }
            case None => println("Não foi possível ler o arquivo da Matriz A.")
        }
        
        println("\nMatriz B\n")
        readTextFile(m2) match {
            case Some(lines) => {
                for(i <- 0 until countlb){
                    for(j <- 0 until countcb){
                        m2i(i)(j) = m2s(i)(j).toInt
                        print(m2i(i)(j))
                        if(j < countcb-1)
                            print("|")
                    }
                    print("\n")
                }
                println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countcb")
            }
            case None => println("Não foi possível ler o arquivo da Matriz B.")
        }
        
        if(verif == true)
            verif = false
            
        println("\nMatriz C\n")
        for(i <- 0 until countcb)
            for(j <- 0 until countlb)
                tran(i)(j) = m2i(j)(i)
                
        if(countla == countcb && countlb == countca){
            verif = true
            for(i <- 0 until countla)
                for(j <- 0 until countca)
                    soma(i)(j) = m1i(i)(j) + tran(i)(j)
        }
        else
            println(s"Soma inválida, pois a ordem da matriz A ($countla" + "x" + s"$countca)\n" +
            s"é diferente da ordem da transposta de B ($countcb" + "x" + s"$countlb)")
            
        if(verif == true){
            for(i <- 0 until countlb){
                for(j <- 0 until countca){
                    for(k <- 0 until countcb)
                        mult(i)(j) += m2i(i)(k) * soma(k)(j)
                    print(mult(i)(j))
                    wres.write(mult(i)(j).toString)
                    if(j < countcb-1){
                        print("|")
                        wres.write("|")
                    }
                    if(mult(i)(j) > 0)
                        npos+=1
                    else if(mult(i)(j) < 0)
                        nneg+=1
                    else
                        zero+=1
                }
                print("\n")
                if(i != countlb-1)
                    wres.write("\n")
            }
            println(s"\nQuantidade de linhas: $countlb\nQuantidade de colunas: $countca")
            wres.close()
            println(s"\nQuantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero\n")
            wint.write(s"Quantidade de inteiros negativos: $nneg\nQuantidade de inteiros positivos: $npos\nQuantidade de zeros: $zero")
            wint.close()
        }
        else
            println(s"Multiplicação inválida, pois a soma da matriz A com a transposta de B não ocorreu.")
    }
    
    override def receive: Receive = {
        case Work(m1, m2, res, int) => sender ! Result(Matriz(m1, m2, res, int))
    }
}
class Master(workers: Int, m1: String, m2: String, res: String, int: String, listener: ActorRef) extends Actor{
    val start: Long = System.currentTimeMillis
 
    val worker = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(workers)), "worker")
      
    override def receive: Receive = {
        case Calculate => {
                worker ! Work(m1, m2, res, int)
        }
        
        case Result(res) => {
            listener ! Show((System.currentTimeMillis - start).millis)
            context.stop(self)
        }
    }
}

class Listener extends Actor{
    def receive: Receive = {
      case Show(duration) ⇒
        println("Tempo de execução: %s\n".format(duration))
        context.system.shutdown()
    }
}

object Main{
    def main(args: Array [String]): Unit = {
        val m1 = "ppar"
        val m2 = "ppar2"
        val res = "matriz_c"
        val int = "inteiro"
        val system = ActorSystem("MainSystem")
        val workers = 12
        val listener = system.actorOf(Props[Listener], "listener")
        val masterActor = system.actorOf(Props(new Master(workers, m1, m2, res, int, listener)),"masterActor")
        
        masterActor ! Calculate
    }
}