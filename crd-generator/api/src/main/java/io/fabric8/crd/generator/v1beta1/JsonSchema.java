/*
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.crd.generator.v1beta1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.fabric8.crd.generator.AbstractJsonSchema;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.JSONSchemaProps;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.JSONSchemaPropsBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.ValidationRule;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.ValidationRuleBuilder;
import io.sundr.model.Property;
import io.sundr.model.TypeDef;
import io.sundr.model.TypeRef;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.fabric8.crd.generator.CRDGenerator.YAML_MAPPER;

public class JsonSchema extends AbstractJsonSchema<JSONSchemaProps, JSONSchemaPropsBuilder> {

  private static final JsonSchema instance = new JsonSchema();

  public static final JSONSchemaProps JSON_SCHEMA_INT_OR_STRING = new JSONSchemaPropsBuilder()
      .withXKubernetesIntOrString(true)
      .withAnyOf(
          new JSONSchemaPropsBuilder().withType("integer").build(),
          new JSONSchemaPropsBuilder().withType("string").build())
      .build();

  /**
   * Creates the JSON schema for the particular {@link TypeDef}.
   *
   * @param definition The definition.
   * @param ignore an optional list of property names to ignore
   * @return The schema.
   */
  public static JSONSchemaProps from(
      TypeDef definition, String... ignore) {
    return instance.internalFrom(definition, ignore);
  }

  @Override
  public JSONSchemaPropsBuilder newBuilder() {
    return newBuilder("object");
  }

  @Override
  public JSONSchemaPropsBuilder newBuilder(String type) {
    final JSONSchemaPropsBuilder builder = new JSONSchemaPropsBuilder();
    builder.withType(type);
    return builder;
  }

  @Override
  public void addProperty(Property property, JSONSchemaPropsBuilder builder,
      JSONSchemaProps schema, SchemaPropsOptions options) {
    if (schema != null) {
      options.getDefault().ifPresent(s -> {
        try {
          schema.setDefault(YAML_MAPPER.readTree(s));
        } catch (JsonProcessingException e) {
          throw new IllegalArgumentException("Cannot parse default value: '" + s + "' as valid YAML.");
        }
      });
      options.getMin().ifPresent(schema::setMinimum);
      options.getMax().ifPresent(schema::setMaximum);
      options.getPattern().ifPresent(schema::setPattern);

      List<ValidationRule> validationRulesFromProperty = options.getValidationRules().stream()
          .map(this::mapValidationRule)
          .collect(Collectors.toList());

      List<ValidationRule> resultingValidationRules = new ArrayList<>(schema.getXKubernetesValidations());
      resultingValidationRules.addAll(validationRulesFromProperty);

      if (!resultingValidationRules.isEmpty()) {
        schema.setXKubernetesValidations(resultingValidationRules);
      }

      if (options.isNullable()) {
        schema.setNullable(true);
      }

      if (options.isPreserveUnknownFields()) {
        schema.setXKubernetesPreserveUnknownFields(true);
      }

      builder.addToProperties(property.getName(), schema);
    }
  }

  @Override
  public JSONSchemaProps build(JSONSchemaPropsBuilder builder, List<String> required,
      List<KubernetesValidationRule> validationRules, boolean preserveUnknownFields) {
    builder = builder.withRequired(required);
    if (preserveUnknownFields) {
      builder.withXKubernetesPreserveUnknownFields(preserveUnknownFields);
    }
    builder.addAllToXKubernetesValidations(mapValidationRules(validationRules));
    return builder.build();
  }

  @Override
  protected JSONSchemaProps arrayLikeProperty(JSONSchemaProps schema) {
    return new JSONSchemaPropsBuilder()
        .withType("array")
        .withNewItems()
        .withSchema(schema)
        .and()
        .build();
  }

  @Override
  protected JSONSchemaProps mapLikeProperty(JSONSchemaProps schema) {
    return new JSONSchemaPropsBuilder()
        .withType("object")
        .withNewAdditionalProperties()
        .withSchema(schema)
        .endAdditionalProperties()
        .build();
  }

  @Override
  protected JSONSchemaProps singleProperty(String typeName) {
    return new JSONSchemaPropsBuilder()
        .withType(typeName)
        .build();
  }

  @Override
  protected JSONSchemaProps mappedProperty(TypeRef ref) {
    return JSON_SCHEMA_INT_OR_STRING;
  }

  @Override
  protected JSONSchemaProps enumProperty(JsonNode... enumValues) {
    return new JSONSchemaPropsBuilder().withType("string").withEnum(enumValues).build();
  }

  @Override
  protected JSONSchemaProps addDescription(JSONSchemaProps schema, String description) {
    return new JSONSchemaPropsBuilder(schema)
        .withDescription(description)
        .build();
  }

  private List<ValidationRule> mapValidationRules(List<KubernetesValidationRule> validationRules) {
    return validationRules.stream()
        .map(this::mapValidationRule)
        .collect(Collectors.toList());
  }

  private ValidationRule mapValidationRule(KubernetesValidationRule validationRule) {
    return new ValidationRuleBuilder()
        .withRule(validationRule.getRule())
        .withMessage(validationRule.getMessage())
        .withMessageExpression(validationRule.getMessageExpression())
        .withReason(validationRule.getReason())
        .withFieldPath(validationRule.getFieldPath())
        .withOptionalOldSelf(validationRule.getOptionalOldSelf())
        .build();
  }
}
