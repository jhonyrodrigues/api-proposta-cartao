package br.com.cartao.proposta.model

import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Entity
class Proposta(
    @field:NotBlank val documento: String,
    @field:NotBlank @field:Email val email: String,
    @field:NotBlank val nome: String,
    @field:NotBlank val endereco: String,
    @field:NotNull @field:Positive val salario: BigDecimal
) {
    @Id
    val propostaId: String = UUID.randomUUID().toString()
}
