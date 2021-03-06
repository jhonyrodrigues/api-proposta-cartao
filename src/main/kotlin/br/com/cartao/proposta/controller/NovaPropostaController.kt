package br.com.cartao.proposta.controller

import br.com.cartao.proposta.model.AcaoAposGerarProposta
import br.com.cartao.proposta.repository.PropostaRepository
import br.com.cartao.proposta.request.NovaPropostaRequest
import br.com.cartao.proposta.response.DadosDaPropostaResponse
import br.com.cartao.proposta.shared.ExecutorDeAcoes
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/proposta")
class NovaPropostaController(private val propostaRepository: PropostaRepository,
                             private val acoes: List<AcaoAposGerarProposta>,
                             private val executorDeAcoes: ExecutorDeAcoes
) {

    private val LOGGER = LoggerFactory.getLogger(NovaPropostaController::class.java)

    @PostMapping
    fun cadastra(@RequestBody @Valid request: NovaPropostaRequest): ResponseEntity<Any> {

        val proposta = request.toModel(propostaRepository)
                ?: return ResponseEntity.unprocessableEntity()
                        .body("Proposta para o documento: ${request.documento} já cadastrada!")

        executorDeAcoes.executa {
            acoes.forEach { acao -> acao.executar(proposta) }
        }

        propostaRepository.save(proposta)

        val uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/${proposta.propostaId}")
                .buildAndExpand().toUri()

        LOGGER.info("Gerando nova proposta: propostaId: ${proposta.propostaId}")

        return ResponseEntity.created(uri).build()
    }

    @GetMapping("/{propostaId}")
    fun consulta(@PathVariable propostaId: String): ResponseEntity<DadosDaPropostaResponse> {

        val proposta = propostaRepository.findById(propostaId)

        return when {
            proposta.isEmpty -> {
                ResponseEntity.notFound().build()
            }
            else -> {
                LOGGER.info("Consultando os dados da proposta: $propostaId")
                ResponseEntity.ok().body(DadosDaPropostaResponse(proposta.get()))
            }
        }
    }
}