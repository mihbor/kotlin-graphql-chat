import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["graphql"])
open class App

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
