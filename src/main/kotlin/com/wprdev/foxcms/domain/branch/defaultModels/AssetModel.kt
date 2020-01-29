package com.wprdev.foxcms.domain.branch.defaultModels

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Concern
import com.wprdev.foxcms.domain.branch.Constraint
import com.wprdev.foxcms.domain.branch.ContentModel
import com.wprdev.foxcms.domain.branch.ModelName
import com.wprdev.foxcms.domain.branch.field.DisplayType
import com.wprdev.foxcms.domain.branch.field.FieldName
import com.wprdev.foxcms.domain.branch.field.ScalarField

object AssetModel : ContentModel(Name("Asset"), ModelName("Asset"), "") {
    init {
        this.addField(ScalarField(Name("Public id"),
                FieldName("public_id"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Filename"),
                FieldName("filename"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Format"),
                FieldName("format"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Resource Type"),
                FieldName("resource_type"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Width"),
                FieldName("width"),
                DisplayType.INTEGER,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Height"),
                FieldName("height"),
                DisplayType.INTEGER,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Bytes"),
                FieldName("bytes"),
                DisplayType.INTEGER,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Url"),
                FieldName("url"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Secure url"),
                FieldName("secure_url"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE))
        this.addField(ScalarField(Name("Thumbnail url"),
                FieldName("thumbnail_url"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE))
    }
}