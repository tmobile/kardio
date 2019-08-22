/*******************************************************************************
 * Copyright 2019 T-Mobile USA, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * See the LICENSE file for additional language around disclaimer of warranties.
 * Trademark Disclaimer: Neither the name of "T-Mobile, USA" nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 ******************************************************************************/
package com.tmobile.kardio.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.AvailabilityData;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.constants.Region;

/**
 * Implements AvailabilityPercentageDao to access availability percentage in the database
 * 
 */
@Repository
public class AvailabilityPercentageDaoImpl implements AvailabilityPercentageDao {

    private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
    private EnvironmentDao environmentDao;

    @Autowired
    private SessionFactory sessionFactory;

    public List<AvailabilityData> getAllAvailabilityPercentage(String environment, String interval,String platform,String region) throws ParseException {
    	
        final int envId = environmentDao.getEnironmentIdFromName(environment);
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        String startDate = null;
        String endDate = sdf.format(cal.getTime());
        if (interval.equalsIgnoreCase("D")) {
            startDate = sdf.format(cal.getTime());
        } else if (interval.equalsIgnoreCase("M")) {
            cal.add(Calendar.MONTH, -1);
            startDate = sdf.format(cal.getTime());
        } else if (interval.equalsIgnoreCase("Y")) {
            cal.add(Calendar.YEAR, -1);
            startDate = sdf.format(cal.getTime());
        } else {
            throw new IllegalArgumentException("The Interval is not valid");
        }
        return getAvailability(envId, startDate, endDate, platform,region);
    }

    private List<AvailabilityData> getAvailability(final int envId, final String startDate, final String endDate, String platform,String region) throws ParseException {
        List<AvailabilityData> appPercentage = getAppAvailabilityPercentage(envId, startDate, endDate, platform,region);
        List<AvailabilityData> infraPercentage = getInfraAvailability(envId, startDate, endDate, platform,region);

        infraPercentage.addAll(appPercentage);
        List<AvailabilityData> listPercentage = infraPercentage;
        return listPercentage;

    }

    /**
     * Get the Region wise Availability % of each App services.
     * @param envId
     * @param startDate
     * @param endDate
     * @param platform
     * @param region
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    private List<AvailabilityData> getAppAvailabilityPercentage(final int envId, final String startDate, final String endDate, String platform, String region) throws ParseException {
    	
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Session session = sessionFactory.openSession();
        Query appQuery = null;
        Query apiQuery = null;
        if(platform.equalsIgnoreCase("All")){
        appQuery = session.createQuery(HQLConstants.QUERY_GET_APP_AVAILABILITY_PERCENTAGE).setCacheable(true).setCacheRegion("QUERY_GET_APP_AVAILABILITY_PERCENTAGE");
        appQuery.setParameter("envId", envId);
        appQuery.setParameter("startDate", sdf.parse(startDate));
        appQuery.setParameter("endDate", sdf.parse(endDate));
        appQuery.setParameter("regionName", region);

        apiQuery = session.createQuery(HQLConstants.QUERY_GET_API_AVAILABILITY_PERCENTAGE).setCacheable(true).setCacheRegion("QUERY_GET_API_AVAILABILITY_PERCENTAGE");
        apiQuery.setParameter("envId", envId);
        apiQuery.setParameter("startDate", sdf.parse(startDate));
        apiQuery.setParameter("endDate", sdf.parse(endDate));
        apiQuery.setParameter("regionName", region);
        
        }
        else{
        	 appQuery= session.createQuery(HQLConstants.QUERY_GET_APP_AVAILABILITY_PERCENTAGE_PLATFORM).setCacheable(true).setCacheRegion("QUERY_GET_APP_AVAILABILITY_PERCENTAGE");
        	 appQuery.setParameter("envId", envId);
        	 appQuery.setParameter("startDate", sdf.parse(startDate));
        	 appQuery.setParameter("endDate", sdf.parse(endDate));
        	 appQuery.setParameter("regionName", region);
        	 appQuery.setParameter("platform",platform);
             
             apiQuery = session.createQuery(HQLConstants.QUERY_GET_API_AVAILABILITY_PERCENTAGE_PLATFORM).setCacheable(true).setCacheRegion("QUERY_GET_API_AVAILABILITY_PERCENTAGE");
             apiQuery.setParameter("envId", envId);
             apiQuery.setParameter("startDate", sdf.parse(startDate));
             apiQuery.setParameter("endDate", sdf.parse(endDate));
             apiQuery.setParameter("regionName", region);
             apiQuery.setParameter("platform",platform);
             
        }
        List<Object[]> appList = appQuery.list();
        List<Object[]> apiList = apiQuery.list();
        session.close();
        appList.addAll(apiList);

        List<AvailabilityData> listOfAvl = new ArrayList<AvailabilityData>();
        for (Object[] aRow : appList) {
            Integer comId = (Integer) aRow[0];
            if (aRow[1] != null) {
                Double percentage = (Double) aRow[1];
                AvailabilityData avlData = new AvailabilityData();
                avlData.setComponentId(comId);
                if(region.equalsIgnoreCase(Region.WEST_REGION.getRegionDescription()))
                avlData.setAvailabilityPercentageWest(percentage);
                else
                avlData.setAvailabilityPercentageEast(percentage);
                
                listOfAvl.add(avlData);
            }
        }
        
        return listOfAvl;
        
    }
    
    /**
     * Get the Region wise Availability % of each Infra services.
     * @param envId
     * @param startDate
     * @param endDate
     * @param platform
     * @param region
     * @return
     * @throws ParseException
     */
    private List<AvailabilityData> getInfraAvailability(final int envId, final String startDate, final String endDate,String platform,String region) throws ParseException {
    	
        Session session = sessionFactory.openSession();
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Query query = null;
        if(platform.equalsIgnoreCase("All")) {
        	query = session.createQuery(HQLConstants.QUERY_GET_INFRA_AVAILABILITY_PERCENTAGE).setCacheable(true).setCacheRegion("QUERY_GET_INFRA_AVAILABILITY_PERCENTAGE");
        	query.setParameter("envId", envId);
        	query.setParameter("startDate", sdf.parse(startDate));
        	query.setParameter("endDate", sdf.parse(endDate));
        	query.setParameter("regionName", region);
        }
        else {
        	query = session.createQuery(HQLConstants.QUERY_GET_INFRA_AVAILABILITY_PERCENTAGE_PLATFORM).setCacheable(true).setCacheRegion("QUERY_GET_INFRA_AVAILABILITY_PERCENTAGE");
        	query.setParameter("envId", envId);
        	query.setParameter("startDate", sdf.parse(startDate));
        	query.setParameter("endDate", sdf.parse(endDate));
        	query.setParameter("platform", platform);
        	query.setParameter("regionName", region);
        }
        @SuppressWarnings("unchecked")
        List<Object[]> listResult = query.list();
        session.close();
        Map<Integer, AvailabilityData> avlMap = new HashMap<Integer, AvailabilityData>();
        for (Object[] aRow : listResult) {
            Integer comId = (Integer) aRow[0];
            Float percentage = (Float) aRow[1];
            AvailabilityData avlData = null;
            if (avlMap.get(comId) == null) {
                avlData = new AvailabilityData();
                avlData.setComponentId(comId);
                if(region.equalsIgnoreCase("West Region"))
                avlData.setAvailabilityPercentageWest(percentage);
                else
                avlData.setAvailabilityPercentageEast(percentage);
            } else {
                avlData = avlMap.get(comId);
                //double avlPerc = avlData.getAvailabilityPercentageWest();
                if(region.equalsIgnoreCase("West Region"))
                avlData.setAvailabilityPercentageWest((avlData.getAvailabilityPercentageWest() + percentage) / 2);
                else
                avlData.setAvailabilityPercentageEast((avlData.getAvailabilityPercentageEast() + percentage) / 2);
            }
            avlMap.put(comId, avlData);
        }
        List<AvailabilityData> listOfAvl = new ArrayList<AvailabilityData>(avlMap.values());
        return listOfAvl;
    }

}
