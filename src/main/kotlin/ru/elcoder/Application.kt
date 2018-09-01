package ru.elcoder

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import ru.elcoder.bus.EventBus
import ru.elcoder.workers.ArrayReader
import ru.elcoder.workers.StringReader
import ru.elcoder.workers.StringReverter

private val log = LogFactory.getLog(Application::class.java)

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan
open class Application : CommandLineRunner {

    @Autowired
    private lateinit var context: ApplicationContext
    @Autowired
    private lateinit var eventBus: EventBus

    override fun run(vararg args: String?) {
        log.info("Start")
        // manual worker registration
        log.debug("creating two instances of StringReverter worker")
        var reverter = context.getBean(StringReverter::class.java)
        eventBus.subscribe(reverter)
        reverter = context.getBean(StringReverter::class.java)
        eventBus.subscribe(reverter)
        // other workers already registered by context because of they are components-singletons

        // main loop
        log.debug("running StringReader and ArrayReader workers")
        val stringReader = context.getBean(StringReader::class.java)
        val arrayReader = context.getBean(ArrayReader::class.java)
        stringReader.run()
        arrayReader.run()
        log.info("Successful finish")
    }

}

fun main(args: Array<String>) {
    val app = SpringApplicationBuilder()
            .sources(Application::class.java)
            .main(Application::class.java)
            .web(false)
            .build()
    app.run(*args)
}
