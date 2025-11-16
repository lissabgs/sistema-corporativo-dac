package com.dac.usuarios.service;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.repository.DepartamentoRepository;
import com.dac.usuarios.dto.DepartamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public Departamento criarDepartamento(DepartamentoDTO dto) {
        Departamento depto = new Departamento();
        depto.setCodigo(dto.getCodigo());
        depto.setNome(dto.getNome());
        depto.setDescricao(dto.getDescricao());
        depto.setGestorId(dto.getGestorId());
        depto.setStatus(true);

        return departamentoRepository.save(depto);
    }
}