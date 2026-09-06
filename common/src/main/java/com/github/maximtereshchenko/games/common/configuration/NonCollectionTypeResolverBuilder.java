package com.github.maximtereshchenko.games.common.configuration;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.impl.DefaultTypeResolverBuilder;

final class NonCollectionTypeResolverBuilder extends DefaultTypeResolverBuilder {

    NonCollectionTypeResolverBuilder() {
        super(
            BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build(),
            DefaultTyping.OBJECT_AND_NON_CONCRETE,
            "type"
        );
    }

    @Override
    public boolean useForType(JavaType javaType) {
        if (
            javaType.isCollectionLikeType() ||
            javaType.isMapLikeType()
        ) {
            return false;
        }
        return super.useForType(javaType);
    }
}
