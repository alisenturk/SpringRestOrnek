package com.alisenturk.dao.musteri;

import java.io.Serializable;
import java.util.List;

import com.alisenturk.controller.musteri.MusteriAramaKriterleri;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.musteri.Musteri;
import com.alisenturk.model.response.ResponseData;

public interface IMusteriDAO extends Serializable {
	
	public ResponseData<List<Musteri>> musteriAraList(Kullanici kullanici,MusteriAramaKriterleri kriter);
}
