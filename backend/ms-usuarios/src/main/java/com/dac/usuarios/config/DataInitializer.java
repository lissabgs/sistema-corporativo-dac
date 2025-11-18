package com.dac.usuarios.config;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existem departamentos para não duplicar
        if (departamentoRepository.count() == 0) {
            System.out.println(">>> [MS-USUARIOS] Inicializando 5 Departamentos padrão...");

            List<Departamento> departamentos = Arrays.asList(
                    criarDepto("TI", "Tecnologia da Informação", "Setor de desenvolvimento e suporte"),
                    criarDepto("RH", "Recursos Humanos", "Gestão de pessoas e talentos"),
                    criarDepto("FIN", "Financeiro", "Contabilidade e pagamentos"),
                    criarDepto("MKT", "Marketing", "Publicidade e propaganda"),
                    criarDepto("COM", "Comercial", "Vendas e relacionamento com clientes")
            );

            departamentoRepository.saveAll(departamentos);
            System.out.println(">>> [MS-USUARIOS] Departamentos criados com sucesso!");
        }
    }

    private Departamento criarDepto(String codigo, String nome, String descricao) {
        Departamento d = new Departamento();
        d.setCodigo(codigo);
        d.setNome(nome);
        d.setDescricao(descricao);
        d.setStatus(true);
        return d;
    }
}