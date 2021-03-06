package br.com.cartao.proposta.repository

import br.com.cartao.proposta.model.Proposta
import br.com.cartao.proposta.model.StatusProposta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PropostaRepository : JpaRepository<Proposta, String> {
    fun findByDocumento(documento: String): Optional<Proposta>
    fun findByStatusPropostaEqualsAndCartaoIdNull(elegivel: StatusProposta): List<Proposta>
}