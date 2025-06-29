package org.getthedocs.documentengine.core.provider.templaterepository.aws.s3;

/*-
 * #%L
 * docs-provider-aws-s3
 * %%
 * Copyright (C) 2023 Levente Ban
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

import lombok.extern.slf4j.Slf4j;
import org.getthedocs.documentengine.core.provider.aws.s3.S3ClientFactory;
import org.getthedocs.documentengine.core.provider.persistence.Page;
import org.getthedocs.documentengine.core.provider.persistence.Pageable;
import org.getthedocs.documentengine.core.provider.templaterepository.TemplateRepository;
import org.getthedocs.documentengine.core.service.TemplateServiceRegistry;
import org.getthedocs.documentengine.core.service.exception.TemplateServiceConfigurationException;
import org.getthedocs.documentengine.core.template.descriptors.TemplateDocument;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
public class AwsS3TemplateRepositoryTest {

    private static TemplateRepository templateRepository;
    private static String TESTBUCKET;
    private static final String BASEDIR;

    static {
        BASEDIR = "testfiles/templates/";
    }

    @BeforeClass
    public static void initialize() {
        templateRepository = TemplateServiceRegistry.getConfiguration().getTemplateRepository();
        TESTBUCKET = ((AwsS3TemplateRepository)templateRepository).getBucketName();

        final S3Client s3 = S3ClientFactory.getS3Client(Region.EU_CENTRAL_1);

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(TESTBUCKET)
                        .key(BASEDIR + "myfile.txt")
                        .build(), RequestBody.fromBytes("Test content.".getBytes()));

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(TESTBUCKET)
                        .key(BASEDIR + "supportedfile.docx")
                        .build(), RequestBody.fromBytes("Test content.".getBytes()));

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(TESTBUCKET)
                        .key(BASEDIR + "supportedfile2.docx")
                        .build(), RequestBody.fromBytes("Test content.".getBytes()));
    }

    @AfterClass
    public static void teardown()	{

    }

    @Test
    public void testExistsFileExists() {

        final String path =  "textfiles/";
        final String fileName = "myfile.txt";

        try {
            assertNotNull(templateRepository.getTemplate(path + fileName));
        } catch (TemplateServiceConfigurationException e) {
            fail();
        }

    }

    @Test
    public void testExistsFileNotExists() {

        final String path =  "textfiles/";
        final String fileName = "nonexistingfile.txt";

        try {
            assertNull(templateRepository.getTemplate(path + fileName));
        } catch (TemplateServiceConfigurationException e) {
            fail();
        }

    }

    @Test
    public void testGetInputStreamForSingleFileToBucketInDefaultRegionOk() throws Exception	{

        final String path =  "textfiles/";
        final String fileName = "myfile.txt";

        final InputStream is = templateRepository.getTemplate(path + fileName);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        final StringBuilder textData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            textData.append(line);
        }

        is.close();

        log.debug("myfile.txt: " + textData);

        assertFalse(textData.toString().isEmpty());

    }

    @Test
    public void testGetInputStreamForSingleFileToBucketNonExisting()	{

        final String path = "textfiles/";
        final String fileName = "nonexistingfile.txt";

        final InputStream is;
        try {
            is = templateRepository.getTemplate(path + fileName);
            assertNull(is);
        } catch (TemplateServiceConfigurationException e) {
            fail();
        }

    }

    @Test
    public void testGetTemplateListExistingPrefix() throws Exception	{

        final Pageable pageRequest = new Pageable();
        pageRequest.setPaged(true);
        pageRequest.setPage(0);
        pageRequest.setSize(10);
        pageRequest.setOffset(0);
        final Page<TemplateDocument> templateDocumentPage = templateRepository.getTemplates(pageRequest);

        assertNotNull(templateDocumentPage);
        assertEquals(2, templateDocumentPage.getSize().intValue());
    }

    @Test
    public void testGetTemplateDocumentById() throws Exception	{

        final String fileName = "supportedfile.docx";

        final Optional<TemplateDocument> templateDocument = templateRepository.getTemplateDocumentById(fileName, null, true);

        assertNotNull(templateDocument);
        assertTrue(templateDocument.isPresent());
        assertTrue(templateDocument.get().getBinary().length > 0);
    }
}
