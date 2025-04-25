package com.agilesolutions.mcp.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final ChatClient chatClient;

    @GetMapping("/count-by-person-id/{personId}")
    String countByPersonId(@PathVariable String personId) {
        PromptTemplate pt = new PromptTemplate("""
                How many accounts has person with {personId} ID ?
                """);
        Prompt p = pt.create(Map.of("personId", personId));
        return this.chatClient.prompt(p)
                .call()
                .content();
    }

    @GetMapping("/balance-by-person-id/{personId}")
    String balanceByPersonId(@PathVariable String personId) {
        PromptTemplate pt = new PromptTemplate("""
                How many accounts has person with {personId} ID ?
                Send a notification message with person name, nationality and a total balance on his/her accounts. 
                """);
        Prompt p = pt.create(Map.of("personId", personId));
        return this.chatClient.prompt(p)
                .call()
                .content();
    }

}