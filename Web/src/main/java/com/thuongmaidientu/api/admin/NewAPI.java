//package com.thuongmaidientu.api.admin;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.thuongmaidientu.dto.NewDTO;
//import com.thuongmaidientu.service.impl.ProductService;
//
//@RestController(value = "newApiOfAdmin")
////@RequestMapping("/api/new")
//public class NewAPI {
//	
////	@PostMapping("/api/new")
////    public NewDTO createNew(@RequestBody NewDTO newDTO) {
////    	return newDTO;
////    }
////	
////
////	@PutMapping("/api/new")
////    public NewDTO updateNew(@RequestBody NewDTO newDTO) {
////    	return newDTO;
////    }
////	
////	@DeleteMapping("/api/new")
////    public void deleteNew(@RequestBody long[] ids) {
////    	System.out.println(ids);
////    }
////	
////	 @Autowired
////	    private ProductService productService;
////
////	    // Tạo bài viết mới
////	    @PostMapping
////	    public ResponseEntity<NewDTO> createNew(@RequestBody NewDTO newDTO) {
////	        NewDTO createdNew = productService.save(newDTO);
////	        return ResponseEntity.ok(createdNew);
////	    }
////
////	    // Cập nhật bài viết
////	    @PutMapping
////	    public ResponseEntity<NewDTO> updateNew(@RequestBody NewDTO newDTO) {
////	        NewDTO updatedNew = productService.save(newDTO);
////	        return ResponseEntity.ok(updatedNew);
////	    }
//
//	    // Xóa bài viết theo danh sách ID
////	    @DeleteMapping
////	    public ResponseEntity<String> deleteNew(@RequestBody long[] ids) {
////	        newService.delete(ids);
////	        return ResponseEntity.ok("Xóa thành công");
////	    }
//}
//
