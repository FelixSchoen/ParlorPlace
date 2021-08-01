package com.fschoen.parlorplace.backend.integration.utility;

import org.hibernate.boot.*;
import org.hibernate.tool.hbm2ddl.*;

import java.util.*;

import static org.hibernate.tool.schema.TargetType.*;

public class TestIsolationService {

    private final Metadata metadata;

    TestIsolationService(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Drops the database and creates all the necessary tables again.
     */
    public void recreateDatabase() {
        SchemaExport schemaExport = new SchemaExport().setFormat(true);
        schemaExport.drop(EnumSet.of(DATABASE), metadata);
        schemaExport.create(EnumSet.of(DATABASE), metadata);
    }

}
