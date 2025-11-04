package io.github.jangalinski.kata.onearmedbandit

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<OneArmedBanditApplication>(*args).let {  }

@SpringBootApplication
open class OneArmedBanditApplication {
    @Bean
    open fun customOpenAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("One Armed Bandit")
                .version("1.0.0")
                .description("REST API for the one armed bandit kata")
        )
}
