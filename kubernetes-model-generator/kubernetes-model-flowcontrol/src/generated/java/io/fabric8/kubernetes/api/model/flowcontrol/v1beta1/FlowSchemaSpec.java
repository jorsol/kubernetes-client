/**
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

package io.fabric8.kubernetes.api.model.flowcontrol.v1beta1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LocalObjectReference;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.BuildableReference;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "apiVersion",
    "kind",
    "metadata",
    "distinguisherMethod",
    "matchingPrecedence",
    "priorityLevelConfiguration",
    "rules"
})
@ToString
@EqualsAndHashCode
@Buildable(editableEnabled = false, validationEnabled = false, generateBuilderPackage = false, lazyCollectionInitEnabled = false, builderPackage = "io.fabric8.kubernetes.api.builder", refs = {
    @BuildableReference(ObjectMeta.class),
    @BuildableReference(LabelSelector.class),
    @BuildableReference(Container.class),
    @BuildableReference(PodTemplateSpec.class),
    @BuildableReference(ResourceRequirements.class),
    @BuildableReference(IntOrString.class),
    @BuildableReference(ObjectReference.class),
    @BuildableReference(LocalObjectReference.class),
    @BuildableReference(PersistentVolumeClaim.class)
})
public class FlowSchemaSpec implements KubernetesResource
{

    @JsonProperty("distinguisherMethod")
    private FlowDistinguisherMethod distinguisherMethod;
    @JsonProperty("matchingPrecedence")
    private Integer matchingPrecedence;
    @JsonProperty("priorityLevelConfiguration")
    private PriorityLevelConfigurationReference priorityLevelConfiguration;
    @JsonProperty("rules")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PolicyRulesWithSubjects> rules = new ArrayList<PolicyRulesWithSubjects>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public FlowSchemaSpec() {
    }

    /**
     * 
     * @param priorityLevelConfiguration
     * @param matchingPrecedence
     * @param rules
     * @param distinguisherMethod
     */
    public FlowSchemaSpec(FlowDistinguisherMethod distinguisherMethod, Integer matchingPrecedence, PriorityLevelConfigurationReference priorityLevelConfiguration, List<PolicyRulesWithSubjects> rules) {
        super();
        this.distinguisherMethod = distinguisherMethod;
        this.matchingPrecedence = matchingPrecedence;
        this.priorityLevelConfiguration = priorityLevelConfiguration;
        this.rules = rules;
    }

    @JsonProperty("distinguisherMethod")
    public FlowDistinguisherMethod getDistinguisherMethod() {
        return distinguisherMethod;
    }

    @JsonProperty("distinguisherMethod")
    public void setDistinguisherMethod(FlowDistinguisherMethod distinguisherMethod) {
        this.distinguisherMethod = distinguisherMethod;
    }

    @JsonProperty("matchingPrecedence")
    public Integer getMatchingPrecedence() {
        return matchingPrecedence;
    }

    @JsonProperty("matchingPrecedence")
    public void setMatchingPrecedence(Integer matchingPrecedence) {
        this.matchingPrecedence = matchingPrecedence;
    }

    @JsonProperty("priorityLevelConfiguration")
    public PriorityLevelConfigurationReference getPriorityLevelConfiguration() {
        return priorityLevelConfiguration;
    }

    @JsonProperty("priorityLevelConfiguration")
    public void setPriorityLevelConfiguration(PriorityLevelConfigurationReference priorityLevelConfiguration) {
        this.priorityLevelConfiguration = priorityLevelConfiguration;
    }

    @JsonProperty("rules")
    public List<PolicyRulesWithSubjects> getRules() {
        return rules;
    }

    @JsonProperty("rules")
    public void setRules(List<PolicyRulesWithSubjects> rules) {
        this.rules = rules;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
