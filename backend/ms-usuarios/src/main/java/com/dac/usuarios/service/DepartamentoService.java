package com.dac.usuarios.service;

import com.dac.usuarios.model.Departamento;
import com.dac.usuarios.repository.DepartamentoRepository;
import com.dac.usuarios.dto.DepartamentoDTO;
import com.dac.usuarios.exception.ResourceNotFoundException;
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

    public Departamento atualizarDepartamento(Long id, DepartamentoDTO dto) {
        Departamento depto = departamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado: " + id));

        depto.setCodigo(dto.getCodigo());
        depto.setNome(dto.getNome());
        depto.setDescricao(dto.getDescricao());
        return departamentoRepository.save(depto);
    }

    public void deletarDepartamento(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Departamento não encontrado: " + id);
        }
        departamentoRepository.deleteById(id);
    }
}