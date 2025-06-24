package org.getthedocs.documentengine.core.provider.aws.s3;

/*-
 * #%L
 * docs-provider-aws-s3
 * %%
 * Copyright (C) 2021 - 2025 Levente Ban
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

import java.util.Properties;

/**
 * Abstract base class for AWS S3 repositories.
 * Provides utility methods to retrieve settings from properties, system properties, and environment variables.
 *
 * @author Levente Ban
 */
public abstract class AbstractAwsS3Repository implements S3Repository {

    /**
     * Retrieves a setting from the provided properties, system properties, or environment variables.
     * The order of precedence is:
     * 1. Properties
     * 2. System properties
     * 3. Environment variables
     *
     * @param props The properties to check first.
     * @param property The name of the property to retrieve.
     * @return The value of the property, or null if not found.
     */
    protected String getSetting(final Properties props, final String property) {

        String result = (String) props.get(property);

        final String paramValueFromSystem = System.getProperty(property);
        if (paramValueFromSystem != null) {
            result = paramValueFromSystem;
        }

        final String paramValueFromEnv = System.getenv(property);
        if (paramValueFromEnv != null) {
            result = paramValueFromEnv;
        }

        return result;
    }
}
