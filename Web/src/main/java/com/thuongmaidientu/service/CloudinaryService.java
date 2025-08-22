package com.thuongmaidientu.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

	@Autowired
	private Cloudinary cloudinary;

	// Upload file với public_id tùy chỉnh
	public String uploadFile(MultipartFile file, String tenSanPham) {
		try {
			// Đặt public_id theo tên sản phẩm
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("public_id", tenSanPham, "overwrite", true) // Đặt public_id = tên sản phẩm
			);

			return uploadResult.get("secure_url").toString(); // Trả về URL ảnh trên Cloudinary
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Upload nhiều file
	public List<String> uploadMultipleFiles(List<MultipartFile> files, List<String> tenSanPhamList) {
		List<String> urls = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			String url = uploadFile(files.get(i), tenSanPhamList.get(i));
			if (url != null) {
				urls.add(url);
			}
		}
		return urls;
	}
}
