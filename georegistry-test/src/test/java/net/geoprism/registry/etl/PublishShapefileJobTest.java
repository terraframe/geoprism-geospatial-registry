/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.etl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.AllJobStatus;
import com.runwaysdk.system.scheduler.ExecutableJob;
import com.runwaysdk.system.scheduler.JobHistoryRecord;
import com.runwaysdk.system.scheduler.JobHistoryRecordQuery;
import com.runwaysdk.system.scheduler.SchedulerManager;

import net.geoprism.registry.MasterList;
import net.geoprism.registry.MasterListVersion;
import net.geoprism.registry.service.MasterListService;
import net.geoprism.registry.service.MasterListTest;
import net.geoprism.registry.test.FastTestDataset;
import net.geoprism.registry.test.SchedulerTestUtils;
import net.geoprism.registry.test.USATestData;

public class PublishShapefileJobTest
{
  protected static FastTestDataset testData;

  @BeforeClass
  public static void setUpClass()
  {
    testData = FastTestDataset.newTestData();
    testData.setSessionUser(testData.USER_CGOV_RA);
    testData.setUpMetadata();

    if (!SchedulerManager.initialized())
    {
      SchedulerManager.start();
    }
  }

  @AfterClass
  public static void cleanUpClass()
  {
//    SchedulerManager.shutdown();

    if (testData != null)
    {
      testData.tearDownMetadata();
    }
  }

  @Before
  public void setUp()
  {
    if (testData != null)
    {
      testData.setUpInstanceData();
    }

    clearData();
  }

  @After
  public void tearDown() throws IOException
  {
    if (testData != null)
    {
      testData.tearDownInstanceData();
    }

    FileUtils.deleteDirectory(new File(VaultProperties.getPath("vault.default"), "files"));

    clearData();
  }

  @Request
  private static void clearData()
  {
    JobHistoryRecordQuery query = new JobHistoryRecordQuery(new QueryFactory());
    OIterator<? extends JobHistoryRecord> jhrs = query.getIterator();

    while (jhrs.hasNext())
    {
      JobHistoryRecord jhr = jhrs.next();

      ExecutableJob job = jhr.getParent();

      if (job instanceof PublishMasterListJob)
      {
        jhr.delete();
        job.delete();
      }
    }
  }
  
  @Test
  public void testCreate() throws InterruptedException
  {
    JsonObject listJson = MasterListTest.getJson(testData.ORG_CGOV.getServerObject(), testData.HIER_ADMIN, testData.PROVINCE, testData.COUNTRY);

    MasterListService service = new MasterListService();
    JsonObject result = service.create(testData.clientRequest.getSessionId(), listJson);
    String oid = result.get(ComponentInfo.OID).getAsString();

    try
    {
      String historyId = service.createPublishedVersionsJob(testData.clientRequest.getSessionId(), oid);

      SchedulerTestUtils.waitUntilStatus(historyId, AllJobStatus.SUCCESS);

      final JsonObject object = service.getVersions(testData.clientRequest.getSessionId(), oid, MasterListVersion.PUBLISHED);
      final JsonArray json = object.get(MasterList.VERSIONS).getAsJsonArray();

      Assert.assertEquals(1, json.size());

      final JsonObject version = json.get(0).getAsJsonObject();

      Assert.assertFalse(version.get("shapefile").getAsBoolean());

      final String versionId = version.get(MasterListVersion.OID).getAsString();

      String historyId2 = service.generateShapefile(testData.clientRequest.getSessionId(), versionId);

      SchedulerTestUtils.waitUntilStatus(historyId2, AllJobStatus.SUCCESS);

      final JsonObject response = service.getVersions(testData.clientRequest.getSessionId(), oid, MasterListVersion.PUBLISHED);
      final JsonArray array = response.get(MasterList.VERSIONS).getAsJsonArray();
      final JsonObject test = array.get(0).getAsJsonObject();

      Assert.assertTrue(test.get("shapefile").getAsBoolean());
    }
    finally
    {
      service.remove(testData.clientRequest.getSessionId(), oid);
    }

  }
}
