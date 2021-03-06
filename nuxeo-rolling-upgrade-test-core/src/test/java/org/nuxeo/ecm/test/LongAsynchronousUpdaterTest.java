package org.nuxeo.ecm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * Empty Unit Testing class.
 * <p/>
 *
 * @see <a href="https://doc.nuxeo.com/corg/unit-testing/">Unit Testing</a>
 */
@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("org.nuxeo.ecm.test.nuxeo-rolling-upgrade-test-core")
public class LongAsynchronousUpdaterTest {

    @Inject
    protected CoreSession session;

    @Test
    public void propertyIsUpdateAsynchronouslyAndAfterALongTime() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "sample", "Note");
        doc = session.createDocument(doc);
        assertNull(doc.getProperty("dc:description").getValue(String.class));
        TransactionHelper.commitOrRollbackTransaction();

        Thread.sleep(5000);

        TransactionHelper.startTransaction();
        doc = session.getDocument(new PathRef("/sample"));
        assertEquals("updated", doc.getProperty("dc:description").getValue(String.class));




    }
}
