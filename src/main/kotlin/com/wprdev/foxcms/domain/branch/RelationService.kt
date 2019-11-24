package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.domain.branch.events.RelationFieldDeleted
import com.wprdev.foxcms.domain.branch.field.RelationField
import com.wprdev.foxcms.infrastructure.ContentModelRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class RelationService(val contentModelRepo: ContentModelRepository) {

    private val logger = LoggerFactory.getLogger(RelationService::class.java)

    @EventListener
    protected fun handle(event: RelationFieldDeleted) {
        logger.info("Deleting bidirection relation field")

        val relatesToModel = contentModelRepo.findById(event.relatedModelId).orElseThrow { IllegalStateException() }

        relatesToModel.fields.find {
            it is RelationField &&
                    it.relatesTo.id == event.modelId &&
                    it.type == RelationField.oppositeDirection(event.relationType)
        }?.let {
            relatesToModel.deleteField(it)
            contentModelRepo.save(relatesToModel)
        }
    }

}