package com.wprdev.foxcms.domain.branch.events

import com.wprdev.foxcms.common.DomainEvent
import com.wprdev.foxcms.domain.branch.RelationType

sealed class ContentModelEvent : DomainEvent

data class RelationFieldDeleted(
        val modelId: Long,
        val relatedModelId: Long,
        val relationType: RelationType) : ContentModelEvent()