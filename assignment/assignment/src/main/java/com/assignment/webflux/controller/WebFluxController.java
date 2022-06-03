package com.assignment.webflux.controller;

import com.assignment.webflux.entity.Message;
import com.assignment.webflux.model.Data;
import com.assignment.webflux.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class WebFluxController {

    @Autowired
    private MessageRepository repository;
    @PostMapping("/addMessage")
    public Mono<Message> addMessage(@RequestBody Data  data){
        Mono<Data> dataMono = WebClient.create("http://localhost:8080/add")
                .post()
                .body(Mono.just(data), Data.class)
                .retrieve()
                .bodyToMono(Data.class);
        Mono<Message> messageMono = dataMono.map(d -> mapToMessage(d)).doOnNext(message -> repository.save(message));
    return  messageMono;
    }
    private Message mapToMessage(Data data){
        Message message = new Message();
        message.setId(data.getId());
        message.setMessage(data.getMessage());
        return message;
    }
}
