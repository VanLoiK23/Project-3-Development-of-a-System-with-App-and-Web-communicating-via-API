package com.thuongmaidientu.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.KhachHangDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.entity.UserEntity;
import com.thuongmaidientu.repository.KhachHangRepository;
import com.thuongmaidientu.repository.UserRepository;
import com.thuongmaidientu.service.IKhachHangService;

@Service
public class KhachHangService implements IKhachHangService {
	@Autowired
	private KhachHangRepository KhachHangRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserDTO> findAll(Pageable pageable) {
		List<UserEntity> entities = userRepository.findByStatusNotLikeAndRole("deleted", "khách hàng", pageable)
				.getContent();
		return entities.stream().map(this::convertToUserDTO).collect(Collectors.toList());
	}

	@Override
	public List<UserDTO> selectAll() {
//		List<UserEntity> entities = userRepository.findAll();
		List<UserEntity> entities = userRepository.findByStatusNotLike("deleted");

		return entities.stream().map(this::convertToUserDTO).collect(Collectors.toList());
	}

	private KhachHangDTO convertToDTO(KhachHangEntity entity) {
		KhachHangDTO dto = new KhachHangDTO();
		dto.setId(entity.getMaKhachHang() != null ? entity.getMaKhachHang().longValue() : null);
		dto.setHoTen(entity.getHoTen());
		dto.setDiaChi(entity.getDiaChi());
		dto.setEmail(entity.getEmail());
		dto.setMatKhau(entity.getMatKhau());
		dto.setSoDienThoai(entity.getSoDienThoai());
		dto.setGioiTinh(entity.getGioiTinh());
		dto.setNgaySinh(entity.getNgaySinh());
		dto.setTrangThai(entity.getTrangThai());

		return dto;
	}

	public KhachHangEntity convertToEntity(KhachHangDTO dto) {

		KhachHangEntity entity = new KhachHangEntity();
		entity.setMaKhachHang(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setHoTen(dto.getHoTen());
		entity.setDiaChi(dto.getDiaChi());
		entity.setEmail(dto.getEmail());
		entity.setSoDienThoai(dto.getSoDienThoai());
		entity.setMatKhau(dto.getMatKhau());
		entity.setGioiTinh(dto.getGioiTinh());
		entity.setNgaySinh(dto.getNgaySinh());

		return entity;
	}

	private UserDTO convertToUserDTO(UserEntity entity) {
		UserDTO dto = new UserDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setName(entity.getName());
		dto.setUsername(entity.getUsername());
		dto.setEmail(entity.getEmail());
		dto.setPassword(entity.getPassword());
		dto.setPhone(entity.getPhone());
		dto.setGender(entity.getGender());
		dto.setNgaySinh(entity.getNgaySinh());
		dto.setStatusString(entity.getStatus());
		dto.setRole(entity.getRole());
		dto.setResetTokenHash(entity.getResetTokenHash());
		dto.setResetTokenExpiresAt(entity.getResetTokenExpiresAt());
		dto.setFirebaseUid(entity.getFirebaseUid());
		// return to API for android
		String[] parts = entity.getName().trim().split("\\s+");

		String firstName = "";
		String lastName = "";

		if (parts.length > 0) {
			firstName = parts[parts.length - 1]; // lấy từ cuối cùng
			lastName = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 1)); // còn lại là họ + tên đệm
		}

		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setAdmin(entity.getRole().trim().contains("nhân viên"));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

		dto.setBod(sdf.format(dto.getNgaySinh()));

		return dto;
	}

	public UserEntity convertToUserEntity(UserDTO dto) {

		UserEntity entity = new UserEntity();
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setName(dto.getName());
		entity.setUsername(dto.getUsername());
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		entity.setPhone(dto.getPhone());
		entity.setGender(dto.getGender());
		entity.setNgaySinh(dto.getNgaySinh());
		entity.setStatus(dto.getStatusString());
		entity.setRole(dto.getRole());
		entity.setResetTokenHash(dto.getResetTokenHash());
		entity.setResetTokenExpiresAt(dto.getResetTokenExpiresAt());
		entity.setFirebaseUid(dto.getFirebaseUid());

		if (dto.isAdmin()) {
			entity.setRole("nhân viên");
		} else {
			entity.setRole("khách hàng");
		}

		return entity;
	}

	@Override
	public void updateStatus(int id, String status) {
		userRepository.updateStatusById(id, status);
	}

	@Override
	public void delete(int id) {
		KhachHangRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public KhachHangDTO findById(int masp) {
		Optional<KhachHangEntity> entity = KhachHangRepository.findById((long) masp);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) userRepository.countByStatus("active");

		return (int) (numTotal);
	}

	@Override
	public KhachHangDTO save(KhachHangDTO ncc) {
		KhachHangEntity entity = convertToEntity(ncc);
		KhachHangEntity savedEntity = KhachHangRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public UserDTO save(UserDTO ncc) {
		UserEntity entity = convertToUserEntity(ncc);
		UserEntity savedEntity = userRepository.save(entity);
		return convertToUserDTO(savedEntity);
	}

	@Override
	public int getTotalItemTrash() {
		int numTotal = (int) userRepository.countByStatus("deleted");

		return (int) (numTotal);
	}

	@Override
	public List<UserDTO> findTrash() {
		List<UserEntity> entities = userRepository.findByStatus("deleted");
		return entities.stream().map(this::convertToUserDTO).collect(Collectors.toList());
	}

	@Override
	public void update(UserDTO ncc) {
		userRepository.update(ncc.getName(), ncc.getUsername(), ncc.getPhone(), ncc.getEmail(), ncc.getNgaySinh(),
				ncc.getGender(), ncc.getId().intValue());
		KhachHangRepository.update(ncc.getName(), ncc.getGender(), ncc.getNgaySinh(), ncc.getPhone(), ncc.getEmail(),
				ncc.getId().intValue());
	}

	@Override
	public void updateFromAndroid(UserDTO ncc) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

		try {
			// Chuyển đổi chuỗi thành đối tượng Date
			Date birthDate = sdf.parse(ncc.getBod());

			ncc.setNgaySinh(birthDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		userRepository.updateFromAndroid(ncc.getName(), ncc.getUsername(), ncc.getPhone(), ncc.getEmail(),
				ncc.getNgaySinh(), ncc.getRole(), ncc.getStatusString(), ncc.getId().intValue());
		KhachHangRepository.updateFromAndroid(ncc.getName(), ncc.getNgaySinh(), ncc.getPhone(), ncc.getEmail(),
				ncc.getId().intValue());
	}

	@Override
	public void saveFCMtoken(Integer userId, String token) {
		userRepository.updateFCMtoken(token, userId);
	}

	@Override
	public String getTokenUser(Integer userId) {
		return userRepository.getTokenUser(userId);
	}

	@Override
	public UserDTO login(String userName) {
		List<UserEntity> entities = userRepository.findByUsername(userName);

		if (entities.isEmpty()) {
			return null;
		} else {
			UserDTO dto = convertToUserDTO(entities.get(0));
			return dto;
		}
	}

	@Override
	public Integer getUserRegister() {
		return (int) userRepository.countByRole("khách hàng");
	}

	@Override
	public boolean checkIsLockByPhoneOrEmail(String email, String phone) {

		return userRepository.countLockedAccount(email, phone) > 0;
	}

	@Override
	public boolean isExistUserName(String name) {
		return userRepository.existsByNameAllIgnoreCase(name);
	}
	
	@Override
	public boolean isExistEmail(String email) {
		return userRepository.existsByEmailAllIgnoreCase(email);
	}

	@Override
	public UserDTO findUserDTOById(int id) {
		Optional<UserEntity> entity = userRepository.findById((long) id);
		return entity.map(this::convertToUserDTO).orElse(null);
	}

	@Override
	public UserDTO findByEmail(String email) {
		List<UserEntity> entities = userRepository.findByEmail(email);
		return (entities != null && !entities.isEmpty()) ? convertToUserDTO(entities.get(0)) : null;
	}

	@Override
	public UserDTO findByTokenHash(String tokenHash) {
		List<UserEntity> entities = userRepository.findByResetTokenHash(tokenHash);
		return (entities != null && !entities.isEmpty()) ? convertToUserDTO(entities.get(0)) : null;
	}
}
