package com.vabp.proposalapp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.pendingproposal.exchange}")
    private String exchangePendingProposal;

    @Value("${rabbitmq.completedproposal.exchange}")
    private String exchangeCompletedProposal;

    @Bean
    public Queue createQueuePendingProposalMsCreditAnalysis() {
        return QueueBuilder.durable("proposta-pendente.ms-analise-credito").build();
    }

    @Bean
    public Queue createQueuePendingProposalMsNotification() {
        return QueueBuilder.durable("proposta-pendente.ms-notificacao").build();
    }

    @Bean
    public Queue createQueueCompletedProposalMsProposal() {
        return QueueBuilder.durable("proposta-concluida.ms-proposta").build();
    }

    @Bean
    public Queue createQueueCompletedProposalMsNotification() {
        return QueueBuilder.durable("proposta-concluida.ms-notificacao").build();
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> AdminInitializer(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange fanoutExchangePendingProposal() {
        return ExchangeBuilder.fanoutExchange(exchangePendingProposal).build();
    }

    @Bean
    public FanoutExchange fanoutExchangeCompletedProposal() {
        return ExchangeBuilder.fanoutExchange(exchangeCompletedProposal).build();
    }

    @Bean
    public Binding createBindingPendingProposalMsCreditAnalysis() {
        return BindingBuilder.bind(createQueuePendingProposalMsCreditAnalysis())
                .to(fanoutExchangePendingProposal());
    }

    @Bean
    public Binding createBindingPendingProposalMsNotification() {
        return BindingBuilder.bind(createQueuePendingProposalMsNotification())
                .to(fanoutExchangePendingProposal());
    }

    @Bean
    public Binding createBindingCompletedProposalMsProposalApp() {
        return BindingBuilder.bind(createQueueCompletedProposalMsProposal())
                .to(fanoutExchangeCompletedProposal());
    }

    @Bean
    public Binding createBindingCompletedProposalMsNotification() {
        return BindingBuilder.bind(createQueueCompletedProposalMsNotification())
                .to(fanoutExchangeCompletedProposal());
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;
    }
}