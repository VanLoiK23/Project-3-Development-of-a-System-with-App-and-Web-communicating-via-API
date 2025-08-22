package com.thuongmaidientu.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.thuongmaidientu.dto.CartDTO;
import com.thuongmaidientu.dto.CartItemDTO;
import com.thuongmaidientu.dto.UserDTO;

@Service
public class FirebaseService {

	public String saveUserToFirebase(UserDTO userDTO, boolean isRegister, boolean changeEmailOnly,
			boolean changePasswordOnly,boolean updateInAuthenticate) throws ExecutionException, InterruptedException, FirebaseAuthException {

		String uid = null;

		if (isRegister) {
			// 1. Tạo user trên Firebase Authentication
			UserRecord.CreateRequest request = new UserRecord.CreateRequest().setEmail(userDTO.getEmail())
					.setPassword(userDTO.getPasswordRaw());

			UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
			uid = userRecord.getUid();
		} else {
			UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(userDTO.getFirebaseUid());

			if (changeEmailOnly) {
				request.setEmail(userDTO.getEmail()); // chỉ đổi email
			}

			if (changePasswordOnly) {
				request.setPassword(userDTO.getPasswordRaw()); // chỉ đổi password
			}

			// Nếu không đổi gì thì không cần gọi update
			if (changeEmailOnly || changePasswordOnly) {
				UserRecord updatedUser = FirebaseAuth.getInstance().updateUser(request);
				System.out.println("Cập nhật thành công Firebase user: " + updatedUser.getUid());

				uid = updatedUser.getUid();

			}

		}

		// Nếu không cần lưu Firestore khi chỉ đổi mật khẩu:
		if (!isRegister && changePasswordOnly && !changeEmailOnly) {
			return null;
		}
		
		//nay goi tu ben android
		if(updateInAuthenticate) {
			return null;
		}

		// 2. Lưu thông tin người dùng vào Firestore, liên kết với uid
		Firestore db = FirestoreClient.getFirestore();

		Date ngaySinh = userDTO.getNgaySinh(); // Kiểu Date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String ngaySinhFormatted = sdf.format(ngaySinh);

		Map<String, Object> data = new HashMap<>();
		data.put("id", userDTO.getId().intValue());
		data.put("firstName", userDTO.getFirstName());
		data.put("lastName", userDTO.getLastName());
		data.put("ngaysinh", ngaySinhFormatted);
		data.put("phone", userDTO.getPhone());
		data.put("role", userDTO.getRole());
		data.put("status", userDTO.getStatusString());
		data.put("gender", userDTO.getGender());
		data.put("email", userDTO.getEmail());

		// Có thể lưu theo uid để dễ tra cứu:
		ApiFuture<WriteResult> result = db.collection("user").document(userDTO.getId().toString()).set(data);

		return uid;
	}
	
	public void updateCartCompleted(String status,String cartId) throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();
		
		db.collection("cart").document(cartId).update("status", status).get();
	}


	public void insertCartSynchFromMySql(CartDTO cart) throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();

		// Nếu có idkh thì kiểm tra tồn tại giỏ hàng "active"
		if (cart.getIdkh() != null) {
			// Truy vấn các cart có idkh trùng và status = active
			ApiFuture<QuerySnapshot> future = db.collection("cart").whereEqualTo("idkh", cart.getIdkh())
					.whereEqualTo("status", "active").get();

			List<QueryDocumentSnapshot> documents = future.get().getDocuments();

			// Nếu đã có giỏ hàng active → không thêm mới
			if (!documents.isEmpty()) {
				System.out.println("Đã tồn tại cart active cho idkh = " + cart.getIdkh());
				return;
			}
		}

		// Nếu không có idkh hoặc không có cart active → thêm mới
		Map<String, Object> data = new HashMap<>();
		data.put("id", cart.getId().intValue());
		data.put("idkh", cart.getIdkh());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		data.put("created_at", sdf.format(cart.getCreatedAt()));
		data.put("updated_at", sdf.format(cart.getUpdatedAt()));
		data.put("status", cart.getStatus());
		data.put("cartItems", new ArrayList<>());

		db.collection("cart").document(String.valueOf(cart.getId())).set(data).get(); // chờ hoàn tất

		System.out.println("Đã thêm mới cart vào Firestore.");
	}

	public Pair<Integer, Boolean> insertCartItem(int cartId, CartItemDTO cartItem) {
		Firestore db = FirestoreClient.getFirestore();
		try {
			CollectionReference cartItemsRef = db.collection("cart").document(String.valueOf(cartId))
					.collection("cart_items");

			// Truy vấn theo maphienbansp
			ApiFuture<QuerySnapshot> query = cartItemsRef.whereEqualTo("maphienbansp", cartItem.getMaphienbansp())
					.get();
			List<QueryDocumentSnapshot> documents = query.get().getDocuments();

			String cartItemId;
			if (documents.isEmpty()) {
				// Thêm mới
				cartItemId = getNextCartItemId(cartId); // Tự viết logic này nếu cần tăng ID
				cartItem.setCart_id(cartId);
				cartItem.setCart_item_id(Integer.parseInt(cartItemId));

				cartItemsRef.document(cartItemId).set(getCartItemFieldInFireBase(cartItem)).get();

				System.out.println("Thêm mới cart item thành công!");
//	            return new Pair<>(Integer.parseInt(cartItemId), false);
			} else {
				// Cập nhật số lượng
				DocumentSnapshot existing = documents.get(0);
				cartItemId = String.valueOf(existing.getLong("cart_item_id"));

				Long currentQty = existing.getLong("soluong");
				int updatedQty = (currentQty != null ? currentQty.intValue() : 0) + cartItem.getSoluong();

				cartItemsRef.document(existing.getId()).update("soluong", updatedQty).get();

				System.out.println("Cập nhật số lượng thành công!");
//	            return new Pair<>(Integer.parseInt(cartItemId), true);
			}
		} catch (Exception e) {
			System.err.println("Lỗi Firestore: " + e.getMessage());
//	        return new Pair<>(0, false);
		}
		return null;
	}

	public String getNextCartItemId(int cartId) throws ExecutionException, InterruptedException {
	    Firestore db = FirestoreClient.getFirestore();
	    CollectionReference cartItemsRef = db.collection("cart").document(String.valueOf(cartId))
	            .collection("cart_items");

	    ApiFuture<QuerySnapshot> future = cartItemsRef.get();
	    List<QueryDocumentSnapshot> documents = future.get().getDocuments();

	    int maxId = 0;
	    for (QueryDocumentSnapshot doc : documents) {
	        Long currentId = doc.getLong("cart_item_id");
	        if (currentId != null && currentId > maxId) {
	            maxId = currentId.intValue();
	        }
	    }

	    return String.valueOf(maxId + 1);
	}


	private Map<String, Object> getCartItemFieldInFireBase(CartItemDTO cartItemDTO) {
		Map<String, Object> data = new HashMap<>();
		data.put("cart_id", cartItemDTO.getCart_id());
		data.put("cart_item_id", cartItemDTO.getCart_item_id());
		data.put("maphienbansp", cartItemDTO.getMaphienbansp());
		data.put("masp", cartItemDTO.getMasp());
		data.put("soluong", cartItemDTO.getSoluong());

		return data;
	}

	public void deleteCartItemByMaphienbansp(int cartId, Integer maphienbansp)
			throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();

		// Bước 1: Truy vấn các cart_item có maphienbansp trong cart cụ thể
		ApiFuture<QuerySnapshot> future = db.collection("cart").document(String.valueOf(cartId))
				.collection("cart_items").whereEqualTo("maphienbansp", maphienbansp).get();

		List<QueryDocumentSnapshot> documents = future.get().getDocuments();

		// Bước 2: Lặp và xóa từng document
		for (QueryDocumentSnapshot doc : documents) {
			doc.getReference().delete();
			System.out.println("Đã xóa cart_item có ID: " + doc.getId());
		}

		if (documents.isEmpty()) {
			System.out.println("Không tìm thấy cart_item với maphienbansp = " + maphienbansp);
		}
	}

}
