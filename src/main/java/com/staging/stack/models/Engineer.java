package com.staging.stack.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "engineers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "engineerId"),
                @UniqueConstraint(columnNames = "engineerName")
        }
)
public class Engineer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long engineerId;

    private String engineerName;

    public Engineer(){
       super();
    }

    public Engineer(long id, long engineerId, String engineerName){
        super();
        this.id = id;
        this.engineerId = engineerId;
        this.engineerName = engineerName;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getengineerId() {
        return engineerId;
    }

    public void setengineerId(long engineerId) {
        this.engineerId = engineerId;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }


}
