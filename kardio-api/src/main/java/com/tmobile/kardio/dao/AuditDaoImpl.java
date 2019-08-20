/*******************************************************************************
 * Copyright 2019 T-Mobile, Inc. or its affiliates. All Rights Reserved.
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
 ******************************************************************************/
package com.tmobile.kardio.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.Audit;
import com.tmobile.kardio.db.entity.AuditEntity;

@Repository
public class AuditDaoImpl implements AuditDao {
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	public void saveAuditLog(Audit audit) {
		AuditEntity auditEntity= new AuditEntity();
		Session session= sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		auditEntity.setAudit_log(audit.getAudit_log());
		auditEntity.setDate(audit.getDate());
		auditEntity.setUser_id(audit.getUser_id());
		session.save(auditEntity);
		tx.commit();
		session.close();
		}

}
