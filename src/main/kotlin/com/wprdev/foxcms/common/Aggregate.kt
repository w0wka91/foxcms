package com.wprdev.foxcms.common

import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents
import javax.persistence.Transient

abstract class Aggregate<T : DomainEvent> : BaseEntity() {
    @Transient
    private val domainEvents = mutableListOf<T>()

    @DomainEvents
    fun domainEvents(): Collection<T> {
        return domainEvents
    }

    protected fun registerEvent(event: T): T {
        this.domainEvents.add(event)
        return event
    }

    @AfterDomainEventPublication
    protected fun clearDomainEvents() {
        this.domainEvents.clear()
    }
}