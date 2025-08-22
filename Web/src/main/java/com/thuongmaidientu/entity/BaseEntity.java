//package com.thuongmaidientu.entity;
//
//import java.util.Date;
//
//import org.springframework.data.annotation.CreatedBy;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedBy;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.MappedSuperclass;
//
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
//public abstract class BaseEntity {
//
////	@Id
////	@GeneratedValue(strategy = GenerationType.IDENTITY)
////	private Long id;
////	
////	@Column(name = "create_at")
////	@CreatedDate
////	private Date createdDate;
//
////	@Column(name = "modifieddate")
////	@LastModifiedDate
////	private Date modifiedDate;
////	
////	@Column(name = "createdby")
////	@CreatedBy
////	private String createdBy;
////	
////	@Column(name = "modifiedby")
////	@LastModifiedBy
////	private String modifiedBy;
//
//	@Column(name = "trash")
//	private String trash;
//
//	@Column(name = "status")
//	private Integer status;
//	
//	@Column(name = "deleted_at")
//	private Date deleted_at;
//
//	public String getTrash() {
//		return trash;
//	}
//
//	public void setTrash(String trash) {
//		this.trash = trash;
//	}
//
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//
//	public Date getDeleted_at() {
//		return deleted_at;
//	}
//
//	public void setDeleted_at(Date deleted_at) {
//		this.deleted_at = deleted_at;
//	}
//
//	
//}