import com.dac.gamificacao.model.Pontuacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PontuacaoRepository extends MongoRepository<Pontuacao, String> {

    // Busca um documento de pontuação pelo ID do funcionário
    Optional<Pontuacao> findByFuncionarioId(Long funcionarioId);
}

