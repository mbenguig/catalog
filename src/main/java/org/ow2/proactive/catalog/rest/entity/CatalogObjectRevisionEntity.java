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
package org.ow2.proactive.catalog.rest.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.ow2.proactive.catalog.rest.util.LocalDateTimeAttributeConverter;
import org.springframework.data.annotation.CreatedDate;


/**
 * @author ActiveEon Team
 */
@Entity
@Table(name = "CATALOG_OBJECT_REVISION", indexes = { @Index(columnList = "NAME"), @Index(columnList = "KIND") })
public class CatalogObjectRevisionEntity implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMIT_ID")
    private Long commitId;

    @Column(name = "KIND", nullable = false)
    private String kind;

    @Column(name = "COMMIT_MESSAGE", nullable = false)
    private String commitMessage;

    @CreatedDate
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "COMMIT_DATE", nullable = false)
    private LocalDateTime commitDate;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "BUCKET_ID", nullable = false)
    private Long bucketId;

    @ManyToOne
    @JoinColumn(name = "CATALOG_OBJECT_ID")
    private CatalogObjectEntity catalogObject;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private List<KeyValueMetadataEntity> keyValueMetadataList;

    @Lob
    @Column(name = "RAW_OBJECT", columnDefinition = "blob", nullable = false)
    private byte[] rawObject;

    public CatalogObjectRevisionEntity() {
        this.keyValueMetadataList = new ArrayList<>();
    }

    public CatalogObjectRevisionEntity(String kind, LocalDateTime commitDate, String name, String commitMessage,
            Long bucketId, String contentType, byte[] rawObject) {
        this();
        this.kind = kind;
        this.name = name;
        this.commitMessage = commitMessage;
        this.commitDate = commitDate;
        this.bucketId = bucketId;
        this.contentType = contentType;
        this.rawObject = rawObject;
    }

    public CatalogObjectRevisionEntity(Long commitId, String kind, LocalDateTime commitDate, String name,
            String commitMessage, Long bucketId, String contentType, List<KeyValueMetadataEntity> keyValueMetadataList,
            byte[] rawObject) {
        this(kind, commitDate, name, commitMessage, bucketId, contentType, keyValueMetadataList, rawObject);
        this.commitId = commitId;
    }

    public CatalogObjectRevisionEntity(String kind, LocalDateTime commitDate, String name, String commitMessage,
            Long bucketId, String contentType, List<KeyValueMetadataEntity> keyValueMetadataList, byte[] rawObject) {
        this(kind, commitDate, name, commitMessage, bucketId, contentType, rawObject);
        this.keyValueMetadataList = keyValueMetadataList;
    }

    public CatalogObjectRevisionEntity(Long commitId, String kind, LocalDateTime commitDate, String name,
            String commitMessage, Long bucketId, String contentType, byte[] rawObject) {
        this(kind, commitDate, name, commitMessage, bucketId, contentType, rawObject);
        this.commitId = commitId;
    }

    public Long getBucketId() {
        return bucketId;
    }

    public LocalDateTime getCommitDate() {
        return commitDate;
    }

    public void addKeyValue(KeyValueMetadataEntity keyValueMetadata) {
        this.keyValueMetadataList.add(keyValueMetadata);
        keyValueMetadata.setCatalogObjectRevision(this);
    }

    public void addKeyValueList(Collection<KeyValueMetadataEntity> keyValueMetadataList) {
        keyValueMetadataList.forEach(kv -> addKeyValue(kv));
    }

    public List<KeyValueMetadataEntity> getKeyValueMetadataList() {
        return keyValueMetadataList;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public Long getCommitId() {
        return commitId;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public String getContentType() {
        return contentType;
    }

    public CatalogObjectEntity getCatalogObject() {
        return catalogObject;
    }

    public byte[] getRawObject() {
        return rawObject;
    }

    public void setBucketId(Long bucketId) {
        this.bucketId = bucketId;
    }

    public void setCommitDate(LocalDateTime commitDate) {
        this.commitDate = commitDate;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCatalogObject(CatalogObjectEntity catalogObject) {
        this.catalogObject = catalogObject;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setKeyValueMetadataList(List<KeyValueMetadataEntity> keyValueMetadataList) {
        this.keyValueMetadataList = keyValueMetadataList;
    }

    public void setRawObject(byte[] rawObject) {
        this.rawObject = rawObject;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("commitId", commitId)
                                        .append("name", name)
                                        .append("kind", kind)
                                        .append("commitMessage", commitMessage)
                                        .append("commitDate", commitDate)
                                        .append("commitDate", commitDate)
                                        .append("bucketId", bucketId)
                                        .append("contentType", contentType)
                                        .append("keyValueMetadataList", keyValueMetadataList)
                                        .toString();
    }

    @Override
    public int compareTo(Object o) {
        return ((CatalogObjectRevisionEntity) o).commitDate.compareTo(commitDate);
    }
}