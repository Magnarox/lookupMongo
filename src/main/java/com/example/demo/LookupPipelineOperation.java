package com.example.demo;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.util.Assert;

public class LookupPipelineOperation extends LookupOperation {

    private Aggregation pipeline;

    public LookupPipelineOperation(String from, String localField, String foreignField, String as, Aggregation pipeline) {
        super(Fields.field(from), Fields.field(localField), Fields.field(foreignField), Fields.field(as));

        Assert.notNull(pipeline, "Pipeline must not be null");

        this.pipeline = pipeline;
    }

    public LookupPipelineOperation(Field from, Field localField, Field foreignField, Field as, Aggregation pipeline) {
        super(from, localField, foreignField, as);

        Assert.notNull(pipeline, "Pipeline must not be null");

        this.pipeline = pipeline;
    }

    public Aggregation getPipeline() {
        return pipeline;
    }

    public void setPipeline(Aggregation pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public Document toDocument(AggregationOperationContext context) {
        Document doc = super.toDocument(context);
        Document pipeDoc = pipeline.toDocument("", context);
        ((Document)doc.get(getOperator())).append("pipeline", pipeDoc.get("pipeline"));

        return doc;
      }

}
