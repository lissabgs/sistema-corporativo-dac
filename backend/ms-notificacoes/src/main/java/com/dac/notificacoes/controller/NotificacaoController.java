package com.dac.notificacoes.controller;

import com.dac.notificacoes.model.Notificacao;
import com.dac.notificacoes.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoRepository repository;

    // Endpoint para o "Sininho": Busca todas as notificações de um usuário
    // GET http://localhost:8080/api/notificacoes/usuario/{id}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacao>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<Notificacao> lista = repository.findByUsuarioId(usuarioId);
        
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    // Endpoint extra: Marcar uma notificação como lida
    // PUT http://localhost:8080/api/notificacoes/{id}/lida
    @PutMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable String id) {
        return repository.findById(id).map(notificacao -> {
            notificacao.setLida(true);
            repository.save(notificacao);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}