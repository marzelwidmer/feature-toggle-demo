package ch.keepcalm.demo.featuretoggle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.hateoas.*
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.server.mvc.add
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Component


fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
@EnableHypermediaSupport(type = arrayOf(EnableHypermediaSupport.HypermediaType.HAL))
class Application() {}


//   _   _    _  _____ _____ ___    _    ____
//  | | | |  / \|_   _| ____/ _ \  / \  / ___|
//  | |_| | / _ \ | | |  _|| | | |/ _ \ \___ \
//  |  _  |/ ___ \| | | |___ |_| / ___ \ ___) |
//  |_| |_/_/   \_\_| |_____\___/_/   \_\____/
//
open class Index : RepresentationModel<Index>()

@RestController
@RequestMapping("/api", produces = [MediaTypes.HAL_JSON_UTF8_VALUE])
@RefreshScope
class IndexController {

    @Value("\${app.feature}")
    lateinit var feature: String

    @GetMapping
    fun api(): Index = Index()
            .apply {
                add(IndexController::class) {
                    linkTo { api() } withRel IanaLinkRelations.SELF
                    when (feature) {
                        "feature1" -> add(Link("http://api.icndb.com/jokes/random", "chuck-norris"))
                        "feature2" -> add(Link("http:/google.com", "google"))
                        "feature3" -> add(Link("https://api.opendota.com/api/heroStats", "heros"))
                    }
                }
            }
}
