package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    private final static Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    Level1Repository mongoRepo;

    @Autowired
    MongoTemplate mongoT;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/create")
    public ResponseEntity<String> create() {
        try {
            Level2 l21 = new Level2();
            l21.setName("L21");
            l21.setValue(40d);
            mongoT.save(l21);

            Level2 l22 = new Level2();
            l22.setName("L22");
            l22.setValue(45d);
            mongoT.save(l22);

            List<Level2> list = new ArrayList<>();
            list.add(l21);
            list.add(l22);
            Level1 l1 = new Level1();
            l1.setName("L1");
            l1.setSubElement(list);
            mongoT.save(l1);

            Level2 l31 = new Level2();
            l31.setName("L31");
            l31.setValue(30d);
            mongoT.save(l31);

            Level2 l32 = new Level2();
            l32.setName("L32");
            l32.setValue(35d);
            mongoT.save(l32);

            List<Level2> list2 = new ArrayList<>();
            list.add(l31);
            list.add(l32);
            Level1 l2 = new Level1();
            l2.setName("L2");
            l2.setSubElement(list2);
            mongoT.save(l2);

            return ResponseEntity.ok("Success");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/loadAll")
    public ResponseEntity<List<Level1>> loadAll() {
        List<Level1> list = mongoT.findAll(Level1.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/load")
    public ResponseEntity<Collection<Level1>> load() {
        LOGGER.info("Load");
        Aggregation pipeAggregation = Aggregation.newAggregation(Aggregation.match(new Criteria().andOperator(Criteria.where("value").gte(30), Criteria.where("value").lte(40))));

        Aggregation aggregations = Aggregation.newAggregation(
           // Join and Match with Level2 collection
           new LookupPipelineOperation("Level2", "subElement.$id", "_id", "subElement", pipeAggregation),

           // Remove empty result
           Aggregation.match(Criteria.where("subElement").not().size(0))
        );

        List<Level1> list = mongoT.aggregate(aggregations, Level1.class, Level1.class).getMappedResults();

        return ResponseEntity.ok(list);
    }
}
