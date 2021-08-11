package br.com.cartao.proposta.component.health

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import java.net.Socket
import java.net.URL

@Component
class CartaoClientHealth : HealthIndicator {

    @Value("\${analise.cartao}")
    private lateinit var url: String

    override fun health(): Health {
        try {
            Socket(URL(url).host, 8888)
        } catch (e: Exception) {
            return Health.down().withDetail("Erro", e.message).build()
        }
        return Health.up().build()
    }
}