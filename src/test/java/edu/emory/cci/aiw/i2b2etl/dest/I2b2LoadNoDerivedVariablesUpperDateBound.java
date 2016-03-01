/*
 * #%L
 * AIW i2b2 ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.i2b2etl.dest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.BeforeClass;
import org.protempa.QueryException;
import org.protempa.ProtempaStartupException;
import org.protempa.backend.dsb.filter.DateTimeFilter;
import org.protempa.proposition.interval.Interval.Side;
import org.protempa.proposition.value.AbsoluteTimeGranularity;
import org.protempa.query.DefaultQueryBuilder;
import org.protempa.query.QueryBuildException;
import org.protempa.query.QueryMode;

/**
 * Data validation tests for the i2b2 ETL. The test initiates Protempa to access the
 * test data and execute a query before AIW ETL loads the processed data into an H2 database.
 * The new loaded data is compared to the one expected using DbUnit.
 *
 * @author Andrew Post
 */
public class I2b2LoadNoDerivedVariablesUpperDateBound extends AbstractI2b2DestLoadTest {
    /**
     *  Executes the i2b2 ETL load.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        DefaultQueryBuilder q = new DefaultQueryBuilder();
        q.setName("i2b2 ETL Test Query No Derived Variables With Upper Date Bound");
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(2008, Calendar.DECEMBER, 31);
        Date first = c.getTime();
        q.setFilters(new DateTimeFilter(new String[]{"Encounter"}, null, null, first, AbsoluteTimeGranularity.DAY, null, Side.FINISH));
        q.setQueryMode(QueryMode.REPLACE);
        getProtempaFactory().execute(q);
        
        File file = File.createTempFile("i2b2LoadNoDerivedVariablesUpperDateBoundTest", ".xml");
        try (FileOutputStream out = new FileOutputStream(file)) {
            getProtempaFactory().exportI2b2DataSchema(out);
            System.out.println("Dumped i2b2 data schema to " + file.getAbsolutePath());
        }
        
        setExpectedDataSet("/truth/i2b2LoadNoDerivedVariablesUpperDateBoundTestData.xml");
    }

}
