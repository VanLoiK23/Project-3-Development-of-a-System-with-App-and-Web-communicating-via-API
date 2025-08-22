package com.thuongmaidientu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.MomoDTO;
import com.thuongmaidientu.entity.MomoEntity;
import com.thuongmaidientu.repository.MomoRepository;
import com.thuongmaidientu.service.IMomoService;

@Service
public class MomoService implements IMomoService {

	@Autowired
	private MomoRepository momoRepository;

	@Override
	public boolean insertMomo(MomoDTO dto) {
		return momoRepository.save(convertToEntity(dto)) != null;
	}

	public MomoEntity convertToEntity(MomoDTO dto) {

		MomoEntity entity = new MomoEntity();
		entity.setIdMomo(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setOrderId(dto.getOrderId());
		entity.setCodeCart(dto.getCodeCart());
		entity.setAmount(dto.getAmount());
		entity.setOrderInfo(dto.getOrderInfo());
		entity.setOrderType(dto.getOrderType());
		entity.setPartnerCode(dto.getPartnerCode());
		entity.setPayType(dto.getPayType());
		entity.setTransId(dto.getTransId());

		return entity;
	}

}
