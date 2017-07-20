package com.alisenturk.dao.kullanici;

import java.io.Serializable;
import java.util.List;

import com.alisenturk.model.base.AramaKriter;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.response.ResponseData;

public interface IKullaniciDAO extends Serializable {

	public Kullanici 						araKullanici(String kullaniciAdi);
	public Kullanici 						araKullaniciToken(String token);
	public List<Kullanici> 					kullaniciListesi(AramaKriter kriter, int recordCount);
	public ResponseData<String> 			makePassiveToToken(String token);
	
}
