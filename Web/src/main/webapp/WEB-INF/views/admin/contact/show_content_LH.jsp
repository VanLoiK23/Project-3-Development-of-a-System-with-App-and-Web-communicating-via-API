<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>


<div class="overlay-background" onclick="closeForm()"></div>

<div class="form-container" style="width: 300px;" id="customer-form">
	<button class="close-btn" title="close" onclick="closeForm()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button class="edit">
			<i class="fa-solid fa-user-edit"></i>
		</button>
		Nội dung
	</h5>
	<form>
		<div class="form-group">
			<label>Nội dung</label>
			<textarea style="min-height: 150px; width: 250px;" readonly name=""
				class="form-control" id="content"></textarea>
		</div>
	</form>
</div>
