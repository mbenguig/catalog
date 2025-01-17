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
package org.ow2.proactive.catalog.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import org.ow2.proactive.catalog.repository.entity.BucketEntity;
import org.ow2.proactive.catalog.repository.entity.CatalogObjectEntity;
import org.springframework.stereotype.Repository;


@Repository
public class BucketRepositoryImpl implements BucketRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Object[]> findBucketContainingKindListAndContentTypeAndObjectName(List<String> kindList,
            String contentType, String objectName) {

        return em.createQuery(buildCriteriaQuery(null, kindList, contentType, objectName)).getResultList();
    }

    @Override
    public List<Object[]> findBucketByOwnerContainingKindListAndContentTypeAndObjectName(List<String> owners,
            List<String> kindList, String contentType, String objectName) {

        return em.createQuery(buildCriteriaQuery(owners, kindList, contentType, objectName)).getResultList();
    }

    @Override
    public List<Object[]> findBucketByOwnerContainingKindList(List<String> owners, List<String> kindList) {
        return em.createQuery(buildCriteriaQuery(owners, kindList, null, null)).getResultList();
    }

    @Override
    public List<Object[]> findBucketContainingKindList(List<String> kindList) {
        return em.createQuery(buildCriteriaQuery(null, kindList, null, null)).getResultList();
    }

    private CriteriaQuery<Object[]> buildCriteriaQuery(List<String> owners, List<String> kindList, String contentType,
            String objectName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<BucketEntity> bucketEntityRoot = cq.from(BucketEntity.class);
        cq.orderBy(cb.asc(bucketEntityRoot.get("id")));
        Join<BucketEntity, CatalogObjectEntity> catalogObjectsJoin = bucketEntityRoot.join("catalogObjects",
                                                                                           JoinType.LEFT);
        List<Predicate> allPredicates = new ArrayList<>();

        if (kindList.size() > 0) {
            List<Predicate> kindPredicatesList = new ArrayList<>();
            for (String kind : kindList) {
                kindPredicatesList.add(cb.like(catalogObjectsJoin.get("kindLower"), kind.toLowerCase() + "%"));
            }
            Predicate kindPredicate;
            if (kindList.size() == 1) {
                kindPredicate = kindPredicatesList.get(0);
            } else {
                kindPredicate = cb.or(kindPredicatesList.toArray(new Predicate[0]));
            }
            allPredicates.add(kindPredicate);
        }

        if (contentType != null) {
            Predicate contentTypePredicate = cb.like(catalogObjectsJoin.get("contentTypeLower"),
                                                     contentType.toLowerCase() + "%");
            allPredicates.add(contentTypePredicate);
        }

        if (objectName != null) {
            Predicate objectNamePredicate = cb.like(catalogObjectsJoin.get("nameLower"),
                                                    "%" + objectName.toLowerCase() + "%");
            allPredicates.add(objectNamePredicate);
        }

        if (owners != null) {
            Predicate ownerPredicate = cb.in(bucketEntityRoot.get("owner")).value(owners);
            allPredicates.add(ownerPredicate);
        }
        if (allPredicates.size() > 0) {
            Predicate finalPredicate;
            if (allPredicates.size() == 1) {
                finalPredicate = allPredicates.get(0);
            } else {
                finalPredicate = cb.and(allPredicates.toArray(new Predicate[0]));
            }

            cq.where(finalPredicate);
        }
        cq.multiselect(bucketEntityRoot.get("bucketName"),
                       bucketEntityRoot.get("owner"),
                       cb.count(catalogObjectsJoin.get("id").get("name")),
                       bucketEntityRoot.get("id"))
          .groupBy(bucketEntityRoot.get("bucketName"), bucketEntityRoot.get("owner"), bucketEntityRoot.get("id"));
        return cq;
    }
}
