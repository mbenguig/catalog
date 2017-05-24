/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.catalog.rest.service.repository;

import org.ow2.proactive.catalog.rest.entity.CatalogObject;
import org.ow2.proactive.catalog.rest.entity.CatalogObjectRevision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * @author ActiveEon Team
 */
public interface CatalogObjectRepository
        extends PagingAndSortingRepository<CatalogObject, Long>, QueryDslPredicateExecutor<CatalogObject> {

    @Query("SELECT cor FROM CatalogObjectRevision cor JOIN cor.catalogObject co WHERE cor.bucketId = ?1 AND co.lastCommitId = cor.commitId")
    Page<CatalogObjectRevision> getMostRecentRevisions(Long bucketId, Pageable pageable);

    @Query("SELECT cor FROM CatalogObjectRevision cor JOIN cor.catalogObject co WHERE cor.bucketId = ?1 AND cor.catalogObject.commitId = ?2 AND co.lastCommitId = cor.commitId")
    CatalogObjectRevision getMostRecentCatalogObjectRevision(Long bucketId, Long objectId);

}
