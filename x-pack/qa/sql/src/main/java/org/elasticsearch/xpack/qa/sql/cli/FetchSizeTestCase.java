/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.qa.sql.cli;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.containsString;

/**
 * Test for setting the fetch size.
 */
public abstract class FetchSizeTestCase extends CliIntegrationTestCase {
    public void testSelect() throws IOException {
        StringBuilder bulk = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            bulk.append("{\"index\":{}}\n");
            bulk.append("{\"test_field\":" + i + "}\n");
        }
        client().performRequest("PUT", "/test/doc/_bulk", singletonMap("refresh", "true"),
                new StringEntity(bulk.toString(), ContentType.APPLICATION_JSON));
        assertEquals("[?1l>[?1000l[?2004lfetch size set to [90m4[0m", command("fetch size = 4"));
        assertEquals("[?1l>[?1000l[?2004lfetch separator set to \"[90m -- fetch sep -- [0m\"",
            command("fetch separator = \" -- fetch sep -- \""));
        assertThat(command("SELECT * FROM test ORDER BY test_field ASC"), containsString("test_field"));
        assertThat(readLine(), containsString("----------"));
        int i = 0;
        while (i < 20) {
            assertThat(readLine(), containsString(Integer.toString(i++)));
            assertThat(readLine(), containsString(Integer.toString(i++)));
            assertThat(readLine(), containsString(Integer.toString(i++)));
            assertThat(readLine(), containsString(Integer.toString(i++)));
            assertThat(readLine(), containsString(" -- fetch sep -- "));
        }
        assertEquals("", readLine());
    }

    public void testInvalidFetchSize() throws IOException {
        assertEquals(ErrorsTestCase.START + "Invalid fetch size [[3;33;22mcat" + ErrorsTestCase.END, command("fetch size = cat"));
        assertEquals(ErrorsTestCase.START + "Invalid fetch size [[3;33;22m0[23;31;1m]. Must be > 0.[0m", command("fetch size = 0"));
        assertEquals(ErrorsTestCase.START + "Invalid fetch size [[3;33;22m-1231[23;31;1m]. Must be > 0.[0m", command("fetch size = -1231"));
        assertEquals(ErrorsTestCase.START + "Invalid fetch size [[3;33;22m" + Long.MAX_VALUE + ErrorsTestCase.END,
            command("fetch size = " + Long.MAX_VALUE));
    }
}
