package ch.keepcalm.demo.featuretoggle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.hateoas.*
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.server.mvc.add
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component


fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
@EnableHypermediaSupport(type = arrayOf(EnableHypermediaSupport.HypermediaType.HAL))
class Application() {
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "feature.toggle", name = ["holidaySeason"], havingValue = "false")
    fun getStandardGreetingHandler(): GreetingHandler {
        return StandardGreetingHandler()
    }

    @Bean
    @ConditionalOnProperty(prefix = "feature.toggle", name = ["holidaySeason"], havingValue = "true")
    fun getGreetingHandler(): GreetingHandler {
        return HolidayGreetingHandler()
    }
}

@Component
@ConditionalOnExpression("\${feature.toggle:false}")
data class Feature1(val name: String = "feature1")



interface GreetingHandler {
    fun getGreeting(name: String): String
}

class StandardGreetingHandler : GreetingHandler {
    override fun getGreeting(name: String): String {
        return String.format(template, name)
    }

    companion object {
        private val template = "Hello, %s!"
    }
}

@RestController
@RequestMapping("/api", produces = [MediaTypes.HAL_JSON_UTF8_VALUE])
class GreetingController (private val handler: GreetingHandler) {

    @RequestMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): String {
        return handler.getGreeting(name)
    }
}

class HolidayGreetingHandler : GreetingHandler {

    override fun getGreeting(name: String): String {
        return String.format(template, name)
    }

    companion object {
        private val template = "Happy Holidays, %s!"
    }
}


//   _   _    _  _____ _____ ___    _    ____
//  | | | |  / \|_   _| ____/ _ \  / \  / ___|
//  | |_| | / _ \ | | |  _|| | | |/ _ \ \___ \
//  |  _  |/ ___ \| | | |___ |_| / ___ \ ___) |
//  |_| |_/_/   \_\_| |_____\___/_/   \_\____/
//
open class Index : RepresentationModel<Index>()

@RestController
@RequestMapping("/api", produces = [MediaTypes.HAL_JSON_UTF8_VALUE])
class IndexController {

    @Value("\${app.feature}")
    lateinit var feature: String

    @GetMapping
    fun api(): Index = Index()
            .apply {
                add(IndexController::class) {
                    linkTo { api() } withRel IanaLinkRelations.SELF
                    linkTo { api() }.slash("greeting") withRel "greeting"
                }
                add(CustomerController::class) {
                    linkTo { getCustomer() } withRel "customer"
                }
                when (feature){
                    "feature1" -> add(Link("http://api.icndb.com/jokes/random" , "chuck-norris"))
                    "feature2" -> add(Link("http:/google.com" , "google"))
                    "feature3" -> add(Link("https://api.opendota.com/api/heroStats" , "heros"))
                }
            }
}

@Service
class CustomerService {
    fun getCustomer(): CustomerModel = CustomerModel(firstname = "John", lastname = "Doe")
}

open class CustomerModel(
        val firstname: String,
        val lastname: String
) : RepresentationModel<CustomerModel>()


@RequestMapping("/api")
@RestController
class CustomerController(private val customerService: CustomerService) {

    @GetMapping(value = ["/customer"], produces = [MediaTypes.HAL_JSON_UTF8_VALUE])
    fun getCustomer(): CustomerModel {
        return customerService.getCustomer()
    }


}
