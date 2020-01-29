package com.wprdev.foxcms.common

import com.vladmihalcea.hibernate.type.array.IntArrayType
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@TypeDefs(
        TypeDef(
                name = "string-array",
                typeClass = StringArrayType::class
        ),
        TypeDef(
                name = "int-array",
                typeClass = IntArrayType::class
        ),
        TypeDef(name = "pgsql_enum",
                typeClass = PostgreSQLEnumType::class)
)
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @CreatedDate
    open var createdAt: LocalDateTime = LocalDateTime.now()
    @LastModifiedDate
    open var updatedAt: LocalDateTime = LocalDateTime.now()
}